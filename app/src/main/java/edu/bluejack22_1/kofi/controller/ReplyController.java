package edu.bluejack22_1.kofi.controller;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import edu.bluejack22_1.kofi.interfaces.FragmentInterface;
import edu.bluejack22_1.kofi.interfaces.listeners.ReplyListener;
import edu.bluejack22_1.kofi.model.Reply;
import edu.bluejack22_1.kofi.model.User;

public class ReplyController {
    private FirebaseFirestore db;

    public ReplyController() {
        db = FirebaseFirestore.getInstance();
    }

    public void addComment(String shopId, String reviewId, String content, FragmentInterface listener){
        DocumentReference ref = db.collection("users").document(User.getCurrentUser().getUserId());
        Reply reply = new Reply(content, User.getCurrentUser(), ref);
        db.collection("coffeeshop").document(shopId).
                collection("reviews").document(reviewId).collection("replies").add(reply).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        listener.returnFragment();
                    }
                });
    }

    public void getReplies(String shopId, String reviewId, ReplyListener listener) {
        db.collection("coffeeshop")
                .document(shopId)
                .collection("reviews")
                .document(reviewId)
                .collection("replies")
                .get()
                .addOnCompleteListener(task -> {
                   if (task.isSuccessful())  {
                       listener.onCompleteReplyCollection(task.getResult());
                   }
                });
    }
}
