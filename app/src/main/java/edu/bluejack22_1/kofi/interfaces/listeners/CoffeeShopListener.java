package edu.bluejack22_1.kofi.interfaces.listeners;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import edu.bluejack22_1.kofi.controller.model.CoffeeShop;

public interface CoffeeShopListener {
    void onCompleteShop(DocumentSnapshot docSnap);
    void onCompleteShopCollection(QuerySnapshot querySnap);
    void onSuccessUpdateShop(CoffeeShop coffeeShop);
    void onSuccessShop();
    void onDeleteShop();
}
