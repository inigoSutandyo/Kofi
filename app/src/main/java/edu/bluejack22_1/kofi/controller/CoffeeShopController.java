package edu.bluejack22_1.kofi.controller;


import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


import edu.bluejack22_1.kofi.R;

import edu.bluejack22_1.kofi.fragments.HomeFragment;
import edu.bluejack22_1.kofi.interfaces.listeners.CoffeeShopListener;
import edu.bluejack22_1.kofi.model.CoffeeShop;

public class CoffeeShopController {
    FirebaseFirestore db;
    FirebaseStorage storage;
    StorageReference storageReference;

    public CoffeeShopController(){
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
    }

    private void uploadImage(Uri ImageUri, String uid) {
        storageReference = FirebaseStorage.getInstance().getReference("images/"+uid);
        storageReference.putFile(ImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        db.collection("coffeeshop").document(uid).update("imageUrl", uri.toString());
                    }
                });
            }
        });

    }

    public void addCoffeeShop(String shopName, String shopAddress, String shopDescription, CoffeeShopListener listener, Uri imageUrl){

        CoffeeShop coffeeShop = new CoffeeShop(shopName, shopAddress, shopDescription, "", "https://firebasestorage.googleapis.com/v0/b/tpaandroid-8e254.appspot.com/o/images%2Fitemplaceholder.png?alt=media&token=c5fe1cbf-b1a1-4939-bc12-57fe7a9440c1");
        db.collection("coffeeshop").add(coffeeShop).addOnCompleteListener(task -> {
            db.collection("coffeeshop")
                    .document(task.getResult().getId())
                    .update("shopId", task.getResult()
                    .getId())
                    .addOnSuccessListener(unused ->{
                        listener.onSuccessShop();
                        if(imageUrl != null){
                            uploadImage(imageUrl, task.getResult().getId());
                        }
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
    
    public void deleteCoffeeShop(String uid, FragmentActivity activity){
        db.collection("coffeeshop").document(uid).delete().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new HomeFragment()).commit();
            }
        });
    }
}
