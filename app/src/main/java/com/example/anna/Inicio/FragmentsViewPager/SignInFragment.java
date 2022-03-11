package com.example.anna.Inicio.FragmentsViewPager;

import android.app.AlertDialog;
import android.app.DownloadManager;
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

import com.example.anna.Alerts.Alert;
import com.example.anna.Alerts.BadPasswordAlert;
import com.example.anna.Alerts.EmptyEmailFieldAlert;
import com.example.anna.Alerts.UnsuccessfulSignInAlert;
import com.example.anna.Alerts.UserNotRegisteredAlert;
import com.example.anna.Inicio.MainActivity;
import com.example.anna.MenuPrincipal.MenuMainActivity;
import com.example.anna.R;
import com.firebase.ui.auth.data.model.Resource;
import com.firebase.ui.auth.util.data.TaskFailureLogger;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import kotlin.Result;

public class SignInFragment extends Fragment{


    private final int GOOGLE_SIGN_IN = 101;
    private EditText emailSignIn, passwordSignIn;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private ImageButton googleButton;
    private Button btnSignIn;
    private String received, name, email;
    private Intent toMenu;
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
        toMenu = new Intent(getContext(), MenuMainActivity.class);
        emailSignIn = view.findViewById(R.id.email);
        passwordSignIn = view.findViewById(R.id.password);
        emailSignIn.setText(received);

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
                        if(TextUtils.isEmpty(emailSignIn.getText())) {
                            showAlert(new EmptyEmailFieldAlert(getContext()));
                        /*
                        Part de codi que serviria per comprovar el format de l'email.

                        else if(validateEmail(mEdtTxtEmail.getText().toString().trim())){
                            // your code
                        }

                        private boolean validateEmail(String data){
                        Pattern emailPattern = Pattern.compile(".+@.+\\.[a-z]+");
                        Matcher emailMatcher = emailPattern.matcher(data);
                        return emailMatcher.matches();

                        */
                        }else if(!TextUtils.isEmpty(emailSignIn.getText()) && !TextUtils.isEmpty(passwordSignIn.getText())){
                            /*
                            Fer la comprovacio, query a la base de dades de si existeix o no l'usuari. Indicar-ho en cas que no, seguir amb el proces en cas que si.

                             */
                           FirebaseAuth.getInstance().signInWithEmailAndPassword(emailSignIn.getText().toString(),passwordSignIn.getText().toString())
                                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if(task.isSuccessful()) {
                                                editor.putString("email",emailSignIn.getText().toString());
                                                editor.commit();
                                                startActivity(toMenu);
                                                getActivity().finish();
                                            }else{
                                                showAlert(task.getException().getMessage());
                                            }
                                        }
                                    });

                        }else{
                            showAlert(new UnsuccessfulSignInAlert(getContext()));
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
                //Resource.forFailure()
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
                                name = account.getDisplayName();
                                email = account.getEmail();
                                FirebaseDatabase database = FirebaseDatabase.getInstance("https://annaapp-322219-default-rtdb.europe-west1.firebasedatabase.app/");
                                DatabaseReference ref =  database.getReference("users");
                                /*if(!emailIsInUse(ref,email)){
                                    showAlert(new UserNotRegisteredAlert(getContext()));
                                }

                                 */
                                editor.putString("email",email);
                                editor.putString("username",name);
                                editor.commit();
                                startActivity(toMenu);
                                getActivity().finish();

                            }else{
                                //task.getException().getMessage()
                                showAlert(new UnsuccessfulSignInAlert(getContext()));
                            }
                        }
                    });

                }
            } catch (ApiException e) {
                showAlert(new UnsuccessfulSignInAlert(getContext()));
            }
        }
    }


    @Override
    public void onResume() {
        super.onResume();
       if(sharedPreferences.getString("email",null)!=null){
            emailSignIn.setText(sharedPreferences.getString("email",null));
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

    private boolean emailIsInUse(DatabaseReference reference, String email){
        boolean isInUse = false;
        Query query = reference.orderByChild("email").equalTo(email);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                    changeboolean(isInUse); // NO EM DEIXA FICARHO A TRUE
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        return isInUse;
    }

    public boolean changeboolean(boolean bool){
        return !bool;
    }


}