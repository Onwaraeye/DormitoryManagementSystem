package com.example.dormitorymanagementsystem.Manager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dormitorymanagementsystem.Adapter.AdapteBill;
import com.example.dormitorymanagementsystem.Adapter.AdapteViewBill;
import com.example.dormitorymanagementsystem.Login;
import com.example.dormitorymanagementsystem.Model.BillModel;
import com.example.dormitorymanagementsystem.R;
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

public class ViewBills extends AppCompatActivity {

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference("Bills");
    private List<BillModel> list = new ArrayList<>();
    private RecyclerView recyclerView;
    private AdapteViewBill adapter;
    List<String> listRoom;

    private Context mContext;
    private String monthThai = "";
    TextView txMonth;

    int monthCurrent = Calendar.getInstance().get(Calendar.MONTH) + 1;
    int yearCurrent = Calendar.getInstance().get(Calendar.YEAR);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_bills);

        mContext = getApplication();

        recyclerView = findViewById(R.id.list_item);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));


        listRoom = new ArrayList<>();

        int yr = yearCurrent + 543;
        txMonth = findViewById(R.id.txMonth);

        String date = getMonth(monthCurrent) + " " + yr;
        txMonth.setText(date);
        getListData(monthCurrent + "", yearCurrent + "", date);
        showMonthYear();

        ImageView arrow_back = findViewById(R.id.ic_arrow_back);
        arrow_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void showMonthYear() {
        final RackMonthPicker rackMonthPicker = new RackMonthPicker(this)
                .setMonthType(MonthType.TEXT)
                .setPositiveButton(new DateMonthDialogListener() {
                    @Override
                    public void onDateMonth(int month, int startDate, int endDate, int year, String monthLabel) {
                        int ye = year + 543;
                        String date = getMonth(month) + " " + ye;
                        String mo = String.valueOf(month);
                        getListData(mo, year + "", date);
                        txMonth.setText(date);

                    }
                })
                .setNegativeButton(new OnCancelMonthDialogListener() {
                    @Override
                    public void onCancel(androidx.appcompat.app.AlertDialog dialog) {
                        dialog.dismiss();
                    }
                });

        ImageButton btMonthPicker = findViewById(R.id.btMonthPicker);
        btMonthPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rackMonthPicker.show();
            }
        });
    }

    public void getListData(String month, String year, String date) {
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                list.clear();
                listRoom.clear();
                for (DataSnapshot dataSnapshot : snapshot.child(year + "").child(month).getChildren()) {
                    BillModel billModel = new BillModel();
                    billModel = dataSnapshot.getValue(BillModel.class);
                    list.add(billModel);
                    String room = dataSnapshot.getKey();
                    listRoom.add(room);
                }
                adapter = new AdapteViewBill(mContext, list, listRoom, date);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
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