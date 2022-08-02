package com.example.anna.Register.Collaborator;

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
    }

    @Override
    protected void onBindViewHolder(@NonNull ShopAdapter.TariffHolder holder, int position, @NonNull Tariff model) {
        String aux = model.getPrice()+"€";
        holder.price.setText(aux);
        holder.condition.setText(model.getCondition());
        holder.description.setText(model.getDescription());
        holder.bind(model);
    }

    @NonNull
    @Override
    public ShopAdapter.TariffHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tarifflayout,parent,false);
        return new TariffHolder(view);
    }

    class TariffHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView condition, description, price;
        Tariff currentTariff;

        public TariffHolder(@NonNull View itemView) {
            super(itemView);
            price = itemView.findViewById(R.id.tarifaPreu);
            condition = itemView.findViewById(R.id.tarifaCondicio);
            description = itemView.findViewById(R.id.tarifaCondicioDescripcio);
            itemView.setOnClickListener(this);
        }

        public void bind (Tariff tariff){
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
