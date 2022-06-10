package com.example.dormitorymanagementsystem.Model;

import java.util.List;

public class ViewCentralModel {
    private String centralType;
    private String year;
    private String month;
    private String day;
    private String userId;
    private String numRoom;
    private String phone;
    private List<String> time;
    private String timeShow;
    private String value;

    public ViewCentralModel() {
    }

    public ViewCentralModel(String centralType, String year, String month, String day, String userId, String numRoom, String phone, List<String> time, String timeShow, String value) {
        this.centralType = centralType;
        this.year = year;
        this.month = month;
        this.day = day;
        this.userId = userId;
        this.numRoom = numRoom;
        this.phone = phone;
        this.time = time;
        this.timeShow = timeShow;
        this.value = value;
    }

    public String getCentralType() {
        return centralType;
    }

    public void setCentralType(String centralType) {
        this.centralType = centralType;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getNumRoom() {
        return numRoom;
    }

    public void setNumRoom(String numRoom) {
        this.numRoom = numRoom;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public List<String> getTime() {
        return time;
    }

    public void setTime(List<String> time) {
        this.time = time;
    }

    public String getTimeShow() {
        return timeShow;
    }

    public void setTimeShow(String timeShow) {
        this.timeShow = timeShow;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
