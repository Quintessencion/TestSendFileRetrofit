package com.example.user.testsendfileretrofit.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Data;

@Data
public class RecognizeResponseData {

    @SerializedName("guid")
    @Expose
    private String guid;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("recognitionStatus")
    @Expose
    private String recognitionStatus;
    @SerializedName("documentKind")
    @Expose
    private String documentKind;
    @SerializedName("startDate")
    @Expose
    private long startDate;
    @SerializedName("finishDate")
    @Expose
    private long finishDate;
    @SerializedName("preprocessJobDuration")
    @Expose
    private int preprocessJobDuration;
    @SerializedName("recognitionJobDuration")
    @Expose
    private int recognitionJobDuration;
    @SerializedName("resultImage")
    @Expose
    private ResultRecognizeImage resultRecognizeImage;
    @SerializedName("fields")
    @Expose
    private List<RecognizeField> recognizeFields = null;
}
