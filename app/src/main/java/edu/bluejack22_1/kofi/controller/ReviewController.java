package edu.bluejack22_1.kofi.controller;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Date;

import edu.bluejack22_1.kofi.interfaces.listeners.ReviewListener;
import edu.bluejack22_1.kofi.model.CoffeeShop;
import edu.bluejack22_1.kofi.model.Review;
import edu.bluejack22_1.kofi.model.User;

public class ReviewController {
    FirebaseFirestore db;
    private UserController userController;
    private FirebaseStorage storage;
    public ReviewController(){
        db = FirebaseFirestore.getInstance();
        userController = new UserController();
        storage = FirebaseStorage.getInstance();
    }

    public void addReview(String content, double rating, String shopId, Uri imageUri, ReviewListener listener) {
        DocumentReference ref = db.collection("users").document(User.getCurrentUser().getUserId());
        String imageUrl = "";
        if (imageUri != null) {
            imageUrl = imageUri.toString();
        }
        Review review = new Review(content, rating, "", ref,new Timestamp(new Date()), imageUrl);
        Log.d("Reference", ref.toString());
        Log.d("ShopID", shopId);
        db.collection("coffeeshop").document(shopId).collection("reviews").add(review)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()) {
                        if(imageUri != null){
                            uploadReviewImage(imageUri, task.getResult(), shopId, rating, listener);
                        } else {
                            setNewReview(task.getResult(), shopId, rating, listener);
                        }

                    }
                });
    }

    private void uploadReviewImage(Uri uri, DocumentReference documentReference,String shopId, Double rating, ReviewListener listener) {
        StorageReference storageReference = FirebaseStorage
                .getInstance()
                .getReference("images/reviews/"+documentReference.getId());

        storageReference.putFile(uri)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        storageReference.getDownloadUrl().addOnSuccessListener(downloadUri -> {
                            documentReference
                                    .update("imageUrl", downloadUri.toString())
                                    .addOnSuccessListener(unused -> {
                                        setNewReview(documentReference, shopId, rating, listener);
                                    });
                        });
                    }
                });
    }

    private void setNewReview(DocumentReference documentReference, String shopId, Double rating, ReviewListener listener) {
        documentReference
                .update("reviewId", documentReference.getId())
                .addOnCompleteListener(t -> {
                    db.collection("users")
                            .document(User.getCurrentUser().getUserId())
                            .update("reviews", FieldValue.arrayUnion(documentReference));
                    db.collection("coffeeshop")
                            .document(shopId)
                            .update("totalRating", FieldValue.increment(rating)
                                    , "reviewCount", FieldValue.increment(1));
                    listener.onSuccessReview();

                });
    }

    public void updateReview(String content, double rating, String shopId, Review review, ReviewListener listener){
        if (review == null) return;
        DocumentReference ref = db.collection("users").document(User.getCurrentUser().getUserId());
        db.collection("coffeeshop")
                .document(shopId)
                .collection("reviews")
                .document(review.getReviewId()).update("content", content, "rating", rating)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentReference documentReference = db.collection("coffeeshop").document(shopId);
                        documentReference.get()
                                .addOnCompleteListener(task1 -> {
                                   if (task1.isSuccessful()) {
                                       CoffeeShop cf = task1.getResult().toObject(CoffeeShop.class);
                                       Double totalRating = cf.getTotalRating() - review.getRating() + rating;
                                       documentReference.update("totalRating", totalRating);
                                   }
                                });
                    }
                    listener.onSuccessReview();
                });
    }

    public void deleteReview(String shopId, Review review, ReviewListener listener){
        String reviewId = review.getReviewId();
        Double rating = review.getRating();
        db.collection("coffeeshop")
                .document(shopId).collection("reviews")
                .document(reviewId).delete()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        DocumentReference reference = db.collection("users").document(reviewId);
                        db.collection("users")
                                .document(User.getCurrentUser().getUserId())
                                .update("reviews", FieldValue.arrayRemove(reference)).addOnCompleteListener(task1 -> {
                                    db.collection("coffeeshop")
                                            .document(shopId)
                                            .update("totalRating", FieldValue.increment(-rating)
                                                    , "reviewCount", FieldValue.increment(-1));
                                    listener.onSuccessReview();
                                });
                    }
                });
    }

    public void getReviews(String shopId, ReviewListener listener) {
        CollectionReference reviewCollection = db.collection("coffeeshop/"+shopId+"/reviews");
        reviewCollection.orderBy("dateCreated", Query.Direction.DESCENDING).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                       if (task.isSuccessful()) {
                           listener.onCompleteReviewCollection(task.getResult());
                       } else {
                           Log.d("REVIEW", "Error getting documents: ", task.getException());
                       }
                     }
                });
    }

    public void getMyReviews(User user, ReviewListener listener) {
        db.collection("users")
                .document(user.getUserId())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        user.setReviews((ArrayList<DocumentReference>) task.getResult().get("reviews"));
                        for (DocumentReference docRef: user.getReviews()) {
                            addReviewFromRef(docRef, listener);
                        }
                    }
                });
    }

    private void addReviewFromRef(DocumentReference docRef, ReviewListener listener) {
        docRef.get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot docSnap = task.getResult();
                        listener.onCompleteReview(docSnap);
                    }
                });
    }

    public void getReview(String shopID, String reviewID, ReviewListener listener) {
        db.collection("coffeeshop")
                .document(shopID)
                .collection("reviews")
                .document(reviewID)
                .get()
                .addOnCompleteListener(task -> {
                   if (task.isSuccessful())  {
                       listener.onCompleteReview(task.getResult());
                   }
                });
    }

}
