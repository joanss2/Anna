package com.example.anna.Alerts;

import android.content.Context;

import com.example.anna.R;

public class UserNotRegisteredAlert extends Alert{

    public UserNotRegisteredAlert(Context context) {
        super(context);
    }
    @Override
    public String getAlertMessage() {
        return this.context.getString(R.string.userNotRegisteredFromSignIn);
    }
}
