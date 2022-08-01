package com.example.dormitorymanagementsystem.Model;

public class RepairModel {
    private String numroom;
    private String titleRepair;
    private String detail;
    private String imageUrl;
    private String phone;
    private String userID;
    private String status;
    private String timestamp;
    private String cost;
    private String timestampComplete;

    public RepairModel(String numroom, String titleRepair, String detail, String imageUrl, String phone, String userID, String status, String timestamp, String cost) {
        this.numroom = numroom;
        this.titleRepair = titleRepair;
        this.detail = detail;
        this.imageUrl = imageUrl;
        this.phone = phone;
        this.userID = userID;
        this.status = status;
        this.timestamp = timestamp;
        this.cost = cost;
    }

    public String getTimestampComplete() {
        return timestampComplete;
    }

    public void setTimestampComplete(String timestampComplete) {
        this.timestampComplete = timestampComplete;
    }

    public RepairModel() {
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getNumroom() {
        return numroom;
    }

    public void setNumroom(String numroom) {
        this.numroom = numroom;
    }

    public String getTitleRepair() {
        return titleRepair;
    }

    public void setTitleRepair(String titleRepair) {
        this.titleRepair = titleRepair;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
