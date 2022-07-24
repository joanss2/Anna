package com.example.anna.MenuPrincipal;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.anna.Models.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;

public class AppCompat extends AppCompatActivity {

    FirebaseDatabase database = FirebaseDatabase.getInstance("https://annaapp-322219-default-rtdb.europe-west1.firebasedatabase.app/");
    DatabaseReference ref = database.getReference("users");
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferences = getSharedPreferences("USERINFO", MODE_PRIVATE);


        System.out.println(sharedPreferences.getString("userKey", null));
        LanguageManager languageManager = new LanguageManager(this);
        //orderByChild("userKey").equalTo(sharedPreferences.getString("userKey",null))
        ref.child(sharedPreferences.getString("userKey", null)).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        User user = snapshot.getValue(User.class);
                        System.out.println(user.getLanguage());
                        if(!Locale.getDefault().getLanguage().equals(user.getLanguage())) {
                            System.out.println("ENTRO");
                            languageManager.updateResource(user.getLanguage());
                            recreate();
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                }
        );

    }
}
