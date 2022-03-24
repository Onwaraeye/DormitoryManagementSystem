package com.example.dormitorymanagementsystem.Model;

public class User {
    private String ID;
    private String password;
    private String birthday;
    private String email;
    private String gender;
    private String firstname;
    private String lastname;
    private String numroom;
    private String personalID;
    private String role;
    private String phone;
    private String pictureUserUrl;

    public User(String ID, String password, String birthday, String email, String gender, String firstname, String lastname, String numroom, String personalID, String role, String phone, String pictureUserUrl) {
        this.ID = ID;
        this.password = password;
        this.birthday = birthday;
        this.email = email;
        this.gender = gender;
        this.firstname = firstname;
        this.lastname = lastname;
        this.numroom = numroom;
        this.personalID = personalID;
        this.role = role;
        this.phone = phone;
        this.pictureUserUrl = pictureUserUrl;
    }

    public String getPictureUserUrl() {
        return pictureUserUrl;
    }

    public void setPictureUserUrl(String pictureUserUrl) {
        this.pictureUserUrl = pictureUserUrl;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
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

    public String getNumroom() {
        return numroom;
    }

    public void setNumroom(String numroom) {
        this.numroom = numroom;
    }

    public String getPersonalID() {
        return personalID;
    }

    public void setPersonalID(String personalID) {
        this.personalID = personalID;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
