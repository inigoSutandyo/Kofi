package edu.bluejack22_1.kofi.model;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;

import java.util.ArrayList;

public class Review {
    private String content, reviewId;
    private double rating;
    private User user;
    private DocumentReference userRef;
    private Timestamp dateCreated;
    private ArrayList<String> likers;
    private String imageUrl;

    public ArrayList<String> getLikers() {
        return likers;
    }



    public Review() {
        this.likers = new ArrayList<>();
    }

    public Review(String content, double rating, String reviewId, DocumentReference userRef, Timestamp dateCreated, String imageUrl) {
        this.content = content;
        this.rating = rating;
        this.reviewId = reviewId;
        this.userRef = userRef;
        this.user = User.getCurrentUser();
        this.dateCreated = dateCreated;
        this.likers = new ArrayList<>();
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setLikers(ArrayList<String> likers) {
        this.likers = likers;
    }

    public Timestamp getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Timestamp dateCreated) {
        this.dateCreated = dateCreated;
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
