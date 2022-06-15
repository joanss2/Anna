package com.example.anna.MenuPrincipal.Home.Discounts;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.Uri;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.example.anna.Models.DiscountUsedEntry;
import com.example.anna.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class DiscountClickedDialog {

    private Dialog dialog;
    private BottomSheetDialog bottomSheetDialog;
    private Uri imageViewResource;
    private String title, key;
    private Context context;
    private final CollectionReference collectionReference = FirebaseFirestore.getInstance().collection("DiscountsUsed");
    private final SharedPreferences sharedPreferences;

    public DiscountClickedDialog(Uri imageViewResource, String title, Context context, String key){
        this.imageViewResource = imageViewResource;
        this.title = title;
        this.context = context;
        this.key = key;
        this.bottomSheetDialog = new BottomSheetDialog(context);
        this.bottomSheetDialog.setContentView(R.layout.dialog_discount_clicked);
        sharedPreferences = context.getSharedPreferences("USERINFO",Context.MODE_PRIVATE);

        ImageView imageViewContainer = bottomSheetDialog.findViewById(R.id.discountClickedImage);
        Glide.with(context)
                .load(imageViewResource)
                .error(R.drawable.ic_launcher_background)
                .into(imageViewContainer);
        TextView titleContainer = bottomSheetDialog.findViewById(R.id.titleDiscountActivateDialog);
        titleContainer.setText(title);

        Button activateDiscount = (Button) bottomSheetDialog.findViewById(R.id.button_activar_discount);
        Button notActivateDiscount = (Button) bottomSheetDialog.findViewById(R.id.button_no_activar_discount);

        activateDiscount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(context).setTitle("Confirm Discount activation")
                        .setMessage("Do you really want to activate this discount?")
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                createDiscountUsedEntry();
                            }
                        })
                        .setNegativeButton(R.string.no,null)
                        .show();
            }
        });

        bottomSheetDialog.show();
    }

    public void createDiscountUsedEntry(){
        DiscountUsedEntry discountUsedEntry = new DiscountUsedEntry(key);

        collectionReference.document(sharedPreferences.getString("userKey",null)).collection("DiscountsReferenceList")
                .add(discountUsedEntry).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Toast.makeText(context,"DISCOUNT ACTIVATED SUCCESSFULLY!",Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context,"ERROR WHILE ACTIVATING DISCOUNT",Toast.LENGTH_LONG).show();
            }
        });

    }
}
