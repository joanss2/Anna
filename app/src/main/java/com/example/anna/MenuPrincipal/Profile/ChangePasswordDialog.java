package com.example.anna.MenuPrincipal.Profile;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.example.anna.Alerts.AlertManager;
import com.example.anna.Alerts.PasswordsNotEqualAlert;
import com.example.anna.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangePasswordDialog extends DialogFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.change_password,container,false);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return createDialogMethod();
    }

    private Dialog createDialogMethod() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());

        LayoutInflater layoutInflater = requireActivity().getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.change_password,null);

        builder.setView(view);

        EditText changePassword = view.findViewById(R.id.changePasswordText);
        EditText confirmPassword = view.findViewById(R.id.changePasswordConfirmtext);
        Button button = view.findViewById(R.id.changePasswordButton);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertManager alertManager = new AlertManager((AppCompatActivity) requireActivity());
                if(changePassword.getText().toString().equals(confirmPassword.getText().toString())){
                    changePasswordFirebase(changePassword.getText().toString());
                }else{
                    alertManager.showAlert(new PasswordsNotEqualAlert(requireContext()));
                }
            }
        });

        return builder.create();
    }

    public void changePasswordFirebase(String password){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


        assert user != null;
        user.updatePassword(password)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(requireContext(), "PASSWORD UPDATED SUCCESSFULLY", Toast.LENGTH_SHORT).show();
                            ChangePasswordDialog.this.dismiss();
                        }
                    }
                });
    }
}
