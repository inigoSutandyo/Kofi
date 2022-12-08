package edu.bluejack22_1.kofi.controller;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import edu.bluejack22_1.kofi.interfaces.FragmentInterface;
import edu.bluejack22_1.kofi.interfaces.listeners.CommentListener;
import edu.bluejack22_1.kofi.model.Comment;
import edu.bluejack22_1.kofi.model.Review;
import edu.bluejack22_1.kofi.model.User;

public class CommentController {
    private FirebaseFirestore db;


    public CommentController() {
        db = FirebaseFirestore.getInstance();
    }

    public void addComment(String shopId, Review review, String content, CommentListener listener){
        DocumentReference ref = db.collection("users").document(User.getCurrentUser().getUserId());
        Comment comment = new Comment(content, "", User.getCurrentUser(), ref, Timestamp.now());
        db.collection("coffeeshop").document(shopId).
                collection("reviews").document(review.getReviewId()).collection("comments").
                add(comment)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        db.collection("coffeeshop")
                                .document(shopId)
                                .collection("reviews")
                                .document(review.getReviewId())
                                .collection("comments")
                                .document(task.getResult().getId())
                                .update("commentId", task.getResult().getId())
                                .addOnCompleteListener(task1 -> {
                                    listener.onSuccessComment();
                                });
                    }
                });
    }

   public void deleteComment(String shopId, String reviewId, String replyId, FragmentInterface listener){
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

    public void getComments(String shopId, String reviewId, CommentListener listener) {
        db.collection("coffeeshop")
                .document(shopId)
                .collection("reviews")
                .document(reviewId)
                .collection("comments")
                .orderBy("dateCreated", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(task -> {
                   if (task.isSuccessful())  {
                       listener.onCompleteCommentCollection(task.getResult());
                   }
                });
    }

    public void getComment(String commentId, String path, CommentListener listener) {
        db.collection(path)
                .document(commentId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        listener.onCompleteComment(task.getResult());
                    }
                });
    }
}
