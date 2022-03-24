package com.example.dormitorymanagementsystem.Model;

public class Bill {
    private int room;
    private String status;
    private int roomRate;
    private int waterBill;
    private int electricityBill;
    private int total;
    private int month;
    private int year;

    public Bill() {
    }

    public int getRoom() {
        return room;
    }

    public void setRoom(int room) {
        this.room = room;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getRoomRate() {
        return roomRate;
    }

    public void setRoomRate(int roomRate) {
        this.roomRate = roomRate;
    }

    public int getWaterBill() {
        return waterBill;
    }

    public void setWaterBill(int waterBill) {
        this.waterBill = waterBill;
    }

    public int getElectricityBill() {
        return electricityBill;
    }

    public void setElectricityBill(int electricityBill) {
        this.electricityBill = electricityBill;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
}
