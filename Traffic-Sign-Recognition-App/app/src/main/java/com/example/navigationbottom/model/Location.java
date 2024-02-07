package com.example.navigationbottom.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.lang.annotation.Annotation;

public class Location  implements Parcelable, Serializable {

    @SerializedName("code")
    private String code;

    @SerializedName("longtitude")
    private double longtitude;

    @SerializedName("latitude")
    private double latitude;

    public Location(){
        this.code = null;
        this.latitude = 0.0;
        this.longtitude = 0.0;
    }

    public Location(String code, double longtitude, double latitude){
        this.code = code;
        this.longtitude = longtitude;
        this.latitude = latitude;
    }

    protected Location(Parcel in) {
        code = in.readString();
        latitude = in.readDouble();
        longtitude = in.readDouble();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(code);
        dest.writeDouble(latitude);
        dest.writeDouble(longtitude);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Location> CREATOR = new Creator<Location>() {
        @Override
        public Location createFromParcel(Parcel in) {
            return new Location(in);
        }

        @Override
        public Location[] newArray(int size) {
            return new Location[size];
        }
    };

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longtitude;
    }

    public void setLongitude(double longitude) {
        this.longtitude = longitude;
    }


}
