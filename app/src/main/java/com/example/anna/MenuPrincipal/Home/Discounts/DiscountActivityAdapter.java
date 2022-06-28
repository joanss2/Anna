package com.example.anna.MenuPrincipal.Home.Discounts;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.anna.MenuPrincipal.OnDiscountClickListener;
import com.example.anna.Models.Discount;
import com.example.anna.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class DiscountActivityAdapter extends FirestoreRecyclerAdapter<Discount, DiscountActivityAdapter.DiscountHolder> {

    private final Context context;
    private final OnDiscountClickListener onDiscountClickListener;

    public DiscountActivityAdapter(@NonNull FirestoreRecyclerOptions<Discount> options, Context context, OnDiscountClickListener onDiscountClickListener) {
        super(options);
        this.context = context;
        this.onDiscountClickListener = onDiscountClickListener;
    }

    @Override
    protected void onBindViewHolder(@NonNull DiscountHolder holder, int position, @NonNull Discount model) {
        holder.bind(model);
    }

    @NonNull
    @Override
    public DiscountHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.discount_cardview, parent, false);
        return new DiscountHolder(view, onDiscountClickListener);
    }

    class DiscountHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        FirebaseStorage firebaseStorage;
        StorageReference storageReference;
        ImageView discountImg;
        TextView discountTitle, discountDescription, discountPercentage;
        Uri uri;
        OnDiscountClickListener onDiscountsListener;
        Discount currentDiscountInViewHolder;

        public DiscountHolder(@NonNull View itemView, OnDiscountClickListener onDiscountsListener) {
            super(itemView);
            this.onDiscountsListener = onDiscountsListener;
            discountImg = itemView.findViewById(R.id.mydiscountimage);
            discountTitle = itemView.findViewById(R.id.mydiscounttitle);
            discountDescription = itemView.findViewById(R.id.mydiscountdescription);
            discountPercentage = itemView.findViewById(R.id.mydiscountPercentage);
            firebaseStorage = FirebaseStorage.getInstance();
            storageReference = firebaseStorage.getReference();
            itemView.setOnClickListener(this);
        }

        public void bind(Discount discount) {
            discountDescription.setText(discount.getDescription());
            discountTitle.setText(discount.getName());
            discountPercentage.setText(String.valueOf(discount.getDiscountPercentage()));
            currentDiscountInViewHolder = new Discount();
            currentDiscountInViewHolder.setKey(discount.getKey());
            currentDiscountInViewHolder.setDescription(discountDescription.getText().toString());
            currentDiscountInViewHolder.setDiscountPercentage(Integer.parseInt(discountPercentage.getText().toString()));
            currentDiscountInViewHolder.setName(discountTitle.getText().toString());
            StorageReference pictureReference = this.storageReference.child(discount.getImageRef());
            pictureReference.getDownloadUrl().addOnSuccessListener(uri -> {
                DiscountHolder.this.uri = uri;
                currentDiscountInViewHolder.setUriImg(DiscountHolder.this.uri);
                Glide.with(context)
                        .load(uri)
                        .error(R.drawable.ic_launcher_background)
                        .into(discountImg);
            }).addOnFailureListener(e -> {
            });
        }

        @Override
        public void onClick(View view) {
            onDiscountsListener.onDiscountClick(currentDiscountInViewHolder);
        }

    }
}
