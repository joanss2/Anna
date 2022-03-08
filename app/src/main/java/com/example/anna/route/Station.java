package com.example.anna.route;

import android.app.Dialog;
import android.content.Context;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

public abstract class Station {

    protected Context context;
    protected Dialog dialog;
    protected LatLng latLng;

    public Station (Context context, LatLng latLng){
        this.context = context;
        this.latLng = latLng;
        this.dialog = new Dialog(this.context);
    }

    public abstract void openStation();
}
