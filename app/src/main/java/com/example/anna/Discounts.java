package com.example.anna;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

public class Discounts extends AppCompatActivity {

    private GridView gridView;
    private CustomAdapter gridAdapter;

    private String[] places = {"Condis Ponts","Forat de Buli","Panta de Rialb","Andorra","Balaguer"
    ,"Cal Solsona", "Sort Rafting","Pastisseria Serra"};
    private int[] idPlaces = {R.drawable.condis, R.drawable.foratdebuli, R.drawable.pantarialb,
    R.drawable.andorra, R.drawable.balaguer, R.drawable.calsolsona, R.drawable.sortrafting, R.drawable.serra};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discounts);

        gridView = (GridView) findViewById(R.id.griddiscounts);
        gridAdapter = new CustomAdapter(places, idPlaces, this);

        gridView.setAdapter(gridAdapter);
    }




    private class CustomAdapter extends BaseAdapter {

        private final String [] places;
        private final int [] idPlaces;
        Context context;
        private LayoutInflater inflater;

        private CustomAdapter(String[] places, int[] idPlaces, Context context) {
            this.places = places;
            this.idPlaces = idPlaces;
            this.context = context;
            this.inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        }

        @Override
        public int getCount() {
            return this.idPlaces.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = inflater.inflate(R.layout.singleframe, null);

            ImageView img = (ImageView) view.findViewById(R.id.iconimage);
            TextView imgTitle = (TextView) view.findViewById(R.id.titleimage);

            img.setImageResource(idPlaces[position]);
            imgTitle.setText(places[position]);

            return view;
        }
    }
}