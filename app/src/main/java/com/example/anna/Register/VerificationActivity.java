package com.example.anna.Register;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.anna.Alerts.AlertManager;
import com.example.anna.MenuPrincipal.MenuMainActivity;
import com.example.anna.R;
import com.example.anna.Register.Collaborator.CollaboratorTariffActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class VerificationActivity extends AppCompatActivity {

    private SharedPreferences userInfoPrefs;
    private String password;
    private String typeOfUser;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        userInfoPrefs = getSharedPreferences("USERINFO", MODE_PRIVATE);
        password = getIntent().getExtras().getString("password", null);
        typeOfUser = getIntent().getExtras().getString("user",null);



        setContentView(R.layout.verification_activity);

        Button resendCode = findViewById(R.id.verificationActivityResend);
        Button verify = findViewById(R.id.verificationActVerify);

        resendCode.setOnClickListener(v -> {
            FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            Objects.requireNonNull(firebaseUser).sendEmailVerification().addOnCompleteListener(task ->
                    Toast.makeText(getApplicationContext(), "Verification email sent", Toast.LENGTH_SHORT).show());
        });

        verify.setOnClickListener(v -> signIn());

    }

    private void signIn() {

        AlertManager alertManager = new AlertManager(this);
        FirebaseAuth.getInstance().signInWithEmailAndPassword(userInfoPrefs.getString("email", null), password)
                .addOnCompleteListener(task -> {

                    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                    assert firebaseUser != null;
                    if (!firebaseUser.isEmailVerified())
                        alertManager.showAlert("Email not verified yet, please verify it");
                    else if (task.isSuccessful()) {
                        if(typeOfUser.equals("client"))
                            startActivity(new Intent(this, MenuMainActivity.class));
                        else
                            startActivity(new Intent(this, CollaboratorTariffActivity.class));
                        finishAffinity();
                    } else {
                        alertManager.showAlert(Objects.requireNonNull(task.getException()).getMessage());
                    }
                });
    }


}
