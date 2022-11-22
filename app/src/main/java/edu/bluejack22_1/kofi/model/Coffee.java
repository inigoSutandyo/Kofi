package edu.bluejack22_1.kofi.model;

import android.content.Intent;

public class Coffee {
    private String name;
    private String id;
    private Long price;

    public Coffee() {}

    public Coffee(String name, Long price) {
        this.name = name;
        this.price = price;
    }

    public Coffee(String name, Long price, String id) {
        this.name = name;
        this.price = price;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }
}
