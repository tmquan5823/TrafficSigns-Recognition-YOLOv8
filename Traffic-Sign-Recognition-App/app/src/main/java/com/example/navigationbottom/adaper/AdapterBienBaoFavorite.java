package com.example.navigationbottom.adaper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.navigationbottom.R;
import com.example.navigationbottom.activity.MapActivity;
import com.example.navigationbottom.model.BienBao;
import com.example.navigationbottom.model.Location;
import com.example.navigationbottom.viewmodel.UserApiService;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdapterBienBaoFavorite extends RecyclerView.Adapter<AdapterBienBaoFavorite.MyHolder>{


    private Context context;
    private ArrayList<BienBao> listBienBao;

    private UserApiService userApiService;

    private ArrayList<Location> listResponse;

    public AdapterBienBaoFavorite(Context context, ArrayList<BienBao> listBienBao) {
        this.context = context;
        this.listBienBao = listBienBao;
        this.listResponse = new ArrayList<>();
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(context).inflate(R.layout.favorite_item, parent, false);

            return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, @SuppressLint("RecyclerView") int position) {


        holder.tv_ma.setText(listBienBao.get(position).getMaBienBao());
        holder.tv_noiDung.setText(listBienBao.get(position).getNoiDung());

        try {
            Picasso.get().load(listBienBao.get(position).getUrl()).into(holder.sv_image_bienBao);
        }catch (Exception e){

        }

        holder.layoutItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MapActivity.class);

                getLocation(listBienBao.get(position).getMaBienBao())
                        .thenAccept(result -> {
                            intent.putExtra("maBienBaoMap", listBienBao.get(position).getMaBienBao());
                            intent.putExtra("noiDungBienBaoMap", listBienBao.get(position).getNoiDung());
                            intent.putExtra("urlBienBaoMap", listBienBao.get(position).getUrl());
                            intent.putParcelableArrayListExtra("location", listResponse);
                            context.startActivity(intent);
                        })
                        .exceptionally(ex -> {
                            return null;
                        });
            }
        });

    }
    @Override
    public int getItemCount() {
        return listBienBao.size();
    }


    class MyHolder extends RecyclerView.ViewHolder{
        ShapeableImageView sv_image_bienBao;
        TextView tv_ma, tv_noiDung;
        RelativeLayout layoutItem;


        public MyHolder(@NonNull View itemView) {
            super(itemView);
            sv_image_bienBao = itemView.findViewById(R.id.iv_itemBienBao_favorite);
            tv_ma = itemView.findViewById(R.id.tv_MaItem_favorite);
            tv_noiDung = itemView.findViewById(R.id.tv_ThongTinItem_favorite);
            layoutItem = itemView.findViewById(R.id.layout_item_favorite);
        }
    }
    private CompletableFuture<Void> getLocation(String code){
        CompletableFuture<Void> future = new CompletableFuture<>();
        userApiService = new UserApiService();
        userApiService.getLocation(code).enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                if(response.isSuccessful()){
                    JsonElement jsonElement = response.body();
                    if (jsonElement.isJsonObject()) {
                        JsonObject jsonObject = jsonElement.getAsJsonObject();
                        if (jsonObject.has("listPosition") && jsonObject.get("listPosition").isJsonArray()) {
                            JsonArray jsonArray = jsonObject.getAsJsonArray("listPosition");
                            for (JsonElement element : jsonArray) {
                                JsonObject locationObject = element.getAsJsonObject();
                                String code = locationObject.get("code").getAsString();
                                double longtitude = locationObject.get("longtitude").getAsDouble();
                                double latitude = locationObject.get("latitude").getAsDouble();
                                Location location1 = new Location(code, longtitude, latitude);
                                listResponse.add(location1);
                            }
                            future.complete(null);
                        }
                    }
                }else{
                    Log.e("Hello", "Response get fail " + response.code());
                    future.completeExceptionally(new Throwable("Get location failed"));
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                String errorMessage = t.getMessage();
                Log.e("Hello", String.valueOf("Request failed: " + errorMessage));
                future.completeExceptionally(t);
            }
        });
        return future;
    }
}
