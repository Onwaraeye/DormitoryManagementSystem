package com.example.dormitorymanagementsystem.Model;

import java.io.Serializable;

public class NewParcelModel implements Serializable {
    private String firstname;
    private String lastname;
    private String imageUrl;
    private String numroom;
    private String status;
    private String timestamp;
    private String nameReceiver;
    private String timestampReceiver;
    private String nameImporter;

    public NewParcelModel() {
    }

    public NewParcelModel(String firstname, String lastname, String imageUrl, String numroom, String status, String timestamp, String nameReceiver, String timestampReceiver, String nameImporter) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.imageUrl = imageUrl;
        this.numroom = numroom;
        this.status = status;
        this.timestamp = timestamp;
        this.nameReceiver = nameReceiver;
        this.timestampReceiver = timestampReceiver;
        this.nameImporter = nameImporter;
    }

    public String getNameImporter() {
        return nameImporter;
    }

    public void setNameImporter(String nameImporter) {
        this.nameImporter = nameImporter;
    }

    public String getNameReceiver() {
        return nameReceiver;
    }

    public void setNameReceiver(String nameReceiver) {
        this.nameReceiver = nameReceiver;
    }

    public String getTimestampReceiver() {
        return timestampReceiver;
    }

    public void setTimestampReceiver(String timestampReceiver) {
        this.timestampReceiver = timestampReceiver;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getNumroom() {
        return numroom;
    }

    public void setNumroom(String numroom) {
        this.numroom = numroom;
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
