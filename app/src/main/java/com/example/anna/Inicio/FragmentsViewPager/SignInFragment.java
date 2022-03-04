package com.example.anna.Inicio.FragmentsViewPager;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
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
import android.widget.ImageView;

import com.example.anna.Alerts.Alert;
import com.example.anna.Alerts.EmailFieldEmptyAlert;
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

public class SignInFragment extends Fragment{


    private final int GOOGLE_SIGN_IN = 101;
    private EditText email, password;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private ImageButton googleButton;
    private Button btnSignIn;
    private String received;
    private Intent intent;
    private Uri userPic;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getActivity().getSharedPreferences(String.valueOf(R.string.sharedpreferencesfile), Context.MODE_PRIVATE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if(getArguments()!=null){
            received = getArguments().getString("emailfromsignup");
        }

        editor = sharedPreferences.edit();
        View view = inflater.inflate(R.layout.fragment_sign_in, container, false);
        googleButton = view.findViewById(R.id.googleIn);
        btnSignIn = view.findViewById(R.id.buttonsignin);
        intent = new Intent(getContext(), MenuMainActivity.class);
        email = view.findViewById(R.id.email);
        password = view.findViewById(R.id.password);
        email.setText(received);


        btnSignIn.setOnClickListener(new View.OnClickListener() {

            /* En aquesta part es voldria separar varios casos:
            -   Primer de tot si el format del string en el camp email es incorrecte avisar que el format no es correcte.
            -   Si passa l'anterior, Que es fagi una query per a l'existència de l'usuari.
            -   Si l'usuari existeix que comprovi si la contrasenya es o no correcta.
            -   Si aconsegueix passar tot l'anterior que inicii la sessio.
             */
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.buttonsignin:

                        /*
                        Part de codi que serviria per comprovar el format de l'email.

                        if(validateEmail(mEdtTxtEmail.getText().toString().trim())){
                            // your code
                        }

                        private boolean validateEmail(String data){
                        Pattern emailPattern = Pattern.compile(".+@.+\\.[a-z]+");
                        Matcher emailMatcher = emailPattern.matcher(data);
                        return emailMatcher.matches();

                        */
                        if(TextUtils.isEmpty(email.getText())) {
                            showAlert(new EmailFieldEmptyAlert());
                        }else if(!TextUtils.isEmpty(email.getText()) && !TextUtils.isEmpty(password.getText())){
                           FirebaseAuth.getInstance().signInWithEmailAndPassword(email.getText().toString(),password.getText().toString())
                                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if(task.isSuccessful()) {
                                                editor.putString("email",email.getText().toString());
                                                editor.commit();
                                                startActivity(intent);
                                                getActivity().finish();
                                            }else{

                                                showAlert();
                                            }
                                        }
                                    });

                        }else{
                            showAlert();
                        }
                }
            }
        });

        googleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoogleSignInOptions googleConf = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();

                GoogleSignInClient googleClient = GoogleSignIn.getClient(getActivity(),googleConf);
                googleClient.signOut();
                startActivityForResult(googleClient.getSignInIntent(),GOOGLE_SIGN_IN);
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==GOOGLE_SIGN_IN){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);

                if(account!=null){

                    AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(),null);
                    FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                //userPic = account.getPhotoUrl();
                                //editor.putString("fotouri",userPic.toString());
                                String name = account.getDisplayName();
                                String mail = account.getEmail();
                                editor.putString("email",mail);
                                editor.putString("username",name);
                                editor.commit();
                                startActivity(intent);
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


    private void showAlert(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Error");
        builder.setMessage("Se ha producido un error iniciando sesion");
        builder.setPositiveButton("Aceptar",null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onResume() {
        super.onResume();
       if(sharedPreferences.getString("email",null)!=null){
            email.setText(sharedPreferences.getString("email",null));
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


}