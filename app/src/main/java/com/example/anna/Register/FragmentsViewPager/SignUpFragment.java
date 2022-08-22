package com.example.anna.Register.FragmentsViewPager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.anna.Alerts.AlertManager;
import com.example.anna.Alerts.BadPasswordAlert;
import com.example.anna.Alerts.PasswordsNotEqualAlert;
import com.example.anna.Models.User;
import com.example.anna.R;
import com.example.anna.Register.Collaborator.CollaboratorInfoDialog;
import com.example.anna.Register.VerificationActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Locale;
import java.util.Objects;

public class SignUpFragment extends Fragment {


    private EditText usernameSignUp, emailSignUp, passwordSignUp, confirmPassword;
    private SharedPreferences.Editor userInfoEditor;
    private String email, userName, key;
    private FirebaseDatabase database;
    private User userTuple;
    private CheckBox checkBox;
    private AlertManager alertManagerSignUp;
    private ImageView infoCollaborator;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences userInfoPreferences = requireActivity().getSharedPreferences("USERINFO", Context.MODE_PRIVATE);
        userInfoEditor = userInfoPreferences.edit();
        database = FirebaseDatabase.getInstance("https://annaapp-322219-default-rtdb.europe-west1.firebasedatabase.app/");
        this.alertManagerSignUp = new AlertManager((AppCompatActivity) requireActivity());
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);
        Button btnSignUp = view.findViewById(R.id.buttonsignup);
        usernameSignUp = view.findViewById(R.id.usernamesignup);
        emailSignUp = view.findViewById(R.id.emailsignup);
        passwordSignUp = view.findViewById(R.id.passwordsignup);
        confirmPassword = view.findViewById(R.id.passwordconfirm);
        checkBox = view.findViewById(R.id.checkboxsignup);
        infoCollaborator = view.findViewById(R.id.info_collaborator);


        btnSignUp.setOnClickListener(v -> {
            if (!TextUtils.isEmpty(passwordSignUp.getText()) && !TextUtils.isEmpty(confirmPassword.getText()) &&
                    passwordSignUp.getText().toString().equals(confirmPassword.getText().toString())) {

                FirebaseAuth.getInstance().createUserWithEmailAndPassword(emailSignUp.getText().toString(),
                        passwordSignUp.getText().toString()).addOnCompleteListener(task -> {


                    if (task.isSuccessful()) {
                        email = emailSignUp.getText().toString();
                        userName = usernameSignUp.getText().toString();
                        DatabaseReference ref = database.getReference("users");
                        DatabaseReference refAdmin = database.getReference("collaborators");
                        Intent intent = new Intent(getContext(), VerificationActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("password", passwordSignUp.getText().toString());

                        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                        Objects.requireNonNull(firebaseUser).sendEmailVerification().addOnCompleteListener(task1 -> {
                            Toast.makeText(getContext(), "Verification email sent", Toast.LENGTH_SHORT).show();


                            if (checkBox.isChecked()) {
                                key = refAdmin.push().getKey();
                                assert key != null;
                                userTuple = new User(userName, email, key, Locale.getDefault().getLanguage());
                                refAdmin.child(key).setValue(userTuple);
                                uploadUserInfoPrefs(userTuple,"collaborator");
                                bundle.putString("user", "collaborator");

                            } else {
                                key = ref.push().getKey();
                                assert key != null;
                                userTuple = new User(userName, email, key, Locale.getDefault().getLanguage());
                                ref.child(key).setValue(userTuple);
                                uploadUserInfoPrefs(userTuple, "client");
                                bundle.putString("user", "client");

                            }

                            intent.putExtras(bundle);
                            startActivity(intent);
                            requireActivity().finish();

                        });


                    } else {
                        alertManagerSignUp.showAlert(Objects.requireNonNull(task.getException()).getMessage());
                    }
                });


            } else if (TextUtils.isEmpty(passwordSignUp.getText()) || TextUtils.isEmpty(confirmPassword.getText())) {
                alertManagerSignUp.showAlert(new BadPasswordAlert(getContext()));
            } else {
                alertManagerSignUp.showAlert(new PasswordsNotEqualAlert(getContext()));
            }
        });
        infoCollaborator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CollaboratorInfoDialog dialogInfo = new CollaboratorInfoDialog();
                dialogInfo.show(requireActivity().getSupportFragmentManager(),"INFO DIALOG");
            }
        });
        return view;
    }


    public void uploadUserInfoPrefs(User userTuple, String userType) {
        this.userInfoEditor.putString("username", userTuple.getUsername());
        this.userInfoEditor.putString("email", userTuple.getEmail());
        this.userInfoEditor.putString("userKey", userTuple.getUserKey());
        this.userInfoEditor.putString("usertype", userType);
        this.userInfoEditor.apply();
    }

}