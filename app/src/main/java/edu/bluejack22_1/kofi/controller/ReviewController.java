package edu.bluejack22_1.kofi.controller;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import edu.bluejack22_1.kofi.adapter.ReviewAdapter;
import edu.bluejack22_1.kofi.model.Review;
import edu.bluejack22_1.kofi.model.User;

public class ReviewController {
    FirebaseFirestore db;
    private UserController userController;
    public ReviewController(){
        db = FirebaseFirestore.getInstance();
        userController = new UserController();
    }

//    public void addReview(String content, User user, int rating) {
//        Review review = new Review(content, rating, user);
//        db.collection("reviews").add(review);
//    }

    public ArrayList<Review> getReviews(String shopId) {
        CollectionReference shopRef = db.collection("coffeeshop/"+shopId+"/reviews");
        ArrayList<Review> list = new ArrayList<>();
        shopRef.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                       if (task.isSuccessful()) {
                           for (QueryDocumentSnapshot document : task.getResult()) {
                               String content = (String) document.getData().get("content");
                               int rating = (int) document.getData().get("rating");
                               User user = userController.getUserByRef((DocumentReference) document.getData().get("user"));
                               String reviewId = document.getId();

                               list.add(new Review(content, rating, user, reviewId));
                           }
                       }
                     }
                });
        return list;
    }

    public void populateReviews(String shopId, ArrayList<Review> reviews, ReviewAdapter adapter) {
        CollectionReference shopRef = db.collection("coffeeshop/"+shopId+"/reviews");
        Log.d("Coffee", shopRef.getPath());
        shopRef.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String content = (String) document.getData().get("content");
                                double rating = (Double) document.getData().get("rating");
                                Log.d("Coffee", "Ref = " + document.getData().get("user"));
                                DocumentReference userRef = (DocumentReference) document.getData().get("user");
                                String reviewId = document.getId();
                                Review rev = new Review(content, rating, reviewId);
                                addUserByRef(userRef, rev, adapter);
                                reviews.add(rev);
                            }

                        } else {
                            Log.d("Coffee", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    private void addUserByRef(DocumentReference ref, Review review, ReviewAdapter adapter) {
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
                                review.setUser(new User(fullName, email, password, address, role, userId));
                                adapter.notifyDataSetChanged();
                            } else {
                                Log.d("Coffee", "No such document");
                            }
                        } else {
                            Log.d("Coffee", "get failed with ", task.getException());
                        }
                    }
                });


    }
}
