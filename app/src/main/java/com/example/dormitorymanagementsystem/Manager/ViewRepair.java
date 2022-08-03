package com.example.dormitorymanagementsystem.Manager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.dormitorymanagementsystem.Adapter.AdapterTabLayout;
import com.example.dormitorymanagementsystem.Fragment.HistoryRepairFragment;
import com.example.dormitorymanagementsystem.Fragment.NewRepairFragment;
import com.example.dormitorymanagementsystem.Login;
import com.example.dormitorymanagementsystem.R;
import com.example.dormitorymanagementsystem.Repairman.RepairSuccessful;
import com.example.dormitorymanagementsystem.Repairman.RepairWorkFragment;
import com.google.android.material.tabs.TabLayout;

public class ViewRepair extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_repair);

        //Context mContext = getApplication();
        String typeUser = Login.getGbTypeUser();

        TabLayout tabLayout = findViewById(R.id.tabRepair);
        ViewPager viewPager = findViewById(R.id.vpRepair);
        tabLayout.setupWithViewPager(viewPager);

        AdapterTabLayout adapterTabLayout = new AdapterTabLayout(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);

        if (typeUser.equals("Admin")){
            adapterTabLayout.addFragment(new NewRepairFragment(),"รายการใหม่");
            adapterTabLayout.addFragment(new RepairWorkFragment(),"งานที่มอบหมาย");
            adapterTabLayout.addFragment(new RepairSuccessful(),"งานที่สำเร็จ");
            adapterTabLayout.addFragment(new HistoryRepairFragment(),"ประวัติการซ่อม");
        }else {
            adapterTabLayout.addFragment(new RepairWorkFragment(),"งานที่มอบหมาย");
            adapterTabLayout.addFragment(new RepairSuccessful(),"งานที่สำเร็จ");
        }
        viewPager.setAdapter(adapterTabLayout);

        ImageView arrow_back = findViewById(R.id.ic_arrow_back);
        arrow_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}