package com.example.anna.Models;

import android.content.Context;


public abstract class Alert {

    protected Context context;
    public Alert(Context context){
        this.context = context;
    }
    public abstract String getAlertMessage();

}
