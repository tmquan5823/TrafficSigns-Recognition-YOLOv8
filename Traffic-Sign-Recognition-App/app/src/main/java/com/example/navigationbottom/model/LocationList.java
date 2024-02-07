package com.example.navigationbottom.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class LocationList implements Serializable {
    @SerializedName("listPosition")
    private List<Location> listPosition;

    public List<Location> getListPosition() {
        return listPosition;
    }

    public void setListPosition(List<Location> listPosition) {
        this.listPosition = listPosition;
    }
}
