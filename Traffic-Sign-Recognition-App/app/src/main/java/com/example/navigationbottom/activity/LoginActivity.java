package com.example.navigationbottom.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.example.navigationbottom.R;
import com.example.navigationbottom.adaper.UserDataSingleton;
import com.example.navigationbottom.fragment.SettingFragment;
import com.example.navigationbottom.model.LoginResponse;
import com.example.navigationbottom.model.User;
import com.example.navigationbottom.viewmodel.UserApiService;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private EditText edtTaiKhoan, edtMauKhau;
    private Button btnLogin;

    private TextView txtSignUp;
    private UserApiService userApiService;

    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnLogin = findViewById(R.id.btn_logout);
        txtSignUp = findViewById(R.id.textView2);

        progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setMessage("Login ....");
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickLogin();
            }
        });
        txtSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignUp();
            }
        });
    }

    private void SignUp() {
        startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
    }


    private void clickLogin() {

        progressDialog.show();
        edtTaiKhoan = findViewById(R.id.edt_taikhoan_login);
        edtMauKhau = findViewById(R.id.edt_matkhau_login);

        User user = new User();
        user.setUsername(edtTaiKhoan.getText().toString().trim());
        user.setPassword(edtMauKhau.getText().toString().trim());

        userApiService = new UserApiService();
        userApiService.postUserLogin(user).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                LoginResponse response1 = response.body();

                if(response1 != null){
                    progressDialog.dismiss();
                    if(response1.getUser()!=null){
                        Toast.makeText(LoginActivity.this, "Login success!!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        UserDataSingleton.getInstance().setUser(response1.getUser());
                    }else{
                        Toast.makeText(LoginActivity.this, "Username or Password invalid", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    progressDialog.dismiss();
                    Toast.makeText(LoginActivity.this, "Username or Password invalid", Toast.LENGTH_SHORT).show();
                }



            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                String errorMessage = t.getMessage();
                Toast.makeText(LoginActivity.this, "Request failed: " + errorMessage, Toast.LENGTH_SHORT).show();
                Log.e("Hello", String.valueOf("Request failed: " + errorMessage));
            }
        });

    }
}