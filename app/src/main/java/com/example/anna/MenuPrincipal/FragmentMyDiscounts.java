package com.example.anna.MenuPrincipal;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.anna.Discount;
import com.example.anna.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FragmentMyDiscounts extends Fragment {

    private CustomAdapter myDiscountsAdapter;
    private List<Discount> myDiscountsList;
    private SharedPreferences sharedPreferences;
    private String usermail;
    private FirebaseDatabase database;
    private DatabaseReference reference;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myDiscountsList = new ArrayList<>();
        sharedPreferences = getActivity().getSharedPreferences(getString(R.string.sharedpreferencesfile), Context.MODE_PRIVATE);
        usermail = sharedPreferences.getString("email",null);
        database = FirebaseDatabase.getInstance("https://annaapp-322219-default-rtdb.europe-west1.firebasedatabase.app/");
        reference = database.getReference("users");

    }
    /*

    private List<Discount> DiscountsUsed(DatabaseReference reference, String email){
        List<Discount> discList = new ArrayList<>();
        Query query = reference.orderByChild("email").equalTo(usermail).orderByChild("discounts");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        }
        return list;
    }

     */

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.activity_fragment_mydiscounts, container, false);

        //PASSAR LLISTA DE DESCOMPTES UTILITZATS PER USUARI



        GridView gridMyDiscounts = (GridView) root.findViewById(R.id.mydiscountsgrid);
        myDiscountsAdapter = new CustomAdapter();
        gridMyDiscounts.setAdapter(myDiscountsAdapter);
        return root;

    }

    private class CustomAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return 0;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            return null;
        }
    }
}
