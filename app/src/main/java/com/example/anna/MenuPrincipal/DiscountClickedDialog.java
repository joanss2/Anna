package com.example.anna.MenuPrincipal;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.anna.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public class DiscountClickedDialog {

    private Dialog dialog;
    private BottomSheetDialog bottomSheetDialog;
    private int imageViewResource;
    private String title;
    private Context context;

    public DiscountClickedDialog(int imageViewResource, String title, Context context){
        this.imageViewResource = imageViewResource;
        this.title = title;
        this.context = context;
        this.bottomSheetDialog = new BottomSheetDialog(context);
        this.bottomSheetDialog.setContentView(R.layout.dialog_discount_clicked);

        ImageView imageViewContainer = bottomSheetDialog.findViewById(R.id.discountClickedImage);
        imageViewContainer.setImageResource(imageViewResource);
        TextView titleContainer = bottomSheetDialog.findViewById(R.id.titleDiscountActivateDialog);
        titleContainer.setText(title);

        Button activateDiscount = (Button) bottomSheetDialog.findViewById(R.id.button_activar_discount);
        Button notActivateDiscount = (Button) bottomSheetDialog.findViewById(R.id.button_no_activar_discount);

        bottomSheetDialog.show();
    }
}
