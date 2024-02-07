package com.example.navigationbottom.adaper;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.navigationbottom.fragment.FavoriteFragment;
import com.example.navigationbottom.fragment.HomeFragment;
import com.example.navigationbottom.fragment.ListBienBaoInHomeFragment;
import com.example.navigationbottom.fragment.SettingFragment;
import com.example.navigationbottom.fragment.SettingMainFragment;

public class MyViewPagerAdapter extends FragmentStateAdapter {

    public MyViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {

        switch (position){
            case 0:
                return new HomeFragment();
            case 1:
                return new FavoriteFragment();
            case 2:
                return new SettingMainFragment();
            default:
                return new HomeFragment();
        }

    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
