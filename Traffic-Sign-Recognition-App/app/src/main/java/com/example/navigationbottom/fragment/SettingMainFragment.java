package com.example.navigationbottom.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.navigationbottom.R;


public class SettingMainFragment extends Fragment{

    private View mView;
    public SettingMainFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_settingmain, container, false);

            SettingFragment settingFragment = new SettingFragment();
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.content_frame_setting, settingFragment)
                    .commit();

        return mView;
    }

}
