package edu.bluejack22_1.kofi.model;

import com.google.firebase.firestore.DocumentReference;

public class Comment {
    private String content, commentId;
    private User user;
    private DocumentReference userRef;

    public Comment() {}

    public Comment(String content, User user, DocumentReference userRef, String commentId) {
        this.content = content;
        this.user = user;
        this.userRef = userRef;
        this.commentId = commentId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public DocumentReference getUserRef() {
        return userRef;
    }

    public void setUserRef(DocumentReference userRef) {
        this.userRef = userRef;
    }

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }
}
