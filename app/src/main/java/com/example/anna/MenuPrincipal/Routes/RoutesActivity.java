package com.example.anna.MenuPrincipal.Routes;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.anna.Models.Station;
import com.example.anna.R;
import com.example.anna.MenuPrincipal.Routes.Stations.CondisStation;
import com.example.anna.MenuPrincipal.Routes.Stations.HomiliesStation;
import com.example.anna.MenuPrincipal.Routes.Stations.NaturlandiaStation;
import com.example.anna.MenuPrincipal.Routes.Stations.SantPereStation;
import com.example.anna.MenuPrincipal.Routes.Stations.SantaMariaStation;

public class RoutesActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView santPereImg, santaMariaImg, condisImg, homiliesImg, naturImg;
    private Station currentStation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_route2);

        santPereImg = findViewById(R.id.imageViewsantpere);
        santaMariaImg = findViewById(R.id.imageViewsantamaria);
        condisImg = findViewById(R.id.imageViewcondis);
        homiliesImg = findViewById(R.id.imageViewhomilies);
        naturImg = findViewById(R.id.imageViewnaturlandia);

        santPereImg.setOnClickListener(this);
        santaMariaImg.setOnClickListener(this);
        condisImg.setOnClickListener(this);
        homiliesImg.setOnClickListener(this);
        naturImg.setOnClickListener(this);

    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageViewsantpere:
                currentStation = new SantPereStation(this, Ubications.SANT_PERE_UBI.getValue());
                break;
            case R.id.imageViewsantamaria:
                currentStation = new SantaMariaStation(this, Ubications.SANTA_MARIA_UBI.getValue());
                break;
            case R.id.imageViewcondis:
                currentStation = new CondisStation(this, Ubications.CONDIS_UBI.getValue());
                break;
            case R.id.imageViewhomilies:
                currentStation = new HomiliesStation(this, Ubications.HOMILIES_UBI.getValue());
                break;
            case R.id.imageViewnaturlandia:
                currentStation = new NaturlandiaStation(this, Ubications.NATURLANDIA_UBI.getValue());
                break;
        }
        currentStation.openStation();
    }

}