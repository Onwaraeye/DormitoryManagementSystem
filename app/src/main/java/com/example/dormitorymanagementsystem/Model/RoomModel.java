package com.example.dormitorymanagementsystem.Model;

import java.util.List;

public class RoomModel {
    private String numroom;
    private List<String> listMember;

    public RoomModel(String numroom, List<String> listMember) {
        this.numroom = numroom;
        this.listMember = listMember;
    }

    public String getNumroom() {
        return numroom;
    }

    public void setNumroom(String numroom) {
        this.numroom = numroom;
    }

    public List<String> getListMember() {
        return listMember;
    }

    public void setListMember(List<String> listMember) {
        this.listMember = listMember;
    }
}
