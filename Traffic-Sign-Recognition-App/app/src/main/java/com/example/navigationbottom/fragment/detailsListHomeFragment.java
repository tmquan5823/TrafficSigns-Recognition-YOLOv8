package com.example.navigationbottom.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.navigationbottom.R;
import com.example.navigationbottom.model.BienBao;
import com.example.navigationbottom.model.GPSTracker;
import com.example.navigationbottom.model.Location;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;

public class detailsListHomeFragment extends Fragment implements OnMapReadyCallback {
    private View mView;
    private GoogleMap ggMap;
    private FrameLayout vungChuaMap;
    private BienBao bienBao;
    private NavController navController;
    private ImageView ivAnhLon, ivBackDetails;
    private ShapeableImageView siAnhNho;
    private TextView tvMaBienBao, tvNoidungBienBao;
    private ShimmerFrameLayout shimmerFrameLayout;
    private NestedScrollView nestedScrollView;

    private ArrayList<Location> listLocation;
    private GPSTracker gps;
    private Location location;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            bienBao = (BienBao) getArguments().getSerializable("bienbao");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

      mView = inflater.inflate(R.layout.fragment_details_list_home, container, false);
      return mView;
    }

    @SuppressLint("CheckResult")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        vungChuaMap = view.findViewById(R.id.map_fragment_detailListHome);

        ivAnhLon = view.findViewById(R.id.iv_anhLon);
        siAnhNho = view.findViewById(R.id.si_anhNho);
        tvMaBienBao = view.findViewById(R.id.tv_maBienBaoDetails);
        tvNoidungBienBao = view.findViewById(R.id.tv_noiDungBienBaoDetail);
        ivBackDetails = view.findViewById(R.id.iv_backDetails);
        shimmerFrameLayout = view.findViewById(R.id.shimmer_details);
        nestedScrollView = view.findViewById(R.id.scrollView_details_listHome);
        listLocation = new ArrayList<>();
        location = new Location();
        shimmerFrameLayout.startShimmer();


        tvMaBienBao.setText(bienBao.getMaBienBao());
        tvNoidungBienBao.setText(bienBao.getNoiDung());
        listLocation.addAll(bienBao.getListPosition());

        gg_map();
        Glide.with(detailsListHomeFragment.this)
                .load(bienBao.getUrl())
                .into(ivAnhLon);
        Glide.with(detailsListHomeFragment.this)
                .load(bienBao.getUrl())
                .into(siAnhNho);

        Handler handler = new Handler();
        handler.postDelayed(()->{
            shimmerFrameLayout.stopShimmer();
            shimmerFrameLayout.setVisibility(View.GONE);
            nestedScrollView.setVisibility(View.VISIBLE);
        }, 1000);

        navController = NavHostFragment.findNavController(this);

        ivBackDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.popBackStack();
            }
        });

    }


    private void gg_map() {
        SupportMapFragment mapFragment = (SupportMapFragment)getChildFragmentManager().findFragmentById(R.id.map_fragment_detailListHome);
        mapFragment.getMapAsync(detailsListHomeFragment.this);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        this.ggMap = googleMap;
        gps = new GPSTracker(getContext());
        if (gps.canGetLocation()) {
            location.setLatitude(gps.getLatitude());
            location.setLongitude(gps.getLongitude());
        }
        if(listLocation.size() != 0){
            for(int i = 0; i < listLocation.size(); i++){
                LatLng mapVietNam = new LatLng( listLocation.get(i).getLatitude(), listLocation.get(i).getLongitude());
                this.ggMap.addMarker(new MarkerOptions().position(mapVietNam).title(bienBao.getMaBienBao()));
                float zoomLevel = 15.0f;
                this.ggMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mapVietNam, zoomLevel));
            }
        }else{
            LatLng mapVietNam = new LatLng( location.getLatitude(), location.getLongitude());
            this.ggMap.addMarker(new MarkerOptions().position(mapVietNam).title("My Location"));
            float zoomLevel = 15.0f;
            this.ggMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mapVietNam, zoomLevel));
        }
    }

}