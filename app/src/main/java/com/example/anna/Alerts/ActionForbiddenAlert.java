package com.example.anna.Alerts;

import android.content.Context;

import com.example.anna.Models.Alert;
import com.example.anna.R;

public class ActionForbiddenAlert extends Alert {
    public ActionForbiddenAlert(Context context){
        super(context);
    }

    @Override
    public String getAlertMessage() {
        return "This action is forbidden for this user";
    }
}
