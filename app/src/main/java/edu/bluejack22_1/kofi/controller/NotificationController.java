package edu.bluejack22_1.kofi.controller;

import android.util.Log;

import com.google.firebase.firestore.FirebaseFirestore;

import edu.bluejack22_1.kofi.interfaces.listeners.NotificationListener;
import edu.bluejack22_1.kofi.model.Notification;
import edu.bluejack22_1.kofi.model.User;

public class NotificationController {

    private FirebaseFirestore db;
    public NotificationController(){ db = FirebaseFirestore.getInstance();}

    public void addNotification(String userId, String content){
        Log.d("NOTIFICATION", User.getCurrentUser().getUserId() + " | " + userId);
        if (User.getCurrentUser().getUserId().equals(userId)) {
            return;
        }
        Notification notification = new Notification(content, User.getCurrentUser());
        db.collection("users").document(userId).collection("notifications").add(notification);
    }

    public void getMyNotifications(NotificationListener listener){
        db.collection("users").document(User.getCurrentUser().getUserId())
                .collection("notifications")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        listener.onCompleteNotification(task.getResult());
                    }
                });
    }
}
