package com.example.anna.Inicio;

public class Session {
    private String userName;
    private String emailAccount;

    public Session(String emailAccount){
        this.userName = null;
        this.emailAccount = emailAccount;
    }
    public void setUserName(String userName){
        this.userName = userName;
    }
    public String getUserName(){
        return this.userName;
    }
    public String getEmailAccount(){
        return this.emailAccount;
    }
    public void restore_values(){
        this.userName = null;
        this.emailAccount = null;
    }
}
