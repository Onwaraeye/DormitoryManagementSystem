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
    private String elecafter;
    private String elecbefore;
    private String fee;
    private String waterafter;
    private String waterbefore;
    private String internet;


    public BillModel() {
    }

    public BillModel(String discount, String electricity, String water, String roomprice, String sum, String status, String imageUrl, String elecafter, String elecbefore, String fee, String waterafter, String waterbefore, String internet) {
        this.discount = discount;
        this.electricity = electricity;
        this.water = water;
        this.roomprice = roomprice;
        this.sum = sum;
        this.status = status;
        this.imageUrl = imageUrl;
        this.elecafter = elecafter;
        this.elecbefore = elecbefore;
        this.fee = fee;
        this.waterafter = waterafter;
        this.waterbefore = waterbefore;
        this.internet = internet;
    }

    public String getInternet() {
        return internet;
    }

    public void setInternet(String internet) {
        this.internet = internet;
    }

    public String getElecafter() {
        return elecafter;
    }

    public void setElecafter(String elecafter) {
        this.elecafter = elecafter;
    }

    public String getElecbefore() {
        return elecbefore;
    }

    public void setElecbefore(String elecbefore) {
        this.elecbefore = elecbefore;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public String getWaterafter() {
        return waterafter;
    }

    public void setWaterafter(String waterafter) {
        this.waterafter = waterafter;
    }

    public String getWaterbefore() {
        return waterbefore;
    }

    public void setWaterbefore(String waterbefore) {
        this.waterbefore = waterbefore;
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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}