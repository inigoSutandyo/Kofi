package edu.bluejack22_1.kofi.controller;


import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import edu.bluejack22_1.kofi.MainActivity;
import edu.bluejack22_1.kofi.adapter.CoffeeShopAdapter;
import edu.bluejack22_1.kofi.interfaces.listeners.CoffeeShopListener;
import edu.bluejack22_1.kofi.model.CoffeeShop;

public class CoffeeShopController {
    FirebaseFirestore db;
    public CoffeeShopController(){
        db = FirebaseFirestore.getInstance();
    }

    public void addCoffeeShop(String shopName, String shopAddress, String shopDescription, CoffeeShopListener listener){
        CoffeeShop coffeeShop = new CoffeeShop(shopName, shopAddress, shopDescription, "");
        db.collection("coffeeshop").add(coffeeShop).addOnCompleteListener(task -> {
            db.collection("coffeeshop")
                    .document(task.getResult().getId())
                    .update("shopId", task.getResult()
                    .getId())
                    .addOnSuccessListener(unused ->{
                        listener.onSuccessShop();
                    });
        });
    }

    public void getCoffeeShopList(CoffeeShopListener listener) {
        db.collection("coffeeshop")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            listener.onCompleteShopCollection(task.getResult());
                        } else {
                            Log.d("Coffee", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

}
