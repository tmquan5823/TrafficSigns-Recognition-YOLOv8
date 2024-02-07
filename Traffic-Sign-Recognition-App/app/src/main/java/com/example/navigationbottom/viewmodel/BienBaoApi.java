package com.example.navigationbottom.viewmodel;

import android.net.Uri;

import com.example.navigationbottom.model.BienBao;
import com.example.navigationbottom.model.DetectionResponse;
import com.example.navigationbottom.model.LoginResponse;
import com.example.navigationbottom.model.User;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.List;

import io.reactivex.rxjava3.core.Single;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface BienBaoApi {
    @GET("all-sign")
    Call<JsonElement> getBienBao();

    @Multipart
    @POST("detect")
    Call<DetectionResponse> postImage(@Part("image") String image);
}
