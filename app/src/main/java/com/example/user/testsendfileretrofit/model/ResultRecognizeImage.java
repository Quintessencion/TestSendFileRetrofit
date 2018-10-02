package com.example.user.testsendfileretrofit.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class ResultRecognizeImage {
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("ext")
    @Expose
    private String ext;
    @SerializedName("size")
    @Expose
    private int size;
    @SerializedName("mimeType")
    @Expose
    private String mimeType;
}
