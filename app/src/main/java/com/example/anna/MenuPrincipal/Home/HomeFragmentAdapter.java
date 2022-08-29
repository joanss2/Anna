package com.example.anna.MenuPrincipal.Home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.anna.Models.HotNews;
import com.example.anna.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.storage.FirebaseStorage;

import java.text.SimpleDateFormat;

public class HomeFragmentAdapter extends FirestoreRecyclerAdapter<HotNews, HomeFragmentAdapter.HotNewsHolder> {

    private final Context context;
    private FragmentManager manager;
    private MoreClickListener moreClickListener;

    public HomeFragmentAdapter(@NonNull FirestoreRecyclerOptions<HotNews> options, Context context) {
        super(options);
        this.context = context;
    }

    public HomeFragmentAdapter(@NonNull FirestoreRecyclerOptions<HotNews> options, Context context,MoreClickListener moreClickListener) {
        super(options);
        this.context = context;
        this.moreClickListener = moreClickListener;
    }

    @Override
    protected void onBindViewHolder(@NonNull HotNewsHolder holder, int position, @NonNull HotNews model) {
        holder.title.setText(model.getTitle());
        holder.description.setText(model.getDescription());
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        holder.dateEnd.setText(simpleDateFormat.format(model.getEndDate()));
        FirebaseStorage.getInstance().getReference("advertisements").child(model.getKey()).getDownloadUrl().addOnSuccessListener(
                uri -> Glide.with(context).load(uri).into(holder.picture)
        );
        holder.onBind(model);

    }

    @NonNull
    @Override
    public HotNewsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.ad_template, parent, false);
        return new HotNewsHolder(view);
    }

    class HotNewsHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView title, description, dateEnd;
        ImageView picture, more;
        HotNews hotNews;

        public HotNewsHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.adTemplateTitle);
            description = itemView.findViewById(R.id.adTemplateDescription);
            dateEnd = itemView.findViewById(R.id.adTemplateDeadline);
            picture = itemView.findViewById(R.id.adTemplatePicture);
            more = itemView.findViewById(R.id.adMoreOptions);

            more.setOnClickListener(this);
        }

        public void onBind(HotNews hotNews){
            this.hotNews = hotNews;
        }

        @Override
        public void onClick(View view) {
            if(moreClickListener==null)
                System.out.println("MORE CLIKCK LISTENER NULL");
            else
                moreClickListener.OnMoreClick(hotNews);

        }
    }

    public interface MoreClickListener{
        void OnMoreClick(HotNews hotNews);
    }

}
