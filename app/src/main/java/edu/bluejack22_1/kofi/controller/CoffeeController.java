package edu.bluejack22_1.kofi.controller;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import edu.bluejack22_1.kofi.interfaces.listeners.CoffeeListener;
import edu.bluejack22_1.kofi.interfaces.listeners.CoffeeShopListener;

public class CoffeeController {

    private FirebaseFirestore db;

    public CoffeeController() {
        db = FirebaseFirestore.getInstance();
    }

    public void getCoffees(String shopId, CoffeeListener listener) {
        db.collection("coffeeshop/"+shopId+"/coffees")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            listener.onCompleteCoffeeCollection(task.getResult());
                        } else {
                            Log.d("Coffee", "Document read error");
                        }
                    }
                });
    }
}
