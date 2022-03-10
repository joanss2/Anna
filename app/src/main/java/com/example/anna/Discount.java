package com.example.anna;

public class Discount {

    private String name;
    private int imageId;

    public Discount(String name, int imageId){
        this.name = name;
        this.imageId = imageId;
    }

    public int getImageId() {
        return imageId;
    }

    public String getName() {
        return name;
    }
}
