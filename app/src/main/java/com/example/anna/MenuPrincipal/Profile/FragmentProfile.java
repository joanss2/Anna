package com.example.anna.MenuPrincipal.Profile;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;

import com.example.anna.MenuPrincipal.MenuMainActivity;
import com.example.anna.R;
import com.example.anna.databinding.ActivityFragmentProfileBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class FragmentProfile extends Fragment {

    private EditText userName, userMail, userTel;
    private SharedPreferences userInfoPrefs;
    private SharedPreferences.Editor userInfoEditor;
    private ActivityFragmentProfileBinding binding;
    private ImageView profileImageView;
    private String urlPicture;
    private Lock lock = new ReentrantLock();
    private Condition condition = lock.newCondition();
    private FloatingActionButton editButton;
    private boolean clicked;
    private final FirebaseDatabase database = FirebaseDatabase.getInstance("https://annaapp-322219-default-rtdb.europe-west1.firebasedatabase.app/");
    private final DatabaseReference reference = database.getReference("users");


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userInfoPrefs = getActivity().getSharedPreferences("USERINFO", Context.MODE_PRIVATE);
        userInfoEditor = userInfoPrefs.edit();
        clicked = false;

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = ActivityFragmentProfileBinding.inflate(getLayoutInflater());
        View root = binding.getRoot();
        userName = binding.profileUsername;
        userName.setClickable(false);
        userName.setEnabled(false);
        userName.setText(userInfoPrefs.getString("username", null));
        userMail = binding.profileUseremail;
        userMail.setText(userInfoPrefs.getString("email", null));
        userMail.setClickable(false);
        userMail.setEnabled(false);
        userTel = binding.profileUsertelefono;
        userTel.setText(userInfoPrefs.getString("usertel", null));
        profileImageView = binding.profileImageView;
        urlPicture = userInfoPrefs.getString("fotourl", null);//
        Glide.with(getContext()).load(urlPicture).into(profileImageView);

        editButton = (FloatingActionButton) root.findViewById(R.id.editnamebutton);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (clicked) {
                    userName.setEnabled(false);
                    clicked = false;
                    editButton.setImageResource(R.drawable.ic_edit);
                    changedUsername();
                    Toast.makeText(getContext(), "Username edited!", Toast.LENGTH_LONG).show();
                } else {
                    userName.setEnabled(true);
                    clicked = true;
                    editButton.setImageResource(R.drawable.ic_tick);
                }

            }
        });


        return root;
    }

    public void changedUsername() {
        userInfoEditor.remove("username");
        userInfoEditor.putString("username", userName.getText().toString());
        userInfoEditor.commit();
        Query query = reference.orderByChild("email").equalTo(FragmentProfile.this.userInfoPrefs.getString("email", null));
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        DatabaseReference dr = database.getReference("users/" + ds.getKey());
                        dr.child("username").setValue(userName.getText().toString());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        ((MenuMainActivity) getActivity()).updateUsername();
    }
}
