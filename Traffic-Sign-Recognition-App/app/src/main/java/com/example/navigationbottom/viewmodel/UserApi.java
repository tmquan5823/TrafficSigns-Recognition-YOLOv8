package com.example.navigationbottom.viewmodel;

import android.net.Uri;

import com.example.navigationbottom.model.BienBao;
import com.example.navigationbottom.model.Location;
import com.example.navigationbottom.model.LocationList;
import com.example.navigationbottom.model.LoginResponse;
import com.example.navigationbottom.model.User;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.core.Single;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface UserApi {
    // https://raw.githubusercontent.com/HoVanThao/json-server/patch-5/taikhoanmatkhau
//    @GET("HoVanThao/json-server/patch-5/taikhoanmatkhau")
//    Single<List<User>> getUser();

    @POST("api/auth/login")
    Call<LoginResponse> postUserLogin(@Body User requestData);

    @FormUrlEncoded
    @POST("api/auth/register")
    Call<JsonObject> postUserSignUp(@Field("username") String username, @Field("password") String password,
                                    @Field("name") String name, @Field("phone") String phone,
                                    @Field("email") String email, @Field("address") String address);

    @POST("api/user/update-user")
    Call<JsonObject> postUserUpdate(@Body User userUpdate);

    @POST("api/position/create-position")
    Call<JsonObject> postLocation(@Body LocationList locations);

    @FormUrlEncoded
    @POST("api/position/all-position-id")
    Call<JsonElement> getLocation(@Field("code") String code);
}
