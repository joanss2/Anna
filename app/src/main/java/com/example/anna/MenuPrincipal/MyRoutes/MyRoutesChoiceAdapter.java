package com.example.anna.MenuPrincipal.MyRoutes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.anna.Models.RouteChoice;
import com.example.anna.R;

import java.util.List;

public class MyRoutesChoiceAdapter extends RecyclerView.Adapter<MyRoutesChoiceAdapter.ViewHolder> {

    Context context;
    List<RouteChoice> choices;
    private final OnChoiceClickListener onChoiceClickListener;

    public MyRoutesChoiceAdapter(Context context, List<RouteChoice> choices, OnChoiceClickListener onChoiceClickListener) {
        this.context = context;
        this.choices = choices;
        this.onChoiceClickListener = onChoiceClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.routechoice_cardview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RouteChoice routeChoice = choices.get(position);
        holder.bind(routeChoice);
    }

    @Override
    public int getItemCount() {
        return choices.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView title;
        ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.routechoiceTitle);
            imageView = itemView.findViewById(R.id.routechoiceimage);
            itemView.setOnClickListener(this);
        }

        public void bind(RouteChoice routeChoice) {
            title.setText(routeChoice.getTitle());
            imageView.setImageResource(routeChoice.getImageDrawable());
        }

        @Override
        public void onClick(View view) {
            onChoiceClickListener.onChoiceClick(title.getText().toString());
        }
    }

    public interface OnChoiceClickListener {
        void onChoiceClick(String title);
    }
}
