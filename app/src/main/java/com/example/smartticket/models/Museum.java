package com.example.smartticket.models;

import java.io.Serializable;

/**
 * Model representing a museum venue with ticket pricing.
 */
public class Museum implements Serializable {
    private int id;
    private String name;
    private String city;
    private String description;
    private double adultPrice;
    private double studentPrice;
    private double childPrice;

    public Museum(int id, String name, String city, String description, double adultPrice, double studentPrice, double childPrice) {
        this.id = id;
        this.name = name;
        this.city = city;
        this.description = description;
        this.adultPrice = adultPrice;
        this.studentPrice = studentPrice;
        this.childPrice = childPrice;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCity() {
        return city;
    }

    public String getDescription() {
        return description;
    }

    public double getAdultPrice() {
        return adultPrice;
    }

    public double getStudentPrice() {
        return studentPrice;
    }

    public double getChildPrice() {
        return childPrice;
    }
}
