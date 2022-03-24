package com.example.dormitorymanagementsystem.Model;

public class ChatList {
    private String userID;
    private String message;
    private String date;
    private String time;

    public ChatList(String userID, String message, String date, String time) {
        this.userID = userID;
        this.message = message;
        this.date = date;
        this.time = time;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
