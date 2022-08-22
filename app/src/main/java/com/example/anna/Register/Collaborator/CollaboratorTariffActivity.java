package com.example.anna.Register.Collaborator;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.anna.Models.Tariff;
import com.example.anna.R;
import com.example.anna.Register.MainActivity;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class CollaboratorTariffActivity extends AppCompatActivity implements ShopAdapter.OnTariffClick {

    private final CollectionReference shopReference = FirebaseFirestore.getInstance().collection("Shop");
    private ShopAdapter shopAdapter;
    private boolean prefsDeletable = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.collaborator_shop);
        
        if(getIntent().getBooleanExtra("ended",false)){
            createEndedSubscriptionDialog();
        }

        OnBackPressedCallback callback = new OnBackPressedCallback(true ) {
            @Override
            public void handleOnBackPressed() {
                getSharedPreferences("USERINFO",MODE_PRIVATE).edit().clear().apply();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finishAffinity();
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);

        RecyclerView rvShop = findViewById(R.id.shopRecyclerView);
        rvShop.setLayoutManager(new LinearLayoutManager(this));

        FirestoreRecyclerOptions<Tariff> options = new FirestoreRecyclerOptions.Builder<Tariff>()
                .setQuery(shopReference, Tariff.class).build();

        shopAdapter = new ShopAdapter(options, this, this);
        rvShop.setAdapter(shopAdapter);

    }

    private void createEndedSubscriptionDialog() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.ended_sub_dialog);
        Button okButton = dialog.findViewById(R.id.okbuttonSubEnded);
        okButton.setOnClickListener(view -> dialog.dismiss());
        dialog.create();
        dialog.show();
    }

    @Override
    public void onTariffClick(Tariff tariff) {
        Toast.makeText(this,"You have clicked the "+tariff.getCondition()+" tariff. Enjoy!",Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, PaymentSelected.class);
        intent.putExtra("price",tariff.getPrice());
        intent.putExtra("tariff",tariff);
        prefsDeletable = false;
        startActivity(intent);
        finish();
    }

    @Override
    public void onStart() {
        shopAdapter.startListening();
        super.onStart();
    }

    @Override
    public void onStop() {
        shopAdapter.stopListening();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(prefsDeletable){
            SharedPreferences sharedPreferences = getSharedPreferences("USERINFO",MODE_PRIVATE);
            sharedPreferences.edit().clear().apply();
        }

    }
}
