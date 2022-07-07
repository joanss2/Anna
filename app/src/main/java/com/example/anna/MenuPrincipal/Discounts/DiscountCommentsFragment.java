package com.example.anna.MenuPrincipal.Discounts;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.anna.Models.Comment;
import com.example.anna.Models.Discount;
import com.example.anna.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DiscountCommentsFragment extends Fragment {

    //private int mLastContentHeight;
    //private ViewTreeObserver.OnGlobalLayoutListener keyboardLayoutListener;
    private final FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private Discount discount;
    private String authorKey;
    private CommentsAdapter adapter;

    public DiscountCommentsFragment(Discount discount, String authorKey){
        this.discount = discount;
        this.authorKey = authorKey;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.discount_comment, container, false);

        TextView textView = view.findViewById(R.id.postComment);
        EditText body = view.findViewById(R.id.addcommentBody);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(body.getText().toString().equals(""))
                    Toast.makeText(getContext(),"Comment can not be empty",Toast.LENGTH_SHORT).show();
                else{
                    firebaseFirestore.collection("Comments").document(discount.getKey()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if(task.isSuccessful()){
                                DocumentSnapshot document = task.getResult();
                                if(document.exists()){
                                    firebaseFirestore.collection("Comments").document(discount.getKey())
                                            .collection("discountComments").add(fillCurrentComment(body))
                                            .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                                @Override
                                                public void onComplete(@NonNull Task<DocumentReference> task) {
                                                    System.out.println("comment created");
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {

                                        }
                                    });
                                }else{
                                    Map<String,String> map = new HashMap<>();
                                    map.put("key",discount.getKey());
                                    firebaseFirestore.collection("Comments").document(discount.getKey()).set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            firebaseFirestore.collection("Comments").document(discount.getKey())
                                                    .collection("discountComments").add(fillCurrentComment(body))
                                                    .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                                @Override
                                                public void onComplete(@NonNull Task<DocumentReference> task) {
                                                    System.out.println("comment created");
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {

                                                }
                                            });

                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {

                                        }
                                    });
                                }
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });
                }

            }
        });


        RecyclerView commentsRv = view.findViewById(R.id.commentRecyclerView);
        commentsRv.setLayoutManager(new LinearLayoutManager(getContext()));

        CollectionReference discountCommentsReference = FirebaseFirestore.getInstance().collection("Comments").document(discount.getKey())
                .collection("discountComments");
        FirestoreRecyclerOptions<Comment> options = new FirestoreRecyclerOptions.Builder<Comment>()
                .setQuery(discountCommentsReference,Comment.class).build();

        adapter = new CommentsAdapter(options, getContext());
        commentsRv.setAdapter(adapter);

        return view;

    }

    public Comment fillCurrentComment(EditText body){
        Comment comment = new Comment();
        comment.setDiscountKey(discount.getKey());
        comment.setAuthorKey(authorKey);
        comment.setBody(body.getText().toString());
        comment.setTimestamp(new Timestamp(new Date()));

        return comment;
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
