package com.example.anna.MenuPrincipal.Home;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
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
import com.example.anna.Models.HotNews;
import com.example.anna.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class AdEdition extends AppCompatActivity {


    private final Calendar myCalendar = Calendar.getInstance();
    private EditText dateText, title, description;
    private SimpleDateFormat dateFormat;
    private SharedPreferences userInfoPrefs;
    private ActivityResultLauncher<String> selectImage;
    private ImageView adPicture;
    private Uri pictureUri;
    private StorageReference storageReference;
    private String adKey, authorKey;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_edition);

        userInfoPrefs = getSharedPreferences("USERINFO", MODE_PRIVATE);
        storageReference = FirebaseStorage.getInstance().getReference("advertisements");

        Date forIntent = new Date();
        title = findViewById(R.id.adTitleEdition);
        description = findViewById(R.id.adDescriptionEdition);
        Button updateAd = findViewById(R.id.postAdEdition);
        Button pickPictureButton = findViewById(R.id.AdbuttonAddPictureEdition);
        String myFormat = "dd/MM/yyyy";
        dateFormat = new SimpleDateFormat(myFormat, Locale.GERMANY);
        dateText = findViewById(R.id.adDateEdition);
        adPicture = findViewById(R.id.adEditionPicture);

        pickPictureButton.setEnabled(false);
        adPicture.setEnabled(false);


        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                finish();
                //startActivity(new Intent(getApplicationContext(), CollaboratorMenu.class));
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);

        DatePickerDialog.OnDateSetListener date = (view, year, month, day) -> {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, month);
            myCalendar.set(Calendar.DAY_OF_MONTH, day);
            updateLabel();
        };

        if (getIntent().getExtras() != null) {
            Bundle bundle = getIntent().getExtras();

            SimpleDateFormat sdf = new SimpleDateFormat("EE MMM dd HH:mm:ss z yyyy",
                    Locale.ENGLISH);
            Date parsedDate = null;
            try {
                parsedDate = sdf.parse(bundle.getString("date", null));
                @SuppressLint("SimpleDateFormat") SimpleDateFormat print = new SimpleDateFormat("dd/MM/yyyy");
                assert parsedDate != null;
                dateText.setText(print.format(parsedDate));
            } catch (ParseException exception) {
                exception.printStackTrace();
            }

            title.setText(bundle.getString("title", null));
            description.setText(bundle.getString("description", null));
            authorKey = bundle.getString("authorKey", null);
            adKey = bundle.getString("adKey", null);
            FirebaseStorage.getInstance().getReference("advertisements").child(adKey).getDownloadUrl().addOnSuccessListener(
                    new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            pictureUri = uri;
                            Glide.with(getApplicationContext()).load(uri).into(adPicture);
                            pickPictureButton.setEnabled(true);
                            adPicture.setEnabled(true);
                        }
                    }
            );

        }


        dateText.setOnClickListener(view -> new DatePickerDialog(AdEdition.this, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show());
        updateAd.setOnClickListener(view -> {
            if (title.getText().toString().isEmpty()) {
                Toast.makeText(AdEdition.this, "Please introduce a title", Toast.LENGTH_SHORT).show();
            } else if (dateText.getText().toString().isEmpty()) {
                Toast.makeText(AdEdition.this, "Please introduce an end date", Toast.LENGTH_SHORT).show();
            } else if (pictureUri == null) {
                Toast.makeText(AdEdition.this, "Please select an ad-related picture", Toast.LENGTH_SHORT).show();
            } else {
                updateAdvertisement(FirebaseFirestore.getInstance().collection("Advertisements").document(authorKey)
                        .collection("AdsOfUser").document(adKey));
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

    private void updateAdvertisement(DocumentReference documentReference) {
        Date date = new Date();
        try {
            date = dateFormat.parse(dateText.getText().toString());
        } catch (ParseException exception) {
            exception.printStackTrace();
        }

        documentReference.update("title", title.getText().toString(),
                "description", description.getText().toString(),
                "endDate", date).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                updateInAllAds();
            }
        });
    }


    private void updatePictureInStorage() {
        FirebaseStorage.getInstance().getReference("advertisements").child(adKey).putFile(pictureUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                System.out.println("IMAGE UPDATED SUCCESSSUFULLY");
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                finish();
            }
        });
    }

    private void updateInAllAds() {
        Date date = new Date();
        try {
            date = dateFormat.parse(dateText.getText().toString());
        } catch (ParseException exception) {
            exception.printStackTrace();
        }

        DocumentReference documentReference = FirebaseFirestore.getInstance().collection("AllAds").document(adKey);
        documentReference.update("title", title.getText().toString(),
                "description", description.getText().toString(),
                "endDate", date).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                //finish();
                updatePictureInStorage();
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
