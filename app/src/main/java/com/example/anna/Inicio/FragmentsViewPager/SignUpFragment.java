package com.example.anna.Inicio.FragmentsViewPager;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.anna.Alerts.Alert;
import com.example.anna.Alerts.BadPasswordAlert;
import com.example.anna.Alerts.EmailFieldEmptyAlert;
import com.example.anna.Alerts.PasswordsNotEqualAlert;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpFragment extends Fragment {


    private EditText emailSignUp, passwordSignUp, confirmPassword;
    private Button btnSignUp;
    private ImageButton  googleButton;
    private final int GOOGLE_SIGN_UP = 100;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private String name, email;
    private DatabaseReference reference;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getActivity().getSharedPreferences(String.valueOf
                (R.string.sharedpreferencesfile),Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        reference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://annaapp-3222q19-default-rtdb.firebaseio.com");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);
        btnSignUp = view.findViewById(R.id.buttonsignup);
        emailSignUp = view.findViewById(R.id.emailsignup);
        passwordSignUp = view.findViewById(R.id.passwordsignup);
        confirmPassword = view.findViewById(R.id.passwordconfirm);
        googleButton = view.findViewById(R.id.googleIn);



        googleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoogleSignInOptions googleConf = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();

                GoogleSignInClient googleClient = GoogleSignIn.getClient(getActivity(),googleConf);
                startActivityForResult(googleClient.getSignInIntent(),GOOGLE_SIGN_UP);
            }
        });


        btnSignUp.setOnClickListener(new View.OnClickListener() {

            /*Ch */
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(emailSignUp.getText())) {
                    //showAlert();
                    showAlert(new EmailFieldEmptyAlert());
                }else{
                    if(!TextUtils.isEmpty(passwordSignUp.getText()) && !TextUtils.isEmpty(confirmPassword.getText()) &&
                            passwordSignUp.getText().toString().equals(confirmPassword.getText().toString())){

                        FirebaseAuth.getInstance().createUserWithEmailAndPassword(emailSignUp.getText().toString(),
                                passwordSignUp.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    Intent same = new Intent(getActivity(), MainActivity.class);
                                    Bundle b = new Bundle();
                                    b.putString("emailfromsignup",emailSignUp.getText().toString());
                                    same.putExtras(b);
                                    startActivity(same);
                                    getActivity().finish();

                                }else{
                                    showAlert();
                                }
                            }
                        });

                    }else if(TextUtils.isEmpty(passwordSignUp.getText()) || TextUtils.isEmpty(confirmPassword.getText())){
                        showAlert(new BadPasswordAlert());
                    }else{
                        showAlert(new PasswordsNotEqualAlert());
                    }
                }
            }
        });


        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==GOOGLE_SIGN_UP){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);

                if(account!=null){
                    AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(),null);
                    FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                Intent toMenu = new Intent(getActivity(), MenuMainActivity.class);
                                name = account.getDisplayName();
                                email = account.getEmail();
                                editor.putString("username",name);
                                editor.putString("email",email);
                                editor.commit();
                                //uploadUser();
                                FirebaseDatabase database = FirebaseDatabase.getInstance("https://annaapp-322219-default-rtdb.europe-west1.firebasedatabase.app/");
                                DatabaseReference ref =  database.getReference("PROBES");
                                ref.push().setValue(new UserTuple(name,email,null));
                                startActivity(toMenu);
                                getActivity().finish();

                            }else{
                                showAlert();
                            }
                        }
                    });

                }
            } catch (ApiException e) {
                showAlert();
            }
        }
    }

    private void showAlert(Alert alert){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(String.valueOf(R.string.error));
        builder.setMessage(alert.getAlertMessage());
        builder.setPositiveButton(String.valueOf(R.string.ok),null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    private void showAlert(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Error");
        builder.setMessage("Se ha producido un error registrando al usuario");
        builder.setPositiveButton("Aceptar",null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }
/*
    public void uploadUser(){

        DatabaseReference rootReference = FirebaseDatabase.getInstance().getReference();
        DatabaseReference usersRef = rootReference.child("users");

        userTuple = new UserTuple(name,email,null);
        usersRef.push().setValue(userTuple);
        return;
    }

 */

}