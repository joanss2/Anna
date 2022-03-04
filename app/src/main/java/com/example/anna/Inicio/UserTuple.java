package com.example.anna.Inicio;

import android.net.Uri;

public class UserTuple {

    private String username, email, telefon;



    public UserTuple(String username, String email, String telefon) {
        this.username = username;
        this.email = email;
        this.telefon = telefon;

    }

    public String getUsername(){
        return this.username;
    }
    public String getEmail(){
        return this.email;
    }
    public String getTelefon(){
        return this.telefon;
    }

}
