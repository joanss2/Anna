package com.example.anna.Register.FragmentsViewPager;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.anna.Models.Alert;
import com.example.anna.Alerts.EmptyEmailFieldAlert;
import com.example.anna.Alerts.UnsuccessfulSignInAlert;
import com.example.anna.Models.Discount;
import com.example.anna.Models.UserTuple;
import com.example.anna.MenuPrincipal.MenuMainActivity;
import com.example.anna.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class SignInFragment extends Fragment {


    private final int GOOGLE_SIGN_IN = 101;
    private EditText emailSignIn, passwordSignIn;
    private SharedPreferences userInfoPreferences;
    private SharedPreferences.Editor userInfoEditor;
    private String name, email;
    private Intent toMenu;
    private Uri userPic;
    FirebaseDatabase database = FirebaseDatabase.getInstance("https://annaapp-322219-default-rtdb.europe-west1.firebasedatabase.app/");
    DatabaseReference ref = database.getReference("users");
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    CollectionReference myDiscountsRef = firestore.collection("DiscountsUsed");


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
                                                downloadUserInfoAndSavePersistent();
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

    private void downloadUserInfoAndSavePersistent() {
        Query query = ref.orderByChild("email").equalTo(emailSignIn.getText().toString());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        UserTuple user = ds.getValue(UserTuple.class);
                        userInfoEditor.putString("email", emailSignIn.getText().toString());
                        userInfoEditor.putString("userKey", user.getUserKey());
                        userInfoEditor.putString("username", user.getUsername());
                        userInfoEditor.commit();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
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
                                newUserCreatedIfNonExistent();

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

        Query query = ref.orderByChild("email").equalTo(email);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    String key = ref.push().getKey();
                    assert key != null;
                    UserTuple userTuple = new UserTuple(name, email, key);
                    createMyDiscountsUserEntry(key);
                    uploadUserInfoPrefs(userTuple);
                    ref.child(key).setValue(userTuple);
                    startActivity(toMenu);
                    requireActivity().finish();
                } else {
                    for (DataSnapshot ds : snapshot.getChildren()) {

                        UserTuple user = ds.getValue(UserTuple.class);
                        uploadUserInfoPrefs(user);
                        startActivity(toMenu);
                        requireActivity().finish();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void createMyDiscountsUserEntry(String key) {
        myDiscountsRef.document(key).collection("MyDiscounts").add(new Discount()).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Log.d("UserDiscount Entry", "Correctly made User Discounts Entry");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("UserDiscount Entry", e.getMessage());

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
        userInfoEditor.putString("username", userTuple.getUsername());
        userInfoEditor.putString("email", userTuple.getEmail());
        userInfoEditor.putString("userKey", userTuple.getUserKey());
        userInfoEditor.putString("fotourl", userPic.toString());
        userInfoEditor.apply();
    }
}