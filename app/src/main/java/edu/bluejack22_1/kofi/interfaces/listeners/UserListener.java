package edu.bluejack22_1.kofi.interfaces.listeners;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import edu.bluejack22_1.kofi.controller.model.User;

public interface UserListener {
    void onCompleteUser(DocumentSnapshot docSnap);
    void onCompleteUserCollection(QuerySnapshot querySnap);
    void onSuccessUpdateUser(User user);
    void onSuccessUser();
}
