package com.example.anna.MenuPrincipal.Home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.anna.Models.HotNews;
import com.example.anna.Models.User;
import com.example.anna.databinding.CollaboratorFragmentHomeBinding;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;


public class CollaboratorFragmentHome extends Fragment implements HomeFragmentAdapter.AdClickListener {

    private CollaboratorFragmentHomeBinding binding;
    private SharedPreferences userInfoPrefs;
    private HomeFragmentAdapter adapter;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userInfoPrefs = requireActivity().getSharedPreferences("USERINFO", Context.MODE_PRIVATE);

    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        CollectionReference adsReference = FirebaseFirestore.getInstance().collection("Advertisements").document(userInfoPrefs.getString("userKey",null)).collection("AdsOfUser");
        adsReference.get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                if(task.getResult().isEmpty()){
                    try {
                        Thread.sleep(1500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }finally {
                        Toast.makeText(getContext(),"NO ADS YET. BE THE FIRST ONE AND POST YOURS",Toast.LENGTH_LONG).show();

                    }
                }
            }
        });


        binding = CollaboratorFragmentHomeBinding.inflate(getLayoutInflater());
        View root = binding.getRoot();


        RecyclerView recyclerView = binding.homeCollaboratorRecyclerView;
        WrapContentLayoutManagerHome managerHome = new WrapContentLayoutManagerHome(getContext(),2);
        recyclerView.setLayoutManager(managerHome);
        CollectionReference adReference = FirebaseFirestore.getInstance().collection("Advertisements").document(userInfoPrefs.getString("userKey",null))
                .collection("AdsOfUser");
        FirestoreRecyclerOptions<HotNews> options = new FirestoreRecyclerOptions.Builder<HotNews>().setQuery(adReference,HotNews.class).build();
        adapter = new HomeFragmentAdapter(options, getContext(),this);
        recyclerView.setAdapter(adapter);

        TextView hiUser = binding.hiCollaboratorUser;
        hiUser.setText(userInfoPrefs.getString("username",null));

        Button buttonPost = binding.addYourAddButton;
        buttonPost.setOnClickListener(view -> {
            startActivity(new Intent(getContext(),AdCreation.class));
        });


        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.startListening();
    }

    @Override
    public void onPause() {
        super.onPause();
        adapter.stopListening();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    public void OnAdMoreClick(HotNews hotNews) {
        MoreDialog moreDialog = new MoreDialog(hotNews);
        moreDialog.show(requireActivity().getSupportFragmentManager(),"MORE CLICK");
    }


    @Override
    public void OnAdClick(HotNews hotnews, Uri uri) {
        FirebaseDatabase.getInstance().getReference("collaborators").child(hotnews.getAuthor()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    User user = snapshot.getValue(User.class);
                    assert user != null;
                    FirebaseStorage.getInstance().getReference("collaborators").child(hotnews.getAuthor()).getDownloadUrl().addOnSuccessListener(
                            new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uriUserImage) {
                                    AdDetailDialog adDetailDialog = new AdDetailDialog(hotnews,uri,uriUserImage,user.getUsername());
                                    adDetailDialog.show(requireActivity().getSupportFragmentManager(),"AD INFO DETAIL");
                                }
                            }
                    ).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            AdDetailDialog adDetailDialog = new AdDetailDialog(hotnews,uri,null,user.getUsername());
                            adDetailDialog.show(requireActivity().getSupportFragmentManager(),"AD INFO DETAIL");
                        }
                    });

                }else{
                    System.out.println("NADA NADA");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
