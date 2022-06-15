package com.example.anna.MenuPrincipal.Home.Discounts;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;


import com.example.anna.MenuPrincipal.OnDiscountClickListener;
import com.example.anna.Models.Discount;
import com.example.anna.R;
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

public class DiscountsActivity extends AppCompatActivity implements OnDiscountClickListener {

    private RecyclerView rvDiscounts;
    private DiscountAdapterForActivity discountAdapter;
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
        userInfoPrefs = getSharedPreferences("USERINFO", Context.MODE_PRIVATE);
        usermail = userInfoPrefs.getString("email", null);
        firebaseFirestore = FirebaseFirestore.getInstance();
        discountsDBRef = firebaseFirestore.collection("Discounts");
        database = FirebaseDatabase.getInstance("https://annaapp-322219-default-rtdb.europe-west1.firebasedatabase.app/");
        reference = database.getReference("users");

        setContentView(R.layout.activity_discounts);
        rvDiscounts = findViewById(R.id.recyclerDiscountsActivity);
        discountAdapter = new DiscountAdapterForActivity(this, loadDiscountsFromFirestore(), this);
        rvDiscounts.setLayoutManager(new LinearLayoutManager(this));
        rvDiscounts.setAdapter(discountAdapter);

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
                    auxiliarDisc.setKey(data.get("key").toString());
                    auxiliarDisc.setDescription(data.get("description").toString());
                    auxiliarDisc.setDiscountPercentage(Integer.parseInt(data.get("discountPercentage").toString()));
                    auxiliarDisc.setImageRef(data.get("imageRef").toString());
                    auxiliarDisc.setName(data.get("name").toString());
                    System.out.println("\n\nAIXO ES EL AUXILIAR DISCOUNT\n" + auxiliarDisc.toString());
                    discountList.add(auxiliarDisc);
                }

                discountAdapter = new DiscountAdapterForActivity(getApplicationContext(), discountList, DiscountsActivity.this);
                rvDiscounts.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                rvDiscounts.setAdapter(discountAdapter);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        return discountList;

    }

    @Override
    public void onDiscountClick(Discount discount) {
        new DiscountClickedDialog(discount.getUriImg(),discount.getName(),this, discount.getKey());
    }
}