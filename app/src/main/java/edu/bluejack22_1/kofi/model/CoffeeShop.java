package edu.bluejack22_1.kofi.model;

import com.google.firebase.Timestamp;

import java.util.ArrayList;

public class CoffeeShop {
    private String shopName, shopAddress, shopDescription, shopId, imageUrl;
    private Double totalRating;
    private int reviewCount;
    private ArrayList<String> userFavorites;
    private Timestamp createdAt;

    public CoffeeShop(String shopName, String shopAddress, String shopDescription, String shopId, String imageUrl) {
        this.shopName = shopName;
        this.shopAddress = shopAddress;
        this.shopDescription = shopDescription;
        this.shopId = shopId;
        this.imageUrl = imageUrl;
        this.userFavorites = new ArrayList<>();
        this.reviewCount = 0;
        this.totalRating = 0.0;
        this.createdAt = Timestamp.now();
    }

    public CoffeeShop(){
        this.userFavorites = new ArrayList<>();
        this.reviewCount = 0;
        this.totalRating = 0.0;
        this.createdAt = Timestamp.now();
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Double getTotalRating() {
        if (totalRating == null) {
            totalRating = 0.0;
        }
        return totalRating;
    }

    public void setTotalRating(Double totalRating) {
        this.totalRating = totalRating;
    }

    public int getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(int reviewCount) {
        this.reviewCount = reviewCount;
    }

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

    public Double getAverageRating() {
        Double d = new Double(this.reviewCount);
        if (d == null || totalRating == null || totalRating.isNaN() || d.isNaN()) {
            return 0.0;
        } else {
            Double avg = totalRating / d;
            if (avg.isNaN()) {
                return 0.0;
            } else {
                return avg;
            }
        }
    }
}
