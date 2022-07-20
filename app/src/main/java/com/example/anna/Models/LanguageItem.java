package com.example.anna.Models;

public class LanguageItem {

    private String name;
    private int imageResource;

    public LanguageItem(String name, int imageResource) {
        this.name = name;
        this.imageResource = imageResource;
    }

    public String getName() {
        return name;
    }

    public int getImageResource() {
        return imageResource;
    }
}
