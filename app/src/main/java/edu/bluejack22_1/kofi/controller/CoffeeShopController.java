package edu.bluejack22_1.kofi.controller;


import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import edu.bluejack22_1.kofi.adapter.CoffeeShopAdapter;
import edu.bluejack22_1.kofi.model.CoffeeShop;

public class CoffeeShopController {
    FirebaseFirestore db;
    public CoffeeShopController(){
        db = FirebaseFirestore.getInstance();
    }

    public void addCoffeeShop(String shopName, String shopAddress, String shopDescription){
        CoffeeShop CoffeeShop = new CoffeeShop(shopName, shopAddress, shopDescription);
        db.collection("coffeeshop").add(CoffeeShop);
    }

    public ArrayList<CoffeeShop> getCoffeeShopList() {
        ArrayList<CoffeeShop> list = new ArrayList<>();
        db.collection("coffeeshop")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String name = (String) document.getData().get("shopName");
                                String address = (String) document.getData().get("shopAddress");
                                String description = (String) document.getData().get("shopDescription");
                                Log.d("Coffee", document.getId() + " => " + name + " , " + address);

                                list.add(new CoffeeShop(name,address,description));
                            }
                        } else {
                            Log.d("Coffee", "Error getting documents: ", task.getException());
                        }
                    }
                });
        return list;
    }

    public void populateCoffeeShopList(ArrayList<CoffeeShop> list, CoffeeShopAdapter adapter) {
        db.collection("coffeeshop")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String name = (String) document.getData().get("shopName");
                                String address = (String) document.getData().get("shopAddress");
                                String description = (String) document.getData().get("shopDescription");
//                                Log.d("Coffee", document.getId() + " => " + name + " , " + address);

                                list.add(new CoffeeShop(name,address,description));
                            }
                            adapter.notifyDataSetChanged();
                        } else {
                            Log.d("Coffee", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }
}
