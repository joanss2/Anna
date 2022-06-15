package com.example.anna.Models;

public class DiscountUsedEntry {
    private String key;

    public DiscountUsedEntry(){}
    public DiscountUsedEntry(String key){
        this.key = key;
    }

    public String getKey(){
        return this.key;
    }
    public void setKey(String key){
        this.key = key;
    }
}
