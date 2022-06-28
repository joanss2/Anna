package com.example.anna.MenuPrincipal.Home.Discounts;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.example.anna.Models.Discount;
import com.example.anna.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class DiscountClickedDialog {

    private final BottomSheetDialog bottomSheetDialog;
    private final String title;
    private final String key;
    private final Context context;
    private final CollectionReference collectionReference = FirebaseFirestore.getInstance().collection("DiscountsUsed");
    private final SharedPreferences sharedPreferences;

    public DiscountClickedDialog(Uri imageViewResource, String title, Context context, String key) {
        this.title = title;
        this.context = context;
        this.key = key;
        this.bottomSheetDialog = new BottomSheetDialog(context);
        this.bottomSheetDialog.setContentView(R.layout.dialog_discount_clicked);
        sharedPreferences = context.getSharedPreferences("USERINFO", Context.MODE_PRIVATE);


        ImageView imageViewContainer = bottomSheetDialog.findViewById(R.id.discountClickedImage);
        assert imageViewContainer != null;
        Glide.with(context)
                .load(imageViewResource)
                .error(R.drawable.ic_launcher_background)
                .into(imageViewContainer);
        TextView titleContainer = bottomSheetDialog.findViewById(R.id.titleDiscountActivateDialog);
        assert titleContainer != null;
        titleContainer.setText(title);

        Button activateDiscount = bottomSheetDialog.findViewById(R.id.button_activar_discount);
        Button notActivateDiscount = bottomSheetDialog.findViewById(R.id.button_no_activar_discount);

        activateDiscount.setOnClickListener(view -> {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
            alertDialog.setTitle("Confirm Discount activation")
                    .setMessage("Do you really want to activate this discount?")
                    .setPositiveButton(R.string.yes, (dialogInterface, i) -> createDiscountUsedEntry())
                    .setNegativeButton(R.string.no, (dialogInterface, i) -> {
                    })
                    .show();
        });

        notActivateDiscount.setOnClickListener(view -> bottomSheetDialog.dismiss());

        bottomSheetDialog.show();
    }

    public void createDiscountUsedEntry() {

        Discount auxDiscount = new Discount();
        Query query = FirebaseFirestore.getInstance().collection("Discounts").whereEqualTo("key", key);
        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Map<String, Object> map = document.getData();
                    auxDiscount.setName(Objects.requireNonNull(map.get("name")).toString());
                    auxDiscount.setDescription(Objects.requireNonNull(map.get("description")).toString());
                    auxDiscount.setImageRef(Objects.requireNonNull(map.get("imageRef")).toString());
                    auxDiscount.setKey(Objects.requireNonNull(map.get("key")).toString());
                    auxDiscount.setDiscountPercentage(Integer.parseInt(Objects.requireNonNull(map.get("discountPercentage")).toString()));
                    createDocumentToAvoidNonExistent(auxDiscount);
                }
            }
        }).addOnFailureListener(e -> {

        });
    }


    public void createDocumentToAvoidNonExistent(Discount discount){
        Map<String,Object> fieldkey = new HashMap<>();
        fieldkey.put("key",sharedPreferences.getString("userKey", null));
        DocumentReference documentReference = collectionReference.document(sharedPreferences.getString("userKey", null));
        documentReference.set(fieldkey).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                documentReference.collection("DiscountsReferenceList")
                        .add(discount).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(context, "DISCOUNT ACTIVATED SUCCESSFULLY!", Toast.LENGTH_LONG).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, "ERROR WHILE ACTIVATING DISCOUNT", Toast.LENGTH_LONG).show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, "ERROR WHILE ACTIVATING DISCOUNT", Toast.LENGTH_LONG).show();
            }
        });
    }
}
