package edu.bluejack22_1.kofi.model;

import android.net.Uri;

import com.google.firebase.firestore.DocumentReference;

import java.sql.Array;
import java.util.ArrayList;

public class User {
    private String fullName, email, password, address, role, userId, imageUrl;
    private ArrayList<DocumentReference> reviews;
    private static User currentUser;

    public static void setCurrentUser(User user){currentUser = user;}
    public static User getCurrentUser(){ return currentUser;}

    public User(){}

    public User(String fullName, String email, String password, String address, String role, String userId) {
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.address = address;
        this.role = role;
        this.userId = userId;
        this.reviews = new ArrayList<>();
        this.imageUrl = "https://firebasestorage.googleapis.com/v0/b/tpaandroid-8e254.appspot.com/o/images%2Fdefaultprofile.png?alt=media&token=059d3e03-7c52-414f-a673-f50deb428122";
    }

    public User(String fullName, String email, String password, String address, String role, String userId, String imageUrl) {
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.address = address;
        this.role = role;
        this.userId = userId;
        this.reviews = new ArrayList<>();
        this.imageUrl = imageUrl;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public ArrayList<DocumentReference> getReviews() {
        return reviews;
    }

    public void setReviews(ArrayList<DocumentReference> reviews) {
        this.reviews = reviews;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
