package com.example.anna.MenuPrincipal.Home;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.anna.Models.HotNews;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class HomeFragmentAdapter extends FirestoreRecyclerAdapter<HotNews, HomeFragmentAdapter.HotNewsHolder> {



    public HomeFragmentAdapter(@NonNull FirestoreRecyclerOptions<HotNews> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull HotNewsHolder holder, int position, @NonNull HotNews model) {

    }

    @NonNull
    @Override
    public HotNewsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    class HotNewsHolder extends RecyclerView.ViewHolder{

        public HotNewsHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
