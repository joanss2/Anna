package com.example.anna.Models;

import android.net.Uri;

import java.util.Date;

public class HotNews {

    private String title;
    private String description;
    private Date endDate;
    private Uri uri;

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
    public void setEndDate(Date date){
        this.endDate = date;
    }
}
