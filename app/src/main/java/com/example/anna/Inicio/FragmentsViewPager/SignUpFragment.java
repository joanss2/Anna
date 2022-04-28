package com.example.anna.Inicio.FragmentsViewPager;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.example.anna.Alerts.PasswordsNotEqualAlert;
import com.example.anna.Alerts.UnSuccessfulSignUpAlert;
import com.example.anna.Inicio.MainActivity;
import com.example.anna.Inicio.UserTuple;
import com.example.anna.MenuPrincipal.MenuMainActivity;
import com.example.anna.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SignUpFragment extends Fragment {


    private EditText usernameSignUp, emailSignUp, passwordSignUp, confirmPassword;
    private SharedPreferences.Editor userInfoEditor;
    private String email, usernName;
    private FirebaseDatabase database;
    private Intent registerAndStart;
    private List<String> list;
    private UserTuple userTuple;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences userInfoPreferences = getActivity().getSharedPreferences("USERINFO", Context.MODE_PRIVATE);
        userInfoEditor = userInfoPreferences.edit();
        database = FirebaseDatabase.getInstance("https://annaapp-322219-default-rtdb.europe-west1.firebasedatabase.app/");
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
        list = new ArrayList<>();

        btnSignUp.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(passwordSignUp.getText()) && !TextUtils.isEmpty(confirmPassword.getText()) &&
                        passwordSignUp.getText().toString().equals(confirmPassword.getText().toString())) {

                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(emailSignUp.getText().toString(),
                            passwordSignUp.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                registerAndStart = new Intent(getActivity(), MenuMainActivity.class);
                                email = emailSignUp.getText().toString();
                                usernName = usernameSignUp.getText().toString();
                                DatabaseReference ref = database.getReference("users");
                                userTuple = new UserTuple(usernName, email, null,null, list);
                                uploadUserInfoPrefs(userTuple);
                                ref.push().setValue(userTuple);
                                startActivity(registerAndStart);
                                requireActivity().finish();

                            } else {
                                showAlert(task.getException().getMessage());
                            }
                        }
                    });
                } else if (TextUtils.isEmpty(passwordSignUp.getText()) || TextUtils.isEmpty(confirmPassword.getText())) {
                    showAlert(new BadPasswordAlert(getContext()));
                } else {
                    showAlert(new PasswordsNotEqualAlert(getContext()));
                }
            }
        });
        return view;
    }

    public void uploadUserInfoPrefs(UserTuple userTuple){
        this.userInfoEditor.putString("username",userTuple.getUsername());
        this.userInfoEditor.putString("email",userTuple.getEmail());
        this.userInfoEditor.putString("uid",null);
        this.userInfoEditor.putString("telefon",null);
        this.userInfoEditor.apply();
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

}