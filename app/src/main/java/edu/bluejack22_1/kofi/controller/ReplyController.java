package edu.bluejack22_1.kofi.controller;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

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

    public void addReply(String path, String content, ReplyListener listener){
        DocumentReference ref = db.collection("users").document(User.getCurrentUser().getUserId());
        Reply reply = new Reply(content, "", ref, Timestamp.now());
        db.collection("coffeeshop")
                .document(path)
                .collection("replies")
                .add(reply)
                .addOnCompleteListener(task -> {
                    db.collection("coffeeshop").document(path).collection("replies").document(task.getResult().getId()).get().addOnCompleteListener(task1 -> {
                        Reply rep = task1.getResult().toObject(Reply.class);
                        String uid = rep.getUser().getUserId();
                        notifcontroller.addNotification(uid, "has replied on your comment");
                    }).addOnCompleteListener(task2 -> {
                        db.collection("coffeeshop")
                                .document(path)
                                .collection("replies")
                                .document(task.getResult().getId()).update("replyId", task.getResult().getId()).addOnCompleteListener(task1 -> {
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
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        listener.onCompleteReplyCollection(task.getResult());
                    }
                });
    }

}
