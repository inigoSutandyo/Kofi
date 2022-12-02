package edu.bluejack22_1.kofi.controller.model;

import com.google.firebase.Timestamp;

public class Notification {
    private String content;
    private User user;
    private String notificationId;
    private Timestamp dateCreated;
    public Notification(){

    }

    public Notification(String content, User user, String notificationId, Timestamp dateCreated) {
        this.content = content;
        this.user = user;
        this.notificationId = notificationId;
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

    public String getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(String notificationId) {
        this.notificationId = notificationId;
    }

    public Timestamp getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Timestamp dateCreated) {
        this.dateCreated = dateCreated;
    }
}
