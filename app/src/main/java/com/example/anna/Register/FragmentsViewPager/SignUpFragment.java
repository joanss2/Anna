package com.example.anna.Register.FragmentsViewPager;

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
import android.widget.CheckBox;
import android.widget.EditText;

import com.example.anna.Models.Alert;
import com.example.anna.Alerts.BadPasswordAlert;
import com.example.anna.Alerts.PasswordsNotEqualAlert;
import com.example.anna.Models.User;
import com.example.anna.MenuPrincipal.MenuMainActivity;
import com.example.anna.R;
import com.example.anna.Register.CollaboratorTariffActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class SignUpFragment extends Fragment {


    private EditText usernameSignUp, emailSignUp, passwordSignUp, confirmPassword;
    private SharedPreferences.Editor userInfoEditor;
    private String email, usernName, key;
    private FirebaseDatabase database;
    private Intent registerAndStart;
    private User userTuple;
    private CheckBox checkBox;
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    CollectionReference myDiscountsRef = firestore.collection("DiscountsUsed");


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
        checkBox = view.findViewById(R.id.checkboxsignup);

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
                                DatabaseReference refAdmin = database.getReference("collaborators");


                                if(checkBox.isChecked()) {
                                    key = refAdmin.push().getKey();
                                    assert key!=null;
                                    userTuple = new User(usernName, email, key);
                                    refAdmin.child(key).setValue(userTuple);
                                    uploadUserInfoPrefs(userTuple);
                                    startActivity(new Intent(getActivity(), CollaboratorTariffActivity.class));
                                    requireActivity().finish();
                                }else {
                                    key = ref.push().getKey();
                                    assert key!=null;
                                    userTuple = new User(usernName, email, key);
                                    ref.child(key).setValue(userTuple);
                                    uploadUserInfoPrefs(userTuple);
                                    startActivity(registerAndStart);
                                    requireActivity().finish();
                                }

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

    public void uploadUserInfoPrefs(User userTuple) {
        this.userInfoEditor.putString("username", userTuple.getUsername());
        this.userInfoEditor.putString("email", userTuple.getEmail());
        this.userInfoEditor.putString("userKey", userTuple.getUserKey());
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