package com.example.navigationbottom.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class DetectionResponse implements Serializable {
    @SerializedName("detect")
    private List<List<String>> detect;

    public List<List<String>> getDetect() {
        return detect;
    }

    public void setDetect(List<List<String>> detect) {
        this.detect = detect;
    }

}
