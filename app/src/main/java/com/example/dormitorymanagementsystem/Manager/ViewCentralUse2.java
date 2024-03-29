package com.example.dormitorymanagementsystem.Manager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.dormitorymanagementsystem.Adapter.AdapterBookingDetails;
import com.example.dormitorymanagementsystem.Model.CentralModel;
import com.example.dormitorymanagementsystem.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ViewCentralUse2 extends AppCompatActivity {

    private ViewPager viewPager;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRefCentral = database.getReference("Central");
    private DatabaseReference myRefUser = database.getReference("Users");

    RecyclerView recyclerView;
    AdapterBookingDetails adapterBookingDetails;
    Context mContext;

    List<CentralModel> listCentral = new ArrayList<>();
    List<String> list = new ArrayList<>();
    String monthThai;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_central_use2);

        Intent intent = getIntent();
        String day = intent.getStringExtra("day");
        String month = intent.getStringExtra("month");
        int mo = Integer.valueOf(month) + 1;
        String year = intent.getStringExtra("year");
        String date = intent.getStringExtra("date");

        mContext = getApplication();
        recyclerView = findViewById(R.id.list_item);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));

        myRefUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshotUser) {
                myRefCentral.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        /*listCentral.clear();
                        GenericTypeIndicator<List<String>> genericTypeIndicator = new GenericTypeIndicator<List<String>>() {
                        };
                        if (snapshot.child("fitness").hasChild(year)) {
                            if (snapshot.child("fitness").child(year).hasChild(month)) {
                                if (snapshot.child("fitness").child(year).child(month).hasChild(day)) {
                                    for (DataSnapshot ds : snapshot.child("fitness").child(year).child(month).child(day).getChildren()) {
                                        String phone = snapshot.child("fitness").child(year).child(month).child(day).child(ds.getKey()).child("phone").getValue(String.class);
                                        String fname = snapshotUser.child(ds.getKey()).child("firstname").getValue(String.class);
                                        String lname = snapshotUser.child(ds.getKey()).child("lastname").getValue(String.class);
                                        String name = fname + " " + lname;
                                        List<String> timefitness = snapshot.child("fitness").child(year).child(month).child(day).child(ds.getKey()).child("time").getValue(genericTypeIndicator);
                                        if (snapshot.child("fitness").child(year).child(month).child(day).child(ds.getKey()).child("time").getChildrenCount() ==0){

                                        }else {
                                            for (int i = 0; i < timefitness.size(); i++) {
                                                CentralModel centralModel = new CentralModel("พื้นที่ออกกำลังกาย", name, date, convertTime(timefitness.get(i)),ds.getKey(),phone);
                                                listCentral.add(centralModel);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        if (snapshot.child("tutoringRoom").hasChild(year)) {
                            if (snapshot.child("tutoringRoom").child(year).hasChild(month)) {
                                if (snapshot.child("tutoringRoom").child(year).child(month).hasChild(day)) {
                                    for (DataSnapshot ds : snapshot.child("tutoringRoom").child(year).child(month).child(day).getChildren()) {
                                        String phone = snapshot.child("fitness").child(year).child(month).child(day).child(ds.getKey()).child("phone").getValue(String.class);
                                        String fname = snapshotUser.child(ds.getKey()).child("firstname").getValue(String.class);
                                        String lname = snapshotUser.child(ds.getKey()).child("firstname").getValue(String.class);
                                        String name = fname + " " + lname;
                                        List<String> timetutoringRoom = snapshot.child("tutoringRoom").child(year).child(month).child(day).child(ds.getKey()).child("time").getValue(genericTypeIndicator);
                                        if (snapshot.child("tutoringRoom").child(year).child(month).child(day).child(ds.getKey()).child("time").getChildrenCount() == 0){

                                        }else {
                                            for (int i = 0; i < timetutoringRoom.size(); i++) {
                                                CentralModel centralModel = new CentralModel("ห้องติวหนังสือ", name, date, convertTime(timetutoringRoom.get(i)),ds.getKey(),phone);
                                                listCentral.add(centralModel);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        adapterBookingDetails = new AdapterBookingDetails(mContext, listCentral);
                        recyclerView.setAdapter(adapterBookingDetails);*/

                        try {
                            list.clear();
                            listCentral.clear();
                            for (DataSnapshot dsTitle : snapshot.getChildren()) {
                                //Log.e("dsTitle", dsTitle.getKey());
                                String title = dsTitle.getKey();
                                for (DataSnapshot ds : snapshot.child(dsTitle.getKey()).child(year).child(mo + "").child(day).getChildren()) {
                                    list.add(ds.getKey());
                                }
                                //Log.e("listtime", list.toString());
                                for (String t : list) {
                                    for (DataSnapshot dataSnapshot : snapshot.child(dsTitle.getKey()).child(year).child(mo + "").child(day).child(t).getChildren()) {
                                        String fname = snapshotUser.child(dataSnapshot.getKey()).child("firstname").getValue(String.class);
                                        String lname = snapshotUser.child(dataSnapshot.getKey()).child("lastname").getValue(String.class);
                                        String name = fname + " " + lname;
                                        String sentTitle;
                                        if (title != null && title.equals("fitness")) {
                                            sentTitle = "พื้นที่ออกกำลังกาย";
                                        } else {
                                            sentTitle = "ห้องอเนกประสงค์";
                                        }
                                        String phone = dataSnapshot.child("phone").getValue(String.class);
                                        CentralModel centralModel = new CentralModel(sentTitle, name, date, t, dataSnapshot.getKey(), phone);
                                        listCentral.add(centralModel);
                                        //Log.e("listCentral", listCentral.get(0).getTime());
                                        adapterBookingDetails = new AdapterBookingDetails(mContext, listCentral);
                                        recyclerView.setAdapter(adapterBookingDetails);
                                        //adapterBookingDetails.notifyDataSetChanged();
                                    }
                                }


                            }
                        } catch (Exception e) {
                            Toast.makeText(mContext, e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        ImageView arrow_back = findViewById(R.id.ic_arrow_back);
        arrow_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
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
}