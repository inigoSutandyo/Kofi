package edu.bluejack22_1.kofi.model;

import com.google.firebase.firestore.DocumentReference;

public class Review {
    private String content, reviewId;
    private double rating;
    private User user;
    private DocumentReference userRef;
    public Review() {}

    public Review(String content, double rating, String reviewId, DocumentReference userRef) {
        this.content = content;
        this.rating = rating;
        this.reviewId = reviewId;
        this.userRef = userRef;
        this.user = User.getCurrentUser();
    }

    public Review(String content, double rating, String reviewId) {
        this.content = content;
        this.rating = rating;
        this.reviewId = reviewId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getReviewId() {
        return reviewId;
    }

    public void setReviewId(String reviewId) {
        this.reviewId = reviewId;
    }

    public DocumentReference getUserRef() {
        return userRef;
    }

    public void setUserRef(DocumentReference userRef) {
        this.userRef = userRef;
    }
}
