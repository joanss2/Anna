package com.example.anna.Models;

import android.net.Uri;

import com.google.firebase.Timestamp;


public class Comment {

    private String body, authorKey, discountKey;
    private Uri uriUserImage;
    private Timestamp timestamp;

    public Comment(){}

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getAuthorKey() {
        return authorKey;
    }

    public void setAuthorKey(String authorKey) {
        this.authorKey = authorKey;
    }

    public String getDiscountKey() {
        return discountKey;
    }

    public void setDiscountKey(String discountKey) {
        this.discountKey = discountKey;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public Uri getUriUserImage() {
        return uriUserImage;
    }

    public void setUriUserImage(Uri uriUserImage) {
        this.uriUserImage = uriUserImage;
    }
}
