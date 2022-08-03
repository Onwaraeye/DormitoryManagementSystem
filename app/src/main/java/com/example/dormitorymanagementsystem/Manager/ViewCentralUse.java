package com.example.dormitorymanagementsystem.Manager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.dormitorymanagementsystem.Adapter.AdapterBookingDetails;
import com.example.dormitorymanagementsystem.Model.CentralModel;
import com.example.dormitorymanagementsystem.R;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ViewCentralUse extends AppCompatActivity {

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRefCentral = database.getReference("Central");

    private DatabaseReference myRefUser = database.getReference("Users");
    private List<CentralModel> listCentral = new ArrayList<>();
    private Context mContext;
    private AdapterBookingDetails adapterBookingDetails;
    private RecyclerView recyclerView;
    private String monthThai = "";
    private List<CentralModel> viewCentralModelListTutoringRoom = new ArrayList<CentralModel>();
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private CalendarView calendarView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_central_use);

        String day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH) + "";
        String month = Calendar.getInstance().get(Calendar.MONTH) + "";
        String year = Calendar.getInstance().get(Calendar.YEAR) + "";
        String date = day + " " + getMonth(Integer.parseInt(month)) + " " + year;
        mContext = getApplication();

        tabLayout = findViewById(R.id.tabCentral);
        viewPager = findViewById(R.id.vpCentral);
        calendarView = findViewById(R.id.calendarView1);
        LinearLayout selectDate = findViewById(R.id.selectDate);
        TextView arrow = findViewById(R.id.arrow);

        //openViewCentral(year, month, day, date);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                String yearDate = year + "";
                String monthDate = month + "";
                String dayDate = dayOfMonth + "";
                String date = dayDate + " " + getMonth(Integer.parseInt(monthDate)) + " " + yearDate;

                Button btConfirm = findViewById(R.id.btConfirm);
                btConfirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getApplicationContext(),ViewCentralUse2.class);
                        intent.putExtra("day",dayDate);
                        intent.putExtra("month",monthDate);
                        intent.putExtra("year",yearDate);
                        intent.putExtra("date",date);
                        startActivity(intent);
                    }
                });
            }
        });

        Button btConfirm = findViewById(R.id.btConfirm);
        btConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),ViewCentralUse2.class);
                intent.putExtra("day",day);
                intent.putExtra("month",month);
                intent.putExtra("year",year);
                intent.putExtra("date",date);
                startActivity(intent);
            }
        });

        ImageView arrow_back = findViewById(R.id.ic_arrow_back);
        arrow_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        /*AdapterParcel adapterParcel = new AdapterParcel(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        adapterParcel.addFragment(new ViewFitnessFragment(), "ที่ออกกำลังกาย");
        adapterParcel.addFragment(new ViewTutoringFragment(), "ห้องติว");
        viewPager.setAdapter(adapterParcel);*/

    }

    public String getMonth(int month) {
        switch (month) {
            case 0:
                monthThai = "มกราคม";
                break;
            case 1:
                monthThai = "กุมภาพันธ์";
                break;
            case 2:
                monthThai = "มีนาคม";
                break;
            case 3:
                monthThai = "เมษายน";
                break;
            case 4:
                monthThai = "พฤษภาคม";
                break;
            case 5:
                monthThai = "มิถุนายน";
                break;
            case 6:
                monthThai = "กรกฎาคม";
                break;
            case 7:
                monthThai = "สิงหาคม";
                break;
            case 8:
                monthThai = "กันยายน";
                break;
            case 9:
                monthThai = "ตุลาคม";
                break;
            case 10:
                monthThai = "พฤศจิกายน";
                break;
            case 11:
                monthThai = "ธันวาคม";
                break;
        }
        return monthThai;
    }

    public String convertTime(String time) {
        String cvTime = "";
        switch (time) {
            case "0":
                cvTime = "08:00 - 09:00 น.";
                break;
            case "1":
                cvTime = "09:00 - 10:00 น.";
                break;
            case "2":
                cvTime = "10:00 - 11:00 น.";
                break;
            case "3":
                cvTime = "11:00 - 12:00 น.";
                break;
            case "4":
                cvTime = "12:00 - 13:00 น.";
                break;
            case "5":
                cvTime = "13:00 - 14:00 น.";
                break;
            case "6":
                cvTime = "14:00 - 15:00 น.";
                break;
            case "7":
                cvTime = "15:00 - 16:00 น.";
                break;
            case "8":
                cvTime = "16:00 - 17:00 น.";
                break;
            case "9":
                cvTime = "17:00 - 18:00 น.";
                break;
            case "10":
                cvTime = "18:00 - 19:00 น.";
                break;
            case "11":
                cvTime = "19:00 - 20:00 น.";
                break;
        }
        return cvTime;
    }
}