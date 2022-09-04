package com.example.anna.MenuPrincipal.Discounts;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.anna.Models.Comment;
import com.example.anna.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageException;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;

public class CommentsAdapter extends FirestoreRecyclerAdapter<Comment, CommentsAdapter.CommentHolder> {

    private final Context context;
    private final String authorkey;

    public CommentsAdapter(@NonNull FirestoreRecyclerOptions<Comment> options, Context context, String authorkey) {
        super(options);
        this.context = context;
        this.authorkey = authorkey;
    }

    @Override
    protected void onBindViewHolder(@NonNull CommentHolder holder, int position, @NonNull Comment model) {
        holder.bind(model);
    }

    @NonNull
    @Override
    public CommentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.comment_template, parent, false);
        return new CommentHolder(view);
    }

    class CommentHolder extends RecyclerView.ViewHolder{

        private final DatabaseReference realtimeref = FirebaseDatabase.getInstance("https://annaapp-322219-default-rtdb.europe-west1.firebasedatabase.app/").getReference("users");
        private final StorageReference storageReference = FirebaseStorage.getInstance().getReference("users");
        private final TextView body;
        private final TextView date;
        private final TextView username;
        private final ImageView userImage;

        public CommentHolder(@NonNull View itemView) {
            super(itemView);
            this.body= itemView.findViewById(R.id.bodyComment);
            this.date = itemView.findViewById(R.id.commentDate);
            this.username = itemView.findViewById(R.id.commentauthorname);
            this.userImage = itemView.findViewById(R.id.commentUserImage);
        }

        public void bind(Comment comment){
            body.setText(comment.getBody());
            @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            date.setText(format.format(comment.getTimestamp().toDate()));
            System.out.println(authorkey);
            realtimeref.child(authorkey).child("username").get().addOnCompleteListener(task -> username.setText(String.valueOf(task.getResult().getValue()))).addOnFailureListener(e -> {

            });
            storageReference.child(authorkey).getDownloadUrl().addOnSuccessListener(uri -> Glide.with(context).load(uri).into(userImage)).addOnFailureListener(e -> {
                int errorCode = ((StorageException) e).getErrorCode();
                if (errorCode == StorageException.ERROR_OBJECT_NOT_FOUND) {
                    userImage.setImageResource(R.drawable.user_icon);
                }
            });

        }
    }
}
