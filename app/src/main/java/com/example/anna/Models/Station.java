package com.example.anna.Models;

import com.google.firebase.firestore.GeoPoint;

import java.util.List;

public class Station{


    private GeoPoint geoLocation;
    private String name;
    private String description;
    private List<String> imageRefs;

    public Station(String name, GeoPoint geoLocation, String description){//}, List<String> imageRefs){
        this.name = name;
        this.geoLocation = geoLocation;
        this.description = description;
        this.imageRefs = imageRefs;
    }

    public Station() {
    }

    public GeoPoint getGeoLocation() {
        return geoLocation;
    }

    public void setGeoLocation(GeoPoint geoLocation) {
        this.geoLocation = geoLocation;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public List<String> getImageRefs() {
        return imageRefs;
    }

    public void setImageRefs(List<String> imageRefs) {
        this.imageRefs = imageRefs;
    }



}
