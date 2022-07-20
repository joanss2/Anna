package com.example.anna.MenuPrincipal.Discounts;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.bumptech.glide.Glide;
import com.example.anna.MenuPrincipal.MenuMainActivity;
import com.example.anna.Models.Discount;
import com.example.anna.R;
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
    private final String key;
    private final Context context;
    private final CollectionReference collectionReference = FirebaseFirestore.getInstance().collection("DiscountsUsed");
    private final SharedPreferences sharedPreferences;
    private final AppCompatActivity activity;

    public DiscountClickedDialog(Discount discount, Context context, AppCompatActivity activity){//Uri imageViewResource, String title, Context context, String key, String description) {
        this.context = context;
        this.key = discount.getKey();
        this.bottomSheetDialog = new BottomSheetDialog(context);
        this.bottomSheetDialog.setContentView(R.layout.dialog_discount_clicked);
        sharedPreferences = context.getSharedPreferences("USERINFO", Context.MODE_PRIVATE);
        this.activity = activity;

        ImageView infoIcon = bottomSheetDialog.findViewById(R.id.infodiscount);
        ImageView imageViewContainer = bottomSheetDialog.findViewById(R.id.discountClickedImage);
        assert imageViewContainer != null;
        Glide.with(context)
                .load(discount.getUriImg())
                .error(R.drawable.ic_launcher_background)
                .into(imageViewContainer);
        TextView titleContainer = bottomSheetDialog.findViewById(R.id.titleDiscountActivateDialog);
        assert titleContainer != null;
        titleContainer.setText(discount.getName());

        Button activateDiscount = bottomSheetDialog.findViewById(R.id.button_activar_discount);
        Button notActivateDiscount = bottomSheetDialog.findViewById(R.id.button_no_activar_discount);

        assert activateDiscount != null;
        activateDiscount.setOnClickListener(view -> {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
            alertDialog.setTitle("Confirm Discount activation")
                    .setMessage("Do you really want to activate this discount?")
                    .setPositiveButton(R.string.yes, (dialogInterface, i) -> createDiscountUsedEntry())
                    .setNegativeButton(R.string.no, (dialogInterface, i) -> {
                    })
                    .show();
        });

        infoIcon.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(context,"ON LONG CLICKED",Toast.LENGTH_SHORT).show();
                showDiscountInfo(discount);
                return true;
            }
        });

        assert notActivateDiscount != null;
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
        documentReference.set(fieldkey).addOnSuccessListener(unused -> documentReference.collection("DiscountsReferenceList")
                .add(discount).addOnSuccessListener(documentReference1 -> {
                    Toast.makeText(context, "DISCOUNT ACTIVATED SUCCESSFULLY!", Toast.LENGTH_LONG).show();
                    this.bottomSheetDialog.dismiss();
                    MenuMainActivity activity = (MenuMainActivity) context;
                    activity.recreate();
                    activity.getBottomNavigationView().setSelectedItemId(R.id.bottom_home);
                })
                .addOnFailureListener(e -> Toast.makeText(context, "ERROR WHILE ACTIVATING DISCOUNT", Toast.LENGTH_LONG).show()))
                .addOnFailureListener(e -> Toast.makeText(context, "ERROR WHILE ACTIVATING DISCOUNT", Toast.LENGTH_LONG).show());
    }
    public void showDiscountInfo(Discount discount){
        DiscountInfoDialog discountInfoDialog = new DiscountInfoDialog(discount);
        discountInfoDialog.show(activity.getSupportFragmentManager(), "Discount Info");
    }
}
