package com.example.anna.Alerts;

import android.content.Context;
import android.content.res.Resources;

import com.example.anna.R;

public class BadPasswordAlert extends Alert {
    public BadPasswordAlert(Context context){
        super(context);
    }

    @Override
    public String getAlertMessage() {
        return this.context.getString(R.string.badPasswordAlert);
    }
}
