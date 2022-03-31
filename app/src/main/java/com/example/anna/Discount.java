package com.example.anna;

public class Discount {

    private final String description;
    private String name;
    private int imageId;

    public Discount(String name, int imageId, String description) {
        this.name = name;
        this.imageId = imageId;
        this.description = description;
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
}
