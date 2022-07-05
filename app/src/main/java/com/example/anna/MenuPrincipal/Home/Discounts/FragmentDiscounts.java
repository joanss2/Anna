package com.example.anna.MenuPrincipal.Home.Discounts;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.example.anna.MenuPrincipal.OnDiscountClickListener;
import com.example.anna.Models.Discount;
import com.example.anna.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FragmentDiscounts extends Fragment implements OnDiscountClickListener {

    private RecyclerView rvDiscounts;
    private SharedPreferences userInfoPrefs;
    private String usermail;
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private FirebaseFirestore firebaseFirestore;
    private CollectionReference discountsDBRef;
    private DiscountActivityAdapter discountActAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userInfoPrefs = getActivity().getSharedPreferences("USERINFO", Context.MODE_PRIVATE);
        usermail = userInfoPrefs.getString("email", null);
        firebaseFirestore = FirebaseFirestore.getInstance();
        discountsDBRef = firebaseFirestore.collection("Discounts");
        database = FirebaseDatabase.getInstance("https://annaapp-322219-default-rtdb.europe-west1.firebasedatabase.app/");
        reference = database.getReference("users");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_discounts, container, false);

        rvDiscounts = view.findViewById(R.id.recyclerDiscountsActivity);


        FirestoreRecyclerOptions<Discount> options = new FirestoreRecyclerOptions.Builder<Discount>()
                .setQuery(discountsDBRef,Discount.class).build();
        discountActAdapter = new DiscountActivityAdapter(options,getContext(), this);
        rvDiscounts.setLayoutManager(new LinearLayoutManager(getContext()));
        rvDiscounts.setAdapter(discountActAdapter);

        return view;
    }

    @Override
    public void onDiscountClick(Discount discount) {
        new DiscountClickedDialog(discount.getUriImg(),discount.getName(),getContext(), discount.getKey());
    }

    @Override
    public void onStart() {
        super.onStart();
        discountActAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        discountActAdapter.stopListening();
    }

    /*

    private RecyclerView rvDiscounts;
    private SharedPreferences userInfoPrefs;
    private String usermail;
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private FirebaseFirestore firebaseFirestore;
    private CollectionReference discountsDBRef;
    private DiscountActivityAdapter discountActAdapter;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        userInfoPrefs = getSharedPreferences("USERINFO", Context.MODE_PRIVATE);
        usermail = userInfoPrefs.getString("email", null);
        firebaseFirestore = FirebaseFirestore.getInstance();
        discountsDBRef = firebaseFirestore.collection("Discounts");
        database = FirebaseDatabase.getInstance("https://annaapp-322219-default-rtdb.europe-west1.firebasedatabase.app/");
        reference = database.getReference("users");

        setContentView(R.layout.activity_discounts);
        rvDiscounts = findViewById(R.id.recyclerDiscountsActivity);


        FirestoreRecyclerOptions<Discount> options = new FirestoreRecyclerOptions.Builder<Discount>()
                .setQuery(discountsDBRef,Discount.class).build();
        discountActAdapter = new DiscountActivityAdapter(options,this, this);
        rvDiscounts.setLayoutManager(new LinearLayoutManager(this));
        rvDiscounts.setAdapter(discountActAdapter);

    }


    @Override
    public void onDiscountClick(Discount discount) {
        new DiscountClickedDialog(discount.getUriImg(),discount.getName(),this, discount.getKey());
    }

    @Override
    protected void onStart() {
        super.onStart();
        discountActAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        discountActAdapter.stopListening();
    }

     */
}