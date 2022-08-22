package com.example.anna.MenuPrincipal.Discounts;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.anna.MenuPrincipal.OnDiscountClickListener;
import com.example.anna.Models.Discount;
import com.example.anna.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;




public class FragmentDiscounts extends Fragment implements OnDiscountClickListener {

    private CollectionReference discountsDBRef;
    private DiscountActivityAdapter discountActAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        discountsDBRef = firebaseFirestore.collection("Discounts");

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_discounts, container, false);

        RecyclerView rvDiscounts = view.findViewById(R.id.recyclerDiscountsActivity);


        FirestoreRecyclerOptions<Discount> options = new FirestoreRecyclerOptions.Builder<Discount>()
                .setQuery(discountsDBRef,Discount.class).build();
        discountActAdapter = new DiscountActivityAdapter(options,getContext(), this);
        rvDiscounts.setLayoutManager(new DiscountsWrapContentLinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
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