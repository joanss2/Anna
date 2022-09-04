package com.example.anna.MenuPrincipal;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import com.example.anna.MenuPrincipal.Home.CollaboratorFragmentHome;
import com.example.anna.MenuPrincipal.Profile.CollaboratorFragmentProfile;
import com.example.anna.R;
import com.example.anna.Register.MainActivity;
import com.example.anna.databinding.CollaboratorMainWithBottomMenuBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

public class CollaboratorMenu extends AppCompat {

    private SharedPreferences.Editor userInfoEditor;
    private Fragment selector;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        CollaboratorMainWithBottomMenuBinding binding = CollaboratorMainWithBottomMenuBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        SharedPreferences userInfoPrefs = getSharedPreferences("USERINFO", MODE_PRIVATE);
        userInfoEditor = userInfoPrefs.edit();

        BottomNavigationView bottomNavigationView = findViewById(R.id.id_collaborator_bottom_navigation);
        getSupportFragmentManager().beginTransaction().replace(R.id.collaborator_main_frame, new CollaboratorFragmentHome()).commit();

        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.collaborator_bottom_home:
                    selector = new CollaboratorFragmentHome();
                    break;
                case R.id.collaborator_bottom_profile:
                    selector = new CollaboratorFragmentProfile();
                    break;
            }
            if (selector != null) {
                getSupportFragmentManager().beginTransaction().replace(R.id.collaborator_main_frame, selector).commit();
            }
            return true;
        });

        FloatingActionButton signOutButton = binding.signoutbutton;
        signOutButton.setOnClickListener(v -> leaving_confirmation());


    }


    public void leaving_confirmation() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.exitConfirmation));
        builder.setTitle(getString(R.string.exitConfirmationQuestion));
        builder.setPositiveButton(getString(R.string.yes), (dialog, which) -> {
            userInfoEditor.clear().commit();
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(getApplicationContext(), MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            finishAffinity();
        });
        builder.setNegativeButton(getString(R.string.no), (dialog, which) -> dialog.dismiss());
        builder.show();
    }

}
