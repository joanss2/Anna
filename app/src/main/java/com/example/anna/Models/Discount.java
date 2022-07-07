package com.example.anna.Models;

import android.net.Uri;

import androidx.annotation.NonNull;

public class Discount {

    private String description;
    private String name, imageRef, key;
    private Uri uriImg;
    private int discountPercentage;

    public Discount() {
    }


    public int getDiscountPercentage() {
        return this.discountPercentage;
    }

    public String getImageRef() {
        return this.imageRef;
    }

    public String getDescription() {
        return description;
    }

    public String getKey() {
        return this.key;
    }

    public String getName() {
        return name;
    }

    public Uri getUriImg() {
        return this.uriImg;
    }

    public void setUriImg(Uri uri) {
        this.uriImg = uri;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImageRef(String imageRef) {
        this.imageRef = imageRef;
    }

    public void setDiscountPercentage(int discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    @NonNull
    @Override
    public String toString() {
        return "\nThis is the discount " + this.name +
                " with discount percentage " + this.discountPercentage +
                " with image reference " + this.imageRef +
                " with description: " + this.description + "\n";
    }
}
