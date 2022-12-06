package edu.bluejack22_1.kofi.controller;

import android.widget.ImageView;

import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import edu.bluejack22_1.kofi.R;
import edu.bluejack22_1.kofi.adapter.CoffeeShopAdapter;
import edu.bluejack22_1.kofi.interfaces.listeners.ReviewListener;
import edu.bluejack22_1.kofi.model.CoffeeShop;
import edu.bluejack22_1.kofi.model.Review;
import edu.bluejack22_1.kofi.model.User;

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

    public void addShopToFavorite(String shopId, ImageView favoriteView, int position, CoffeeShop cf, CoffeeShopAdapter adapter) {
        User user = User.getCurrentUser();
        db.collection("coffeeshop")
                .document(shopId)
                .update("userFavorites", FieldValue.arrayUnion(user.getUserId()))
                .addOnCompleteListener(task -> {
                    db.collection("users")
                            .document(user.getUserId())
                            .update("favoriteShops", FieldValue.arrayUnion(shopId))
                            .addOnCompleteListener(t -> {
                                favoriteView.setImageResource(R.drawable.ic_baseline_favorite_24);
                                cf.getUserFavorites().add(user.getUserId());
                                adapter.notifyItemChanged(position);
                            });
                });
    }

    public void removeShopFromFavorite(String shopId, ImageView favoriteView, int position, CoffeeShop cf, CoffeeShopAdapter adapter) {
        User user = User.getCurrentUser();
        db.collection("coffeeshop")
                .document(shopId)
                .update("userFavorites", FieldValue.arrayRemove(user.getUserId()))
                .addOnCompleteListener(task -> {
                    db.collection("users")
                            .document(user.getUserId())
                            .update("favoriteShops", FieldValue.arrayRemove(shopId))
                            .addOnCompleteListener(t -> {
                                favoriteView.setImageResource(R.drawable.ic_baseline_favorite_border_24);
                                cf.getUserFavorites().remove(user.getUserId());
                                adapter.notifyItemChanged(position);
                            });
                });
    }
}
