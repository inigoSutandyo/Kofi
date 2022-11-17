package edu.bluejack22_1.kofi.controller;

import android.net.Uri;


import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import edu.bluejack22_1.kofi.model.User;

public class UserController {
    FirebaseFirestore Firestore;
    FirebaseStorage storage;
    StorageReference storageReference;
    public UserController(){
        Firestore = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
    }

    public void addUser(String fullName, String email, String password, String address, Uri imageUri, String uid){

        User user = new User(fullName, email, password, address, "User");
        Firestore.collection("users").add(user).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                UploadImage(imageUri, uid);
            }
        });
    }
    private void UploadImage(Uri ImageUri, String uid)
    {
        storageReference = FirebaseStorage.getInstance().getReference("images/"+uid);
        storageReference.putFile(ImageUri);
    }
}
