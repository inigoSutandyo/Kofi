package edu.bluejack22_1.kofi.interfaces.listeners;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import edu.bluejack22_1.kofi.model.Review;

public interface ReviewListener {
    void onCompleteReview(DocumentSnapshot docSnap);
    void onCompleteReviewCollection(QuerySnapshot querySnap);
    void onSuccessUpdateReview(Review review);
    void onLikedReview(int position);
    void onSuccessReview();
}
