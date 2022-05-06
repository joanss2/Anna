package com.example.anna.MenuPrincipal;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
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
    private SharedPreferences userInfoPrefs;
    private String usermail;
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private Dialog dialog;
    private String[] places = {"Forat de Buli", "Cal Solsona",""};
    private int[] idPlaces = {R.drawable.foratdebuli, R.drawable.calsolsona, R.drawable.calsolsona};
    private String[] descriptions = {"Holaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
            "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
            "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa","jjj",""};//new String[places.length];
    private List<Discount> discountsUsed = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myDiscountsList = new ArrayList<>();
        userInfoPrefs = getActivity().getSharedPreferences("USERINFO", Context.MODE_PRIVATE);
        usermail = userInfoPrefs.getString("email",null);
        database = FirebaseDatabase.getInstance("https://annaapp-322219-default-rtdb.europe-west1.firebasedatabase.app/");
        reference = database.getReference("users");
        dialog = new Dialog(getContext());

        for (int i=0; i<this.places.length; i++){
            discountsUsed.add(new Discount(places[i],idPlaces[i],descriptions[i]));
        }


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

        //LLISTA SIMULADA DE DESCOMPTES UTILITZATS. S'HAURIEN D'AGAFAR DE LA BBDD

        GridView gridMyDiscounts = (GridView) root.findViewById(R.id.mydiscountsgrid);
        myDiscountsAdapter = new CustomAdapter(discountsUsed,getContext());
        gridMyDiscounts.setAdapter(myDiscountsAdapter);
        return root;

    }

    private class CustomAdapter extends BaseAdapter{

        private List<Discount> discountList;
        Context context;
        private LayoutInflater inflater;

        private CustomAdapter(List<Discount> discountList, Context context){
            this.discountList = discountList;
            this.context = context;
            this.inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return discountList.size();
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
        public View getView(int position, View view, ViewGroup viewGroup) {

            View viewcustomAdapter = inflater.inflate(R.layout.discount_cardview, null);

            CardView discountCardview = (CardView) viewcustomAdapter.findViewById(R.id.discountTemplate);
            ImageView img = (ImageView) viewcustomAdapter.findViewById(R.id.mydiscountimage);
            img.setImageResource(this.discountList.get(position).getImageId());
            TextView imgTitle = (TextView) viewcustomAdapter.findViewById(R.id.mydiscounttitle);
            imgTitle.setText(discountList.get(position).getName());
            TextView imgDescription = (TextView) viewcustomAdapter.findViewById(R.id.mydiscountdescription);
            imgDescription.setText(this.discountList.get(position).getDescription());

            discountCardview.setOnClickListener(view1 -> {
                new DiscountClickedDialog(this.discountList.get(position).getImageId(),discountList.get(position).getName(),getContext());
            });

            return viewcustomAdapter;
        }
    }
}
