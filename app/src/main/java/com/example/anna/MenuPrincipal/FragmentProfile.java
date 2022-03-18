package com.example.anna.MenuPrincipal;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.module.AppGlideModule;

import com.example.anna.R;
import com.example.anna.databinding.ActivityFragmentProfileBinding;


public class FragmentProfile extends Fragment {

    private EditText userName, userMail, userTel;
    private SharedPreferences sharedPreferences;
    private ActivityFragmentProfileBinding binding;
    private ImageView profileImageView;
    private String urlPicture;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getActivity().getSharedPreferences(getString(R.string.sharedpreferencesfile), Context.MODE_PRIVATE);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = ActivityFragmentProfileBinding.inflate(getLayoutInflater());
        View root = binding.getRoot();
        userName  = binding.profileUsername;
        userName.setText(sharedPreferences.getString("username",null));
        userName.setClickable(false);
        userName.setEnabled(false);
        userMail = binding.profileUseremail;
        userMail.setText(sharedPreferences.getString("email",null));
        userMail.setClickable(false);
        userMail.setEnabled(false);
        userTel = binding.profileUsertelefono;
        userTel.setText(sharedPreferences.getString("usertel",null));
        profileImageView = binding.profileImageView;
        urlPicture = sharedPreferences.getString("fotourl",null);//
        Glide.with(getContext()).load(urlPicture).into(profileImageView);
        return root;
    }

}