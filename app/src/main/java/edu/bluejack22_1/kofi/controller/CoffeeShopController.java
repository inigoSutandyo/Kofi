package edu.bluejack22_1.kofi.controller;


import com.google.firebase.firestore.FirebaseFirestore;

import edu.bluejack22_1.kofi.model.CoffeeShop;

public class CoffeeShopController {
    FirebaseFirestore Firestore;
    public CoffeeShopController(){
        Firestore = FirebaseFirestore.getInstance();
    }

    public void addCoffeeShop(String shopName, String shopAddress, String shopDescription){
        CoffeeShop CoffeeShop = new CoffeeShop(shopName, shopAddress, shopDescription);
        Firestore.collection("coffeeshop").add(CoffeeShop);
    }
}
