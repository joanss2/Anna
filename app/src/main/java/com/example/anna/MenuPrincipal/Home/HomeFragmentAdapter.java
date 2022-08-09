package com.example.anna.MenuPrincipal.Home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.anna.Models.HotNews;
import com.example.anna.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class HomeFragmentAdapter extends FirestoreRecyclerAdapter<HotNews, HomeFragmentAdapter.HotNewsHolder> {

    private Context context;

    public HomeFragmentAdapter(@NonNull FirestoreRecyclerOptions<HotNews> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull HotNewsHolder holder, int position, @NonNull HotNews model) {
        holder.title.setText(model.getTitle());
        holder.description.setText(model.getDescription());
        holder.dateEnd.setText(model.getEndDate().toString());
        holder.picture.setImageResource(R.drawable.catalunya);

    }

    @NonNull
    @Override
    public HotNewsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.add_template, parent, false);
        return new HotNewsHolder(view);
    }

    class HotNewsHolder extends RecyclerView.ViewHolder{

        TextView title, description, dateEnd;
        ImageView picture;

        public HotNewsHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.adTemplateTitle);
            description = itemView.findViewById(R.id.adTemplateDescription);
            dateEnd = itemView.findViewById(R.id.adTemplateDeadline);
            picture = itemView.findViewById(R.id.adTemplatePicture);
        }
    }
}
