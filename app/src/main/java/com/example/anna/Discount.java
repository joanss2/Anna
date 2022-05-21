package com.example.anna;

import android.net.Uri;

public class Discount {

    private String description;
    private String name, imageRef;
    private int imageId;
    private Uri uriImg;
    private int discountPercentage;

    public Discount (String description, int discountPercentage, int imageId, String name){
        this.description = description;
        this.discountPercentage = discountPercentage;
        this.imageId = imageId;
        this.name = name;
    }

    public Discount(String description, int discountPercentage, String imageRef, String name){
        this.description = description;
        this.discountPercentage = discountPercentage;
        this.imageRef = imageRef;
        this.name = name;
    }

    public Discount (String description, int discountPercentage, Uri imageRef, String name){
        this.description = description;
        this.discountPercentage = discountPercentage;
        this.uriImg = imageRef;
        this.name = name;
    }
    public Discount(){}

    public int getDiscountPercentage() {
        return this.discountPercentage;
    }
    public String getImageRef(){
        return this.imageRef;
    }
    public String getDescription() {
        return description;
    }
    public int getImageId() {
        return imageId;
    }
    public String getName() {
        return name;
    }
    public Uri getUriImg(){
        return this.uriImg;
    }

    public void setUriImg(Uri uri){this.uriImg= uri;}
    public void setDescription(String description){
        this.description= description;
    }
    public void setName(String name){
        this.name = name;
    }
    public void setImageRef(String imageRef){
        this.imageRef = imageRef;
    }
    public void setDiscountPercentage(int discountPercentage){
        this.discountPercentage = discountPercentage;
    }

    @Override
    public String toString(){
        return "\nThis is the discount "+this.name+
                " with discount percentage "+ this.discountPercentage +
                " with image reference " + this.imageRef+
                " with description: "+this.description +"\n";
    }
}
