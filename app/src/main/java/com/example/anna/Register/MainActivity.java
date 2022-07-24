package com.example.anna.Register;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import com.example.anna.MenuPrincipal.MenuMainActivity;
import com.example.anna.R;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {

    public static TabLayout tabLayout;
    public static ViewPager viewPager;

    TabItem tabSignUp, tabSignIn;
    PagerController pagerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences userInfoPrefs = getSharedPreferences("USERINFO", MODE_PRIVATE);
        setContentView(R.layout.activity_main);

        //userInfoPrefs.edit().clear().commit();


        if(userInfoPrefs.getString("email",null)!=null){
            startActivity(new Intent(this, MenuMainActivity.class));
            finish();
        }

        tabLayout = findViewById(R.id.tablayoutsignupsignin);
        viewPager = findViewById(R.id.viewpagermain);
        tabSignUp = findViewById(R.id.tabsignup);
        tabSignIn = findViewById(R.id.tabsignin);



        pagerAdapter = new PagerController(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(pagerAdapter);
        viewPager.setCurrentItem(1);

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
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