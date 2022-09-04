package com.example.anna.Models;

import android.net.Uri;

import com.google.firebase.Timestamp;

import java.util.Date;

public class HotNews {

    private String title, description, key, author;
    private Date endDate;

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public HotNews(){}

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Date getEndDate(){
        return this.endDate;
    }
    //public void setEndDate(Timestamp date){
        //this.endDate = date.toDate();
    //}
    public void setEndDate(Date date){
        this.endDate = date;
    }

}
