package com.example.dormitorymanagementsystem.ChatNew;

public class ModelUser {
    String birthday,email,firstname,id,lastname,numroom,password,personalID,phone,pictureUserUrl,role,gender;

    public ModelUser() {
    }

    public ModelUser(String birthday, String email, String firstname, String id, String lastname, String numroom, String password, String personalID, String phone, String pictureUserUrl, String role, String gender) {
        this.birthday = birthday;
        this.email = email;
        this.firstname = firstname;
        this.id = id;
        this.lastname = lastname;
        this.numroom = numroom;
        this.password = password;
        this.personalID = personalID;
        this.phone = phone;
        this.pictureUserUrl = pictureUserUrl;
        this.role = role;
        this.gender = gender;
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

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPersonalID() {
        return personalID;
    }

    public void setPersonalID(String personalID) {
        this.personalID = personalID;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPictureUserUrl() {
        return pictureUserUrl;
    }

    public void setPictureUserUrl(String pictureUserUrl) {
        this.pictureUserUrl = pictureUserUrl;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
