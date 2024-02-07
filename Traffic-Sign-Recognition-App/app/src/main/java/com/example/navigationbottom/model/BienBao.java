package com.example.navigationbottom.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class BienBao implements Serializable {

    @SerializedName("id")
    private int id;
    @SerializedName("code")
    private String maBienBao;
    @SerializedName("content")
    private String noiDung;
    @SerializedName("url")
    private String url;

    private ArrayList<Location> listPosition;




    public BienBao(int id, String maBienBao, String noiDung, String url, ArrayList<Location> listPosition) {
        this.id = id;
        this.maBienBao = maBienBao;
        this.noiDung = noiDung;
        this.url = url;
        this.listPosition = listPosition;
    }

    public BienBao(int id, String maBienBao, String noiDung, String url) {
        this.id = id;
        this.maBienBao = maBienBao;
        this.noiDung = noiDung;
        this.url = url;
    }

    public BienBao(String maBienBao, String noiDung, String url) {
        this.id = id;
        this.maBienBao = maBienBao;
        this.noiDung = noiDung;
        this.url = url;
    }

    public BienBao(String maBienBao, String noiDung, String url, ArrayList<Location> listPosition) {
        this.maBienBao = maBienBao;
        this.noiDung = noiDung;
        this.url = url;
        this.listPosition = listPosition;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMaBienBao() {
        return maBienBao;
    }

    public void setMaBienBao(String maBienBao) {
        this.maBienBao = maBienBao;
    }

    public String getNoiDung() {
        return noiDung;
    }

    public void setNoiDung(String noiDung) {
        this.noiDung = noiDung;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public ArrayList<Location> getListPosition() {
        return listPosition;
    }

    public void setListPosition(ArrayList<Location> listPosition) {
        this.listPosition = listPosition;
    }
}
