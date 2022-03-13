package com.example.anna.Inicio;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.example.anna.MenuPrincipal.MenuMainActivity;
import com.example.anna.R;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.analytics.FirebaseAnalytics;

public class MainActivity extends AppCompatActivity {

    public static TabLayout tabLayout;
    public static ViewPager viewPager;
    private SharedPreferences sharedPreferences;

    TabItem tabSignUp, tabSignIn;
    PagerController pagerAdapter;
    private Button btnsignIn;
    private String emailToSignIn;
    private boolean fromup;
    private FirebaseAnalytics mFirebaseAnalytics;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*
        * Quan fem el onCreate de l'activitat principal mirem si a SharedPreferences tenim guardat un
        * valor d'email, i si és així entrem directament al menú principal de quan ja hem iniciat sessió.
        */


        sharedPreferences = getSharedPreferences(getString(R.string.sharedpreferencesfile),MODE_PRIVATE);
        System.out.println("PRINCIPAL AIXO SON SHARED PREFERENCES MAIL"+sharedPreferences.getString("email",null));
        sharedPreferences.edit().clear().commit();
        System.out.println("PRINCIPAL AIXO SON SHARED PREFERENCES MAIL"+sharedPreferences.getString("email",null));
        if(sharedPreferences.getString("email",null)!=null){
            startActivity(new Intent(this, MenuMainActivity.class));
            finish();
        }

        tabLayout = findViewById(R.id.tablayoutsignupsignin);
        viewPager = findViewById(R.id.viewpagermain);
        tabSignUp = findViewById(R.id.tabsignup);
        tabSignIn = findViewById(R.id.tabsignin);
        btnsignIn = (Button) findViewById(R.id.buttonsignin);


        /*
        * Si aquesta activitat s'ha creat amb aquesta key, vol dir que ens hem registrat i ens guardem el correu utilitzat per a fer-ho
        * i utilitzar-lo per a iniciar sessió.
        */

        if(getIntent().getExtras()!=null){
            emailToSignIn = getIntent().getExtras().getString("emailfromsignup");
            fromup = true;
        }

        pagerAdapter = new PagerController(getSupportFragmentManager(), tabLayout.getTabCount(), emailToSignIn);
        viewPager.setAdapter(pagerAdapter);

        if(fromup)
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

    /*
    @Override
    protected void onStart() {
        super.onStart();
        session();
    }


    private void session(){
        String session = MainActivity.sharedPreferences.getString("email",null);
        if(session!=null){
            startActivity(new Intent(this, MainMenu.class));
            finish();
        }
    }

     */


}