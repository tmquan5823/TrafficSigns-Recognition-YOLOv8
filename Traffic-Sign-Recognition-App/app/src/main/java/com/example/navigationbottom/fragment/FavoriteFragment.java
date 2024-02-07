package com.example.navigationbottom.fragment;

import static kotlin.io.ByteStreamsKt.readBytes;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.navigationbottom.R;
import com.example.navigationbottom.activity.LoginActivity;
import com.example.navigationbottom.activity.SignUpActivity;
import com.example.navigationbottom.adaper.AdapterBienBaoFavorite;
import com.example.navigationbottom.model.BienBao;
import com.example.navigationbottom.model.DetectionResponse;
import com.example.navigationbottom.model.GPSTracker;
import com.example.navigationbottom.model.Location;
import com.example.navigationbottom.model.LocationList;
import com.example.navigationbottom.viewmodel.BienBaoApiService;
import com.example.navigationbottom.viewmodel.UserApiService;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavoriteFragment extends Fragment {
    ShapeableImageView ivAnhCamera;
    RecyclerView recyclerList;
    FloatingActionButton btnCamera;
    FloatingActionButton btnThuVienAnh;
    private Uri imageUri;
    private static final int CAMERA_PERMISSION_CODE = 123;
    private static final int GALLERY_PERMISSION_CODE = 124;
    private static final int CAMERA_REQUEST_CODE = 125;


    private BienBaoApiService bienbaoApiService;

    private UserApiService userApiService;

    private String imageUrl_tempSave1;
    private List<List<String>> listBienBaoResponse;


    private AdapterBienBaoFavorite adapter;

    private ArrayList<BienBao> formattedList;
    private ProgressDialog progressDialog;

    private Location location;

    private ArrayList<Location> listLocation;

    private GPSTracker gps;


    public FavoriteFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favorite, container, false);


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ivAnhCamera = view.findViewById(R.id.iv_anhCamera);
        btnCamera = view.findViewById(R.id.btn_camera);
        btnThuVienAnh = view.findViewById(R.id.btn_thuvienanh);
        recyclerList = view.findViewById(R.id.rcv_fragment_favorte);
        recyclerList.setLayoutManager(new LinearLayoutManager(getContext()));
        listBienBaoResponse = new ArrayList<>();
        listLocation = new ArrayList<>();
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Recognizing...");


        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    chupTuCamera();

                } else {
                    requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, CAMERA_PERMISSION_CODE);
                }
            }
        });

        btnThuVienAnh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    moThuVien();

                } else {
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, GALLERY_PERMISSION_CODE);
                }
            }
        });


    }

    private void chupTuCamera() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From Camera");
        imageUri = requireContext().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE);
    }

    private void moThuVien() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, GALLERY_PERMISSION_CODE);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Context context = getContext();
        if (requestCode == GALLERY_PERMISSION_CODE && resultCode == Activity.RESULT_OK && data != null) {
            location = new Location();
            Uri selectedImage = data.getData();
            ivAnhCamera.setImageURI(selectedImage);
            imageUrl_tempSave1 = convertImageToBase64(context, selectedImage);
            postImage();
        } else if (requestCode == CAMERA_REQUEST_CODE) {
            location = new Location();
            gps = new GPSTracker(getContext());
            if (resultCode == Activity.RESULT_OK) {
                ivAnhCamera.setImageURI(imageUri);
                imageUrl_tempSave1 = convertImageToBase64(context, imageUri);
                if (gps.canGetLocation()) {
                    location.setLatitude(gps.getLatitude());
                    location.setLongitude(gps.getLongitude());
                    Log.d("Hello", "Your Location is - \nLat: " + location.getLatitude() + "\nLong: " + location.getLongitude());
                }
                postImage();
            }

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (requestCode == CAMERA_PERMISSION_CODE) {
                chupTuCamera();
            }
        } else if (requestCode == GALLERY_PERMISSION_CODE) {
            moThuVien();
        } else {
            Toast.makeText(requireContext(), "Access denied.", Toast.LENGTH_SHORT).show();
        }
    }


    public static String convertImageToBase64(Context context, Uri imageUri) {
        try {
            ContentResolver contentResolver = context.getContentResolver();
            InputStream inputStream = contentResolver.openInputStream(imageUri);

            if (inputStream != null) {
                byte[] bytes = readBytes(inputStream);
                inputStream.close();

                if (bytes != null) {
                    return Base64.encodeToString(bytes, Base64.DEFAULT);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static byte[] readBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len;

        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }

        return byteBuffer.toByteArray();
    }


    private void postImage() {

        progressDialog.show();
        bienbaoApiService = new BienBaoApiService();
        bienbaoApiService.postImage(imageUrl_tempSave1)
                .enqueue(new Callback<DetectionResponse>() {
                    public void onResponse(Call<DetectionResponse> call, Response<DetectionResponse> response) {
                        if (response.isSuccessful()) {

                            DetectionResponse detectionResponse = response.body();
                            if (detectionResponse != null) {
                                listBienBaoResponse = detectionResponse.getDetect();
                                if (listBienBaoResponse.size() != 0) {
                                    formattedList = new ArrayList<>();
                                    adapter = new AdapterBienBaoFavorite(getContext(), formattedList);
                                    recyclerList.setAdapter(adapter);
                                    for (List<String> bienBao : listBienBaoResponse) {
                                        if (location.getLongitude() != 0.0 && location.getLatitude() != 0.0) {
                                            listLocation = new ArrayList<>();
                                            String code = bienBao.get(0);
                                            String content = bienBao.get(1);
                                            String url = bienBao.get(2);
                                            Location adjustedLocation = new Location();
                                            double randomAngle = Math.random() * 2 * Math.PI;
                                            double offsetLatitude = 4 * Math.cos(randomAngle) / 111111.0;
                                            double offsetLongitude = 4 * Math.sin(randomAngle) / (111111.0 * Math.cos(location.getLatitude()));
                                            adjustedLocation.setLatitude(location.getLatitude() + offsetLatitude);
                                            adjustedLocation.setLongitude(location.getLongitude() + offsetLongitude);
                                            adjustedLocation.setCode(code);
                                            listLocation.add(adjustedLocation);
                                            pustLocationOnServer()
                                                    .thenAccept(result -> {
                                                        progressDialog.dismiss();
                                                        BienBao bienBao1 = new BienBao(code, content, url);
                                                        formattedList.add(bienBao1);
                                                        adapter.notifyDataSetChanged();
                                                    })
                                                    .exceptionally(ex -> {
                                                        return null;
                                                    });
                                        } else {
                                            String code = bienBao.get(0);
                                            String content = bienBao.get(1);
                                            String url = bienBao.get(2);
                                            progressDialog.dismiss();
                                            BienBao bienBao1 = new BienBao(code, content, url);
                                            formattedList.add(bienBao1);
                                            adapter.notifyDataSetChanged();

                                        }

                                    }
                                    Toast.makeText(getContext(), "Recognition successful.", Toast.LENGTH_SHORT).show();
                                } else {
                                    progressDialog.dismiss();
                                    formattedList = new ArrayList<>();
                                    adapter = new AdapterBienBaoFavorite(getContext(), formattedList);
                                    recyclerList.setAdapter(adapter);
                                    formattedList = new ArrayList<>();
                                    adapter.notifyDataSetChanged();
                                    Toast.makeText(getContext(), "No detected objects for recognition.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        } else {
                            progressDialog.dismiss();
                            Log.e("Hello", "Response failed with code: " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<DetectionResponse> call, Throwable t) {
                        progressDialog.dismiss();
                        String errorMessage = t.getMessage();
                        Toast.makeText(getContext(), "Request failed: " + errorMessage, Toast.LENGTH_SHORT).show();
                        Log.e("Hello", String.valueOf("Request failed: " + errorMessage));
                    }
                });
    }

    private CompletableFuture<Void> pustLocationOnServer() {
        CompletableFuture<Void> future = new CompletableFuture<>();
        LocationList locationList = new LocationList();
        locationList.setListPosition(listLocation);
        userApiService = new UserApiService();
        userApiService.postLocation(locationList).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if(response.isSuccessful()){
                    future.complete(null);
                }else{
                    Log.e("Hello", "Response Post position failed with code: " + response.code());
                    future.completeExceptionally(new Throwable("Post location failed"));
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                String errorMessage = t.getMessage();
                Toast.makeText(getContext(), "Request failed: " + errorMessage, Toast.LENGTH_SHORT).show();
                Log.e("Hello", String.valueOf("Request failed: " + errorMessage));
                future.completeExceptionally(t);
            }
        });
        return future;
    }

}
