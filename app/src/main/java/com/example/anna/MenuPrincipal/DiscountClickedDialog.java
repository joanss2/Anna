package com.example.anna.MenuPrincipal;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.anna.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public class DiscountClickedDialog {

    private Dialog dialog;
    private BottomSheetDialog bottomSheetDialog;
    private Uri imageViewResource;
    private String title;
    private Context context;

    public DiscountClickedDialog(Uri imageViewResource, String title, Context context){
        this.imageViewResource = imageViewResource;
        this.title = title;
        this.context = context;
        this.bottomSheetDialog = new BottomSheetDialog(context);
        this.bottomSheetDialog.setContentView(R.layout.dialog_discount_clicked);
        System.out.println("DISCOUNT CLICKED DIALOG");

        ImageView imageViewContainer = bottomSheetDialog.findViewById(R.id.discountClickedImage);
        Glide.with(context)
                .load(imageViewResource)
                .error(R.drawable.ic_launcher_background)
                .into(imageViewContainer);
        TextView titleContainer = bottomSheetDialog.findViewById(R.id.titleDiscountActivateDialog);
        titleContainer.setText(title);

        Button activateDiscount = (Button) bottomSheetDialog.findViewById(R.id.button_activar_discount);
        Button notActivateDiscount = (Button) bottomSheetDialog.findViewById(R.id.button_no_activar_discount);

        bottomSheetDialog.show();
    }
}
