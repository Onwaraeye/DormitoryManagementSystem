package com.example.dormitorymanagementsystem.Manager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.opengl.Visibility;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.dormitorymanagementsystem.Adapter.AdapterBookingDetails;
import com.example.dormitorymanagementsystem.Adapter.AdapterParcel;
import com.example.dormitorymanagementsystem.Fragment.CentralFragment;
import com.example.dormitorymanagementsystem.Fragment.CentralReservationFragment;
import com.example.dormitorymanagementsystem.Fragment.ViewFitnessFragment;
import com.example.dormitorymanagementsystem.Fragment.ViewTutoringFragment;
import com.example.dormitorymanagementsystem.Login;
import com.example.dormitorymanagementsystem.Model.CentralManagerModel;
import com.example.dormitorymanagementsystem.Model.CentralModel;
import com.example.dormitorymanagementsystem.Model.ManagerModel;
import com.example.dormitorymanagementsystem.Model.ViewCentralModel;
import com.example.dormitorymanagementsystem.R;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
                String date = dayDate + " " + monthDate + " " + yearDate;


                /*myRefCentral.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        myRefCentral.child("fitness").child(yearDate).child(monthDate).child(dayDate).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                                GenericTypeIndicator<List<String>> genericTypeIndicator = new GenericTypeIndicator<List<String>>() {
                                };
                                List<String> timefitness = new ArrayList<>();
                                List<String> listUserName = new ArrayList<>();
                                List<CentralManagerModel> centralManagerModelList = new ArrayList<>();
                                centralManagerModelList.clear();
                                // get List name user by key
                                for (DataSnapshot ds : snapshot.getChildren()) {
                                    myRefUser.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                            if (snapshot.hasChild(ds.getKey())) {
                                                String fName = snapshot.child(ds.getKey()).child("firstname").getValue(String.class);
                                                String lName = snapshot.child(ds.getKey()).child("lastname").getValue(String.class);
                                                listUserName.add(fName + " " + lName);
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull @NotNull DatabaseError error) {
                                        }
                                    });
                                    //get time fitness by Display Date form Firebase
                                    timefitness = ds.child("time").getValue(genericTypeIndicator);
                                    CentralManagerModel centralManagerModel = ds.getValue(CentralManagerModel.class);
                                    centralManagerModelList.add(centralManagerModel);
                                }
                                for (int i = 0; i < listUserName.size(); i++) {
                                    for (String time : timefitness) {
                                        CentralModel centralModel = new CentralModel("พื้นที่ออกกำลังกาย", listUserName.get(i), date, convertTime(time));

                                    }
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                            }

                        });
                        myRefCentral.child("tutoringRoom").child(yearDate).child(monthDate).child(dayDate).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                                viewCentralModelListTutoringRoom.clear();
                                GenericTypeIndicator<List<String>> genericTypeIndicator = new GenericTypeIndicator<List<String>>() {
                                };
                                List<String> timefitness = new ArrayList<>();
                                List<String> listUserName = new ArrayList<>();
                                List<CentralManagerModel> centralManagerModelList = new ArrayList<>();
                                centralManagerModelList.clear();
                                // get List name user by key
                                for (DataSnapshot ds : snapshot.getChildren()) {
                                    myRefUser.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                            if (snapshot.hasChild(ds.getKey())) {
                                                String fName = snapshot.child(ds.getKey()).child("firstname").getValue(String.class);
                                                String lName = snapshot.child(ds.getKey()).child("lastname").getValue(String.class);
                                                listUserName.add(fName + " " + lName);
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull @NotNull DatabaseError error) {
                                        }
                                    });
                                    //get time fitness by Display Date form Firebase
                                    timefitness = ds.child("time").getValue(genericTypeIndicator);
                                    CentralManagerModel centralManagerModel = ds.getValue(CentralManagerModel.class);
                                    centralManagerModelList.add(centralManagerModel);
                                }
                                for (int i = 0; i < listUserName.size(); i++) {
                                    for (String time : timefitness) {
                                        CentralModel centralModel = new CentralModel("ห้องติวหนังสือ", listUserName.get(i), date, convertTime(time));
                                        viewCentralModelListTutoringRoom.add(centralModel);
                                    }
                                    Log.e("list", viewCentralModelListTutoringRoom.size() + "");
                                }

                            }


                            @Override
                            public void onCancelled(@NonNull @NotNull DatabaseError error) {

                            }
                        });

                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                    }
                });*/


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

    public void openViewCentral(String yearDate, String monthDate, String dayDate, String date) {
        final List<CentralManagerModel> centralManagerModelList = new ArrayList<>();
        final List<CentralModel> viewCentralModelListFitness = new ArrayList<CentralModel>();
        myRefCentral.child("fitness").child(yearDate).child(monthDate).child(dayDate).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                GenericTypeIndicator<List<CentralManagerModel>> genericTypeIndicator = new GenericTypeIndicator<List<CentralManagerModel>>() {
                };
                List<String> timefitness = new ArrayList<>();
                final List<String> listUserName = new ArrayList<>();

                for (DataSnapshot ds : snapshot.getChildren()) {
                    CentralManagerModel centralManagerModel = ds.getValue(CentralManagerModel.class);
                    centralManagerModel.setKey(ds.getKey());
                    centralManagerModelList.add(centralManagerModel);
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
            }
        });

        // Add Delay for loaded data
        Handler handler = new Handler();
        int delay = 1000;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //checking if the data is loaded or not
                if (!centralManagerModelList.isEmpty()) {
                    viewCentralModelListFitness.clear();
                    myRefUser.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                            for (CentralManagerModel cmm : centralManagerModelList) {
                                if (snapshot.hasChild(cmm.getKey())) {
                                    String fName = snapshot.child(cmm.getKey()).child("firstname").getValue(String.class);
                                    String lName = snapshot.child(cmm.getKey()).child("lastname").getValue(String.class);
                                    String name = fName + " " + lName;
                                    for (String time : cmm.getTime()) {
                                        CentralModel centralModel = new CentralModel("ห้องติวหนังสือ", name, date, convertTime(time));
                                        Log.e("ad", centralModel.getName());
                                        viewCentralModelListFitness.add(centralModel);
                                    }
                                }
                            }


                        }

                        @Override
                        public void onCancelled(@NonNull @NotNull DatabaseError error) {

                        }
                    });
                    ViewFitnessFragment viewFitnessFragment = new ViewFitnessFragment();
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

                    Bundle bundle = new Bundle();
                    bundle.putString("test", "xxxxxx");
                    bundle.putParcelableArrayList("viewFitness", (ArrayList<? extends Parcelable>) viewCentralModelListFitness);
                    viewFitnessFragment.setArguments(bundle);
                    fragmentTransaction.replace(R.id.my_fragment, viewFitnessFragment).commit();
                    Log.e("ad", centralManagerModelList.get(0).getKey());
                } else
                    handler.postDelayed(this, delay);
            }
        }, delay);


    }
}