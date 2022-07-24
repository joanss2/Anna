package com.example.anna.Alerts;

import android.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.anna.Models.Alert;
import com.example.anna.R;

public class AlertManager {

    private AppCompatActivity activity;

    public AlertManager(AppCompatActivity activity){
        this.activity = activity;
    }

    public void showAlert(Alert alert) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(activity.getResources().getString(R.string.error));
        builder.setMessage(alert.getAlertMessage());
        builder.setPositiveButton(activity.getResources().getString(R.string.ok), null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void showAlert(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(activity.getResources().getString(R.string.error));
        builder.setMessage(message);
        builder.setPositiveButton(activity.getResources().getString(R.string.ok), null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
