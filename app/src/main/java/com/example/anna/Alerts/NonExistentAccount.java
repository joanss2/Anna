package com.example.anna.Alerts;

import android.content.Context;

import com.example.anna.Models.Alert;

public class NonExistentAccount extends Alert {
    public NonExistentAccount(Context context) {
        super(context);
    }

    @Override
    public String getAlertMessage() {
        return "The email provided is not registered yet";
    }
}
