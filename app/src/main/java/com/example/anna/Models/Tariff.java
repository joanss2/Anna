package com.example.anna.Models;

import android.os.Parcel;
import android.os.Parcelable;

import java.math.BigDecimal;

public class Tariff implements Parcelable {

    private float price;
    private String condition, description;

    public Tariff(Float price, String condition, String description) {
        this.price = price;
        this.condition = condition;
        this.description = description;
    }
    public Tariff(){}

    protected Tariff(Parcel in) {
        price = in.readFloat();
        condition = in.readString();
        description = in.readString();
    }

    public static final Creator<Tariff> CREATOR = new Creator<Tariff>() {
        @Override
        public Tariff createFromParcel(Parcel in) {
            return new Tariff(in);
        }

        @Override
        public Tariff[] newArray(int size) {
            return new Tariff[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeFloat(price);
        parcel.writeString(condition);
        parcel.writeString(description);
    }
}
