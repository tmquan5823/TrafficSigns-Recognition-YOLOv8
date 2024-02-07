package com.example.navigationbottom.viewmodel;

import android.net.Uri;

import com.example.navigationbottom.model.BienBao;
import com.example.navigationbottom.model.DetectionResponse;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.List;
import java.util.concurrent.TimeUnit;

import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory;
import io.reactivex.rxjava3.core.Single;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.Part;

public class BienBaoApiService {
    private static final String BASE_URL = "https://d1k8b3q5-5000.asse.devtunnels.ms/";
    private BienBaoApi api;

    OkHttpClient client;


    public BienBaoApiService(){

        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
        clientBuilder.connectTimeout(30, TimeUnit.SECONDS);
        clientBuilder.readTimeout(30, TimeUnit.SECONDS);
        clientBuilder.writeTimeout(30, TimeUnit.SECONDS);
        client = clientBuilder.build();
        api = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create()) // cover  laij
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create()) // ep luongg vao
                .client(client)
                .build()
                .create(BienBaoApi.class);
    }

    public Call<JsonElement> getBienBaoInApiSv(){
        return api.getBienBao();
    }

    public Call<DetectionResponse> postImage(@Part("image") String image){
        return api.postImage(image);
    }
}
