package edu.bluejack22_1.kofi.controller;

import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import edu.bluejack22_1.kofi.interfaces.listeners.ReviewListener;
import edu.bluejack22_1.kofi.model.Review;

public class LikeController {
    private FirebaseFirestore db;
    private NotificationController notificationController;
    public LikeController(){ db = FirebaseFirestore.getInstance(); notificationController = new NotificationController();}

    public void likeReview(String shopId, String userId, Review review, ReviewListener listener){
        String reviewId = review.getReviewId();
        String targetId = review.getUser().getUserId();
        db.collection("coffeeshop").document(shopId).collection("reviews")
                .document(reviewId)
                .update("likers", FieldValue.arrayUnion(userId))
                .addOnCompleteListener(task -> {
                    db.collection("users").document(userId).update("likedreviews", FieldValue.arrayUnion(reviewId));
                    notificationController.addNotification(targetId, "has liked your review");
                    listener.onSuccessReview();
                });
    }
    public void dislikeReview(String shopId, String userId, Review review, ReviewListener listener){
        String reviewId = review.getReviewId();
        db.collection("coffeeshop").document(shopId).collection("reviews").document(reviewId)
                .update("likers", FieldValue.arrayRemove(userId))
                .addOnCompleteListener(task -> {
                    db.collection("users").document(userId).update("likedreviews", FieldValue.arrayRemove(reviewId));
                    listener.onSuccessReview();
                });
    }
}
