package com.example.anna.MenuPrincipal.Discounts;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.anna.MenuPrincipal.Home.FragmentHome;
import com.example.anna.MenuPrincipal.MenuMainActivity;
import com.example.anna.MenuPrincipal.OnDiscountClickListener;
import com.example.anna.MenuPrincipal.Routes.FragmentRoutes;
import com.example.anna.MenuPrincipal.Routes.RoutesClickedFragment;
import com.example.anna.Models.Discount;
import com.example.anna.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;


public class FragmentDiscounts extends Fragment implements OnDiscountClickListener {

    private CollectionReference discountsDBRef;
    private DiscountActivityAdapter discountActAdapter;
    private FragmentDiscounts fragmentDiscounts;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        discountsDBRef = firebaseFirestore.collection("Discounts");
        fragmentDiscounts = this;

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_discounts, container, false);
        /////////////////
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        System.out.println("back stack in discountsfragment: "+fragmentManager.getBackStackEntryCount());
        /////////////////
        RecyclerView rvDiscounts = view.findViewById(R.id.recyclerDiscountsActivity);


        FirestoreRecyclerOptions<Discount> options = new FirestoreRecyclerOptions.Builder<Discount>()
                .setQuery(discountsDBRef,Discount.class).build();
        discountActAdapter = new DiscountActivityAdapter(options,getContext(), this);
        rvDiscounts.setLayoutManager(new LinearLayoutManager(getContext()));
        rvDiscounts.setAdapter(discountActAdapter);

        return view;
    }

    @Override
    public void onDiscountClick(Discount discount) {
        new DiscountClickedDialog(discount,getContext(),(AppCompatActivity) getActivity());
    }

    @Override
    public void onStart() {
        super.onStart();
        discountActAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        discountActAdapter.stopListening();

    }

}