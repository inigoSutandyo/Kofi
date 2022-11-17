package edu.bluejack22_1.kofi.model;

public class User {
    private String FullName, Email, Password, Address, Role;

    public User(){}

    public User(String fullName, String email, String password, String address, String role) {
        FullName = fullName;
        Email = email;
        Password = password;
        Address = address;
        Role = role;
    }
    public String getFullName() {
        return FullName;
    }

    public void setFullName(String fullName) {
        FullName = fullName;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getRole() {
        return Role;
    }

    public void setRole(String role) {
        Role = role;
    }
}
