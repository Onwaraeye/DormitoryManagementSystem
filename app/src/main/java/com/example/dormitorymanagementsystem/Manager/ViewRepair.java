package com.example.dormitorymanagementsystem.Manager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.dormitorymanagementsystem.Adapter.AdapterTabLayout;
import com.example.dormitorymanagementsystem.Fragment.HistoryRepairFragment;
import com.example.dormitorymanagementsystem.Fragment.NewRepairFragment;
import com.example.dormitorymanagementsystem.Login;
import com.example.dormitorymanagementsystem.R;
import com.example.dormitorymanagementsystem.Repair;
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


        AdapterTabLayout adapterTabLayout = new AdapterTabLayout(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);

        if (typeUser.equals("Admin")){
            adapterTabLayout.addFragment(new NewRepairFragment(),"รายการใหม่");
            adapterTabLayout.addFragment(new RepairWorkFragment(),"งานที่มอบหมาย");
            adapterTabLayout.addFragment(new RepairSuccessful(),"ตรวจสอบงาน");
            adapterTabLayout.addFragment(new HistoryRepairFragment(),"ประวัติการซ่อม");
        }else if (typeUser.equals("Repairman")){
            adapterTabLayout.addFragment(new RepairWorkFragment(),"งานที่ได้รับการแจ้งซ่อม'");
            adapterTabLayout.addFragment(new HistoryRepairFragment(),"ประวัติการซ่อม");
        }else {
            adapterTabLayout.addFragment(new NewRepairFragment(),"ปัจจุบัน");
            adapterTabLayout.addFragment(new HistoryRepairFragment(),"ประวัติ");
            Button button = findViewById(R.id.button);
            button.setVisibility(View.VISIBLE);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), Repair.class);
                    startActivity(intent);
                }
            });
        }
        tabLayout.setupWithViewPager(viewPager);
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