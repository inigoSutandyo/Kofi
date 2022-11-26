package edu.bluejack22_1.kofi.model;

import com.google.firebase.firestore.DocumentReference;

public class Reply {
    private String content, replyId;
    private User user;
    private DocumentReference userRef;

    public Reply(){}

    public Reply(String content, String replyId, User user, DocumentReference userRef) {
        this.content = content;
        this.replyId = replyId;
        this.user = user;
        this.userRef = userRef;
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
