package com.example.anna.MenuPrincipal.Discounts.MyDiscounts;

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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class DiscountUsedDetail extends Fragment {

    private final Discount discount;
    private final FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private SharedPreferences sharedPreferences;
    private String key, documentID;
    private int defaultColor;

    public DiscountUsedDetail(Discount discount) {
        this.discount = discount;
        System.out.println(discount.getKey());
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = requireActivity().getSharedPreferences("USERINFO", Context.MODE_PRIVATE);
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

        Glide.with(getContext()).load(discount.getUriImg()).into(imageView);
        title.setText(discount.getName());
        description.setText(discount.getDescription());

        initializeLikeColor(favourite);

        favourite.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Map<String, String> map = new HashMap<>();
                map.put("key", key);

                firebaseFirestore.collection("DiscountsFavs").document(key)
                        .collection("FavsList").whereEqualTo("discountKey", discount.getKey()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (!task.getResult().isEmpty()) {
                                DrawableCompat.setTint(favourite.getDrawable(), defaultColor);

                                firebaseFirestore.collection("DiscountsFavs").document(key)
                                        .collection("FavsList").document(documentID).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        System.out.println("DELETED FROM FAVS");
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                    }
                                });
                            } else {
                                DrawableCompat.setTint(favourite.getDrawable(), Color.RED);
                                firebaseFirestore.collection("DiscountsFavs").document(key).set(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Map<String, String> map2 = new HashMap<>();
                                            map2.put("discountKey", discount.getKey());
                                            firebaseFirestore.collection("DiscountsFavs").document(key)
                                                    .collection("FavsList").add(map2).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                                @Override
                                                public void onComplete(@NonNull Task<DocumentReference> task) {
                                                    if (task.isSuccessful()) {
                                                        documentID = task.getResult().getId();
                                                        System.out.println(documentID);
                                                    }
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {

                                                }
                                            });
                                        }
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                    }
                                });
                            }

                        } else {
                            System.out.println("NOT SUCCESSFUL");
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        System.out.println("FAILED");
                    }
                });
            }
        });
        comments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_frame,new DiscountCommentsFragment(discount, key))
                        .addToBackStack(null).commit();
            }
        });
        return view;
    }

    public void initializeLikeColor(ImageView favourit) {
        firebaseFirestore.collection("DiscountsFavs").document(key)
                .collection("FavsList").whereEqualTo("discountKey", discount.getKey()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    if (!task.getResult().isEmpty()) {
                        documentID=task.getResult().getDocuments().get(0).getId();
                        DrawableCompat.setTint(favourit.getDrawable(), Color.RED);
                    } else {
                        DrawableCompat.setTint(favourit.getDrawable(), defaultColor);
                    }
                }
            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println("FAILED");
            }
        });
    }

}
