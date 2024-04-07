package com.example.whattowatch.src.Domain;

public class User {
    private String UserId;
    private String Password;
    private String Email;
    private String FullName;

    // Constructor
    public User(String userId, String fullName,  String email, String password) {
        UserId = userId;
        Password = password;
        Email = email;
        FullName = fullName;
    }

    // Getters and Setters
    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getFullName() {
        return FullName;
    }

    public void setFullName(String fullName) {
        FullName = fullName;
    }
}
