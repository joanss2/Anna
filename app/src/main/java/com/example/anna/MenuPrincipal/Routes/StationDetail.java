package com.example.anna.MenuPrincipal.Routes;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.anna.MenuPrincipal.MenuMainActivity;
import com.example.anna.MenuPrincipal.Routes.Verification.ScannerFragment;
import com.example.anna.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.FileDescriptor;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class StationDetail extends AppCompatActivity {


    private List<String> receivedStrings;
    private List<Uri> receivedUris;
    private ImageSwitcher imageSwitcher;
    private ImageButton nextButton, backSwitcherButton;
    private StorageReference storageReference;
    private int counter = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.station_detail);

        String stationName = getIntent().getStringExtra("stationName");
        String routeID = getIntent().getStringExtra("routeID");
        String stationKey = getIntent().getStringExtra("stationKey");
        String descriptionStation = getIntent().getStringExtra("stationDescription");
        TextView title = findViewById(R.id.stationdetailTitle);
        TextView description = findViewById(R.id.stationdetailDescription);
        description.setText(descriptionStation);
        title.setText(stationName);

        receivedStrings = getIntent().getStringArrayListExtra("uriStrings");

        System.out.println(receivedStrings.size());

        receivedUris = toUris(receivedStrings);

        storageReference = FirebaseStorage.getInstance().getReference(stationName);


        nextButton = findViewById(R.id.imageswitcherbutton);
        backSwitcherButton = findViewById(R.id.imageswitcherbackbutton);
        ImageButton backButton = findViewById(R.id.stationdetailBackButton);
        ImageButton button = findViewById(R.id.buttonCertificateVisit);
        button.setOnClickListener(v -> getSupportFragmentManager().beginTransaction().replace(R.id.wrapperScanner, new ScannerFragment(stationKey, routeID)).commit());

        imageSwitcher = findViewById(R.id.imageSwitcher);

        imageSwitcher.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                ImageView imageView = new ImageView(getApplicationContext());
                imageView.setLayoutParams(new ImageSwitcher.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT));
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);

                Glide.with(getApplicationContext()).load(receivedUris.get(0)).into(imageView);
                return imageView;

            }
        });

        Animation out = AnimationUtils.loadAnimation(this, android.R.anim.slide_out_right);
        Animation in = AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left);

        imageSwitcher.setOutAnimation(out);
        imageSwitcher.setInAnimation(in);


        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                counter++;
                if (counter == receivedUris.size()){
                    counter = 0;
                }
                Glide.with(getApplicationContext()).load(receivedUris.get(counter)).into((ImageView) imageSwitcher.getCurrentView());

            }
        });
        backSwitcherButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                counter--;
                if (counter == -1){
                    counter = receivedUris.size()-1;
                }
                Glide.with(getApplicationContext()).load(receivedUris.get(counter)).into((ImageView) imageSwitcher.getCurrentView());

            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), MenuMainActivity.class));
                finishAffinity();
            }
        });

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                startActivity(new Intent(getApplicationContext(), MenuMainActivity.class));
                finishAffinity();
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);
    }

    private List<Uri> toUris(List<String> receivedStrings) {
        List<Uri> uri = new ArrayList<>();
        for (String string : receivedStrings) {
            Uri aux = Uri.parse(string);
            uri.add(aux);
        }
        return uri;
    }




}
