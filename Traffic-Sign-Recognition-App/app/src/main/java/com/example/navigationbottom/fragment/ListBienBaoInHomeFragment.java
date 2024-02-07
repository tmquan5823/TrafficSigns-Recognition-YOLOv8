package com.example.navigationbottom.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.navigationbottom.R;
import com.example.navigationbottom.activity.LoginActivity;
import com.example.navigationbottom.activity.MainActivity;
import com.example.navigationbottom.adaper.BienBaoAdapter;
import com.example.navigationbottom.model.BienBao;
import com.example.navigationbottom.model.Location;
import com.example.navigationbottom.model.User;
import com.example.navigationbottom.viewmodel.BienBaoApiService;
import com.example.navigationbottom.viewmodel.UserApiService;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ListBienBaoInHomeFragment extends Fragment{

    private RecyclerView rvBienBao;
    private ArrayList<BienBao> bienBaos;


    private BienBaoAdapter bienBaoAdapter;

    private ShimmerFrameLayout shimmerFrameLayout;

    private BienBaoApiService bienbaoApiService;




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list_bien_bao_in_home, container, false);
    }

    @SuppressLint("CheckResult")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvBienBao = view.findViewById(R.id.rv_bienbaolistfraghome);
        bienBaos = new ArrayList<>();
        bienBaoAdapter = new BienBaoAdapter(bienBaos);
        rvBienBao.setAdapter(bienBaoAdapter);
        rvBienBao.setLayoutManager(new GridLayoutManager(getContext(), 2));

        shimmerFrameLayout = view.findViewById(R.id.shimmer_listBienBao);
        shimmerFrameLayout.startShimmer();


        bienbaoApiService = new BienBaoApiService();
        bienbaoApiService.getBienBaoInApiSv()
                .enqueue(new Callback<JsonElement>() {
                    @Override
                    public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                        if (response.isSuccessful()) {
                            JsonElement jsonElement = response.body();
                            if (jsonElement.isJsonObject()) {
                                JsonObject jsonObject = jsonElement.getAsJsonObject();
                                if (jsonObject.has("listSign") && jsonObject.get("listSign").isJsonArray()) {
                                    JsonArray jsonArray = jsonObject.getAsJsonArray("listSign");
                                    shimmerFrameLayout.stopShimmer();
                                    shimmerFrameLayout.setVisibility(View.GONE);
                                    rvBienBao.setVisibility(View.VISIBLE);
                                    for (JsonElement element : jsonArray) {
                                        JsonObject signObject = element.getAsJsonObject();
                                        int id = signObject.get("id").getAsInt();
                                        String code = signObject.get("code").getAsString();
                                        String content = signObject.get("content").getAsString();
                                        String url = signObject.get("url").getAsString();
                                        BienBao bienBao = new BienBao(id, code, content, url);
                                        bienBaos.add(bienBao);
                                        bienBaoAdapter.notifyDataSetChanged();
                                    }

                                }
                            }
                        } else {
                            shimmerFrameLayout.stopShimmer();
                            shimmerFrameLayout.setVisibility(View.GONE);
                            int errorCode = response.code();
                            Log.e("Hello", "Request failed with code: " + errorCode);
                        }
                    }
                    @Override
                    public void onFailure(Call<JsonElement> call, Throwable t) {
                        String errorMessage = t.getMessage();
                        Log.e("Hello", "Request failed: " + errorMessage);
                    }
                });
        setFragmentToolbar(view);
        setToolbarMenu();
    }

    private void setToolbarMenu() {
        requireActivity().addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.menu_search, menu);
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                SearchView searchView = (SearchView) menuItem.getActionView();

                EditText searchEditText = searchView.findViewById(androidx.appcompat.R.id.search_src_text);

                // Thiết lập màu chữ cho EditText
                searchEditText.setTextColor(getResources().getColor(R.color.white));

                // (Tùy chọn) Thiết lập màu chữ cho phần gợi ý (suggestions)
                searchEditText.setHintTextColor(getResources().getColor(R.color.hintTextColor));

                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        bienBaoAdapter.getFilter().filter(newText);
                        return false;
                    }
                });

                return false;
            }
        },getViewLifecycleOwner(), Lifecycle.State.RESUMED);

    }

    private void setFragmentToolbar(View view) {
        Toolbar toolbar = view.findViewById(R.id.toolbarMain);
        ((MainActivity)requireActivity()).setSupportActionBar(toolbar);
    }

}


