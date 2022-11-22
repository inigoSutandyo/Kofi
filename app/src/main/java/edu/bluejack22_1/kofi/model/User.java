package edu.bluejack22_1.kofi.model;

public class User {
    private String fullName, email, password, address, role, userId;

    public User(){}

    public User(String fullName, String email, String password, String address, String role) {
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.address = address;
        this.role = role;
    }

    public User(String userId, String fullName, String email, String password, String address, String role) {
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.address = address;
        this.role = role;
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
