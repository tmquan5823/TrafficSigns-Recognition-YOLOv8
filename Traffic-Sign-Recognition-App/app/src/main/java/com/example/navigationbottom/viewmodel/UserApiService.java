package com.example.navigationbottom.viewmodel;

import android.net.Uri;

import com.example.navigationbottom.model.Location;
import com.example.navigationbottom.model.LocationList;
import com.example.navigationbottom.model.LoginResponse;
import com.example.navigationbottom.model.User;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory;
import io.reactivex.rxjava3.core.Single;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public class UserApiService {

    private static final String BASE_URL_2 = "https://d1k8b3q5-1410.asse.devtunnels.ms/";
    private UserApi api;

    public UserApiService(){
        api = new Retrofit.Builder()
                .baseUrl(BASE_URL_2)
                .addConverterFactory(GsonConverterFactory.create()) // cover  laij
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create()) // ep luongg vao
                .build()
                .create(UserApi.class);
    }

    public Call<LoginResponse> postUserLogin(@Body User requestData){
        return api.postUserLogin(requestData);
    }

    public Call<JsonObject> postUserSignUp(@Field("username") String username, @Field("password") String password,
                                           @Field("name") String name, @Field("phone") String phone,
                                           @Field("email") String email, @Field("address") String address){
        return api.postUserSignUp(username, password, name, phone, email, address);
    }

    public Call<JsonObject> postUserUpdate(@Body User userUpdate){
        return api.postUserUpdate(userUpdate);
    }

    public Call<JsonObject> postLocation(@Body LocationList locations){
        return api.postLocation(locations);
    }

    public Call<JsonElement> getLocation(@Field("code") String code){
        return api.getLocation(code);
    }
}
