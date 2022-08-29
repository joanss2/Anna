package com.example.anna.MenuPrincipal.Routes;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.anna.Models.Station;
import com.example.anna.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class RouteDetailAdapter extends FirestoreRecyclerAdapter<Station, RouteDetailAdapter.StationsImageHolder> {


    private final Context context;
    private final OnStationClickListener onStationClickListener;
    private SharedPreferences userInfoPrefs;


    public RouteDetailAdapter(@NonNull FirestoreRecyclerOptions<Station> options, Context context, OnStationClickListener onStationClickListener) {
        super(options);
        this.context = context;
        this.onStationClickListener = onStationClickListener;
        this.userInfoPrefs = context.getSharedPreferences("USERINFO",Context.MODE_PRIVATE);
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
        LinearLayout container;

        public StationsImageHolder(@NonNull View itemView) {
            super(itemView);
            this.imageView = itemView.findViewById(R.id.routesClickedStationImageView);
            this.stationNameView = itemView.findViewById(R.id.routesClickedStationName);
            this.container = itemView.findViewById(R.id.stationContainerInRouteDetail);

            stationNameView.setOnClickListener(this);
        }

        public void bind(Station station) {
            currentStation = station;
            stationNameView.setText(station.getName());

            FirebaseFirestore.getInstance().collection("StartedRoutes").document(userInfoPrefs.getString("userKey",null)).collection("Routes")
                    .document(station.getRouteParentKey()).collection("Stations").whereEqualTo("key",station.getKey()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if(task.isSuccessful()){
                        if(!task.getResult().isEmpty()){
                            container.setBackgroundColor(Color.parseColor("#E91E63"));
                        }else{
                            container.setBackgroundColor(Color.parseColor("#CDDC39"));
                        }
                    }
                }
            });

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
