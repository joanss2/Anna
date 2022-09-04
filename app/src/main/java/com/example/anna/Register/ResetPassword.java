package com.example.anna.Register;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.anna.Alerts.AlertManager;
import com.example.anna.Alerts.NonExistentAccountAlert;
import com.example.anna.databinding.ResetPasswordActivityBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ResetPassword extends AppCompatActivity {

    private final FirebaseAuth auth = FirebaseAuth.getInstance();
    private final DatabaseReference reference = FirebaseDatabase.getInstance("https://annaapp-322219-default-rtdb.europe-west1.firebasedatabase.app/")
            .getReference("users");
    private final DatabaseReference referenceCollaborators = FirebaseDatabase.getInstance("https://annaapp-322219-default-rtdb.europe-west1.firebasedatabase.app/")
            .getReference("collaborators");
    private AlertManager alertManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        com.example.anna.databinding.ResetPasswordActivityBinding binding = ResetPasswordActivityBinding.inflate(getLayoutInflater());
        View root = binding.getRoot();
        setContentView(root);

        alertManager = new AlertManager(this);

        EditText email = binding.textviewResetPassword;
        Button button = binding.buttonResetPasswordActivity;

        button.setOnClickListener(v -> checkUser(email.getText().toString()));

    }

    private void checkCollaborator(String email) {
        referenceCollaborators.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            sendPasswordResetMail(email);
                        }else{
                            alertManager.showAlert(new NonExistentAccountAlert(getApplicationContext()));
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                }
        );
    }

    public void checkUser(String email){
        reference.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            sendPasswordResetMail(email);
                        }else{
                            checkCollaborator(email);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                }
        );

    }

    private void sendPasswordResetMail(String email){
        auth.sendPasswordResetEmail(email).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                Toast.makeText(getApplicationContext(),"Email sent!",Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

}
