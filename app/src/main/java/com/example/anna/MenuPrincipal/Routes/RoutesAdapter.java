package com.example.anna.MenuPrincipal.Routes;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.anna.Models.RouteModel;
import com.example.anna.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class RoutesAdapter extends FirestoreRecyclerAdapter<RouteModel, RoutesAdapter.RouteHolder> {

    private OnRoutesClick onRoutesClick;

    public RoutesAdapter(@NonNull FirestoreRecyclerOptions<RouteModel> options, OnRoutesClick onRoutesClick) {
        super(options);
        this.onRoutesClick = onRoutesClick;
    }

    @Override
    protected void onBindViewHolder(@NonNull RouteHolder holder, int position, @NonNull RouteModel model) {
        holder.routeTitle.setText(model.getName());
        switch (model.getCategory()) {
            case "gold":
                holder.cardview.setBackgroundColor(Color.parseColor("#FFD700"));
                break;
            case "silver":
                holder.cardview.setBackgroundColor(Color.parseColor("#C0C0C0"));
                break;
            case "bronze":
                holder.cardview.setBackgroundColor(Color.parseColor("#CD7F32"));
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

        private TextView routeTitle;
        private CardView cardview;
        private RouteModel routeModel;

        public RouteHolder(@NonNull View itemView) {
            super(itemView);

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

    public interface OnRoutesClick{
        public void onRoutesClick(RouteModel routeModel);
    }
}
