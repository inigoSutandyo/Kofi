package edu.bluejack22_1.kofi.controller;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import edu.bluejack22_1.kofi.MainActivity;
import edu.bluejack22_1.kofi.interfaces.listeners.UserListener;
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

        User user = new User(fullName, email, password, address, "User", uid);
        db.collection("users").document(uid).set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                if(imageUri != null){
                    uploadImage(imageUri, uid, user);
                }
            }
        });
    }

    public void addGoogleUser(String uid, String fullname, String email, String imageUrl, Activity activity){
        User user = new User(fullname, email, "", "", "User", uid);
        User.setCurrentUser(user);
        user.setImageUrl(imageUrl);
        db.collection("users").document(uid).set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                MoveMainPage(activity);
            }
        });
    }
    private void MoveMainPage(Activity activity){
        activity.finish();
        Intent mainIntent = new Intent(activity, MainActivity.class);
        activity.startActivity(mainIntent);
    }
    private void uploadImage(Uri ImageUri, String uid, User user) {
        storageReference = FirebaseStorage.getInstance().getReference("images/"+uid);
        storageReference.putFile(ImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        db.collection("users").document(uid).update("imageUrl", uri.toString());
                    }
                });
            }
        });

    }

    public void updateUser(String uid, String fullName, String address, Uri imageUri, UserListener listener){
        DocumentReference ref = db.collection("users").document(uid);
        ref.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    User tempUser = task.getResult().toObject(User.class);
                    tempUser.setFullName(fullName);
                    tempUser.setAddress(address);
                    ref.set(tempUser).addOnSuccessListener(v -> {
                            if(imageUri != null) {
                                storageReference = FirebaseStorage.getInstance().getReference("images/"+uid);
                                storageReference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                db.collection("users").document(uid).update("imageUrl", uri.toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        listener.onSuccessUpdateUser(tempUser);
                                                    }
                                                });
                                            }
                                        });
                                    }
                                });

                            } else{
                                listener.onSuccessUpdateUser(tempUser);
                            }
                        }
                    );
                } else {
                    Log.d("User", "Error getting User");
                }
            }
        );
    }

    public User getUserByRef(DocumentReference ref) {
        final User[] user = {null};
        ref.get()
            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            String fullName = (String) document.getData().get("fullName");
                            String email = (String) document.getData().get("email");
                            String address = (String) document.getData().get("address");
                            String password = (String) document.getData().get("password");
                            String role = (String) document.getData().get("role");
                            String userId= document.getId();

                            user[0] = new User(fullName, email, password, address, role, userId);
                        } else {
                            Log.d("User", "No such document");
                        }
                    } else {
                        Log.d("User", "get failed with ", task.getException());
                    }
                }
            });
        return user[0];
    }
}
