package com.example.anna.MenuPrincipal.Discounts;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.anna.Models.Comment;
import com.example.anna.Models.Discount;
import com.example.anna.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class CommentsAdapter extends FirestoreRecyclerAdapter<Comment, CommentsAdapter.CommentHolder> {

    private Context context;
    private String authorkey;

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
        private TextView body, date, username;
        private ImageView userImage;

        public CommentHolder(@NonNull View itemView) {
            super(itemView);
            this.body= itemView.findViewById(R.id.bodyComment);
            this.date = itemView.findViewById(R.id.commentDate);
            this.username = itemView.findViewById(R.id.commentauthorname);
            this.userImage = itemView.findViewById(R.id.commentUserImage);
        }

        public void bind(Comment comment){
            body.setText(comment.getBody());
            date.setText(comment.getTimestamp().toString());
            System.out.println(authorkey);
            realtimeref.child(authorkey).child("username").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    username.setText(String.valueOf(task.getResult().getValue()));
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });
            userImage.setImageResource(R.drawable.andorra);

        }
    }
}
