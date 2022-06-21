package com.example.anna.Register.FragmentsViewPager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.anna.Models.Tariff;
import com.example.anna.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class ShopAdapter extends FirestoreRecyclerAdapter<Tariff, ShopAdapter.TariffHolder> {

    Context context;
    private final OnTariffClick onTariffClick;

    public ShopAdapter(@NonNull FirestoreRecyclerOptions<Tariff> options, Context context, OnTariffClick onTariffClick) {
        super(options);
        this.context = context;
        this.onTariffClick = onTariffClick;
        System.out.println("constructor Shop Adapter");
    }

    @Override
    protected void onBindViewHolder(@NonNull ShopAdapter.TariffHolder holder, int position, @NonNull Tariff model) {
        holder.price.setText(String.valueOf(model.getPrice()));
        holder.condition.setText(model.getCondition());
        holder.description.setText(model.getDescription());
        System.out.println("OnbindViewHolder");
        holder.bind(model);
    }

    @NonNull
    @Override
    public ShopAdapter.TariffHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tarifflayout,parent,false);
        System.out.println("VIsta ShopAdapter Tariff holder");
        return new TariffHolder(view);
    }

    class TariffHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView condition, description, price;
        Tariff currentTariff;

        public TariffHolder(@NonNull View itemView) {
            super(itemView);
            System.out.println("creacio Tariff holder");
            price = itemView.findViewById(R.id.tarifaPreu);
            condition = itemView.findViewById(R.id.tarifaCondicio);
            description = itemView.findViewById(R.id.tarifaCondicioDescripcio);
            itemView.setOnClickListener(this);
        }

        public void bind (Tariff tariff){
            System.out.println("bind");
            currentTariff = tariff;
        }

        @Override
        public void onClick(View view) {
            onTariffClick.onTariffClick(currentTariff);
        }
    }

    public interface OnTariffClick{
        void onTariffClick(Tariff tariff);
    }
}
