package com.example.anna.Models;

import java.util.List;

public class RouteModel {

    private List<Station> stations;
    private String key, name, category;
    private int numberOfStages;


    public RouteModel() {
    }

    public List<Station> getStations() {
        return stations;
    }

    public void setStations(List<Station> stations) {
        this.stations = stations;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumberOfStages() {
        return numberOfStages;
    }

    public void setNumberOfStages(int numberOfStages) {
        this.numberOfStages = numberOfStages;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        if (!categoryCorrect(category))
            throw new IllegalArgumentException("Incorrect category. Must be gold, silver or bronze");
        else
            this.category = category;
    }

    public boolean categoryCorrect(String category) {
        return (category.equals("gold") || category.equals("silver") || category.equals("bronze"));
    }
}
