package edu.bluejack22_1.kofi.model;

import java.util.ArrayList;

public class CoffeeShop {
    private String shopName, shopAddress, shopDescription, shopId, imageUrl;
    private ArrayList<String> userFavorites;

    public CoffeeShop(String shopName, String shopAddress, String shopDescription, String shopId, String imageUrl) {
        this.shopName = shopName;
        this.shopAddress = shopAddress;
        this.shopDescription = shopDescription;
        this.shopId = shopId;
        this.imageUrl = imageUrl;
        this.userFavorites = new ArrayList<>();
    }

    public CoffeeShop(){}

    public ArrayList<String> getUserFavorites() {
        return userFavorites;
    }

    public void setUserFavorites(ArrayList<String> userFavorites) {
        this.userFavorites = userFavorites;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getShopAddress() {
        return shopAddress;
    }

    public void setShopAddress(String shopAddress) {
        this.shopAddress = shopAddress;
    }

    public String getShopDescription() {
        return shopDescription;
    }

    public void setShopDescription(String shopDescription) {
        this.shopDescription = shopDescription;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
