package com.example.anna.Alerts;
import com.example.anna.R;

public class EmailFieldEmptyAlert extends Alert{

    public EmailFieldEmptyAlert(){
        super.alertMessage = String.valueOf(R.string.emailFieldCannotBeEmpty);
    }

    @Override
    public String getAlertMessage() {
        return super.alertMessage;
    }
}
