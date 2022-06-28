package com.example.anna.MenuPrincipal.Routes;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.budiyev.android.codescanner.CodeScanner;
import com.example.anna.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class StationDetail extends AppCompatActivity {


    private String stationName, routeID;
    private List<String> stringImages;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.station_detail);

        stationName = getIntent().getStringExtra("stationName");
        routeID = getIntent().getStringExtra("routeID");

        Button button = findViewById(R.id.buttonCertificateVisit);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction().replace(R.id.wrapperScanner,new ScannerFragment()).commit();
            }
        });

        //getPictures(stationName, routeID);
    }
























    public void getPictures(String stationName, String routeID){
        CollectionReference collectionReference = FirebaseFirestore.getInstance().collection("Routes")
                .document(routeID).collection("Stations");
        collectionReference.whereEqualTo("name",stationName).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot document: task.getResult()){
                        Map<String,Object> map = document.getData();
                        stringImages = (List<String>) map.get("imageRefs");
                    }
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

}
