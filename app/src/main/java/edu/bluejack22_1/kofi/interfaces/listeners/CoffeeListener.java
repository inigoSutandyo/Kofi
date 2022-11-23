package edu.bluejack22_1.kofi.interfaces.listeners;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public interface CoffeeListener {
    void onCompleteCoffee(DocumentSnapshot docSnap);
    void onCompleteCoffeeCollection(QuerySnapshot querySnap);
    void onSuccessCoffee();
}
