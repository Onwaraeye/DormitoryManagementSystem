package com.example.dormitorymanagementsystem.Model;

import android.os.Parcel;
import android.os.Parcelable;


public class CentralModel implements Parcelable {
    private String central;
    private String name;
    private String date;
    private String time;
    private String userID;

    public CentralModel() {
    }

    public CentralModel(String central, String name, String date, String time,String userID) {
        this.central = central;
        this.name = name;
        this.date = date;
        this.time = time;
        this.userID = userID;
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

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    protected CentralModel(Parcel in) {
        central = in.readString();
        name = in.readString();
        date = in.readString();
        time = in.readString();
    }

    public static final Creator<CentralModel> CREATOR = new Creator<CentralModel>() {
        @Override
        public CentralModel createFromParcel(Parcel in) {
            return new CentralModel(in);
        }

        @Override
        public CentralModel[] newArray(int size) {
            return new CentralModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(central);
        dest.writeString(name);
        dest.writeString(date);
        dest.writeString(time);
    }
}
