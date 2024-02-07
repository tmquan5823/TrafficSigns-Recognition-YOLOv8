package com.example.navigationbottom.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.navigationbottom.R;
import com.example.navigationbottom.activity.LoginActivity;
import com.example.navigationbottom.activity.SignUpActivity;
import com.example.navigationbottom.model.User;
import com.example.navigationbottom.viewmodel.UserApiService;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.gson.JsonObject;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SettingUserFragment extends Fragment {

    private FloatingActionButton btnCamera;
    private ShapeableImageView siAvata;
    private EditText edtName, edtPhone, edtEmail, edtAddress;
    private AppCompatButton btnSave;
    private Uri imageUri;
    private  Dialog dialog;
    private static final int CAMERA_PERMISSION_CODE = 123;
    private static final int GALLERY_PERMISSION_CODE = 124;
    private static final int CAMERA_REQUEST_CODE = 125;
    private static final int GALLERY_REQUEST_CODE = 126;
    private View mView;

    private byte[] imageUrl_tempSave;
    private User user_ReceivedFromSettingFragment;
    private Uri uriImage;

    private UserApiService userApiService;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_setting_user, container, false);

        initUI();

        return mView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Lấy dữ liệu từ Bundle
        Bundle bundle = getArguments();
        if (bundle != null) {
            user_ReceivedFromSettingFragment = (User) bundle.getSerializable("user_data_setting");
            if (user_ReceivedFromSettingFragment != null) {
                // Gọi phương thức nhận dữ liệu từ SettingFragment
                receiveDataFromSettingFragment(user_ReceivedFromSettingFragment);
            }
        }

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    Save();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCustomAlertDialog(Gravity.CENTER);
            }
        });
    }



    private User userUpdateDataToFragmentSetting() {

        String strName = edtName.getText().toString().trim();
        String strPhone = edtPhone.getText().toString().trim();
        String strEmail = edtEmail.getText().toString().trim();
        String strAddress = edtAddress.getText().toString().trim();
        byte[] strUrl = imageUrl_tempSave;
        if(imageUrl_tempSave == null){
            return new User(user_ReceivedFromSettingFragment.getId(), strName, strPhone, strEmail, strAddress, user_ReceivedFromSettingFragment.getAvatar());
        }
        return new User(user_ReceivedFromSettingFragment.getId(), strName, strPhone, strEmail, strAddress, new User.AvatarData("Buffer", strUrl));
    }
    public void receiveDataFromSettingFragment(User user) {
        edtName.setText(user.getName());
        edtPhone.setText(user.getPhone());
        edtEmail.setText(user.getEmail());
        edtAddress.setText(user.getAddress());
        imageUrl_tempSave = user.getAvatar().getData();
        if(user.getAvatar().getType().equals("Buffer") && user.getAvatar().getData()!=null){
            Bitmap bitmap = BitmapFactory.decodeByteArray(user.getAvatar().getData(), 0, user.getAvatar().getData().length);
            Glide.with(SettingUserFragment.this)
                    .asBitmap()
                    .load(bitmap)
                    .into(siAvata);
        }
    }

    private void initUI() {
        btnCamera = mView.findViewById(R.id.btn_camera_fragmentSettingUser);
        btnSave = mView.findViewById(R.id.btn_save_fragmentSettingUser);
        siAvata = mView.findViewById(R.id.si_anh_avata_fragment_setting_user);
        edtName = mView.findViewById(R.id.edt_hovaten_fragmentsettingUser);
        edtPhone = mView.findViewById(R.id.edt_sodienthoai_fragmentsettingUser);
        edtEmail = mView.findViewById(R.id.edt_email_fragmentsettingUser);
        edtAddress = mView.findViewById(R.id.edt_diachi_fragmentsettingUser);

    }

    private void showCustomAlertDialog(int gravity) {

        dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog_layout);
        Window window = dialog.getWindow();
        if(window == null){
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = gravity;
        window.setAttributes(windowAttributes);


        AppCompatButton appCompatButtonCamera = dialog.findViewById(R.id.btn_diaCamera);
        AppCompatButton appCompatButtonGallery = dialog.findViewById(R.id.btn_diaGallery);

        appCompatButtonCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Chụp từ camera
                if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    dialog.dismiss();
                    chupTuCamera();
                } else {
                    requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, CAMERA_PERMISSION_CODE);
                }
            }
        });

        appCompatButtonGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Chọn từ thư viện ảnh
                if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    moThuVien();
                } else {
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, GALLERY_PERMISSION_CODE);
                }
            }
        });

        dialog.show();
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
        startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE);
        dialog.dismiss();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Context context = getContext();

        if (requestCode == GALLERY_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            siAvata.setImageURI(selectedImage);
            uriImage = selectedImage;
            imageUrl_tempSave = convertUriToByteArray(context, selectedImage);

        } else if (requestCode == CAMERA_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            siAvata.setImageURI(imageUri);
            uriImage = imageUri;
            imageUrl_tempSave = convertUriToByteArray(context, imageUri);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (requestCode == CAMERA_PERMISSION_CODE) {
                chupTuCamera();
            }
        }
        else if (requestCode == GALLERY_PERMISSION_CODE) {
            moThuVien();
        }
        else {
            Toast.makeText(requireContext(), "Access denied.", Toast.LENGTH_SHORT).show();
        }
    }



    public static byte[] convertUriToByteArray(Context context, Uri uri) {
        try {
            ContentResolver contentResolver = context.getContentResolver();
            InputStream inputStream = contentResolver.openInputStream(uri);

            if (inputStream != null) {
                // Đọc dữ liệu từ InputStream thành mảng byte
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int len;

                while ((len = inputStream.read(buffer)) != -1) {
                    byteArrayOutputStream.write(buffer, 0, len);
                }

                inputStream.close();
                byteArrayOutputStream.close();

                return byteArrayOutputStream.toByteArray();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void Save() throws IOException {


        String name = edtName.getText().toString();
        String phone = edtPhone.getText().toString();
        String email = edtEmail.getText().toString();
        String address = edtAddress.getText().toString();
        Bitmap bitmap = BitmapFactory.decodeByteArray(imageUrl_tempSave, 0, imageUrl_tempSave.length);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 0, byteArrayOutputStream);
        byte[] compressedBytes = byteArrayOutputStream.toByteArray();


        User.AvatarData avatar = new User.AvatarData("Buffer", compressedBytes);
        User user = new User(user_ReceivedFromSettingFragment.getId(), name, phone, email, address, avatar);

            Log.e("Hello", avatar.toString());

            if(TextUtils.isEmpty(name.trim()) || TextUtils.isEmpty(phone.trim()) || TextUtils.isEmpty(email.trim()) || TextUtils.isEmpty(address.trim())){
                Toast.makeText(getContext(), "Please enter complete information!", Toast.LENGTH_SHORT).show();
            }else if(isValidPhoneNumber(phone.trim()) == false){
                Toast.makeText(getContext(), "Invalid phone number!", Toast.LENGTH_SHORT).show();
            }else if (isValidEmail(email.trim()) == false) {
                Toast.makeText(getContext(), "Invalid email!", Toast.LENGTH_SHORT).show();
            } else {
                userApiService = new UserApiService();
                userApiService.postUserUpdate(user).enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        if (response.isSuccessful()) {
                            JsonObject jsonObject = response.body();
                            if(jsonObject!=null){
                                String message = jsonObject.get("message").getAsString();
                                Log.e("Hello", String.valueOf(message.toString()));
                                if (message.toString() != null && message.toString().equals("Update User Success!")) {
                                    // Lấy dữ liệu từ giao diện người dùng
                                    Bundle bundle = new Bundle();
                                    bundle.putSerializable("updated_user_data", userUpdateDataToFragmentSetting());

                                    // Tạo instance của SettingFragment
                                    SettingFragment settingFragment = new SettingFragment();

                                    // Đặt bundle vào fragment
                                    settingFragment.setArguments(bundle);

                                    getParentFragmentManager().beginTransaction()
                                            .replace(R.id.content_frame_setting, settingFragment)
                                            .addToBackStack(null)
                                            .commit();

                                }
                            }
                            else {
                                Toast.makeText(getContext(), "Update user fails", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else{
                            Log.e("Hello",  "Response failed with code: " + response.code());
                        }


                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        String errorMessage = t.getMessage();
                        Toast.makeText(getContext(), "Request failed: " + errorMessage, Toast.LENGTH_SHORT).show();
                        Log.e("Hello", String.valueOf("Request failed: " + errorMessage));
                    }
                });
            }
        }




    private boolean isValidPhoneNumber(String phoneNumber) {
        // Định dạng số điện thoại: ^0\d{9}$
        // ^0 : Bắt đầu bằng số 0
        // \d{9} : Theo sau là chính xác 9 chữ số (\d là đại diện cho số)
        // $ : Kết thúc chuỗi

        String regex = "^0\\d{9}$";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(phoneNumber);

        return matcher.matches();
    }

    private boolean isValidEmail(String email) {

        String regex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);

        return matcher.matches();
    }


}