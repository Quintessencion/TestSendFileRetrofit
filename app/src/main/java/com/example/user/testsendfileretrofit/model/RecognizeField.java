package com.example.user.testsendfileretrofit.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Data;

@Data
public class RecognizeField {
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("kind")
    @Expose
    private String kind;
    @SerializedName("owner")
    @Expose
    private String owner;
    @SerializedName("value")
    @Expose
    private String value;
    @SerializedName("cleanedValue")
    @Expose
    private String cleanedValue;
    @SerializedName("valid")
    @Expose
    private boolean valid;
    @SerializedName("wordConfidence")
    @Expose
    private double wordConfidence;
    @SerializedName("symbolConfidence")
    @Expose
    private List<Double> symbolConfidence = null;
    @SerializedName("width")
    @Expose
    private int width;
    @SerializedName("height")
    @Expose
    private int height;
    @SerializedName("x")
    @Expose
    private int x;
    @SerializedName("y")
    @Expose
    private int y;
    @SerializedName("dictionaryCheck")
    @Expose
    private boolean dictionaryCheck;
}
