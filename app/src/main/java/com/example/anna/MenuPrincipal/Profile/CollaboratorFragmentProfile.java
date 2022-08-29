package com.example.anna.MenuPrincipal.Profile;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import com.bumptech.glide.Glide;
import com.example.anna.R;
import com.example.anna.databinding.CollaboratorProfileFragmentBinding;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageException;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Objects;

public class CollaboratorFragmentProfile extends Fragment {
    private ProfileMenu profileMenu;
    private SharedPreferences userInfoPrefs;
    private StorageReference storageReference;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userInfoPrefs = requireActivity().getSharedPreferences("USERINFO", Context.MODE_PRIVATE);
        String authorKey = userInfoPrefs.getString("userKey", null);
        storageReference = FirebaseStorage.getInstance().getReference("collaborators").child(authorKey);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        CollaboratorProfileFragmentBinding binding = CollaboratorProfileFragmentBinding.inflate(getLayoutInflater());
        View root = binding.getRoot();

        ImageView profileImageView = binding.collaboratorprofileImageView;
        Button editProfileButton = binding.collaboratoreditProfileButton;

        TextView profileName = binding.collaboratorprofileName;
        ImageButton menu = binding.collaboratormenuProfile;


        profileName.setText(userInfoPrefs.getString("username", null));

        loadPicture(storageReference, profileImageView);
        editProfileButton.setOnClickListener(v -> startActivity(new Intent(getContext(), EditProfile.class)));

        menu.setOnClickListener(v -> {
            profileMenu = new ProfileMenu(getContext());
            profileMenu.show(requireActivity().getSupportFragmentManager(), "bottomSheetSettings");
        });



        return root;
    }



    private void loadPicture(StorageReference storageReference, ImageView userPicture) {
        storageReference.getDownloadUrl().addOnSuccessListener(uri -> Glide.with(requireContext()).load(uri).into(userPicture)).addOnFailureListener(e -> {
            int errorCode = ((StorageException) e).getErrorCode();
            if (errorCode == StorageException.ERROR_OBJECT_NOT_FOUND) {
                userPicture.setImageResource(R.drawable.user_icon);
            }
        });

    }
}
