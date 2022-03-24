package com.example.dormitorymanagementsystem.Model;

public class EditMemberModel {
    private String userId;
    private String name;
    private String room;

    public EditMemberModel(String userId, String name, String room) {
        this.userId = userId;
        this.name = name;
        this.room = room;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
