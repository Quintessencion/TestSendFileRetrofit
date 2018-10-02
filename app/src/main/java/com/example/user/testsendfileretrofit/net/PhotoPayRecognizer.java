package com.example.user.testsendfileretrofit.net;

import com.example.user.testsendfileretrofit.model.RecognizeRequest;
import com.example.user.testsendfileretrofit.model.RecognizeResponse;
import com.google.gson.internal.bind.util.ISO8601Utils;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import lombok.val;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class PhotoPayRecognizer {

    public static final String PAYMENT_ORDER = "PAYMENT_ORDER";
    public static final String PAYMENT_INVOICE = "PAYMENT_INVOICE";
    public static final String RECEIPT_UTILITY = "RECEIPT_UTILITY";
    public static final String RECEIPT_KINDERGARDEN = "RECEIPT_KINDERGARDEN";
    public static final String RECEIPT_STATE_SERVICES = "RECEIPT_STATE_SERVICES";
    public static final String OTHER = "OTHER";

    public static final String API_BASE_URL = "https://vtb.itfb.group:10012/recognition-service/api/";

    private PhotoPayApi api;

    public PhotoPayRecognizer() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient
                .Builder()
                .addInterceptor(interceptor)
                .readTimeout(45, TimeUnit.SECONDS)
                .writeTimeout(45, TimeUnit.SECONDS)
                .build();

        api = new Retrofit
                .Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createAsync())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(API_BASE_URL)
                .client(client)
                .build()
                .create(PhotoPayApi.class);
    }

    public Single<RecognizeResponse> uploadFile(byte[] bytes) {
        MultipartBody.Part body = MultipartBody.Part
                .createFormData("file", "photo_document",
                        RequestBody.create(MediaType.parse("image/*"), bytes));

        val request = RecognizeRequest
                .builder()
                .documentKind(PAYMENT_ORDER)
                .serviceNumber("1")
                .userDate(ISO8601Utils.format(new Date()))
                .userId("VTB")
                .build();

        return api.postImage(request, body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
