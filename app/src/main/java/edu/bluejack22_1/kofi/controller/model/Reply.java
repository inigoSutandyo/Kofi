package edu.bluejack22_1.kofi.controller.model;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;

public class Reply {
    private String content, replyId;
    private User user;
    private DocumentReference userRef;
    private Timestamp dateCreated;

    public Reply(){}

    public Reply(String content, String replyId, DocumentReference userRef, Timestamp dateCreated) {
        this.content = content;
        this.replyId = replyId;
        this.user = User.getCurrentUser();
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

    public String getReplyId() {
        return replyId;
    }

    public void setReplyId(String replyId) {
        this.replyId = replyId;
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
}
