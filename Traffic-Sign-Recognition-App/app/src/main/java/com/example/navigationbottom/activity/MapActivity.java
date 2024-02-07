package com.example.navigationbottom.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.navigationbottom.R;
import com.example.navigationbottom.fragment.detailsListHomeFragment;
import com.example.navigationbottom.model.GPSTracker;
import com.example.navigationbottom.model.Location;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback{
    private GoogleMap ggMap;
    private FrameLayout vungChuaMap;
    private ImageView ivAnhLon, ivBack;
    private ShapeableImageView siAnhNho;
    private TextView tvMaBienBao, tvNoidungBienBao;
    private String maBienBao, noiDungBienBao, urlBienBao;

    private ArrayList<Location> listLocation;

    private GPSTracker gps;

    private Location location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        location = new Location();
        Intent intent = getIntent();
        maBienBao = intent.getStringExtra("maBienBaoMap");
        noiDungBienBao = intent.getStringExtra("noiDungBienBaoMap");
        urlBienBao = intent.getStringExtra("urlBienBaoMap");
        listLocation = intent.getParcelableArrayListExtra("location");
        initView();
        gg_map();

        tvMaBienBao.setText(maBienBao);
        tvNoidungBienBao.setText(noiDungBienBao);

        Glide.with(MapActivity.this)
                .load(urlBienBao)
                .into(ivAnhLon);
        Glide.with(MapActivity.this)
                .load(urlBienBao)
                .into(siAnhNho);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Sử dụng NavController để quay lại Fragment danh sách
                finish();
            }
        });
    }

    private void gg_map() {
        SupportMapFragment mapFragment = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map_fragment);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        this.ggMap = googleMap;
        gps = new GPSTracker(MapActivity.this);
        if (gps.canGetLocation()) {
            location.setLatitude(gps.getLatitude());
            location.setLongitude(gps.getLongitude());
        }
        if(listLocation.size() != 0){
            for(int i = 0; i < listLocation.size(); i++){
                LatLng mapVietNam = new LatLng(listLocation.get(i).getLatitude(), listLocation.get(i).getLongitude());
                this.ggMap.addMarker(new MarkerOptions().position(mapVietNam).title(maBienBao));
                float zoomLevel = 15.0f;
                this.ggMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mapVietNam, zoomLevel));
            }
        }
        else{
            LatLng mapVietNam = new LatLng( location.getLatitude(), location.getLongitude());
            this.ggMap.addMarker(new MarkerOptions().position(mapVietNam).title("My location."));
            float zoomLevel = 15.0f;
            this.ggMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mapVietNam, zoomLevel));
        }
    }

    private void initView() {
        vungChuaMap = findViewById(R.id.map_fragment);
        ivAnhLon = findViewById(R.id.iv_anhLon_mapAc);
        siAnhNho = findViewById(R.id.si_anhNho_mapAc);
        tvMaBienBao = findViewById(R.id.tv_maBienBao_mapAc);
        tvNoidungBienBao = findViewById(R.id.tv_noiDungBienBao_mapAc);
        ivBack = findViewById(R.id.iv_back_mapAc);
    }
}