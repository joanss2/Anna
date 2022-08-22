package com.example.anna.MenuPrincipal.Discounts.MyDiscounts;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.anna.MenuPrincipal.Discounts.DiscountCommentsFragment;
import com.example.anna.Models.Discount;
import com.example.anna.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

public class DiscountUsedDetail extends Fragment {

    private final Discount discount;
    private final FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private String key, documentID;
    private int defaultColor;

    public DiscountUsedDetail(Discount discount) {
        this.discount = discount;
        System.out.println(discount.getKey());
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("USERINFO", Context.MODE_PRIVATE);
        key = sharedPreferences.getString("userKey", null);
        defaultColor = Color.parseColor("#969292");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.discount_used_detail, container, false);


        ImageView favourite = view.findViewById(R.id.likeDiscountUsedDetail);
        ImageView imageView = view.findViewById(R.id.imageDiscountUsedDetail);
        ImageView comments = view.findViewById(R.id.commentDiscountUsedDetail);
        TextView title = view.findViewById(R.id.titleDiscountUsedDetail);
        TextView description = view.findViewById(R.id.descriptionDiscountUsedDetail);
        //TextView endDate = view.findViewById(R.id.dateEditTextUsedDetail);
        TextView discountPercentage = view.findViewById(R.id.discountPercentageEditTextDetail);

        discountPercentage.setText(String.valueOf(discount.getDiscountPercentage()));
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        //endDate.setText(simpleDateFormat.format(discount.ge));

        Glide.with(requireContext()).load(discount.getUriImg()).into(imageView);
        title.setText(discount.getName());
        description.setText(discount.getDescription());

        initializeLikeColor(favourite);

        favourite.setOnClickListener(v -> {

            Map<String, String> map = new HashMap<>();
            map.put("key", key);

            firebaseFirestore.collection("DiscountsFavs").document(key)
                    .collection("FavsList").whereEqualTo("discountKey", discount.getKey()).get().addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            if (!task.getResult().isEmpty()) {
                                DrawableCompat.setTint(favourite.getDrawable(), defaultColor);

                                firebaseFirestore.collection("DiscountsFavs").document(key)
                                        .collection("FavsList").document(documentID).delete().addOnSuccessListener(unused -> System.out.println("DELETED FROM FAVS")).addOnFailureListener(e -> {

                                        });
                            } else {
                                DrawableCompat.setTint(favourite.getDrawable(), Color.RED);
                                firebaseFirestore.collection("DiscountsFavs").document(key).set(map).addOnCompleteListener(task1 -> {
                                    if (task1.isSuccessful()) {
                                        Map<String, String> map2 = new HashMap<>();
                                        map2.put("discountKey", discount.getKey());
                                        firebaseFirestore.collection("DiscountsFavs").document(key)
                                                .collection("FavsList").add(map2).addOnCompleteListener(task11 -> {
                                                    if (task11.isSuccessful()) {
                                                        documentID = task11.getResult().getId();
                                                    }
                                                });
                                    }
                                });
                            }

                        }
                    }).addOnFailureListener(e -> System.out.println("FAILED"));
        });
        comments.setOnClickListener(v -> requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_frame,new DiscountCommentsFragment(discount, key))
                .addToBackStack(null).commit());
        return view;
    }

    public void initializeLikeColor(ImageView favourit) {
        firebaseFirestore.collection("DiscountsFavs").document(key)
                .collection("FavsList").whereEqualTo("discountKey", discount.getKey()).get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (!task.getResult().isEmpty()) {
                            documentID=task.getResult().getDocuments().get(0).getId();
                            DrawableCompat.setTint(favourit.getDrawable(), Color.RED);
                        } else {
                            DrawableCompat.setTint(favourit.getDrawable(), defaultColor);
                        }
                    }
                }).addOnFailureListener(e -> System.out.println("FAILED"));
    }

}
