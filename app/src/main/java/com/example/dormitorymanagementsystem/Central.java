package com.example.dormitorymanagementsystem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.dormitorymanagementsystem.Adapter.AdapterTabLayout;
import com.example.dormitorymanagementsystem.Fragment.CentralFragment;
import com.example.dormitorymanagementsystem.Fragment.CentralReservationFragment;
import com.google.android.material.tabs.TabLayout;

public class Central extends AppCompatActivity {

    private Context mContext;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_central);

        mContext = getApplication();

        ImageView arrow_back = findViewById(R.id.ic_arrow_back);
        arrow_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        tabLayout = findViewById(R.id.tabCentral);
        viewPager = findViewById(R.id.vpCentral);

        /*viewPager.setCurrentItem(2);
        tabLayout.setupWithViewPager(viewPager);*/
        tabLayout.setupWithViewPager(viewPager);

        AdapterTabLayout adapterTabLayout = new AdapterTabLayout(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        adapterTabLayout.addFragment(new CentralFragment(),"ส่วนกลาง");
        adapterTabLayout.addFragment(new CentralReservationFragment(),"การจอง");
        viewPager.setAdapter(adapterTabLayout);
    }
}