package edu.bluejack22_1.kofi.controller;

import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.w3c.dom.Document;

import edu.bluejack22_1.kofi.fragments.UpdateProfileFragment;
import edu.bluejack22_1.kofi.model.CoffeeShop;
import edu.bluejack22_1.kofi.model.User;

public class UserController {
    FirebaseFirestore db;
    FirebaseStorage storage;
    StorageReference storageReference;
    public UserController(){
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
    }

    public void addUser(String fullName, String email, String password, String address, Uri imageUri, String uid){

        User user = new User(fullName, email, password, address, "User");
        db.collection("users").document(uid).set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                UploadImage(imageUri, uid);
            }
        });
    }
    private void UploadImage(Uri ImageUri, String uid)
    {
        storageReference = FirebaseStorage.getInstance().getReference("images/"+uid);
        storageReference.putFile(ImageUri);
    }

    public void UpdateUser(String uid, String fullname, String address, Uri imageUri){
        DocumentReference ref = db.collection("users").document(uid);
        ref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    User tempUser = task.getResult().toObject(User.class);
                    tempUser.setFullName(fullname);
                    tempUser.setAddress(address);
                    ref.set(tempUser).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            if(imageUri != null) {
                                UploadImage(imageUri, uid);
                            }
                        }
                    });
                } else {
                    Log.d("User", "Error getting User");
                }
            }
        });
    }
}
