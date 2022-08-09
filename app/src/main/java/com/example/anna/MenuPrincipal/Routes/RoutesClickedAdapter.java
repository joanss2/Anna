package com.example.anna.MenuPrincipal.Routes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.anna.Models.Station;
import com.example.anna.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class RoutesClickedAdapter extends FirestoreRecyclerAdapter<Station, RoutesClickedAdapter.StationsImageHolder> {


    private final Context context;
    private final OnStationClickListener onStationClickListener;


    public RoutesClickedAdapter(@NonNull FirestoreRecyclerOptions<Station> options, Context context, OnStationClickListener onStationClickListener) {
        super(options);
        this.context = context;
        this.onStationClickListener = onStationClickListener;
    }

    @Override
    protected void onBindViewHolder(@NonNull StationsImageHolder holder, int position, @NonNull Station model) {
        holder.bind(model);
    }

    @NonNull
    @Override
    public StationsImageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.routes_clicked_stations, parent, false);
        return new StationsImageHolder(view);
    }

    class StationsImageHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView imageView;
        TextView stationNameView;
        Station currentStation;

        public StationsImageHolder(@NonNull View itemView) {
            super(itemView);
            this.imageView = itemView.findViewById(R.id.routesClickedStationImageView);
            this.stationNameView = itemView.findViewById(R.id.routesClickedStationName);

            stationNameView.setOnClickListener(this);
        }

        public void bind(Station station) {
            currentStation = station;
            stationNameView.setText(station.getName());

            List<String> stationsImg = station.getImageRefs();
            StorageReference storageReference = FirebaseStorage.getInstance().getReference().child(stationsImg.get(0));

            storageReference.getDownloadUrl().addOnSuccessListener(uri -> Glide.with(context).load(uri).into(imageView)).addOnFailureListener(e -> {

            });


        }

        @Override
        public void onClick(View v) {
            onStationClickListener.onStationClick(currentStation);
        }
    }

    public interface OnStationClickListener {
        void onStationClick(Station station);
    }
}
