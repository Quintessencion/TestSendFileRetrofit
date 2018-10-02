package com.example.user.testsendfileretrofit.net;

import com.example.user.testsendfileretrofit.model.RecognizeRequest;
import com.example.user.testsendfileretrofit.model.RecognizeResponse;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.Single;
import okhttp3.MultipartBody;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface PhotoPayApi {

    @Multipart
    @POST("recognize/document")
    Single<RecognizeResponse> postImage(@Part("request") RecognizeRequest request, @Part MultipartBody.Part image);
}
