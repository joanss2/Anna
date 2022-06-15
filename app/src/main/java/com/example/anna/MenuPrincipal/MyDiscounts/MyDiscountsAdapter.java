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
import com.example.anna.MenuPrincipal.OnDiscountClickListener;
import com.example.anna.Models.Discount;
import com.example.anna.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class MyDiscountsAdapter extends FirestoreRecyclerAdapter<Discount, MyDiscountsAdapter.ViewHolder> {

    private final OnDiscountClickListener onDiscountClickListener;
    Context context;
    StorageReference storageReference;
    FirebaseStorage firebaseStorage;


    public MyDiscountsAdapter(@NonNull FirestoreRecyclerOptions<Discount> options, OnDiscountClickListener onDiscountClickListener, Context context) {
        super(options);
        this.onDiscountClickListener = onDiscountClickListener;
        this.context = context;
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        System.out.println("AQUI ARRIBO");


    }


    @Override
    protected void onBindViewHolder(@NonNull MyDiscountsAdapter.ViewHolder holder, int position, @NonNull Discount model) {
        holder.discountTitle.setText(model.getName());
        holder.discountDescription.setText(model.getDescription());
        holder.discountPercentage.setText(String.valueOf(model.getDiscountPercentage()));
        holder.bind(model);
    }

    @NonNull
    @Override
    public MyDiscountsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.discount_cardview, parent, false);
        return new ViewHolder(view, onDiscountClickListener);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        ImageView discountImg;
        TextView discountTitle, discountDescription, discountPercentage;
        Uri uri;
        OnDiscountClickListener onDiscountsListener;
        Discount currentDiscountInViewHolder;

        public ViewHolder(@NonNull View itemView, OnDiscountClickListener onMyDiscountsListener) {
            super(itemView);
            this.onDiscountsListener = onMyDiscountsListener;
            discountImg = itemView.findViewById(R.id.mydiscountimage);
            discountTitle = itemView.findViewById(R.id.mydiscounttitle);
            discountDescription = itemView.findViewById(R.id.mydiscountdescription);
            discountPercentage = itemView.findViewById(R.id.mydiscountPercentage);

            itemView.setOnClickListener(this);
        }

        public void bind(Discount discount) {

            currentDiscountInViewHolder = discount;
            StorageReference pictureReference = storageReference.child(discount.getImageRef());
            pictureReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    ViewHolder.this.uri = uri;
                    currentDiscountInViewHolder.setUriImg(ViewHolder.this.uri);
                    Glide.with(context)
                            .load(uri)
                            .error(R.drawable.ic_launcher_background)
                            .into(discountImg);
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
