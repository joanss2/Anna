package com.example.anna.MenuPrincipal.Discounts;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.example.anna.MenuPrincipal.Discounts.Verification.ScannerFragmentDiscount;
import com.example.anna.Models.Discount;
import com.example.anna.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;


public class DiscountClickedDialog {

    private final BottomSheetDialog bottomSheetDialog;
    private final AppCompatActivity activity;

    public DiscountClickedDialog(Discount discount, Context context, AppCompatActivity activity){//Uri imageViewResource, String title, Context context, String key, String description) {
        this.bottomSheetDialog = new BottomSheetDialog(context);
        this.bottomSheetDialog.setContentView(R.layout.dialog_discount_clicked);
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
                    .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            activity.getSupportFragmentManager().beginTransaction().replace(R.id.main_frame_parentlayout, new ScannerFragmentDiscount(discount.getKey())).commit();
                            bottomSheetDialog.dismiss();
                        }
                    })
                    .setNegativeButton(R.string.no, (dialogInterface, i) -> {
                    })
                    .show();
        });

        assert infoIcon != null;
        infoIcon.setOnLongClickListener(v -> {
            Toast.makeText(context,"ON LONG CLICKED",Toast.LENGTH_SHORT).show();
            showDiscountInfo(discount);
            return true;
        });

        assert notActivateDiscount != null;
        notActivateDiscount.setOnClickListener(view -> bottomSheetDialog.dismiss());

        bottomSheetDialog.show();
    }

    public void showDiscountInfo(Discount discount){
        DiscountInfoDialog discountInfoDialog = new DiscountInfoDialog(discount);
        discountInfoDialog.show(activity.getSupportFragmentManager(), "Discount Info");
    }
}
