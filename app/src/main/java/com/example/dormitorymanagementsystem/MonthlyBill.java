package com.example.dormitorymanagementsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dormitorymanagementsystem.Adapter.AdapteBill;
import com.example.dormitorymanagementsystem.Adapter.AdapterManagerPhone;
import com.example.dormitorymanagementsystem.Model.BillModel;
import com.example.dormitorymanagementsystem.Model.ManagerModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class MonthlyBill extends AppCompatActivity {

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference("Bills");
    private List<BillModel> list = new ArrayList<>();
    private RecyclerView recyclerView;
    private AdapteBill adapter;

    private Context mContext;
    private String monthThai = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monthly_bill);

        mContext = getApplication();
        String room = Login.getGbNumroom();

        recyclerView = findViewById(R.id.list_item);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));



        List<String> listYear = new ArrayList<>();
        List<String> listDate = new ArrayList<>();
        List<String> listMonth = new ArrayList<>();

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                listYear.clear();
                list.clear();
                listDate.clear();
                for (DataSnapshot ds : snapshot.getChildren()){
                    listYear.add(ds.getKey());
                }
                for (String y : listYear){
                    for (DataSnapshot dataSnapshot : snapshot.child(y).getChildren()){
                        Log.e("Month",dataSnapshot.getKey());
                        if (snapshot.child(y).child(dataSnapshot.getKey()).child(room).hasChild("status")){
                            BillModel billModel = new BillModel();
                            billModel = dataSnapshot.child(room).getValue(BillModel.class);
                            list.add(0,billModel);
                            int yr = Integer.parseInt(y)+543;
                            String date = getMonth(Integer.parseInt(dataSnapshot.getKey()))+" "+yr;
                            listDate.add(0,date);
                            listMonth.add(0,dataSnapshot.getKey());
                        }
                    }
                }
                adapter = new AdapteBill(mContext,list,listDate);
                recyclerView.setAdapter(adapter);
            }
            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

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

    public String getMonth(int month){
        switch(month) {
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