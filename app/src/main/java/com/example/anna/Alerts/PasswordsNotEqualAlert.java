package com.example.anna.Alerts;

import android.content.Context;

import com.example.anna.Models.Alert;
import com.example.anna.R;

public class PasswordsNotEqualAlert extends Alert {

    public PasswordsNotEqualAlert(Context context){
        super(context);
    }

    @Override
    public String getAlertMessage() {
        return this.context.getString(R.string.passwordsNotEqual);
    }
}
