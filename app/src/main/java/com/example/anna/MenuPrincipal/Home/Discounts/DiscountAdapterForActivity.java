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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class DiscountAdapterForActivity extends RecyclerView.Adapter<DiscountAdapterForActivity.ViewHolder> {

    private List<Discount> discountList;
    private Context context;
    private OnDiscountClickListener onDiscountsListener;

    public DiscountAdapterForActivity(Context context, List<Discount> discountList, OnDiscountClickListener onDiscountsListener) {
        this.context = context;
        this.discountList = discountList;
        this.onDiscountsListener = onDiscountsListener;
    }

    @NonNull
    @Override
    public DiscountAdapterForActivity.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.discount_cardview, parent, false);
        return new DiscountAdapterForActivity.ViewHolder(view, onDiscountsListener);
    }

    @Override
    public void onBindViewHolder(@NonNull DiscountAdapterForActivity.ViewHolder holder, int position) {
        Discount discount = discountList.get(position);
        holder.bind(discount);
    }

    @Override
    public int getItemCount() {
        System.out.println("SIZE " + discountList.size());
        return discountList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        FirebaseStorage firebaseStorage;
        StorageReference storageReference;
        ImageView discountImg;
        TextView discountTitle, discountDescription, discountPercentage;
        Uri uri;
        OnDiscountClickListener onDiscountsListener;
        Discount currentDiscountInViewHolder;

        public ViewHolder(@NonNull View itemView, OnDiscountClickListener onDiscountsListener) {
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
            pictureReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    DiscountAdapterForActivity.ViewHolder.this.uri = uri;
                    currentDiscountInViewHolder.setUriImg(DiscountAdapterForActivity.ViewHolder.this.uri);
                    Glide.with(context)
                            .load(uri)
                            .error(R.drawable.ic_launcher_background)
                            .into(discountImg);
                    System.out.println("GLIDE IS BEING DONE");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });
        }

        @Override
        public void onClick(View view) {
            onDiscountsListener.onDiscountClick(currentDiscountInViewHolder);
        }
    }


}
