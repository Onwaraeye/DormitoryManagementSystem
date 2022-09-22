package com.example.dormitorymanagementsystem.notifications;

public class Token {

    String token,role;

    public Token() {
    }

    public Token(String token, String role) {
        this.token = token;
        this.role = role;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
