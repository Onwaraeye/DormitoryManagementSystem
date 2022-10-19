package com.example.dormitorymanagementsystem.Manager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.dormitorymanagementsystem.Adapter.AdapteViewBill;
import com.example.dormitorymanagementsystem.Adapter.AdapterMeterRecord;
import com.example.dormitorymanagementsystem.Adapter.AdapterRoom;
import com.example.dormitorymanagementsystem.Adapter.AdapterTabLayout;
import com.example.dormitorymanagementsystem.Fragment.MeterElectricFragment;
import com.example.dormitorymanagementsystem.Fragment.MeterWaterFragment;
import com.example.dormitorymanagementsystem.Fragment.NewRepairFragment;
import com.example.dormitorymanagementsystem.Model.BillModel;
import com.example.dormitorymanagementsystem.Model.RoomModel;
import com.example.dormitorymanagementsystem.R;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kal.rackmonthpicker.MonthType;
import com.kal.rackmonthpicker.RackMonthPicker;
import com.kal.rackmonthpicker.listener.DateMonthDialogListener;
import com.kal.rackmonthpicker.listener.OnCancelMonthDialogListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MeterRecord extends AppCompatActivity {

    DatabaseReference myRefRoom = FirebaseDatabase.getInstance().getReference("Room");

    List<RoomModel> listRoom = new ArrayList<>();

    TextView txMonth;
    String monthThai;
    FrameLayout frameLayoutElectric,frameLayoutWater;
    ImageView btMoreElectric,btMoreWater;

    int monthCurrent = Calendar.getInstance().get(Calendar.MONTH)+1;
    int yearCurrent = Calendar.getInstance().get(Calendar.YEAR);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meter_record);

        Context mContext = getApplication();

        ImageButton btMonth = findViewById(R.id.btMonth);

        txMonth = findViewById(R.id.txMonth);
        frameLayoutElectric = findViewById(R.id.frameLayoutElectric);
        btMoreElectric = findViewById(R.id.btMoreElectric);
        frameLayoutWater = findViewById(R.id.frameLayoutWater);
        btMoreWater = findViewById(R.id.btMoreWater);

        setVisibility();

        int ye = yearCurrent+543;
        String date = getMonth(monthCurrent) + " " + ye;
        txMonth.setText(date);

        btMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMonthYear();
            }
        });

        openFragment(String.valueOf(monthCurrent),String.valueOf(yearCurrent),date);

        ImageView arrow_back = findViewById(R.id.ic_arrow_back);
        arrow_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void showMonthYear(){
        final RackMonthPicker rackMonthPicker = new RackMonthPicker(this)
                .setMonthType(MonthType.TEXT)
                .setPositiveButton(new DateMonthDialogListener() {
                    @Override
                    public void onDateMonth(int month, int startDate, int endDate, int year, String monthLabel) {
                        int ye = year+543;
                        String date = getMonth(month) + " " + ye;
                        String mo = String.valueOf(month);
                        //adapterTabLayout.removeFragment();
                        openFragment(mo,year+"",date);
                        /*adapterTabLayout.addFragmentWithBundle(new MeterElectricFragment(),mo,year+"","จดมิเตอร์ค่าไฟ");
                        adapterTabLayout.addFragmentWithBundle(new MeterWaterFragment(),mo,year+"","จดมิเตอร์ค่าน้ำ");*/
                        //adapterTabLayout.notifyDataSetChanged();

                        //tabLayout.setupWithViewPager(viewPager);
                        //viewPager.setAdapter(adapterTabLayout);
                        //viewPager.getAdapter().notifyDataSetChanged();

                        txMonth.setText(date);
                    }
                })
                .setNegativeButton(new OnCancelMonthDialogListener() {
                    @Override
                    public void onCancel(androidx.appcompat.app.AlertDialog dialog) {
                        dialog.dismiss();
                    }
                });
                rackMonthPicker.show();
    }

    private void openFragment(String month, String year,String date) {
        /*AdapterTabLayout adapterTabLayout = new AdapterTabLayout(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        adapterTabLayout.addFragmentWithBundle(new MeterElectricFragment(),month,year,"จดมิเตอร์ค่าไฟ");
        adapterTabLayout.addFragmentWithBundle(new MeterWaterFragment(),month,year,"จดมิเตอร์ค่าน้ำ");
        adapterTabLayout.notifyDataSetChanged();
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setAdapter(adapterTabLayout);
        viewPager.getAdapter().notifyDataSetChanged();*/

        Bundle bundle = new Bundle();
        bundle.putString("monthCurrent",month);
        bundle.putString("yearCurrent",year);
        bundle.putString("date",date);
        MeterElectricFragment meterElectricFragment = new MeterElectricFragment();
        meterElectricFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayoutElectric, meterElectricFragment).commit();

        MeterWaterFragment meterWaterFragment = new MeterWaterFragment();
        meterWaterFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayoutWater, meterWaterFragment).commit();
    }

    public String getMonth(int month) {
        switch (month) {
            case 1:
                monthThai = "มกราคม";
                break;
            case 2:
                monthThai = "กุมภาพันธ์";
                break;
            case 3:
                monthThai = "มีนาคม";
                break;
            case 4:
                monthThai = "เมษายน";
                break;
            case 5:
                monthThai = "พฤษภาคม";
                break;
            case 6:
                monthThai = "มิถุนายน";
                break;
            case 7:
                monthThai = "กรกฎาคม";
                break;
            case 8:
                monthThai = "สิงหาคม";
                break;
            case 9:
                monthThai = "กันยายน";
                break;
            case 10:
                monthThai = "ตุลาคม";
                break;
            case 11:
                monthThai = "พฤศจิกายน";
                break;
            case 12:
                monthThai = "ธันวาคม";
                break;
        }
        return monthThai;
    }

    public void setVisibility(){
        btMoreElectric.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (frameLayoutElectric.getVisibility() == View.VISIBLE){
                    frameLayoutElectric.setVisibility(View.GONE);
                }else {
                    frameLayoutElectric.setVisibility(View.VISIBLE);
                }
            }
        });
        btMoreWater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (frameLayoutWater.getVisibility() == View.VISIBLE){
                    frameLayoutWater.setVisibility(View.GONE);
                }else {
                    frameLayoutWater.setVisibility(View.VISIBLE);
                }
            }
        });
    }
}