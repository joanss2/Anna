package com.example.anna.Models;

import java.math.BigDecimal;

public class Tariff {

    private Float price;
    private String condition, description;

    public Tariff(Float price, String condition, String description) {
        this.price = price;
        this.condition = condition;
        this.description = description;
    }
    public Tariff(){}

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
