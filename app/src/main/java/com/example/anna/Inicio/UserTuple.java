package com.example.anna.Inicio;

import android.net.Uri;

import com.example.anna.Discount;

import java.util.ArrayList;
import java.util.List;

public class UserTuple {

    private String username, email, telefon;
    private List<String> discountList;

    public UserTuple(String username, String email, String telefon, List<String> discountList) {
        this.username = username;
        this.email = email;
        this.telefon = telefon;
        this.discountList = new ArrayList<>();
    }

    public String getUsername() {
        return this.username;
    }

    public String getEmail() {
        return this.email;
    }

    public String getTelefon() {
        return this.telefon;
    }

    public List<String> getDiscountList() {
        return this.discountList;
    }

    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }

    public void addDiscountUsed(String discountName) {
        this.discountList.add(discountName);
    }

}
