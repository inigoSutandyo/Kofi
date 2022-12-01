package edu.bluejack22_1.kofi.controller;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import edu.bluejack22_1.kofi.interfaces.FragmentInterface;
import edu.bluejack22_1.kofi.interfaces.listeners.CommentListener;
import edu.bluejack22_1.kofi.model.Comment;
import edu.bluejack22_1.kofi.model.Review;
import edu.bluejack22_1.kofi.model.User;

public class CommentController {
    private FirebaseFirestore db;
    private NotificationController notifcontroller;

    public CommentController() {
        db = FirebaseFirestore.getInstance();
        notifcontroller = new NotificationController();
    }

    public void addComment(String shopId, String reviewId, String content, CommentListener listener){
        DocumentReference ref = db.collection("users").document(User.getCurrentUser().getUserId());
        Comment comment = new Comment(content, User.getCurrentUser(), ref, "");
        db.collection("coffeeshop").document(shopId).
                collection("reviews").document(reviewId).collection("comments").
                add(comment)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        db.collection("coffeeshop").document(shopId).
                                collection("reviews").document(reviewId).collection("comments").document(task.getResult().getId()).get().addOnCompleteListener(task1 -> {
                                    Comment temp = task1.getResult().toObject(Comment.class);
                                    notifcontroller.addNotification(temp.getUser().getUserId(), "has commented on your review");
                                }).addOnCompleteListener(task1 -> {
                                    db.collection("coffeeshop").document(shopId).
                                            collection("reviews")
                                            .document(reviewId)
                                            .collection("comments")
                                            .document(task.getResult().getId())
                                            .update("commentId", task.getResult().getId())
                                            .addOnCompleteListener(task2 -> {
                                                listener.onSuccessComment();
                                            });
                                });
                    }
                });
    }

   public void deleteCommment(String shopId, String reviewId, String replyId, FragmentInterface listener){
       db.collection("coffeeshop")
               .document(shopId)
               .collection("reviews")
               .document(reviewId).collection("comments")
               .document(replyId)
               .delete()
               .addOnCompleteListener(new OnCompleteListener<Void>() {
                   @Override
                   public void onComplete(@NonNull Task<Void> task) {
                       listener.returnFragment();
                   }
               });
   }

    public void getReplies(String shopId, String reviewId, CommentListener listener) {
        db.collection("coffeeshop")
                .document(shopId)
                .collection("reviews")
                .document(reviewId)
                .collection("comments")
                .get()
                .addOnCompleteListener(task -> {
                   if (task.isSuccessful())  {
                       listener.onCompleteCommentCollection(task.getResult());
                   }
                });
    }
}
