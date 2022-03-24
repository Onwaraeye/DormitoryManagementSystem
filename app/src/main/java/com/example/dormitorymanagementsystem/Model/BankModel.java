package com.example.dormitorymanagementsystem.Model;

import android.graphics.drawable.Drawable;

import java.io.Serializable;

public class BankModel implements Serializable {
    private int icon;
    private String title;

    public BankModel() {
    }

    public BankModel(int icon, String title) {
        this.icon = icon;
        this.title = title;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
