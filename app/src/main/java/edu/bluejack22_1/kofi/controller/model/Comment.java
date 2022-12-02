package edu.bluejack22_1.kofi.controller.model;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;

public class Comment {
    private String content, commentId;
    private User user;
    private DocumentReference userRef;
    private Timestamp dateCreated;

    public Comment() {}

    public Comment(String content, String commentId, User user, DocumentReference userRef, Timestamp dateCreated) {
        this.content = content;
        this.commentId = commentId;
        this.user = user;
        this.userRef = userRef;
        this.dateCreated = dateCreated;
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
