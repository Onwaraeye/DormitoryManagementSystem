package com.example.dormitorymanagementsystem.Adapter;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class AdapterTabLayout extends FragmentPagerAdapter {

    private final ArrayList<Fragment> fragmentArrayList = new ArrayList<>();
    private final ArrayList<String> fragmentTitle = new ArrayList<>();

    public AdapterTabLayout(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragmentArrayList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentArrayList.size();
    }

   /* @Override
    public int getItemPosition(@NonNull @NotNull Object object) {
        return POSITION_NONE;
    }*/

    public void addFragment(Fragment fragment, String title){
        fragmentArrayList.add(fragment);
        fragmentTitle.add(title);
    }

    /*public void addFragmentWithBundle(Fragment fragment,String month,String year ,String title){
        Bundle bundle = new Bundle();
        bundle.putString("monthCurrent",month);
        bundle.putString("yearCurrent",year);
        fragment.setArguments(bundle);
        fragmentArrayList.add(fragment);
        fragmentTitle.add(title);
    }*/

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return fragmentTitle.get(position);
    }
}
