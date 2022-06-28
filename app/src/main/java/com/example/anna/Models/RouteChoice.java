package com.example.anna.Models;


public class RouteChoice {

    private String title;
    private int imageDrawable;

    public RouteChoice(String title, int imageDrawable) {
        this.title = title;
        this.imageDrawable = imageDrawable;
    }

    public RouteChoice() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getImageDrawable() {
        return imageDrawable;
    }

    public void setImageDrawable(int imageDrawable) {
        this.imageDrawable = imageDrawable;
    }
}
