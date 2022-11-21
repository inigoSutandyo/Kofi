package edu.bluejack22_1.kofi.controller;

import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.w3c.dom.Document;

import edu.bluejack22_1.kofi.R;
import edu.bluejack22_1.kofi.fragments.ProfileFragment;
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

    public void addGoogleUser(String uid, String fullname, String email){
        User user = new User(fullname, email, "", "", "User");
        db.collection("users").document(uid).set(user);
    }

    private void UploadImage(Uri ImageUri, String uid)
    {
        storageReference = FirebaseStorage.getInstance().getReference("images/"+uid);
        storageReference.putFile(ImageUri);
    }

    public void UpdateUser(String uid, String fullname, String address, Uri imageUri, Fragment fragment){
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
                                storageReference = FirebaseStorage.getInstance().getReference("images/"+uid);
                                storageReference.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                        replaceFragment(new ProfileFragment(tempUser), fragment);
                                    }
                                });
                            } else{
                                replaceFragment(new ProfileFragment(tempUser), fragment);
                            }
                        }
                    });
                } else {
                    Log.d("User", "Error getting User");
                }
            }
        });
    }
    private void replaceFragment(Fragment fragment, Fragment source){
        FragmentManager fragmentManager = source.getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
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
                            String fullName = (String) document.getData().get("fullname");
                            String email = (String) document.getData().get("email");
                            String address = (String) document.getData().get("address");
                            String password = (String) document.getData().get("password");
                            String role = (String) document.getData().get("role");
                            String userId= document.getId();

                            user[0] = new User(fullName, email, password, address, role, userId);
                        } else {
                            Log.d("Coffee", "No such document");
                        }
                    } else {
                        Log.d("Coffee", "get failed with ", task.getException());
                    }
                }
            });
        return user[0];
    }
}
