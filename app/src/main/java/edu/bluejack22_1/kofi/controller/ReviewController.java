package edu.bluejack22_1.kofi.controller;

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

import java.util.ArrayList;
import java.util.Date;

import edu.bluejack22_1.kofi.interfaces.listeners.ReviewListener;
import edu.bluejack22_1.kofi.model.Review;
import edu.bluejack22_1.kofi.model.User;

public class ReviewController {
    FirebaseFirestore db;
    private UserController userController;
    public ReviewController(){
        db = FirebaseFirestore.getInstance();
        userController = new UserController();
    }

    public void addReview(String content, double rating, String shopId, ReviewListener listener) {
        DocumentReference ref = db.collection("users").document(User.getCurrentUser().getUserId());

        Review review = new Review(content, rating, "", ref,new Timestamp(new Date()));
        Log.d("Reference", ref.toString());
        Log.d("ShopID", shopId);
        db.collection("coffeeshop").document(shopId).collection("reviews").add(review)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()) {
                        task.getResult()
                                .update("reviewId", task.getResult().getId())
                                .addOnCompleteListener(t -> {
                                    db.collection("users")
                                            .document(User.getCurrentUser().getUserId())
                                            .update("reviews", FieldValue.arrayUnion(task.getResult()));
                                    listener.onSuccessReview();

                                });
                    }
                });
    }

    public void deleteReview(String shopId, String reviewId, ReviewListener listener){
        db.collection("coffeeshop")
                .document(shopId).collection("reviews")
                .document(reviewId).delete()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        DocumentReference reference = db.collection("users").document(reviewId);
                        db.collection("users")
                                .document(User.getCurrentUser().getUserId())
                                .update("reviews", FieldValue.arrayRemove(reference)).addOnCompleteListener(task1 -> {
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
