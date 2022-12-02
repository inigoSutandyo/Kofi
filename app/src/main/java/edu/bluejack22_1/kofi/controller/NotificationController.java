package edu.bluejack22_1.kofi.controller;

import android.util.Log;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import edu.bluejack22_1.kofi.interfaces.listeners.NotificationListener;
import edu.bluejack22_1.kofi.controller.model.Notification;
import edu.bluejack22_1.kofi.controller.model.User;

public class NotificationController {

    private FirebaseFirestore db;
    public NotificationController(){ db = FirebaseFirestore.getInstance();}

    public void addNotification(String userId, String content){
        if (User.getCurrentUser().getUserId().equals(userId)) {
            return;
        }
        Notification notification = new Notification(content, User.getCurrentUser(), "", Timestamp.now());
        db.collection("users")
                .document(userId)
                .collection("notifications")
                .add(notification)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        task.getResult()
                                .update("notificationId", task.getResult().getId());
                    }
                });
    }

    public void getMyNotifications(NotificationListener listener){
        db.collection("users")
                .document(User.getCurrentUser().getUserId())
                .collection("notifications")
                .orderBy("dateCreated", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        listener.onCompleteNotification(task.getResult());
                    }
                });
    }

    public void deleteNotification(String userId, String notificationId, NotificationListener listener) {
        db.collection("users")
                .document(userId)
                .collection("notifications")
                .document(notificationId)
                .delete()
                .addOnCompleteListener(task -> {
                    Log.d("NOTIFICATIONS", "Successfully deleted");
                    listener.onSuccessNotification();
                });
    }
}
