package com.example.anna.MenuPrincipal;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.example.anna.MenuPrincipal.Discounts.FragmentDiscounts;
import com.example.anna.MenuPrincipal.Home.FragmentHome;
import com.example.anna.MenuPrincipal.Profile.FragmentProfile;
import com.example.anna.MenuPrincipal.Routes.FragmentRoutes;
import com.example.anna.Register.MainActivity;
import com.example.anna.R;
import com.example.anna.databinding.MainWithBottomMenuBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import androidx.fragment.app.Fragment;


public class MenuMainActivity extends AppCompat {

    private SharedPreferences userInfoPrefs;
    private SharedPreferences.Editor userInfoEditor;
    private Fragment selector;
    private BottomNavigationView bottomNavigationView;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        userInfoPrefs = getSharedPreferences("USERINFO", Context.MODE_PRIVATE);
        userInfoEditor = userInfoPrefs.edit();


        com.example.anna.databinding.MainWithBottomMenuBinding binding = MainWithBottomMenuBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);


        bottomNavigationView = findViewById(R.id.id_bottom_navigation);
        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, new FragmentHome()).commit();


        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.bottom_home:
                    selector = new FragmentHome();
                    break;
                case R.id.bottom_discounts:
                    selector = new FragmentDiscounts();
                    break;
                case R.id.bottom_routes:
                    selector = new FragmentRoutes();
                    break;
                case R.id.bottom_profile:
                    selector = new FragmentProfile();
                    break;
            }
            if (selector != null) {
                getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, selector).commit();
            }
            return true;
        });

        FloatingActionButton signOutButton = binding.signoutbutton;
        signOutButton.setOnClickListener(v -> leaving_confirmation());

    }


    @Override
    protected void onResume() {
        super.onResume();
        userHasDiscounts(userInfoPrefs.getString("userKey", null));
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

    public void userHasDiscounts(String key) {
        DocumentReference ref = FirebaseFirestore.getInstance().collection("DiscountsUsed").document(key);
        ref.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                userInfoEditor.putBoolean("hasDiscounts", task.getResult().exists());
                userInfoEditor.commit();
            }
        }).addOnFailureListener(e -> System.out.println("Failed"));
    }

    public BottomNavigationView getBottomNavigationView() {
        return this.bottomNavigationView;
    }


}