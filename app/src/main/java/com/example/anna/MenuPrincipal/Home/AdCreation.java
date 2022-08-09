package com.example.anna.MenuPrincipal.Home;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.anna.MenuPrincipal.CollaboratorMenu;
import com.example.anna.Models.HotNews;
import com.example.anna.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class AdCreation extends AppCompatActivity {

    private final Calendar myCalendar = Calendar.getInstance();
    private EditText dateText, title, description;
    private String myFormat;
    private SimpleDateFormat dateFormat;
    private SharedPreferences userInfoPrefs;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_creation);

        userInfoPrefs = getSharedPreferences("USERINFO",MODE_PRIVATE);

        title = findViewById(R.id.adTitle);
        description = findViewById(R.id.adDescription);
        Button postAd = findViewById(R.id.postAd);

        myFormat = "MM/dd/yy";
        dateFormat = new SimpleDateFormat(myFormat, Locale.GERMANY);

        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                finish();
                startActivity(new Intent(getApplicationContext(), CollaboratorMenu.class));
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);

        dateText = (EditText) findViewById(R.id.adDate);
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, day);
                updateLabel();
            }
        };

        dateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(AdCreation.this, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        postAd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (title.getText().toString().isEmpty()) {
                    Toast.makeText(AdCreation.this, "Please introduce a title", Toast.LENGTH_SHORT).show();
                } else if (dateText.getText().toString().isEmpty()){
                    Toast.makeText(AdCreation.this, "Please introduce an end date", Toast.LENGTH_SHORT).show();
                }else{
                    postAdvertisement();
                }
            }
        });
    }

    private void postAdvertisement(){
        HotNews hotNews = new HotNews();
        hotNews.setTitle(title.getText().toString());
        try {
            hotNews.setEndDate(dateFormat.parse(dateText.getText().toString()));
        } catch (ParseException exception) {
            exception.printStackTrace();
        }
        hotNews.setDescription(description.getText().toString());
        String key = userInfoPrefs.getString("userKey",null);

        hotNews.setAuthor(key);
        Map<String,Object> map = new HashMap<>();
        map.put("key",key);

        DocumentReference adReference = FirebaseFirestore.getInstance().collection("Advertisements").document(key);
        adReference.set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                adReference.collection("AdsOfUser").add(hotNews).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(getApplicationContext(),"Advertisement created correctly",Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
                FirebaseFirestore.getInstance().collection("AllAds").add(hotNews).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {

                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

    }

    private void updateLabel() {
        dateText.setText(dateFormat.format(myCalendar.getTime()));
    }


}
