package com.example.petscommunicator.server;

import java.util.HashMap;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface RetrofitInterface {

    @Multipart
    @POST("/upload")
    Call<UploadAudio> executeUpload(@Part MultipartBody.Part audio);

    @GET("/")
    Call<String> executeMain();
}
