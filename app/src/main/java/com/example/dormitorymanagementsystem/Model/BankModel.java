package com.example.dormitorymanagementsystem.Model;

import android.graphics.drawable.Drawable;

import java.io.Serializable;

public class BankModel implements Serializable {
    private String icon;
    private String title;

    public BankModel() {
    }

    public BankModel(String icon, String title) {
        this.icon = icon;
        this.title = title;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
