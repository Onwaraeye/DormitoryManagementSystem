package com.example.dormitorymanagementsystem.ChatNew;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.dormitorymanagementsystem.Adapter.AdapterTabLayout;
import com.example.dormitorymanagementsystem.R;
import com.google.android.material.tabs.TabLayout;

public class ContactActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        ImageView arrow_back = findViewById(R.id.ic_arrow_back);
        arrow_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        TabLayout tabLayout = findViewById(R.id.tabChat);
        ViewPager viewPager = findViewById(R.id.vpChat);
        tabLayout.setupWithViewPager(viewPager);

        AdapterTabLayout adapterTabLayout = new AdapterTabLayout(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        adapterTabLayout.addFragment(new UsersFragment(),"ผู้ใช้งาน");
        adapterTabLayout.addFragment(new UsersFragment(),"แชท");
        viewPager.setAdapter(adapterTabLayout);



    }
}