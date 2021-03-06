package com.example.dormitorymanagementsystem.Model;

import java.io.Serializable;

public class BillModel implements Serializable {
    private String discount;
    private String electricity;
    private String water;
    private String roomprice;
    private String sum;
    private String status;
    private String imageUrl;

    public BillModel() {
    }

    public BillModel(String discount, String electricity, String water, String roomprice, String sum, String status,String imageUrl) {
        this.discount = discount;
        this.electricity = electricity;
        this.water = water;
        this.roomprice = roomprice;
        this.sum = sum;
        this.status = status;
        this.imageUrl = imageUrl;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getElectricity() {
        return electricity;
    }

    public void setElectricity(String electricity) {
        this.electricity = electricity;
    }

    public String getWater() {
        return water;
    }

    public void setWater(String water) {
        this.water = water;
    }

    public String getRoomprice() {
        return roomprice;
    }

    public void setRoomprice(String roomprice) {
        this.roomprice = roomprice;
    }

    public String getSum() {
        return sum;
    }

    public void setSum(String sum) {
        this.sum = sum;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
