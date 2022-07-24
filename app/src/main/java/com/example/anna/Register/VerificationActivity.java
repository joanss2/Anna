package com.example.anna.Register;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.anna.Alerts.AlertManager;
import com.example.anna.MenuPrincipal.MenuMainActivity;
import com.example.anna.Models.Alert;
import com.example.anna.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class VerificationActivity extends AppCompatActivity {

    private SharedPreferences userInfoPrefs;
    private SharedPreferences.Editor userInfoEditor;
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private String password;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        userInfoPrefs = getSharedPreferences("USERINFO", MODE_PRIVATE);
        userInfoEditor = userInfoPrefs.edit();
        database = FirebaseDatabase.getInstance("https://annaapp-322219-default-rtdb.europe-west1.firebasedatabase.app/");
        reference = database.getReference("users");


password = getIntent().getExtras().getString("password", null);


        setContentView(R.layout.verification_activity);

        Button resendCode = findViewById(R.id.verificationActivityResend);
        Button verify = findViewById(R.id.verificationActVerify);

        resendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getApplicationContext(), "Verification email sent", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

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
                        startActivity(new Intent(this, MenuMainActivity.class));
                        finishAffinity();
                    } else {
                        alertManager.showAlert(Objects.requireNonNull(task.getException()).getMessage());
                    }
                });
    }


}
