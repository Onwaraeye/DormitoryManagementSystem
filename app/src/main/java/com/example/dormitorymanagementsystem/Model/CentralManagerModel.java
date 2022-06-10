package com.example.dormitorymanagementsystem.Model;

import java.io.Serializable;
import java.util.List;

public class CentralManagerModel implements Serializable {
    private String key;
    private String numroom;
    private String phone;
    private List<String> time;
    private String timeShow;
    private String value;

    public CentralManagerModel() {
    }

    public CentralManagerModel(String numRoom, String phone, List<String> time, String timeShow, String value) {
        this.numroom = numRoom;
        this.phone = phone;
        this.time = time;
        this.timeShow = timeShow;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getNumRoom() {
        return numroom;
    }

    public void setNumRoom(String numRoom) {
        this.numroom = numRoom;
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
