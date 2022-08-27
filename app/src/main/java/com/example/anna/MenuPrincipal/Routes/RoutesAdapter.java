package com.example.anna.MenuPrincipal.Routes;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.anna.Models.RouteModel;
import com.example.anna.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class RoutesAdapter extends FirestoreRecyclerAdapter<RouteModel, RoutesAdapter.RouteHolder> {

    private final OnRoutesClickListener onRoutesClick;

    public RoutesAdapter(@NonNull FirestoreRecyclerOptions<RouteModel> options, OnRoutesClickListener onRoutesClick) {
        super(options);
        this.onRoutesClick = onRoutesClick;

    }

    @Override
    protected void onBindViewHolder(@NonNull RouteHolder holder, int position, @NonNull RouteModel model) {
        holder.routeTitle.setText(model.getName());
        switch (model.getCategory()) {
            case "gold":
                holder.starIcon.setImageResource(R.drawable.ic_star_gold);
                holder.routeTitle.setTextColor(Color.parseColor("#FFD700"));
                //holder.cardview.setBackgroundColor(Color.parseColor("#FFD700"));
                break;
            case "silver":
                holder.starIcon.setImageResource(R.drawable.ic_star_silver);
                holder.routeTitle.setTextColor(Color.parseColor("#C0C0C0"));
                //holder.cardview.setBackgroundColor(Color.parseColor("#C0C0C0"));
                break;
            case "bronze":
                holder.starIcon.setImageResource(R.drawable.ic_star_bronze);
                holder.routeTitle.setTextColor(Color.parseColor("#CD7F32"));
                //holder.cardview.setBackgroundColor(Color.parseColor("#CD7F32"));
                break;
            default:
                break;
        }
        holder.bind(model);
    }

    @NonNull
    @Override
    public RouteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.route_cardview_in_all_routes, parent, false);
        return new RouteHolder(view);
    }

    class RouteHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private final TextView routeTitle;
        private final CardView cardview;
        private final ImageView starIcon;
        private RouteModel routeModel;

        public RouteHolder(@NonNull View itemView) {
            super(itemView);
            starIcon = itemView.findViewById(R.id.icon_route_available_routes);
            routeTitle = itemView.findViewById(R.id.routeInAllTitle);
            cardview = itemView.findViewById(R.id.titleholderroutes);
            itemView.setOnClickListener(this);
        }

        public void bind(RouteModel routeModel){
            this.routeModel = routeModel;
        }

        @Override
        public void onClick(View view) {
            onRoutesClick.onRoutesClick(routeModel);
        }
    }

    public interface OnRoutesClickListener{
        void onRoutesClick(RouteModel routeModel);
    }
}
