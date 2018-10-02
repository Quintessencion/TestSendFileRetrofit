package com.example.user.testsendfileretrofit.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RecognizeRequest {

    private String documentKind;
    private String serviceNumber;
    private String userDate;
    private String userId;
}
