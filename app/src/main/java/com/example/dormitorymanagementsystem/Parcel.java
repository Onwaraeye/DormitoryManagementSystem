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

    private ImageView arrow_back;
    private Context mContext;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parcel);

        mContext = getApplication();

        arrow_back = findViewById(R.id.ic_arrow_back);
        arrow_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ( getFragmentManager().getBackStackEntryCount() > 0)
                {
                    getFragmentManager().popBackStack();
                    return;
                }
                onBackPressed();
            }
        });

        tabLayout = findViewById(R.id.tabParcel);
        viewPager = findViewById(R.id.vpParcel);

        tabLayout.setupWithViewPager(viewPager);

        AdapterParcel adapterParcel = new AdapterParcel(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        adapterParcel.addFragment(new NewParcelFragment(),"พัสดุใหม่");
        adapterParcel.addFragment(new HistoryPacelFragment(),"ประวัติพัสดุ");
        viewPager.setAdapter(adapterParcel);
    }
}