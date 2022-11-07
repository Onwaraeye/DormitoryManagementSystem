package com.example.dormitorymanagementsystem.Model;

public class EditMemberModel {
    private String userId;
    private String name;
    private String room;
    private String owner;
    private String image;

    public EditMemberModel(String userId, String name, String room, String owner, String image) {
        this.userId = userId;
        this.name = name;
        this.room = room;
        this.owner = owner;
        this.image = image;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
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
