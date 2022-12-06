package edu.bluejack22_1.kofi.controller;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import edu.bluejack22_1.kofi.interfaces.listeners.ReplyListener;
import edu.bluejack22_1.kofi.model.Reply;
import edu.bluejack22_1.kofi.model.User;

public class ReplyController {

    private FirebaseFirestore db;
    private NotificationController notifcontroller;

    public ReplyController(){
        db = FirebaseFirestore.getInstance();
        notifcontroller = new NotificationController();
    }

    public void addReply(String path, String userId, String content, ReplyListener listener){
        DocumentReference ref = db.collection("users").document(User.getCurrentUser().getUserId());
        Reply reply = new Reply(content, "", ref, Timestamp.now());
        db.collection("coffeeshop")
                .document(path)
                .collection("replies")
                .add(reply)
                .addOnCompleteListener(task -> {
                    db.collection("coffeeshop")
                            .document(path)
                            .collection("replies")
                            .document(task.getResult().getId())
                            .get()
                            .addOnCompleteListener(t1 -> {
                                notifcontroller.addNotification(userId, "has replied on your comment");
                                db.collection("coffeeshop")
                                        .document(path)
                                        .collection("replies")
                                        .document(task.getResult().getId())
                                        .update("replyId", task.getResult().getId())
                                        .addOnCompleteListener(t2 -> {
                                            listener.onSuccessReply();
                                        });
                            });
                });
    }

    public void deleteReply(String path, String replyId, ReplyListener listener){
        db.collection("coffeeshop")
                .document(path)
                .collection("replies").document(replyId).delete().addOnCompleteListener(task -> {
                    listener.onSuccessReply();
                });
    }

    public void getReplies(String path, ReplyListener listener) {
        db.collection("coffeeshop")
                .document(path)
                .collection("replies")
                .orderBy("dateCreated", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        listener.onCompleteReplyCollection(task.getResult());
                    }
                });
    }

}
