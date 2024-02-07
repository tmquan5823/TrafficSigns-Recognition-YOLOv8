package com.example.navigationbottom.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.navigationbottom.R;
import com.example.navigationbottom.activity.LoginActivity;
import com.example.navigationbottom.adaper.UserDataSingleton;
import com.example.navigationbottom.model.User;
import com.google.android.material.imageview.ShapeableImageView;
import com.squareup.picasso.Picasso;

public class SettingFragment extends Fragment {

    private View mView;

    private  Dialog dialog;
    private TextView tvtenUser, tvdienthoai, tvemail, tvdiachi;
    private ShapeableImageView siAnhavata;
    private AppCompatButton appCompatButtonLogout;
    private LinearLayout linearLayoutBtnSetting, linearLayoutBtnDoiMatKhau;;
    private User user_nhan, updatedUser;

    public SettingFragment() {

    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user_nhan = UserDataSingleton.getInstance().getUser();

        if (getArguments() != null) {

        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_setting, container, false);

        initUI();

        return mView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Kiểm tra xem có dữ liệu từ bundle hay không
        Bundle bundle = getArguments();
        if (bundle != null) {
            // Nhận dữ liệu từ bundle
            updatedUser = (User) bundle.getSerializable("updated_user_data");
            if (updatedUser != null) {
                // Hiển thị dữ liệu trong giao diện người dùng
                receiveDataFromFragmentUserSetting(updatedUser);
            }

        }


            linearLayoutBtnSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SettingUserFragment settingUserFragment = new SettingUserFragment();

                // Tạo bundle để đính kèm dữ liệu
                Bundle bundle = new Bundle();
                bundle.putSerializable("user_data_setting", userSendDataToFragmentUserSetting());
                settingUserFragment.setArguments(bundle);
                // Mở SettingUserFragment
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.content_frame_setting, settingUserFragment)
                        .addToBackStack(null)
                        .commit();

            }
        });

        linearLayoutBtnDoiMatKhau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCustomAlertDialogDoiMatKhau(Gravity.CENTER);
            }
        });

        if(user_nhan != null){
            tvtenUser.setText(user_nhan.getName().toString());
            tvdienthoai.setText(user_nhan.getPhone().toString());
            tvdiachi.setText(user_nhan.getAddress().toString());
            tvemail.setText(user_nhan.getEmail().toString());
            if(user_nhan.getAvatar().getType().equals("Buffer") && user_nhan.getAvatar().getData()!=null){
                Bitmap bitmap = BitmapFactory.decodeByteArray(user_nhan.getAvatar().getData(), 0, user_nhan.getAvatar().getData().length);
                Glide.with(SettingFragment.this)
                        .asBitmap()
                        .load(bitmap)
                        .into(siAnhavata);
            }
            UserDataSingleton.getInstance().setUser(null);
        }


        appCompatButtonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(requireActivity(), LoginActivity.class));
                requireActivity().finish();
            }
        });


    }

    private void showCustomAlertDialogDoiMatKhau(int gravity) {
        dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog_layout_doimk);
        Window window = dialog.getWindow();
        if(window == null){
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = gravity;
        window.setAttributes(windowAttributes);

        //  cai dat thuoc tinh o day

        EditText edtMatKhauCu = dialog.findViewById(R.id.edt_matkhaucu_doimatkhau_dialog_doimk);
        EditText edtMatKhauMoi = dialog.findViewById(R.id.edt_matkhaumoi_doimatkhau_dialog_doimk);
        AppCompatButton btnDoiMatKhau = dialog.findViewById(R.id.btn_dialog_xacnhan);

        btnDoiMatKhau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // khi nhan thi se lay du lieu do textView để thay đổi mat khau

            }
        });

        dialog.show();

    }

    private User userSendDataToFragmentUserSetting() {
        String strName = tvtenUser.getText().toString().trim();
        String strPhone = tvdienthoai.getText().toString().trim();
        String strEmail = tvemail.getText().toString().trim();
        String strAddress = tvdiachi.getText().toString().trim();
        if(user_nhan == null){
            return new User(updatedUser.getId(), strName, strPhone, strEmail, strAddress, updatedUser.getAvatar());
        }
        return new User(user_nhan.getId(), strName, strPhone, strEmail, strAddress, user_nhan.getAvatar());
    }


    private void initUI() {
        appCompatButtonLogout = mView.findViewById(R.id.btn_logout);

        tvtenUser = mView.findViewById(R.id.tv_profile_tenNguoi);
        tvdienthoai = mView.findViewById(R.id.tv_sodienthoai_fragmentsetting);
        tvemail = mView.findViewById(R.id.tv_email_fragmentsetting);
        tvdiachi = mView.findViewById(R.id.tv_diachi_fragmentsetting);
        siAnhavata = mView.findViewById(R.id.si_anh_avata_fragment_setting);
        linearLayoutBtnSetting = mView.findViewById(R.id.linearLayout_caidat_fragment_setting);
        linearLayoutBtnDoiMatKhau = mView.findViewById(R.id.linearLayout_doimatkhau_fragment_setting);

        if(user_nhan != null){
            tvtenUser.setText(user_nhan.getName().toString());
            tvdienthoai.setText(user_nhan.getPhone().toString());
            tvdiachi.setText(user_nhan.getAddress().toString());
            tvemail.setText(user_nhan.getEmail().toString());
            if(user_nhan.getAvatar().getType().equals("Buffer") && user_nhan.getAvatar().getData()!=null){
                Bitmap bitmap = BitmapFactory.decodeByteArray(user_nhan.getAvatar().getData(), 0, user_nhan.getAvatar().getData().length);
                Glide.with(SettingFragment.this)
                        .asBitmap()
                        .load(bitmap)
                        .into(siAnhavata);
            }
        }

        appCompatButtonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), LoginActivity.class));
                getActivity().finish();
            }
        });
    }

    public void receiveDataFromFragmentUserSetting(User updatedUser){
        tvtenUser.setText(updatedUser.getName());
        tvdienthoai.setText(updatedUser.getPhone());
        tvemail.setText(updatedUser.getEmail());
        tvdiachi.setText(updatedUser.getAddress());
        if(updatedUser.getAvatar().getType().equals("Buffer") && updatedUser.getAvatar().getData()!=null){
            Bitmap bitmap = BitmapFactory.decodeByteArray(updatedUser.getAvatar().getData(), 0, updatedUser.getAvatar().getData().length);
            Glide.with(SettingFragment.this)
                    .asBitmap()
                    .load(bitmap)
                    .into(siAnhavata);
        }
    }
}
