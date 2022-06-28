package com.example.anna.Register;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.anna.Models.Tariff;
import com.example.anna.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class CollaboratorTariffActivity extends AppCompatActivity implements ShopAdapter.OnTariffClick{

    private CollectionReference shopReference = FirebaseFirestore.getInstance().collection("Shop");
    private RecyclerView rvShop;
    private ShopAdapter shopAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.collaborator_shop);

        rvShop = findViewById(R.id.shopRecyclerView);
        rvShop.setLayoutManager(new LinearLayoutManager(this));

        FirestoreRecyclerOptions<Tariff> options = new FirestoreRecyclerOptions.Builder<Tariff>()
                .setQuery(shopReference, Tariff.class).build();

        shopAdapter = new ShopAdapter(options, this, this);
        rvShop.setAdapter(shopAdapter);

    }

    @Override
    public void onTariffClick(Tariff tariff) {
        Toast.makeText(this,"You have clicked the "+tariff.getCondition()+" tariff. Enjoy!",Toast.LENGTH_LONG).show();
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
}
