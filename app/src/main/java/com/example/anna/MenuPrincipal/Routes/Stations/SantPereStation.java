package com.example.anna.MenuPrincipal.Routes.Stations;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

import com.example.anna.R;
import com.example.anna.Models.Station;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class SantPereStation extends Station {


    public SantPereStation(Context context, LatLng latLng) {
        super(context, latLng);
    }

    @Override
    public void openStation() {
        dialog.setContentView(R.layout.santperedepontscard);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();


        MapView mapView = dialog.findViewById(R.id.mapViewsantpere);
        MapsInitializer.initialize(this.context);

        mapView.onCreate(dialog.onSaveInstanceState());
        mapView.onResume();
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(final GoogleMap googleMap) {
                LatLng ubication = latLng; ////your lat lng
                googleMap.addMarker(new MarkerOptions().position(ubication).title("Sant Pere de Ponts"));
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(ubication));
                googleMap.getUiSettings().setZoomControlsEnabled(true);
                googleMap.animateCamera(CameraUpdateFactory.zoomTo(10), 1000, null);

            }
        });
    }
}
