package edu.bluejack22_1.kofi.model;

public class Notification {
    private String content;
    private User user;
    private String notificationId;
    public Notification(){

    }

    public Notification(String content, User user, String notificationId) {
        this.content = content;
        this.user = user;
        this.notificationId = notificationId;
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
}
