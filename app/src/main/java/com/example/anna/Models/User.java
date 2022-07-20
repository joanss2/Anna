package com.example.anna.Models;

public class User {

    private String username, email, userKey, language;
    /*
    private Roles role;
    public Roles getRole() {
        return this.role;
    }
    public void setRole(Roles role) {
        this.role = role;
    }

     */

    public User(String username, String email, String userKey, String language) {
        this.username = username;
        this.email = email;
        this.userKey = userKey;
        this.language = language;
    }

    public User() {
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


    public void setUserKey(String userKey) {
        this.userKey = userKey;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}
