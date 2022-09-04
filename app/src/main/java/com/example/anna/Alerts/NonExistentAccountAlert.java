package com.example.anna.Alerts;

import android.content.Context;

import com.example.anna.Models.Alert;

public class NonExistentAccountAlert extends Alert {
    public NonExistentAccountAlert(Context context) {
        super(context);
    }

    @Override
    public String getAlertMessage() {
        return "The email provided is not registered yet";
    }
}
