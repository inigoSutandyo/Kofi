package edu.bluejack22_1.kofi.interfaces.listeners;

import com.google.firebase.firestore.QuerySnapshot;

public interface NotificationListener {
    public void onCompleteNotification(QuerySnapshot querySnap);
}
