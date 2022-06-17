package com.example.anna.MenuPrincipal.MyDiscounts;

import android.content.Context;
import android.content.SharedPreferences;
import android.icu.text.SymbolTable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.anna.MenuPrincipal.Home.Discounts.DiscountClickedDialog;
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
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FragmentMyDiscounts extends Fragment implements OnDiscountClickListener {


    private RecyclerView rvDiscounts;
    private MyDiscountsAdapter myDiscountsAdapter;
    private SharedPreferences userInfoPrefs;
    private String usermail;
    private List<String> discountsUsedRefs;
    private FirebaseDatabase database;
    private FirebaseFirestore firebaseFirestore;
    private CollectionReference discountsUsedDBRef;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        userInfoPrefs = getActivity().getSharedPreferences("USERINFO", Context.MODE_PRIVATE);
        usermail = userInfoPrefs.getString("email", null);
        firebaseFirestore = FirebaseFirestore.getInstance();

        discountsUsedDBRef = firebaseFirestore.collection("DiscountsUsed")
                .document(userInfoPrefs.getString("userKey",null))
                .collection("DiscountsReferenceList");
        discountsUsedRefs = new ArrayList<>();

        discountsUsedDBRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for(QueryDocumentSnapshot document: task.getResult()){
                    discountsUsedRefs.add((document.getData().toString()));
                    System.out.println(discountsUsedRefs.toString());

                }}

            }
        ).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

        database = FirebaseDatabase.getInstance("https://annaapp-322219-default-rtdb.europe-west1.firebasedatabase.app/");


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.activity_fragment_mydiscounts, container, false);
        rvDiscounts = root.findViewById(R.id.recyclerDiscounts);



/*
        FirestoreRecyclerOptions<Discount> options = new FirestoreRecyclerOptions.Builder<Discount>()
                .setQuery(discountsUsedDBRef, Discount.class)
                .build();




        myDiscountsAdapter = new MyDiscountsAdapter(options, this, getContext());
        rvDiscounts.setLayoutManager(new LinearLayoutManager(getContext()));
        rvDiscounts.setAdapter(myDiscountsAdapter);
 */
        discountsUsedDBRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for(QueryDocumentSnapshot document: task.getResult()){
                    Map<String,Object> map = document.getData();
                    System.out.println((String) map.get("name"));

            }}
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println("hello failure");
            }
        });
        return root;
    }

    @Override
    public void onDiscountClick(Discount discount) {
        new DiscountClickedDialog(discount.getUriImg(),discount.getName(),getContext(),discount.getKey());
    }

}
