package com.example.anna.Register;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.anna.MenuPrincipal.CollaboratorMenu;
import com.example.anna.MenuPrincipal.MenuMainActivity;
import com.example.anna.Models.Subscription;
import com.example.anna.R;
import com.example.anna.Register.Collaborator.CollaboratorTariffActivity;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.Date;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    public static TabLayout tabLayout;
    public static ViewPager viewPager;

    TabItem tabSignUp, tabSignIn;
    PagerController pagerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences userInfoPrefs = getSharedPreferences("USERINFO", MODE_PRIVATE);


        //userInfoPrefs.edit().clear().apply();


        if(userInfoPrefs.getString("email",null)!=null){
            if(userInfoPrefs.getString("usertype",null).equals("client")){
                startActivity(new Intent(this, MenuMainActivity.class));
                finish();
            }else{
                CollectionReference subReference = FirebaseFirestore.getInstance().collection("Subscriptions").document(userInfoPrefs.getString("userKey", null))
                        .collection("SubsOfUser");
                subReference.get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if(task.getResult().isEmpty()){
                            startActivity(new Intent(MainActivity.this, CollaboratorTariffActivity.class));
                            finish();
                        }else{
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map<String, Object> map = document.getData();

                                Subscription subscription = new Subscription(map);
                                if (subscription.getDateEnd().after(new Date())) {
                                    startActivity(new Intent(MainActivity.this, CollaboratorMenu.class));
                                } else {
                                    startActivity(new Intent(MainActivity.this, CollaboratorTariffActivity.class).putExtra(
                                            "ended",true
                                    ));
                                }
                                finish();
                            }
                        }


                    }
                }).addOnFailureListener(e -> System.out.println("FAILURE"));
                //startActivity(new Intent(this, CollaboratorMenu.class));

            }
            //
        }else{
            setContentView(R.layout.activity_main);
            tabLayout = findViewById(R.id.tablayoutsignupsignin);
            viewPager = findViewById(R.id.viewpagermain);
            tabSignUp = findViewById(R.id.tabsignup);
            tabSignIn = findViewById(R.id.tabsignin);



            pagerAdapter = new PagerController(getSupportFragmentManager(), tabLayout.getTabCount());
            viewPager.setAdapter(pagerAdapter);
            viewPager.setCurrentItem(1);

            tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    viewPager.setCurrentItem(tab.getPosition());
                    if (tab.getPosition() == 0) {
                        pagerAdapter.notifyDataSetChanged();
                    }
                    if (tab.getPosition() == 1) {
                        pagerAdapter.notifyDataSetChanged();
                    }

                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {
                }
                @Override
                public void onTabReselected(TabLayout.Tab tab) {
                }
            });
            viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        }


    }

}