package com.example.anna.MenuPrincipal.Routes;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.anna.MenuPrincipal.MenuMainActivity;
import com.example.anna.R;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.List;
import java.util.Map;

public class StationDetail extends AppCompatActivity {


    private List<?> stringImages;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.station_detail);

        String stationName = getIntent().getStringExtra("stationName");
        String routeID = getIntent().getStringExtra("routeID");

        Button button = findViewById(R.id.buttonCertificateVisit);
        button.setOnClickListener(v -> getSupportFragmentManager().beginTransaction().replace(R.id.wrapperScanner,new ScannerFragment()).commit());

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                startActivity(new Intent(getApplicationContext(),MenuMainActivity.class));
                finishAffinity();
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);

        //getPictures(stationName, routeID);
    }











    public void getPictures(String stationName, String routeID){
        CollectionReference collectionReference = FirebaseFirestore.getInstance().collection("Routes")
                .document(routeID).collection("Stations");
        collectionReference.whereEqualTo("name",stationName).get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                for(QueryDocumentSnapshot document: task.getResult()){
                    Map<String,Object> map = document.getData();
                    stringImages = (List<?>) map.get("imageRefs");
                }
            }

        }).addOnFailureListener(e -> {

        });
    }

}
