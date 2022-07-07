package com.example.anna.MenuPrincipal.MyDiscounts;

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
import com.example.anna.Models.Discount;
import com.example.anna.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class MyDiscountsAdapter extends FirestoreRecyclerAdapter<Discount, MyDiscountsAdapter.DiscountHolder> {

    private final OnMyDiscountUsedClickListener onMyDiscountUsedClickListener;
    Context context;
    StorageReference storageReference;
    FirebaseStorage firebaseStorage;


    public MyDiscountsAdapter(@NonNull FirestoreRecyclerOptions<Discount> options, OnMyDiscountUsedClickListener onMyDiscountUsedClickListener, Context context) {
        super(options);
        this.onMyDiscountUsedClickListener = onMyDiscountUsedClickListener;
        this.context = context;
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
    }

    @Override
    protected void onBindViewHolder(@NonNull DiscountHolder holder, int position, @NonNull Discount model) {
        holder.discountTitle.setText(model.getName());
        holder.discountDescription.setText(model.getDescription());
        holder.discountPercentage.setText(String.valueOf(model.getDiscountPercentage()));
        holder.bind(model);
    }

    @NonNull
    @Override
    public DiscountHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.discount_cardview, parent, false);
        return new DiscountHolder(view, onMyDiscountUsedClickListener);
    }

    class DiscountHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        ImageView discountImg;
        TextView discountTitle, discountDescription, discountPercentage;
        Uri uri;
        OnMyDiscountUsedClickListener onMyDiscountUsedClickListener;
        Discount currentDiscountInViewHolder;

        public DiscountHolder(@NonNull View itemView, OnMyDiscountUsedClickListener onMyDiscountUsedClickListener) {
            super(itemView);
            this.onMyDiscountUsedClickListener = onMyDiscountUsedClickListener;
            discountImg = itemView.findViewById(R.id.mydiscountimage);
            discountTitle = itemView.findViewById(R.id.mydiscounttitle);
            discountDescription = itemView.findViewById(R.id.mydiscountdescription);
            discountPercentage = itemView.findViewById(R.id.mydiscountPercentage);

            itemView.setOnClickListener(this);
        }

        public void bind(Discount discount) {
            currentDiscountInViewHolder = discount;
            StorageReference pictureReference = storageReference.child(discount.getImageRef());
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
            onMyDiscountUsedClickListener.onMyDiscountUsedClick(currentDiscountInViewHolder);
        }
    }

    public interface OnMyDiscountUsedClickListener{
         void onMyDiscountUsedClick(Discount discount);
    }
}
