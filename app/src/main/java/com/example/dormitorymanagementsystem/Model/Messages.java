package com.example.dormitorymanagementsystem.Model;

public class Messages {

    private String userID, lastMessage, chatKey;
    private int unseenMessages;

    public Messages(String userID, String lastMessage, int unseenMessages, String chatKey) {
        this.userID = userID;
        this.lastMessage = lastMessage;
        this.unseenMessages = unseenMessages;
        this.chatKey = chatKey;
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
