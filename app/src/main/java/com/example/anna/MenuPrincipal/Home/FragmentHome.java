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
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.anna.Models.HotNews;
import com.example.anna.databinding.ActivityFragmentHomeBinding;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;


public class FragmentHome extends Fragment {

    private ActivityFragmentHomeBinding binding;
    private SharedPreferences userInfoPrefs;
    private RecyclerView recyclerView;
    private HomeFragmentAdapter adapter;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userInfoPrefs = requireActivity().getSharedPreferences("USERINFO", Context.MODE_PRIVATE);
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        binding = ActivityFragmentHomeBinding.inflate(getLayoutInflater());
        View root = binding.getRoot();

        ////////////
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        System.out.println("back stack in homefragment: "+fragmentManager.getBackStackEntryCount());
        ////////////

        recyclerView = binding.homeRecyclerView;
        WrapContentLayoutManagerHome managerHome = new WrapContentLayoutManagerHome(getContext(), 2);
        recyclerView.setLayoutManager(managerHome);


        CollectionReference allAdReference = FirebaseFirestore.getInstance().collection("AllAds");
        FirestoreRecyclerOptions<HotNews> options = new FirestoreRecyclerOptions.Builder<HotNews>().setQuery(allAdReference,HotNews.class).build();
        adapter = new HomeFragmentAdapter(options,getContext());

        recyclerView.setAdapter(adapter);

        TextView hiUser = binding.hiUser;
        hiUser.setText(userInfoPrefs.getString("username",null));


        return root;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }


}