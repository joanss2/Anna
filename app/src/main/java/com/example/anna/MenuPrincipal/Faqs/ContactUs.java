package com.example.anna.MenuPrincipal.Faqs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.anna.R;

public class ContactUs extends AppCompatActivity implements View.OnClickListener {


    private Intent emailIntent;
    private EditText editTextSubject, editTextMessage;
    private TextView addressTo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);

        emailIntent = new Intent(Intent.ACTION_SEND, Uri.parse("mailto:"));
        Button sendButton = findViewById(R.id.buttonsendmail);
        addressTo = findViewById(R.id.addressmail);
        editTextSubject = findViewById(R.id.subjectmail);
        editTextMessage = findViewById(R.id.messagemail);
        sendButton.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.buttonsendmail){
            sendMail();
        }
    }

    private void sendMail(){
        String correu = addressTo.getText().toString();
        String[] TO = {correu};
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO); // * configurar email aquí!
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, editTextSubject.getText().toString());
        emailIntent.putExtra(Intent.EXTRA_TEXT, editTextMessage.getText().toString());
        startActivity(emailIntent);
    }


}