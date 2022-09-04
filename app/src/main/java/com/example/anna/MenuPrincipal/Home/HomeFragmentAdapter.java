package com.example.anna.MenuPrincipal.Home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;

import java.text.SimpleDateFormat;
import java.util.concurrent.atomic.AtomicReference;

public class HomeFragmentAdapter extends FirestoreRecyclerAdapter<HotNews, HomeFragmentAdapter.HotNewsHolder> {

    private final Context context;
    private FragmentManager manager;
    private AdClickListener adClickListener;

    public HomeFragmentAdapter(@NonNull FirestoreRecyclerOptions<HotNews> options, Context context) {
        super(options);
        this.context = context;
    }

    public HomeFragmentAdapter(@NonNull FirestoreRecyclerOptions<HotNews> options, Context context, AdClickListener adClickListener) {
        super(options);
        this.context = context;
        this.adClickListener = adClickListener;
    }

    @Override
    protected void onBindViewHolder(@NonNull HotNewsHolder holder, int position, @NonNull HotNews model) {
        holder.title.setText(model.getTitle());
        holder.description.setText(model.getDescription());
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        holder.dateEnd.setText(simpleDateFormat.format(model.getEndDate()));
        FirebaseStorage.getInstance().getReference("advertisements").child(model.getKey()).getDownloadUrl().addOnSuccessListener(
                new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Glide.with(context).load(uri).into(holder.picture);
                        holder.onBind(model, uri);
                    }
                }
        );

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
        Uri uri;

        public HotNewsHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.adTemplateTitle);
            description = itemView.findViewById(R.id.adTemplateDescription);
            dateEnd = itemView.findViewById(R.id.adTemplateDeadline);
            picture = itemView.findViewById(R.id.adTemplatePicture);
            more = itemView.findViewById(R.id.adMoreOptions);

            more.setOnClickListener(this);
            title.setOnClickListener(this);
            description.setOnClickListener(this);
            picture.setOnClickListener(this);
        }

        public void onBind(HotNews hotNews, Uri uri) {
            this.hotNews = hotNews;
            this.uri = uri;
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.adMoreOptions:
                    adClickListener.OnAdMoreClick(hotNews);
                    break;
                default:
                    adClickListener.OnAdClick(hotNews, uri);
                    break;
            }
        }
    }

    public interface AdClickListener {

        void OnAdMoreClick(HotNews hotNews);
        void OnAdClick(HotNews hotnews, Uri uri);
    }

}
