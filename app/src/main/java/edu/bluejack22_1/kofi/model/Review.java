package edu.bluejack22_1.kofi.model;

public class Review {
    private String content, reviewId;
    private double rating;
    private User user;


    public Review() {}

    public Review(String content, double rating, User user) {
        this.content = content;
        this.rating = rating;
        this.user = user;
    }

    public Review(String content, double rating, String reviewId) {
        this.content = content;
        this.rating = rating;
        this.reviewId = reviewId;
    }


    public Review(String content, double rating, User user, String reviewId) {
        this.content = content;
        this.rating = rating;
        this.user = user;
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
}
