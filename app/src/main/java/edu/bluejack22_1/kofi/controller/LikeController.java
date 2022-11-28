package edu.bluejack22_1.kofi.controller;

import android.util.Log;

import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import edu.bluejack22_1.kofi.interfaces.listeners.ReviewListener;

public class LikeController {
    private FirebaseFirestore db;
    public LikeController(){ db = FirebaseFirestore.getInstance();}

    public void LikeReview(String shopId, String userId, String reviewId, ReviewListener listener){
        db.collection("coffeeshop").document(shopId).collection("reviews").document(reviewId)
                .update("likers", FieldValue.arrayUnion(userId)).addOnCompleteListener(task -> {
                    db.collection("users").document(userId).update("likedreviews", FieldValue.arrayUnion(reviewId));
                }).addOnCompleteListener(task -> {
                    listener.onSuccessReview();
                });
    }
    public void DislikeReview(String shopId, String userId, String reviewId, ReviewListener listener){
        db.collection("coffeeshop").document(shopId).collection("reviews").document(reviewId)
                .update("likers", FieldValue.arrayRemove(userId)).addOnCompleteListener(task -> {
                    db.collection("users").document(userId).update("likedreviews", FieldValue.arrayRemove(reviewId));
                }).addOnCompleteListener(task -> {
                    listener.onSuccessReview();
                });
    }
}
