package com.example.anna.MenuPrincipal;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
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
import androidx.appcompat.app.AppCompatActivity;


public class MenuMainActivity extends AppCompatActivity {

    private SharedPreferences userInfoPrefs;
    private SharedPreferences.Editor userInfoEditor;
    private Fragment selector;
    private BottomNavigationView bottomNavigationView;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        com.example.anna.databinding.MainWithBottomMenuBinding binding = MainWithBottomMenuBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        userInfoPrefs = getSharedPreferences("USERINFO", Context.MODE_PRIVATE);
        userInfoEditor = userInfoPrefs.edit();
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.id_bottom_navigation);

        if(getIntent().getStringExtra("fromEditProfile")!=null){
            selector=new FragmentProfile();
            bottomNavigationView.setSelectedItemId(R.id.bottom_profile);
            getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, selector).commit();
        }else{
            getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, new FragmentHome()).commit();
        }


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
        builder.setTitle("Exit Confirmation");
        builder.setTitle("Are you sure you want to log out?");
        builder.setPositiveButton("YES", (dialog, which) -> {
            userInfoEditor.clear().commit();
            Toast.makeText(getApplicationContext(), "i have been clicked", Toast.LENGTH_SHORT).show();
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(getApplicationContext(), MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            finish();
        });
        builder.setNegativeButton("NO", (dialog, which) -> dialog.dismiss());
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

}