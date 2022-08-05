package com.example.anna.Register.FragmentsViewPager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.anna.Alerts.AlertManager;
import com.example.anna.Alerts.EmptyEmailFieldAlert;
import com.example.anna.Alerts.UnsuccessfulSignInAlert;
import com.example.anna.MenuPrincipal.CollaboratorMenu;
import com.example.anna.Models.Subscription;
import com.example.anna.Models.User;
import com.example.anna.MenuPrincipal.MenuMainActivity;
import com.example.anna.R;
import com.example.anna.Register.Collaborator.CollaboratorTariffActivity;
import com.example.anna.Register.ResetPassword;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class SignInFragment extends Fragment {


    private final int GOOGLE_SIGN_IN = 101;
    private EditText emailSignIn, passwordSignIn;
    private SharedPreferences userInfoPreferences;
    private SharedPreferences.Editor userInfoEditor;
    private String name, email;
    private Intent toMenu, toMenuCollaborator;
    private Uri userPic;
    private AlertManager alertManagerSignIn;
    FirebaseDatabase database = FirebaseDatabase.getInstance("https://annaapp-322219-default-rtdb.europe-west1.firebasedatabase.app/");
    DatabaseReference ref = database.getReference("users");
    DatabaseReference refCollaborator = database.getReference("collaborators");


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userInfoPreferences = requireActivity().getSharedPreferences("USERINFO", Context.MODE_PRIVATE);
        userInfoEditor = userInfoPreferences.edit();
        alertManagerSignIn = new AlertManager((AppCompatActivity) requireActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_sign_in, container, false);
        ImageButton googleButton = view.findViewById(R.id.googleIn);
        Button btnSignIn = view.findViewById(R.id.buttonsignin);
        TextView forgetPassword = view.findViewById(R.id.forgetPasswordButton);
        toMenu = new Intent(getContext(), MenuMainActivity.class);
        toMenuCollaborator = new Intent(getContext(), CollaboratorMenu.class);
        emailSignIn = view.findViewById(R.id.email);
        passwordSignIn = view.findViewById(R.id.password);

        btnSignIn.setOnClickListener(v -> {
            if (v.getId() == R.id.buttonsignin) {
                if (TextUtils.isEmpty(emailSignIn.getText())) {
                    alertManagerSignIn.showAlert(new EmptyEmailFieldAlert(getContext()));
                } else if (!TextUtils.isEmpty(emailSignIn.getText()) && !TextUtils.isEmpty(passwordSignIn.getText())) {
                    FirebaseAuth.getInstance().signInWithEmailAndPassword(emailSignIn.getText().toString(), passwordSignIn.getText().toString())
                            .addOnCompleteListener(task -> {

                                FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                                if (!Objects.requireNonNull(firebaseUser).isEmailVerified())
                                    alertManagerSignIn.showAlert("Email not verified yet, please verify it");
                                else if (task.isSuccessful()) {
                                    downloadUserInfoAndSavePersistent();
                                } else {
                                    alertManagerSignIn.showAlert(Objects.requireNonNull(task.getException()).getMessage());
                                }
                            });

                } else {
                    alertManagerSignIn.showAlert(new UnsuccessfulSignInAlert(getContext()));
                }
            }
        });

        googleButton.setOnClickListener(v -> {
            GoogleSignInOptions googleConf = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();

            GoogleSignInClient googleClient = GoogleSignIn.getClient(requireActivity(), googleConf);
            googleClient.signOut();
            startActivityForResult(googleClient.getSignInIntent(), GOOGLE_SIGN_IN);
        });

        forgetPassword.setOnClickListener(v -> startActivity(new Intent(getContext(), ResetPassword.class)));


        return view;
    }

    private void downloadUserInfoAndSavePersistent() {
        Query query = ref.orderByChild("email").equalTo(emailSignIn.getText().toString());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        User user = ds.getValue(User.class);
                        assert user != null;
                        /*userInfoEditor.putString("email", emailSignIn.getText().toString());
                        userInfoEditor.putString("userKey", user.getUserKey());
                        userInfoEditor.putString("username", user.getUsername());
                        userInfoEditor.putString("usertype", "client");
                        userInfoEditor.commit();

                         */
                        uploadUserInfoPrefs(user, "client");
                        startActivity(toMenu);
                        requireActivity().finish();
                    }
                } else {

                    Query query = refCollaborator.orderByChild("email").equalTo(emailSignIn.getText().toString());
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                for (DataSnapshot ds : snapshot.getChildren()) {
                                    User user = ds.getValue(User.class);
                                    assert user != null;
                                    /*userInfoEditor.putString("email", emailSignIn.getText().toString());
                                    userInfoEditor.putString("userKey", user.getUserKey());
                                    userInfoEditor.putString("username", user.getUsername());
                                    userInfoEditor.putString("usertype", "collaborator");
                                    userInfoEditor.commit();

                                     */
                                    uploadUserInfoPrefs(user, "collaborator");
                                    checkCollaboratorSubscription();

                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });

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
                    FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener(task1 -> {
                        if (task1.isSuccessful()) {
                            userPic = account.getPhotoUrl();
                            name = account.getDisplayName();
                            email = account.getEmail();
                            newUserCreatedIfNonExistent();

                        } else {
                            alertManagerSignIn.showAlert(Objects.requireNonNull(task1.getException()).getMessage());
                        }
                    });

                }
            } catch (ApiException e) {
                alertManagerSignIn.showAlert(new UnsuccessfulSignInAlert(getContext()));
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
                    User userTuple = new User(name, email, key, Locale.getDefault().getLanguage());
                    uploadUserInfoPrefs(userTuple, "client");
                    ref.child(key).setValue(userTuple);
                    startActivity(toMenu);
                    requireActivity().finish();
                } else {
                    for (DataSnapshot ds : snapshot.getChildren()) {

                        User user = ds.getValue(User.class);
                        assert user != null;
                        uploadUserInfoPrefs(user, "client");
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


    @Override
    public void onResume() {
        super.onResume();
        if (userInfoPreferences.getString("email", null) != null) {
            emailSignIn.setText(userInfoPreferences.getString("email", null));
        }
    }

    public void uploadUserInfoPrefs(User userTuple, String usertype) {
        userInfoEditor.putString("username", userTuple.getUsername());
        userInfoEditor.putString("email", userTuple.getEmail());
        userInfoEditor.putString("userKey", userTuple.getUserKey());
        userInfoEditor.putString("usertype", usertype);
        userInfoEditor.apply();
    }

    public void checkCollaboratorSubscription() {
        CollectionReference subReference = FirebaseFirestore.getInstance().collection("Subscriptions").document(userInfoPreferences.getString("userKey", null))
                .collection("SubsOfUser");
        subReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Map<String, Object> map = document.getData();

                        Subscription subscription = new Subscription(map);
                        if (subscription.getDateEnd().after(new Date())) {
                            startActivity(toMenuCollaborator);
                            requireActivity().finish();
                        } else {
                            startActivity(new Intent(getContext(), CollaboratorTariffActivity.class).putExtra(
                                    "ended",true
                            ));
                            requireActivity().finish();
                        }
                    }
                }
            }
        });
    }


}