package com.example.anna.MenuPrincipal.Discounts.MyDiscounts;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.anna.MenuPrincipal.Discounts.DiscountsWrapContentLinearLayoutManager;
import com.example.anna.MenuPrincipal.Discounts.FragmentDiscounts;
import com.example.anna.MenuPrincipal.MenuMainActivity;
import com.example.anna.Models.Discount;
import com.example.anna.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;



public class FragmentMyDiscounts extends Fragment implements MyDiscountsAdapter.OnMyDiscountUsedClickListener {


    private MyDiscountsAdapter myDiscountsAdapter;
    private CollectionReference discountsUsed;
    private boolean hasDiscounts;
    private View viewParent;


    public FragmentMyDiscounts(){}
    public FragmentMyDiscounts(View view){
        this.viewParent = view;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        SharedPreferences userInfoPrefs = requireActivity().getSharedPreferences("USERINFO", Context.MODE_PRIVATE);
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        discountsUsed = firebaseFirestore.collection("DiscountsUsed").document(userInfoPrefs.getString("userKey", null))
        .collection("DiscountsReferenceList");
        hasDiscounts = userInfoPrefs.getBoolean("hasDiscounts", false);
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed(){
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root;

        if (hasDiscounts) {

            root = inflater.inflate(R.layout.activity_fragment_mydiscounts, container, false);
            RecyclerView rvDiscounts = root.findViewById(R.id.recyclerDiscounts);


            FirestoreRecyclerOptions<Discount> options = new FirestoreRecyclerOptions.Builder<Discount>()
                    .setQuery(discountsUsed, Discount.class)
                    .build();


            myDiscountsAdapter = new MyDiscountsAdapter(options, this, getContext());
            DiscountsWrapContentLinearLayoutManager layoutManager = new DiscountsWrapContentLinearLayoutManager(
                    getContext(),LinearLayoutManager.VERTICAL,false
            );
            rvDiscounts.setLayoutManager(layoutManager);
            rvDiscounts.setAdapter(myDiscountsAdapter);


        } else {
            root = inflater.inflate(R.layout.activity_fragment_mydiscounts_empty, container, false);
        }

        return root;
    }


    @Override
    public void onStart() {
        if (hasDiscounts) {
            myDiscountsAdapter.startListening();
        }

        super.onStart();
    }

    @Override
    public void onStop() {
        if (hasDiscounts) {
            myDiscountsAdapter.stopListening();
        }
        super.onStop();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (!hasDiscounts && this.viewParent!=null) {
            Snackbar snackbar = Snackbar.make(viewParent, getString(R.string.goToDiscounts), Snackbar.LENGTH_INDEFINITE);

            snackbar.setAction("GO", v -> {
                FragmentManager manager = requireActivity().getSupportFragmentManager();
                FragmentTransaction trans = manager.beginTransaction();
                //trans.remove(fragmentParent);
                //manager.popBackStack();
                trans.replace(R.id.main_frame, new FragmentDiscounts()).addToBackStack(null);
                MenuMainActivity activity = (MenuMainActivity) getActivity();
                assert activity != null;
                activity.getBottomNavigationView().setSelectedItemId(R.id.bottom_discounts);
                trans.commit();

            });
            snackbar.show();
            final Handler handler = new Handler();
            handler.postDelayed(snackbar::dismiss, 4000);

        }
    }

    @Override
    public void onMyDiscountUsedClick(Discount discount) {
        requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, new DiscountUsedDetail(discount)).addToBackStack(null).commit();
    }
}
