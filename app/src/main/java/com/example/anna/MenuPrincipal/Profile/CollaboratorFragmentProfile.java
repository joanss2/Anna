package com.example.anna.MenuPrincipal.Profile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.bumptech.glide.Glide;
import com.example.anna.Models.Subscription;
import com.example.anna.Models.User;
import com.example.anna.R;
import com.example.anna.databinding.CollaboratorProfileFragmentBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageException;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class CollaboratorFragmentProfile extends Fragment {
    private ProfileMenu profileMenu;
    private SharedPreferences userInfoPrefs;
    private StorageReference storageReference;
    private String authorKey;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userInfoPrefs = requireActivity().getSharedPreferences("USERINFO", Context.MODE_PRIVATE);
        authorKey = userInfoPrefs.getString("userKey", null);
        storageReference = FirebaseStorage.getInstance().getReference("collaborators").child(authorKey);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        CollaboratorProfileFragmentBinding binding = CollaboratorProfileFragmentBinding.inflate(getLayoutInflater());
        View root = binding.getRoot();
////////////////
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        System.out.println("back stack in proflefragment: " + fragmentManager.getBackStackEntryCount());
///////////////
        ImageView profileImageView = binding.collaboratorprofileImageView;
        Button editProfileButton = binding.collaboratoreditProfileButton;
        Button subButton = binding.subactivebutton;

        TextView profileName = binding.collaboratorprofileName;
        ImageButton menu = binding.collaboratormenuProfile;


        profileName.setText(userInfoPrefs.getString("username", null));

        loadPicture(storageReference, profileImageView);
        editProfileButton.setOnClickListener(v -> {
            startActivity(new Intent(getContext(), EditProfile.class));
        });

        menu.setOnClickListener(v -> {
            profileMenu = new ProfileMenu();
            profileMenu.show(requireActivity().getSupportFragmentManager(), "bottomSheetSettings");
        });

        subButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CollectionReference subReference = FirebaseFirestore.getInstance().collection("Subscriptions").document(userInfoPrefs.getString("userKey", null))
                        .collection("SubsOfUser");
                subReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map<String,Object> map = document.getData();

                                Subscription subscription = new Subscription(map);
                                System.out.println(subscription.getStatus());

                                Date dateStart = ((Timestamp) map.get("dateStart")).toDate();
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
                                assert dateStart != null;
                                System.out.println("DATE START "+dateStart);
                                String hola = sdf.format(dateStart);

                                Date dateEnd = ((Timestamp) map.get("dateEnd")).toDate();
                                System.out.println("DATE END "+dateEnd);
                                assert dateStart != null;
                                String ho = sdf.format(dateEnd);
                                System.out.println("Subscription from: "+hola +" to "+ho);

                                System.out.println("IS SUBSCRIPTION ACTIVE? "+dateEnd.after(new Date()));

/*
                                System.out.println(map.get("dateStart"));
                                System.out.println(map.get("dateEnd"));
                                System.out.println(map.get("tariff"));

 */

                            }
                        }
                    }
                });
            }
        });

        return root;
    }



    private void loadPicture(StorageReference storageReference, ImageView userPicture) {
        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(getContext()).load(uri).into(userPicture);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                int errorCode = ((StorageException) e).getErrorCode();
                if (errorCode == StorageException.ERROR_OBJECT_NOT_FOUND) {
                    userPicture.setImageResource(R.drawable.user_icon);
                }
            }
        });

    }
}
