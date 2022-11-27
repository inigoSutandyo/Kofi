package edu.bluejack22_1.kofi.controller;

import com.google.firebase.firestore.FirebaseFirestore;

import edu.bluejack22_1.kofi.interfaces.listeners.ReplyListener;

public class ReplyController {

    private FirebaseFirestore db;

    public ReplyController(){
        db = FirebaseFirestore.getInstance();
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
