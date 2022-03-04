package com.example.anna.MenuPrincipal;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.anna.Inicio.MainActivity;
import com.example.anna.R;
import com.example.anna.databinding.ActivityFragmentHomeBinding;
import com.example.anna.databinding.ActivityFragmentProfileBinding;
import com.google.firebase.auth.FirebaseAuth;

public class FragmentProfile extends Fragment {

    private EditText userName, userMail, userTel;
    private SharedPreferences sharedPreferences;
    private ActivityFragmentProfileBinding binding;
    private Button signoutButton;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getActivity().getSharedPreferences(String.valueOf(R.string.sharedpreferencesfile), Context.MODE_PRIVATE);

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

        return root;
    }

}