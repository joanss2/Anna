package com.example.anna.MenuPrincipal.Profile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.anna.Models.LanguageItem;
import com.example.anna.R;

import java.util.List;

public class LanguagesAdapter extends RecyclerView.Adapter<LanguagesAdapter.LanguageHolder> {

    private final Context context;
    private final List<LanguageItem> list;
    private final LanguageSelectionListener listener;

    public LanguagesAdapter(Context context, List<LanguageItem> list, LanguageSelectionListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public LanguageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.language_template,parent,false);
        return new LanguageHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LanguageHolder holder, int position) {
        LanguageItem item = list.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class LanguageHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView name;
        private final ImageView imageView;
        private LanguageItem currentItem;

        public LanguageHolder(@NonNull View itemView) {
            super(itemView);
            this.name = itemView.findViewById(R.id.languageName);
            this.imageView = itemView.findViewById(R.id.languageIcon);
            itemView.setOnClickListener(this);
        }
        public void bind(LanguageItem item){
            this.name.setText(item.getName());
            this.imageView.setImageResource(item.getImageResource());
            this.currentItem = item;
        }

        @Override
        public void onClick(View v) {
            listener.onLangugeSelected(currentItem);
        }
    }

    public interface LanguageSelectionListener{
        void onLangugeSelected(LanguageItem languageItem);
    }


}
