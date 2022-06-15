package com.example.anna.Models;

import java.util.ArrayList;
import java.util.List;

public class UserTuple {

    private String username, email, userKey;
    private List<String> discountList;

    public UserTuple(String username, String email, String userKey, List<String> discountList) {
        this.username = username;
        this.email = email;
        this.userKey = userKey;
        this.discountList = new ArrayList<>();
    }

    public String getUsername() {
        return this.username;
    }

    public String getEmail() {
        return this.email;
    }


    public String getUserKey() {
        return this.userKey;
    }

    public List<String> getDiscountList() {
        return this.discountList;
    }

    public void setUserKey(String userKey) {
        this.userKey = userKey;
    }

    public void addDiscountUsed(String discountName) {
        this.discountList.add(discountName);
    }

}
