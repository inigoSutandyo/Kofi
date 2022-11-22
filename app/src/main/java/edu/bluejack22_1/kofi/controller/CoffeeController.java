package edu.bluejack22_1.kofi.controller;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import edu.bluejack22_1.kofi.adapter.CoffeeAdapter;
import edu.bluejack22_1.kofi.model.Coffee;

public class CoffeeController {

    private FirebaseFirestore db;

    public CoffeeController() {
        db = FirebaseFirestore.getInstance();
    }

    public void populateCoffees(String shopId, ArrayList<Coffee> coffees, CoffeeAdapter adapter) {
        db.collection("coffeeshop/"+shopId+"/coffees")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot documentSnapshot: task.getResult()) {
                                String name = (String) documentSnapshot.getData().get("name");
                                long price = (Long) documentSnapshot.getData().get("price");
                                coffees.add(new Coffee(name,price,documentSnapshot.getId()));
                            }
                            adapter.notifyDataSetChanged();
                        } else {
                            Log.d("Coffee", "Document read error");
                        }
                    }
                });
    }
}
