package com.example.anna.Models;

import android.net.Uri;

import com.google.firebase.Timestamp;

import java.util.Date;

public class HotNews {

    private String title;
    private String description;
    private Date endDate;
    private Uri uri;
    private String author;

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

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
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
