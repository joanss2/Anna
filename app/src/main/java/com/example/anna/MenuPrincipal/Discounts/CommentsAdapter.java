package com.example.anna.MenuPrincipal.Discounts;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.anna.Models.Comment;
import com.example.anna.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class CommentsAdapter extends FirestoreRecyclerAdapter<Comment, CommentsAdapter.CommentHolder> {

    private Context context;

    public CommentsAdapter(@NonNull FirestoreRecyclerOptions<Comment> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull CommentHolder holder, int position, @NonNull Comment model) {
        holder.body.setText(model.getBody());
        holder.date.setText(model.getTimestamp().toString());
    }

    @NonNull
    @Override
    public CommentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.comment_template, parent, false);
        return new CommentHolder(view);
    }

    class CommentHolder extends RecyclerView.ViewHolder{

        private TextView body, date;

        public CommentHolder(@NonNull View itemView) {
            super(itemView);
            this.body= itemView.findViewById(R.id.bodyComment);
            this.date = itemView.findViewById(R.id.commentDate);
        }
    }
}
