package com.example.anna.Alerts;

import com.example.anna.R;

public class BadPasswordAlert extends Alert{

    public BadPasswordAlert (){
        super.alertMessage = String.valueOf(R.string.badPasswordAlert);
    }
    @Override
    public String getAlertMessage() {
        return super.alertMessage;
    }
}
