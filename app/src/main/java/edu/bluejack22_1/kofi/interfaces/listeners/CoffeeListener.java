package edu.bluejack22_1.kofi.interfaces.listeners;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import edu.bluejack22_1.kofi.controller.model.Coffee;

public interface CoffeeListener {
    void onCompleteCoffee(DocumentSnapshot docSnap);
    void onCompleteCoffeeCollection(QuerySnapshot querySnap);
    void onSuccessCoffee();
    void onSuccessUpdateCoffee(Coffee coffee);
}
