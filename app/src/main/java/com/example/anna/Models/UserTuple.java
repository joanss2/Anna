package com.example.anna.Models;

import java.util.ArrayList;
import java.util.List;

public class UserTuple {

    private String username, email, userKey;

    public UserTuple(String username, String email, String userKey) {
        this.username = username;
        this.email = email;
        this.userKey = userKey;
    }
    public UserTuple(){}

    public String getUsername() {
        return this.username;
    }

    public String getEmail() {
        return this.email;
    }


    public String getUserKey() {
        return this.userKey;
    }


    public void setUserKey(String userKey) {
        this.userKey = userKey;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
