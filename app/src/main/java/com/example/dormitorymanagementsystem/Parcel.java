package com.example.dormitorymanagementsystem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.dormitorymanagementsystem.Adapter.AdapterParcel;
import com.example.dormitorymanagementsystem.Fragment.HistoryPacelFragment;
import com.example.dormitorymanagementsystem.Fragment.NewParcelFragment;
import com.google.android.material.tabs.TabLayout;

public class Parcel extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parcel);

        //Context mContext = getApplication();

        ImageView arrow_back = findViewById(R.id.ic_arrow_back);
        arrow_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        TabLayout tabLayout = findViewById(R.id.tabParcel);
        ViewPager viewPager = findViewById(R.id.vpParcel);
        tabLayout.setupWithViewPager(viewPager);

        AdapterParcel adapterParcel = new AdapterParcel(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        adapterParcel.addFragment(new NewParcelFragment(),"พัสดุใหม่");
        adapterParcel.addFragment(new HistoryPacelFragment(),"ประวัติพัสดุ");
        viewPager.setAdapter(adapterParcel);
    }
}