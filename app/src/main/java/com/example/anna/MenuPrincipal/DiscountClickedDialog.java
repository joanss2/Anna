package com.example.anna.MenuPrincipal;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.anna.R;

public class DiscountClickedDialog {

    private Dialog dialog;
    private int imageViewResource;
    private String title;
    private Context context;

    public DiscountClickedDialog(int imageViewResource, String title, Context context){
        this.imageViewResource = imageViewResource;
        this.title = title;
        this.context = context;

        dialog = new Dialog(new ContextThemeWrapper(context, R.style.DialogSlideAnim));
        dialog.setContentView(R.layout.dialog_discount_clicked);
        ImageView imageViewContainer = dialog.findViewById(R.id.discountClickedImage);
        imageViewContainer.setImageResource(imageViewResource);
        TextView titleContainer = dialog.findViewById(R.id.titleDiscountActivateDialog);
        titleContainer.setText(title);

        Button activateDiscount = (Button) dialog.findViewById(R.id.button_activar_discount);
        Button notActivateDiscount = (Button) dialog.findViewById(R.id.button_no_activar_discount);

        activateDiscount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(activateDiscount.getContext(), "DISCOUNT ACTIVATED",Toast.LENGTH_SHORT).show();
            }
        });

        notActivateDiscount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(activateDiscount.getContext(), "DISCOUNT ACTIVATION CANCELLED",Toast.LENGTH_SHORT).show();
            }
        });
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.GRAY));
        dialog.show();
    }
}
