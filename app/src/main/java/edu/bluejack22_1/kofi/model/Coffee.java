package edu.bluejack22_1.kofi.model;

import android.content.Intent;

import com.google.firebase.Timestamp;

public class Coffee {
    private String coffeeId, name;
    private Double price;
    private Timestamp dateCreated;
    private String imageUrl;

    public Coffee() {}

    public Coffee(String coffeeId, String name, Double price, Timestamp dateCreated, String imageUrl) {
        this.coffeeId = coffeeId;
        this.name = name;
        this.price = price;
        this.dateCreated = dateCreated;
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getCoffeeId() {
        return coffeeId;
    }

    public void setCoffeeId(String coffeeId) {
        this.coffeeId = coffeeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Timestamp getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Timestamp dateCreated) {
        this.dateCreated = dateCreated;
    }
}
