package com.example.dormitorymanagementsystem.Model;

public class PostModel {
    private String title;
    private String detail;
    private String imageUrl;
    private String timestamp;

    public PostModel() {
    }

    public PostModel(String title, String detail, String imageUrl, String timestamp) {
        this.title = title;
        this.detail = detail;
        this.imageUrl = imageUrl;
        this.timestamp = timestamp;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
