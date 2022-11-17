package edu.bluejack22_1.kofi.model;

public class CoffeeShop {
    private String ShopName, ShopAddress, ShopDescription;
    public CoffeeShop(String shopName, String shopAddress, String shopDescription) {
        ShopName = shopName;
        ShopAddress = shopAddress;
        ShopDescription = shopDescription;
    }
    public CoffeeShop(){}

    public String getShopName() {
        return ShopName;
    }

    public void setShopName(String shopName) {
        ShopName = shopName;
    }

    public String getShopAddress() {
        return ShopAddress;
    }

    public void setShopAddress(String shopAddress) {
        ShopAddress = shopAddress;
    }

    public String getShopDescription() {
        return ShopDescription;
    }

    public void setShopDescription(String shopDescription) {
        ShopDescription = shopDescription;
    }
}
