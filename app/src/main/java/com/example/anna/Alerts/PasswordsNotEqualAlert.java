package com.example.anna.Alerts;

import android.content.Context;
import android.content.res.Resources;

import com.example.anna.R;

public class PasswordsNotEqualAlert extends Alert{

    public PasswordsNotEqualAlert(Context context){
        super(context);
    }

    @Override
    public String getAlertMessage() {
        return this.context.getString(R.string.passwordsNotEqual);
    }
}
