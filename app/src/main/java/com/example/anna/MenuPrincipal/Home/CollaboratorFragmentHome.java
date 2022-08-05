package com.example.anna.MenuPrincipal.Home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.anna.R;
import com.example.anna.databinding.ActivityFragmentHomeBinding;
import com.example.anna.databinding.CollaboratorFragmentHomeBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class CollaboratorFragmentHome extends Fragment {

    private CollaboratorFragmentHomeBinding binding;
    private SharedPreferences userInfoPrefs;
    private RecyclerView recyclerView;
    private View root;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userInfoPrefs = requireActivity().getSharedPreferences("USERINFO", Context.MODE_PRIVATE);

    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        CollectionReference adsReference = FirebaseFirestore.getInstance().collection("Advertisements");
        adsReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    if(task.getResult().isEmpty()){
                        Toast.makeText(getContext(),"NO ADS YET. BE THE FIRST ONE AND POST YOURS",Toast.LENGTH_LONG).show();
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            }
        });



        binding = CollaboratorFragmentHomeBinding.inflate(getLayoutInflater());
        View root = binding.getRoot();

        ////////////
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        System.out.println("back stack in homefragment: "+fragmentManager.getBackStackEntryCount());
        ////////////

        recyclerView = binding.homeCollaboratorRecyclerView;
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(mLayoutManager);

        TextView hiUser = binding.hiCollaboratorUser;
        hiUser.setText(userInfoPrefs.getString("username",null));

        Button button = binding.addYourAddButton;
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(),AdCreation.class));
                getActivity().finishAffinity();
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
