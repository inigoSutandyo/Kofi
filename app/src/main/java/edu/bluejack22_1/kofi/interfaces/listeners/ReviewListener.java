package edu.bluejack22_1.kofi.interfaces.listeners;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public interface ReviewListener {
    void onCompleteReview(DocumentSnapshot docSnap);
    void onCompleteReviewCollection(QuerySnapshot querySnap);
    void onSuccessReview();
}
