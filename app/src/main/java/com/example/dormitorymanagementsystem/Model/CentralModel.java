package com.example.dormitorymanagementsystem.Model;

public class CentralModel {
    private String central;
    private String name;
    private String date;
    private String time;

    public CentralModel(String central, String name, String date, String time) {
        this.central = central;
        this.name = name;
        this.date = date;
        this.time = time;
    }

    public String getCentral() {
        return central;
    }

    public void setCentral(String central) {
        this.central = central;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
