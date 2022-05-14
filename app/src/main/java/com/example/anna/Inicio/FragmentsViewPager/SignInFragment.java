package com.example.anna.Inicio.FragmentsViewPager;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.anna.Alerts.Alert;
import com.example.anna.Alerts.BadPasswordAlert;
import com.example.anna.Alerts.EmptyEmailFieldAlert;
import com.example.anna.Alerts.UnsuccessfulSignInAlert;
import com.example.anna.Alerts.UserNotRegisteredAlert;
import com.example.anna.Inicio.MainActivity;
import com.example.anna.Inicio.UserTuple;
import com.example.anna.MenuPrincipal.MenuMainActivity;
import com.example.anna.R;
import com.firebase.ui.auth.data.model.Resource;
import com.firebase.ui.auth.util.data.TaskFailureLogger;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import kotlin.Result;

public class SignInFragment extends Fragment {


    private final int GOOGLE_SIGN_IN = 101;
    private EditText emailSignIn, passwordSignIn;
    private SharedPreferences userInfoPreferences;
    private SharedPreferences.Editor userInfoEditor;
    private String name, email;
    private Intent toMenu;
    private Uri userPic;
    private UserTuple userTuple;
    Lock lock = new ReentrantLock();
    Condition condition = lock.newCondition();
    FirebaseDatabase database = FirebaseDatabase.getInstance("https://annaapp-322219-default-rtdb.europe-west1.firebasedatabase.app/");
    DatabaseReference ref = database.getReference("users");


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userInfoPreferences = requireActivity().getSharedPreferences("USERINFO", Context.MODE_PRIVATE);
        userInfoEditor = userInfoPreferences.edit();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_sign_in, container, false);
        ImageButton googleButton = view.findViewById(R.id.googleIn);
        Button btnSignIn = view.findViewById(R.id.buttonsignin);
        toMenu = new Intent(getContext(), MenuMainActivity.class);
        emailSignIn = view.findViewById(R.id.email);
        passwordSignIn = view.findViewById(R.id.password);

        btnSignIn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.buttonsignin:
                        if (TextUtils.isEmpty(emailSignIn.getText())) {
                            showAlert(new EmptyEmailFieldAlert(getContext()));
                        } else if (!TextUtils.isEmpty(emailSignIn.getText()) && !TextUtils.isEmpty(passwordSignIn.getText())) {
                            FirebaseAuth.getInstance().signInWithEmailAndPassword(emailSignIn.getText().toString(), passwordSignIn.getText().toString())
                                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if (task.isSuccessful()) {
                                                userInfoEditor.putString("email", emailSignIn.getText().toString()).commit();
                                                startActivity(toMenu);
                                                getActivity().finish();
                                            } else {
                                                showAlert(task.getException().getMessage());
                                            }
                                        }
                                    });

                        } else {
                            showAlert(new UnsuccessfulSignInAlert(getContext()));
                        }
                }
            }
        });

        googleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoogleSignInOptions googleConf = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();

                GoogleSignInClient googleClient = GoogleSignIn.getClient(getActivity(), googleConf);
                googleClient.signOut();
                startActivityForResult(googleClient.getSignInIntent(), GOOGLE_SIGN_IN);
            }
        });

        return view;

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GOOGLE_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                if (account != null) {

                    AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
                    FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                userPic = account.getPhotoUrl();
                                name = account.getDisplayName();
                                email = account.getEmail();
                                FirebaseDatabase database = FirebaseDatabase.getInstance("https://annaapp-322219-default-rtdb.europe-west1.firebasedatabase.app/");
                                DatabaseReference ref = database.getReference("users");
                                userTuple = new UserTuple(name, email, null, null, null);
                                uploadUserInfoPrefs(userTuple);
                                newUserCreatedIfNonExistent();
                                //
                                //
                                // new UserInDatabase().execute();
                                startActivity(toMenu);
                                requireActivity().finish();


                            } else {
                                showAlert(task.getException().getMessage());
                            }
                        }
                    });

                }
            } catch (ApiException e) {
                showAlert(new UnsuccessfulSignInAlert(getContext()));
            }
        }
    }

    private void newUserCreatedIfNonExistent() {

        Query query = ref.orderByChild("email").equalTo(SignInFragment.this.userInfoPreferences.getString("email", null));
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                lock.lock();
                if (!snapshot.exists()) {
                    ref.push().setValue(userTuple);
                }
                lock.unlock();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        if (userInfoPreferences.getString("email", null) != null) {
            emailSignIn.setText(userInfoPreferences.getString("email", null));
        }
    }

    private void showAlert(Alert alert) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.error));
        builder.setMessage(alert.getAlertMessage());
        builder.setPositiveButton(getString(R.string.ok), null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showAlert(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.error));
        builder.setMessage(message);
        builder.setPositiveButton(getString(R.string.ok), null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void uploadUserInfoPrefs(UserTuple userTuple) {
        this.userInfoEditor.putString("username", userTuple.getUsername());
        this.userInfoEditor.putString("email", userTuple.getEmail());
        this.userInfoEditor.putString("uid", null);
        this.userInfoEditor.putString("telefon", null);
        this.userInfoEditor.putString("fotourl", userPic.toString());
        this.userInfoEditor.apply();
    }


/*
    private class UserInDatabase extends AsyncTask<String, String, String> {
        private final FirebaseDatabase database = FirebaseDatabase.getInstance("https://annaapp-322219-default-rtdb.europe-west1.firebasedatabase.app/");
        private final DatabaseReference reference = database.getReference("users");
        private String s = "";

        @Override
        protected String doInBackground(String... strings) {
            Query query = reference.orderByChild("email").equalTo(SignInFragment.this.userInfoPreferences.getString("email", null));
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    lock.lock();
                    if (snapshot.exists()) {
                        System.out.println("EXISTEIX, INUSE HAURIA DE SER TRUE");
                        s = "true";
                        condition.signal();
                    } else {
                        reference.push().setValue(userTuple);
                    }
                    lock.unlock();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
            try {
                lock.lock();
                condition.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
            return s;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }

 */

}