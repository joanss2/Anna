package com.example.anna.MenuPrincipal.Discounts;


import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.bumptech.glide.Glide;
import com.example.anna.Models.Discount;
import com.example.anna.R;


public class DiscountInfoDialog extends DialogFragment {

    private final Discount discount;

    public DiscountInfoDialog(Discount discount) {
        this.discount = discount;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.discount_info, null);

        builder.setView(view);
        ImageView imageView = view.findViewById(R.id.discount_info_image);
        TextView title = view.findViewById(R.id.discount_info_title);
        TextView description = view.findViewById(R.id.discount_info_description);
        TextView discount_percentage = view.findViewById(R.id.discount_info_percentage);

        Glide.with(requireContext()).load(discount.getUriImg()).into(imageView);
        title.setText(discount.getName());
        description.setText(discount.getDescription());
        discount_percentage.setText(String.valueOf(discount.getDiscountPercentage()));

        return builder.create();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.discount_info, container, false);
    }
}
