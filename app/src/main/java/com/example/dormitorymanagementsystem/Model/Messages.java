package com.example.dormitorymanagementsystem.Model;

public class Messages {

    private String userID, lastMessage, chatKey;
    private int unseenMessages;

    public Messages(String userID, String lastMessage, String chatKey, int unseenMessages) {
        this.userID = userID;
        this.lastMessage = lastMessage;
        this.chatKey = chatKey;
        this.unseenMessages = unseenMessages;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public int getUnseenMessages() {
        return unseenMessages;
    }

    public void setUnseenMessages(int unseenMessages) {
        this.unseenMessages = unseenMessages;
    }

    public String getChatKey() {
        return chatKey;
    }

    public void setChatKey(String chatKey) {
        this.chatKey = chatKey;
    }
}
