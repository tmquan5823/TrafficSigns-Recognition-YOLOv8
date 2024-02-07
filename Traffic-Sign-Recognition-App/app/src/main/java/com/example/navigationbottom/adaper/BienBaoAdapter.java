package com.example.navigationbottom.adaper;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.navigationbottom.R;
import com.example.navigationbottom.databinding.BienbaoItemBinding;
import com.example.navigationbottom.fragment.ListBienBaoInHomeFragment;
import com.example.navigationbottom.model.BienBao;
import com.example.navigationbottom.model.Location;
import com.example.navigationbottom.viewmodel.UserApiService;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BienBaoAdapter extends RecyclerView.Adapter<BienBaoAdapter.ViewHolder> implements Filterable {

    public static ArrayList<BienBao> bienBaos;
    public static ArrayList<BienBao> bienBaoscopy;

    private UserApiService userApiService;
    private ArrayList<Location> listLocation;

    public BienBaoAdapter(ArrayList<BienBao> bienBaos) {
        this.bienBaos = bienBaos;
        this.bienBaoscopy = bienBaos;
        this.listLocation = new ArrayList<>();
    }

    @NonNull
    @Override
    public BienBaoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        BienbaoItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.bienbao_item,
                parent,
                false
        );

        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.binding.setBienbao(bienBaos.get(position));
        Picasso.get().load(bienBaos.get(position).getUrl()).into(holder.binding.ivBienbao);
    }

    @Override
    public int getItemCount() {
        if(bienBaos != null){
            return bienBaos.size();
        }
        return 0;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String input = charSequence.toString().toLowerCase();
                List<BienBao> filteredBienBao = new ArrayList<>();


                if (input.isEmpty()) {
                    filteredBienBao.addAll(bienBaoscopy);
                } else {
                    for (BienBao bb : bienBaoscopy) {
                        String normalizedNoiDung = removeAccents(bb.getNoiDung().toLowerCase());
                        String normalizedSearchText = removeAccents(input).toLowerCase();
                        if (normalizedNoiDung.contains(normalizedSearchText)) {
                            filteredBienBao.add(bb);
                        }
                    }
                }


                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredBienBao;

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                bienBaos = new ArrayList<>();
                bienBaos.addAll((List<BienBao>) results.values);
                notifyDataSetChanged();
            }
        };
    }

    // Hàm để loại bỏ dấu tiếng Việt
    public String removeAccents(String input) {
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
        normalized = normalized.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
        return normalized;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public BienbaoItemBinding binding;
        public ViewHolder(BienbaoItemBinding itemBinding) {
            super(itemBinding.getRoot());
            this.binding = itemBinding;
            itemBinding.ivBienbao.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    BienBao bienBao = bienBaos.get(getAdapterPosition());
                    getLocation(bienBao.getMaBienBao())
                            .thenAccept(result -> {
                                BienBao bienBao1 = new BienBao(bienBao.getId(), bienBao.getMaBienBao(), bienBao.getNoiDung(),
                                        bienBao.getUrl(), listLocation);
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("bienbao", bienBao1);
                                Navigation.findNavController(view).navigate(R.id.detailsListHomeFragment, bundle);
                            })
                            .exceptionally(ex -> {
                                return null;
                            });
                }
            });
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
                                listLocation.add(location1);
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
