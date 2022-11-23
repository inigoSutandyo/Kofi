package edu.bluejack22_1.kofi.interfaces.listeners;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public interface CoffeeShopListener {
    void onCompleteShop(DocumentSnapshot docSnap);
    void onCompleteShopCollection(QuerySnapshot querySnap);
    void onSuccessShop();
}
