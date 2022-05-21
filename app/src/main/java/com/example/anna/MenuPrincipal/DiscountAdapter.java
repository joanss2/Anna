package com.example.anna.MenuPrincipal;

import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.anna.Discount;
import com.example.anna.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class DiscountAdapter extends RecyclerView.Adapter<DiscountAdapter.ViewHolder> {

    private List<Discount> discountList;
    private Context context;
    private OnMyDiscountsListener onMyDiscountsListener;

    public DiscountAdapter(Context context, List<Discount> discountList, OnMyDiscountsListener onMyDiscountsListener) {
        this.context = context;
        this.discountList = discountList;
        this.onMyDiscountsListener = onMyDiscountsListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.discount_cardview, parent, false);
        System.out.println("entro al oncreateviewholder");
        return new ViewHolder(view, onMyDiscountsListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Discount discount = discountList.get(position);
        System.out.println("entro al bind");
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
        OnMyDiscountsListener onMyDiscountsListener;
        Discount currentDiscountInViewHolder;

        public ViewHolder(@NonNull View itemView, OnMyDiscountsListener onMyDiscountsListener) {
            super(itemView);
            this.onMyDiscountsListener = onMyDiscountsListener;
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
            currentDiscountInViewHolder.setDescription(discountDescription.getText().toString());
            currentDiscountInViewHolder.setDiscountPercentage(Integer.parseInt(discountPercentage.getText().toString()));
            currentDiscountInViewHolder.setName(discountTitle.getText().toString());
            StorageReference pictureReference = this.storageReference.child(discount.getImageRef());
            pictureReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    ViewHolder.this.uri = uri;
                    currentDiscountInViewHolder.setUriImg(ViewHolder.this.uri);
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
            onMyDiscountsListener.onMyDiscountsClick(currentDiscountInViewHolder);
        }
    }

    public interface OnMyDiscountsListener{
        public void onMyDiscountsClick(Discount discount);
    }

}
