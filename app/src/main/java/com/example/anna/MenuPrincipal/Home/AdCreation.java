package com.example.anna.MenuPrincipal.Home;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.anna.MenuPrincipal.CollaboratorMenu;
import com.example.anna.Models.HotNews;
import com.example.anna.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class AdCreation extends AppCompatActivity {

    private final Calendar myCalendar = Calendar.getInstance();
    private EditText dateText, title, description;
    private SimpleDateFormat dateFormat;
    private SharedPreferences userInfoPrefs;
    private ActivityResultLauncher<String> selectImage;
    private ImageView adPicture;
    private Uri pictureUri;
    private StorageReference storageReference;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ad_creation);

        userInfoPrefs = getSharedPreferences("USERINFO", MODE_PRIVATE);
        storageReference = FirebaseStorage.getInstance().getReference("advertisements");

        title = findViewById(R.id.adTitle);
        description = findViewById(R.id.adDescription);
        Button postAd = findViewById(R.id.postAd);
        Button pickPictureButton = findViewById(R.id.AdbuttonAddPicture);
        String myFormat = "MM/dd/yy";
        dateFormat = new SimpleDateFormat(myFormat, Locale.GERMANY);
        dateText = findViewById(R.id.adDate);
        adPicture = findViewById(R.id.adCreationPicture);



        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                finish();
                startActivity(new Intent(getApplicationContext(), CollaboratorMenu.class));
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);

        DatePickerDialog.OnDateSetListener date = (view, year, month, day) -> {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, month);
            myCalendar.set(Calendar.DAY_OF_MONTH, day);
            updateLabel();
        };


        dateText.setOnClickListener(view -> new DatePickerDialog(AdCreation.this, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show());
        postAd.setOnClickListener(view -> {
            if (title.getText().toString().isEmpty()) {
                Toast.makeText(AdCreation.this, "Please introduce a title", Toast.LENGTH_SHORT).show();
            } else if (dateText.getText().toString().isEmpty()) {
                Toast.makeText(AdCreation.this, "Please introduce an end date", Toast.LENGTH_SHORT).show();
            } else if (pictureUri==null) {
                Toast.makeText(AdCreation.this,"Please select an ad-related picture",Toast.LENGTH_SHORT).show();
            } else {
                postAdvertisement();
            }
        });

        selectImage = registerForActivityResult(new ActivityResultContracts.GetContent(), result -> {
            pictureUri = result;
            if (result != null) {
                Glide.with(getApplicationContext()).load(result).into(adPicture);
                pictureUri = result;
            }
        });
        adPicture.setOnClickListener(v -> changePicture());
        pickPictureButton.setOnClickListener(v -> changePicture());


    }

    /*
    storageReference.putFile(result)
                        .addOnSuccessListener(taskSnapshot -> Toast.makeText(getApplicationContext(),"Profile picture changed successfully!",Toast.LENGTH_SHORT).show())
                        .addOnFailureListener(e -> Toast.makeText(getApplicationContext(),"Could not change profile picture. Try later. ",Toast.LENGTH_SHORT).show());
    */

    private void postAdvertisement() {
        HotNews hotNews = new HotNews();
        hotNews.setTitle(title.getText().toString());
        try {
            hotNews.setEndDate(dateFormat.parse(dateText.getText().toString()));
        } catch (ParseException exception) {
            exception.printStackTrace();
        }
        hotNews.setDescription(description.getText().toString());
        String userkey = userInfoPrefs.getString("userKey", null);
        hotNews.setAuthor(userkey);
        //hotNews.setUri(pictureUri.toString());


        Map<String, Object> map = new HashMap<>();
        map.put("key", userkey);
        map.put("username", userInfoPrefs.getString("username", null));


        ///


        DocumentReference adReference = FirebaseFirestore.getInstance().collection("Advertisements").document(userkey);
        adReference.set(map).addOnSuccessListener(unused -> {
            Map<String, Object> auxiliarMap = new HashMap<>();
            auxiliarMap.put("auxiliar", "toBeUpdated");
            auxiliarMap.put("key", userkey);
            adReference.collection("AdsOfUser").add(auxiliarMap).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    adReference.collection("AdsOfUser").whereEqualTo("auxiliar", "toBeUpdated").whereEqualTo("key", userkey).get().addOnCompleteListener(
                            new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        String adKey = task.getResult().getDocuments().get(0).getId();
                                        hotNews.setKey(adKey);
                                        updateAuxiliarAd(adKey, adReference.collection("AdsOfUser"), hotNews);

                                    }
                                }
                            }
                    );

                }
            });
        });


        ///
/*
        DocumentReference adReference = FirebaseFirestore.getInstance().collection("Advertisements").document(key);
        adReference.set(map).addOnSuccessListener(unused -> {
            adReference.collection("AdsOfUser").add(hotNews).addOnCompleteListener(task -> {
                if(task.isSuccessful()){

                    Toast.makeText(getApplicationContext(),"Advertisement created correctly",Toast.LENGTH_SHORT).show();
                    FirebaseFirestore.getInstance().collection("AllAds").add(hotNews).addOnCompleteListener(task2 -> {
                        if(task2.isSuccessful())
                            finish();
                    });

                }
            });
        });

 */

    }

    private void updateAuxiliarAd(String adKey, CollectionReference reference, HotNews hothotNews) {
        reference.document(adKey).set(hothotNews).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(getApplicationContext(), "Advertisement created correctly", Toast.LENGTH_SHORT).show();
                FirebaseFirestore.getInstance().collection("AllAds").add(hothotNews).addOnCompleteListener(task2 -> {
                    if (task2.isSuccessful())
                        storageReference.child(adKey).putFile(pictureUri).addOnSuccessListener(taskSnapshot ->{
                            System.out.println("PICTURE SUCCESSFULLY ADDED TO ADVERTISEMENT AND SAVED");
                        });
                        finish();
                });
            }
        });
    }

    private void updateLabel() {
        dateText.setText(dateFormat.format(myCalendar.getTime()));
    }

    private void changePicture() {
        selectImage.launch("image/*");
    }

}
