package com.example.anna.MenuPrincipal;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.anna.Discount;
import com.example.anna.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StreamDownloadTask;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class FragmentMyDiscounts extends Fragment implements DiscountAdapter.OnMyDiscountsListener {

    private RecyclerView rvDiscounts;
    private DiscountAdapter discountAdapter;
    private SharedPreferences userInfoPrefs;
    private String usermail;
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private List<Discount> discountsUsed;
    private FirebaseFirestore firebaseFirestore;
    private CollectionReference discountsDBRef;


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

        View root = inflater.inflate(R.layout.activity_fragment_mydiscounts, container, false);
        rvDiscounts = root.findViewById(R.id.recyclerDiscounts);
        discountAdapter = new DiscountAdapter(getContext(), loadDiscountsFromFirestore(),this);
        rvDiscounts.setLayoutManager(new LinearLayoutManager(getContext()));
        rvDiscounts.setAdapter(discountAdapter);

        return root;

    }

    public List<Discount> loadDiscountsFromFirestore() {

        List<Discount> discountList = new ArrayList<>();

        discountsDBRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (QueryDocumentSnapshot documentDiscount : task.getResult()) {
                    Discount auxiliarDisc = new Discount();

                    Map<String, Object> data = documentDiscount.getData();
                    System.out.println("\n\n" + data + "\n\n");

                    auxiliarDisc.setDescription(data.get("description").toString());
                    auxiliarDisc.setDiscountPercentage(Integer.parseInt(data.get("discountPercentage").toString()));
                    auxiliarDisc.setImageRef(data.get("imageRef").toString());
                    auxiliarDisc.setName(data.get("name").toString());
                    System.out.println("\n\nAIXO ES EL AUXILIAR DISCOUNT\n" + auxiliarDisc.toString());
                    discountList.add(auxiliarDisc);
                }

                discountAdapter = new DiscountAdapter(getContext(), discountList, FragmentMyDiscounts.this);
                rvDiscounts.setLayoutManager(new LinearLayoutManager(getContext()));
                rvDiscounts.setAdapter(discountAdapter);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        return discountList;

    }

    @Override
    public void onMyDiscountsClick(Discount discount) {
        new DiscountClickedDialog(discount.getUriImg(),discount.getName(),getContext());
    }
}
