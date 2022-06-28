package com.example.anna.Models;

import java.util.List;

public class RouteModel {

    private List<Station> stations;
    private String name;
    private String category;
    private int numberOfStages;

    public RouteModel(List<Station> stations, String name, int numberOfStages, String category) {
        this.stations = stations;
        this.name = name;
        this.numberOfStages = numberOfStages;
        setCategory(category);
    }

    public RouteModel() {
    }

    public List<Station> getStations() {
        return stations;
    }

    public void setStations(List<Station> stations) {
        this.stations = stations;
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
