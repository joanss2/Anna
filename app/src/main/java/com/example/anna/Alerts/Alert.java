package com.example.anna.Alerts;

import android.content.Context;

public abstract class Alert {

    protected Context context;
    public Alert(Context context){
        this.context = context;
    }
    public abstract String getAlertMessage();
}
