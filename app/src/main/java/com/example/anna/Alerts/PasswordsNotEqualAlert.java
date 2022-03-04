package com.example.anna.Alerts;

import com.example.anna.R;

public class PasswordsNotEqualAlert extends Alert{

    public PasswordsNotEqualAlert(){
        super.alertMessage = String.valueOf(R.string.passwordsNotEqual);
    }

    @Override
    public String getAlertMessage() {
        return super.alertMessage;
    }
}
