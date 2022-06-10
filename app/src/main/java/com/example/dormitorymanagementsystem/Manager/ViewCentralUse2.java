package com.example.dormitorymanagementsystem.Manager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.util.Log;

import com.example.dormitorymanagementsystem.Adapter.AdapterBookingDetails;
import com.example.dormitorymanagementsystem.Adapter.AdapterParcel;
import com.example.dormitorymanagementsystem.Fragment.ViewFitnessFragment;
import com.example.dormitorymanagementsystem.Fragment.ViewTutoringFragment;
import com.example.dormitorymanagementsystem.Model.CentralManagerModel;
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

        Context mContext = getApplication();
        RecyclerView recyclerView = findViewById(R.id.list_item);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));

        myRefCentral.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                listCentral.clear();
                GenericTypeIndicator<List<String>> genericTypeIndicator = new GenericTypeIndicator<List<String>>() {
                };
                if (snapshot.child("fitness").hasChild(year)) {
                    if (snapshot.child("fitness").child(year).hasChild(month)) {
                        if (snapshot.child("fitness").child(year).child(month).hasChild(day)) {
                            if (snapshot.child("fitness").child(year).child(month).child(day).hasChild(userID)) {
                                List<String> timefitness = snapshot.child("fitness").child(year).child(month).child(day).child(userID).child("time").getValue(genericTypeIndicator);
                                for (int i = 0; i < timefitness.size(); i++) {
                                    CentralModel centralModel = new CentralModel("พื้นที่ออกกำลังกาย", name, date, convertTime(timefitness.get(i)));
                                    listCentral.add(centralModel);
                                }
                            }
                        }
                    }
                }
                if (snapshot.child("tutoringRoom").hasChild(year)) {
                    if (snapshot.child("tutoringRoom").child(year).hasChild(month)) {
                        if (snapshot.child("tutoringRoom").child(year).child(month).hasChild(day)) {
                            if (snapshot.child("tutoringRoom").child(year).child(month).child(day).hasChild(userID)) {
                                List<String> timetutoringRoom = snapshot.child("tutoringRoom").child(year).child(month).child(day).child(userID).child("time").getValue(genericTypeIndicator);
                                for (int i = 0; i < timetutoringRoom.size(); i++) {
                                    CentralModel centralModel = new CentralModel("ห้องติวหนังสือ", name, date, convertTime(timetutoringRoom.get(i)));
                                    listCentral.add(centralModel);
                                }
                            }
                        }
                    }
                }
                adapterBookingDetails = new AdapterBookingDetails(mContext, listCentral);
                recyclerView.setAdapter(adapterBookingDetails);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }
}