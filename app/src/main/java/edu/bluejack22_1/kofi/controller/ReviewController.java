package edu.bluejack22_1.kofi.controller;

import android.app.Activity;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import edu.bluejack22_1.kofi.adapter.ReviewAdapter;
import edu.bluejack22_1.kofi.fragments.CoffeeShopFragment;
import edu.bluejack22_1.kofi.interfaces.FragmentInterface;
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

    public void addReview(String content, double rating, String shopId, FragmentInterface listener) {
        DocumentReference ref = db.collection("users").document(User.getCurrentUser().getUserId());

        Review review = new Review(content, rating, "", ref);
        Log.d("Reference", ref.toString());
        Log.d("ShopID", shopId);
        db.collection("coffeeshop").document(shopId).collection("reviews").add(review).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                db.collection("coffeeshop").document(shopId).collection("reviews")
                        .document(task.getResult().getId())
                        .update("reviewId", task.getResult().getId()).addOnCompleteListener(task1 ->{
                            listener.returnFragment();
                        });
            }
        });
    }

    public void getReviews(String shopId, ReviewListener listener) {
        CollectionReference shopRef = db.collection("coffeeshop/"+shopId+"/reviews");
        Log.d("COFFEE", "ID = " + shopId);
        shopRef.get()
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

    public void addUserByRef(DocumentReference ref, Review review, ReviewAdapter adapter) {
        ref.get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            String fullName = (String) document.getData().get("fullName");
                            String email = (String) document.getData().get("email");
                            String address = (String) document.getData().get("address");
                            String password = (String) document.getData().get("password");
                            String role = (String) document.getData().get("role");
                            String userId = document.getId();
                            review.setUser(new User(fullName, email, password, address, role, userId));
                            adapter.notifyDataSetChanged();
                        } else {
                            Log.d("Coffee", "No such document");
                        }
                    } else {
                        Log.d("Coffee", "get failed with ", task.getException());
                    }
                });
    }

    public void getMyReviews(User user, ReviewListener listener) {
        if (user.getReviews().size() < 1) {
            return;
        }
        for (DocumentReference docRef: user.getReviews()) {
            addReviewFromRef(docRef, listener);
        }
    }

    private void addReviewFromRef(DocumentReference docRef, ReviewListener listener) {
        docRef.get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot docSnap = task.getResult();
                        Log.d("Coffee", "DocSnap = " + docSnap);
                        listener.onCompleteReview(docSnap);
                    }
                });
    }


}
