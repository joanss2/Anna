package com.example.anna.MenuPrincipal.MyDiscounts;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.text.SymbolTable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.anna.MenuPrincipal.Home.Discounts.DiscountClickedDialog;
import com.example.anna.MenuPrincipal.Home.Discounts.DiscountsActivity;
import com.example.anna.MenuPrincipal.OnDiscountClickListener;
import com.example.anna.Models.Discount;
import com.example.anna.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class FragmentMyDiscounts extends Fragment implements OnDiscountClickListener {


    private RecyclerView rvDiscounts;
    private MyDiscountsAdapter myDiscountsAdapter;
    private SharedPreferences userInfoPrefs;
    private String usermail;
    private Set<String> setOfDiscounts;
    private FirebaseDatabase database;
    private FirebaseFirestore firebaseFirestore;
    private CollectionReference discountsDBRef,discountsUsed;
    private boolean hasDiscounts;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        userInfoPrefs = getActivity().getSharedPreferences("USERINFO", Context.MODE_PRIVATE);
        usermail = userInfoPrefs.getString("email", null);
        firebaseFirestore = FirebaseFirestore.getInstance();
        discountsUsed = firebaseFirestore.collection("DiscountsUsed").document(userInfoPrefs.getString("userKey", null))
        .collection("DiscountsReferenceList");
        discountsDBRef = firebaseFirestore.collection("Discounts");
        hasDiscounts = userInfoPrefs.getBoolean("hasDiscounts", false);

        database = FirebaseDatabase.getInstance("https://annaapp-322219-default-rtdb.europe-west1.firebasedatabase.app/");

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root;

        if (hasDiscounts) {

            root = inflater.inflate(R.layout.activity_fragment_mydiscounts, container, false);
            rvDiscounts = root.findViewById(R.id.recyclerDiscounts);


            FirestoreRecyclerOptions<Discount> options = new FirestoreRecyclerOptions.Builder<Discount>()
                    .setQuery(discountsUsed, Discount.class)
                    .build();


            myDiscountsAdapter = new MyDiscountsAdapter(options, this, getContext());
            rvDiscounts.setLayoutManager(new LinearLayoutManager(getContext()));
            rvDiscounts.setAdapter(myDiscountsAdapter);


        } else {
            root = inflater.inflate(R.layout.activity_fragment_mydiscounts_empty, container, false);
        }

        return root;
    }

    @Override
    public void onDiscountClick(Discount discount) {
        new DiscountClickedDialog(discount.getUriImg(), discount.getName(), getContext(), discount.getKey());
    }

    @Override
    public void onStart() {
        if (hasDiscounts) {
            myDiscountsAdapter.startListening();
        }

        super.onStart();
    }

    @Override
    public void onStop() {
        if (hasDiscounts) {
            myDiscountsAdapter.stopListening();
        }
        super.onStop();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (!hasDiscounts) {

            Snackbar snackbar = Snackbar.make(view, "GO TO DISCOUNTS PAGE", Snackbar.LENGTH_INDEFINITE);
            snackbar.setAction("GO", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(getContext(), DiscountsActivity.class));
                }
            });
            snackbar.show();
        }
    }
}
