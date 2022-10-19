package com.example.dormitorymanagementsystem.Manager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.dormitorymanagementsystem.Adapter.AdapterBookingDetails;
import com.example.dormitorymanagementsystem.Model.CentralModel;
import com.example.dormitorymanagementsystem.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_central_use2);

        Intent intent = getIntent();
        String day = intent.getStringExtra("day");
        String month = intent.getStringExtra("month");
        String year = intent.getStringExtra("year");
        String date = intent.getStringExtra("date");

        mContext = getApplication();
        recyclerView = findViewById(R.id.list_item);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));

        myRefCentral.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                myRefUser.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshotUser) {
                        listCentral.clear();
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
                        recyclerView.setAdapter(adapterBookingDetails);
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
}