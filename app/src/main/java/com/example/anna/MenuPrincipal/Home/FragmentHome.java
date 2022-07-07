package com.example.anna.MenuPrincipal.Home;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.anna.databinding.ActivityFragmentHomeBinding;



public class FragmentHome extends Fragment {

    private ActivityFragmentHomeBinding binding;
    private SharedPreferences userInfoPrefs;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userInfoPrefs = requireActivity().getSharedPreferences("USERINFO", Context.MODE_PRIVATE);
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = ActivityFragmentHomeBinding.inflate(getLayoutInflater());
        View root = binding.getRoot();

        TextView hiUser = binding.hiUser;
        hiUser.setText(userInfoPrefs.getString("username",null));


        return root;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}