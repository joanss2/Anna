package com.example.anna.MenuPrincipal.Home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;


import com.example.anna.MenuPrincipal.Home.Discounts.DiscountsActivity;
import com.example.anna.R;
import com.example.anna.databinding.ActivityFragmentHomeBinding;
import com.example.anna.MenuPrincipal.Routes.RoutesActivity;


public class FragmentHome extends Fragment {

    private ActivityFragmentHomeBinding binding;
    private Intent toRoute, toDiscounts;
    private SharedPreferences userInfoPrefs;
    private SharedPreferences.Editor userInfoEditor;
    private Animation scaleUp, scaleDown;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userInfoPrefs = getActivity().getSharedPreferences("USERINFO", Context.MODE_PRIVATE);
        userInfoEditor = userInfoPrefs.edit();

    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        toRoute = new Intent(getActivity(), RoutesActivity.class);
        toDiscounts = new Intent(getActivity(), DiscountsActivity.class);


        binding = ActivityFragmentHomeBinding.inflate(getLayoutInflater());
        View root = binding.getRoot();

        scaleUp = AnimationUtils.loadAnimation(getActivity(),R.anim.scale_up);
        scaleDown = AnimationUtils.loadAnimation(getActivity(),R.anim.scale_down);


        CardView cardviewRoute = binding.cardViewRuta;
        CardView cardviewDiscounts = binding.cardViewDisc;

        cardviewRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardviewRoute.startAnimation(scaleDown);
                Handler handler = new Handler();
                Runnable r = new Runnable() {
                    @Override
                    public void run() {
                        toRoute.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(toRoute);
                    }
                };
                handler.postDelayed(r,120);
            }
        });


        cardviewDiscounts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardviewDiscounts.startAnimation(scaleDown);
                Handler handler = new Handler();
                Runnable r = new Runnable() {
                    @Override
                    public void run() {
                        toDiscounts.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(toDiscounts);
                    }
                };
                handler.postDelayed(r,120);
            }
        });

        return root;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}