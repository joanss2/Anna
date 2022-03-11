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

public class SignUpFragment extends Fragment {


    private EditText emailSignUp, passwordSignUp, confirmPassword;
    private Button btnSignUp;
    private ImageButton  googleButton;
    private final int GOOGLE_SIGN_UP = 100;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private String name, email;
    //private DatabaseReference reference;
    private FirebaseDatabase database;
    private Intent same;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getActivity().getSharedPreferences(String.valueOf
                (R.string.sharedpreferencesfile),Context.MODE_PRIVATE);
        //editor = sharedPreferences.edit();
        //reference = FirebaseDatabase.getInstance("https://annaapp-322219-default-rtdb.europe-west1.firebasedatabase.app/").getReference();
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://annaapp-322219-default-rtdb.europe-west1.firebasedatabase.app/");
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

            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(passwordSignUp.getText()) && !TextUtils.isEmpty(confirmPassword.getText()) &&
                        passwordSignUp.getText().toString().equals(confirmPassword.getText().toString())) {

                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(emailSignUp.getText().toString(),
                            passwordSignUp.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                same = new Intent(getActivity(), MainActivity.class);
                                Bundle b = new Bundle();
                                b.putString("emailfromsignup", emailSignUp.getText().toString());
                                same.putExtras(b);
                                startActivity(same);
                                getActivity().finish();

                            } else {
                                showAlert(task.getException().getMessage());
                            }
                        }
                    });
                }else if(TextUtils.isEmpty(passwordSignUp.getText()) || TextUtils.isEmpty(confirmPassword.getText())){
                    showAlert(new BadPasswordAlert(getContext()));
                }else{
                    showAlert(new PasswordsNotEqualAlert(getContext()));
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
                                DatabaseReference ref =  database.getReference("users");
                                UserTuple userTuple = new UserTuple(name,email,null);
                                /*if(!emailIsInUse(ref,email)) {
                                    ref.push().setValue(userTuple);
                                }

                                 */
                                startActivity(toMenu);
                                getActivity().finish();

                            }else{
                                showAlert(task.getException().getMessage());
                            }
                        }
                    });

                }
            } catch (ApiException e) {
                showAlert(new UnSuccessfulSignUpAlert(getContext()));
            }
        }
    }

    private void showAlert(Alert alert){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.error));
        builder.setMessage(alert.getAlertMessage());
        builder.setPositiveButton(getString(R.string.ok),null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    private void showAlert(String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.error));
        builder.setMessage(message);
        builder.setPositiveButton(getString(R.string.ok),null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

/*
    private boolean emailIsInUse(DatabaseReference reference, String email){
        boolean isInUse = false;
        Query query = reference.orderByChild("email").equalTo("iscoralarcon@gmail.com");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists()) {
                    //isInUse=true;// NO EM DEIXA FICARHO A TRUE
                    Toast.makeText(getContext(),"EN TEORIA ESTA A LA BBDD",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        return isInUse;
    }


 */
}