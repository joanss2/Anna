package com.example.anna.Models;

import com.google.firebase.firestore.GeoPoint;

import java.util.List;

public class Station{

    private String key, name, description, routeParentKey;
    private GeoPoint geoLocation;
    private List<String> imageRefs;

    public Station(String key,String name, GeoPoint geoLocation, String description, String routeParentKey){//}, List<String> imageRefs){
        this.key = key;
        this.name = name;
        this.geoLocation = geoLocation;
        this.description = description;
        this.routeParentKey = routeParentKey;
        this.imageRefs = imageRefs;
    }

    public Station() {
    }

    public String getKey(){
        return this.key;
    }

    public void setKey(String key){
        this.key = key;
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

    public String getRouteParentKey() {
        return routeParentKey;
    }

    public void setRouteParentKey(String routeParentKey) {
        this.routeParentKey = routeParentKey;
    }

    public List<String> getImageRefs() {
        return imageRefs;
    }

    public void setImageRefs(List<String> imageRefs) {
        this.imageRefs = imageRefs;
    }



}
