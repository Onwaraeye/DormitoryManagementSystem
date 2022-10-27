package com.example.dormitorymanagementsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class CentralReservation1 extends AppCompatActivity {

    private Button getValue;
    private CheckBox time8, time9, time10, time11, time12, time13, time14, time15, time16, time17, time18, time19;
    private int value;
    private String time = "", timeshow = "";
    private String monthThai;
    List<String> listTime = new ArrayList<>();
    List<String> listTimeAdd = new ArrayList<>();
    List<String> list = new ArrayList<>();

    String title;
    String day;
    String month;
    String year;

    private DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Central");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_central_reservation1);

        Intent intent = getIntent();
        title = intent.getStringExtra("central");
        Toast.makeText(this, title, Toast.LENGTH_SHORT).show();
        day = intent.getStringExtra("day");
        month = intent.getStringExtra("month");
        int mo = Integer.valueOf(month) + 1;
        year = intent.getStringExtra("year");
        int yearTh = Integer.parseInt(year) + 543;
        String userID = Login.getGbIdUser();

        TextView txDate = findViewById(R.id.txDate);
        txDate.setText("วันที่ " + day + " " + getMonth(mo) + " " + yearTh);
        TextView txCentral = findViewById(R.id.txCentral);
        getValue = findViewById(R.id.btNext);
        time8 = findViewById(R.id.time8am_9am);
        time9 = findViewById(R.id.time9am_10am);
        time10 = findViewById(R.id.time10am_11am);
        time11 = findViewById(R.id.time11am_12am);
        time12 = findViewById(R.id.time12am_13am);
        time13 = findViewById(R.id.time13am_14am);
        time14 = findViewById(R.id.time14am_15am);
        time15 = findViewById(R.id.time15am_16am);
        time16 = findViewById(R.id.time16am_17am);
        time17 = findViewById(R.id.time17am_18am);
        time18 = findViewById(R.id.time18am_19am);
        time19 = findViewById(R.id.time19am_20am);


        String central = "";
        if (title.equals("fitness")) {
            central = "พื้นที่ออกกำลังกาย";
        } else {
            central = "ห้องติวหนังสือ";
        }
        txCentral.setText(central);


        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot ds : snapshot.child(title).child(year).child(mo + "").child(day).getChildren()) {
                    list.add(ds.getKey());
                    Log.e("centraltime", list.toString());
                }
                Log.e("listtimesize", list.size()+"");
                for (int i = 0; i < list.size(); i++) {
                    String check = "time"+list.get(i);
                    if (check.equals("time8")) {
                        time8.setEnabled(false);
                    }
                    if (check.equals("time9")) {
                        time9.setEnabled(false);
                    }
                    if (check.equals("time10")) {
                        time10.setEnabled(false);
                    }
                    if (check.equals("time11")) {
                        time11.setEnabled(false);
                    }
                    if (check.equals("time12")) {
                        time12.setEnabled(false);
                    }
                    if (check.equals("time13")) {
                        time13.setEnabled(false);
                    }
                    if (check.equals("time14")) {
                        time14.setEnabled(false);
                    }
                    if (check.equals("time15")) {
                        time15.setEnabled(false);
                    }
                    if (check.equals("time16")) {
                        time16.setEnabled(false);
                    }
                    if (check.equals("time17")) {
                        time17.setEnabled(false);
                    }
                    if (check.equals("time18")) {
                        time18.setEnabled(false);
                    }
                    if (check.equals("time19")) {
                        time19.setEnabled(false);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });


        getValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkBok();
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

    private void checkBok() {

            value = 0;
            if (time8.isChecked()) {
                value += 1;
                time = time8.getText().toString();
                listTimeAdd.add("8");
                listTime.add(time);
            }
            if (time9.isChecked()) {
                value += 1;
                time = time9.getText().toString();
                listTimeAdd.add("9");
                listTime.add(time);
            }
            if (time10.isChecked()) {
                value += 1;
                time = time10.getText().toString();
                listTimeAdd.add("10");
                listTime.add(time);
            }
            if (time11.isChecked()) {
                value += 1;
                time = time11.getText().toString();
                listTimeAdd.add("11");
                listTime.add(time);
            }
            if (time12.isChecked()) {
                value += 1;
                time = time12.getText().toString();
                listTimeAdd.add("12");
                listTime.add(time);
            }
            if (time13.isChecked()) {
                value += 1;
                time = time13.getText().toString();
                listTimeAdd.add("13");
                listTime.add(time);
            }
            if (time14.isChecked()) {
                value += 1;
                time = time14.getText().toString();
                listTimeAdd.add("14");
                listTime.add(time);
            }
            if (time15.isChecked()) {
                value += 1;
                time = time15.getText().toString();
                listTimeAdd.add("15");
                listTime.add(time);
            }
            if (time16.isChecked()) {
                value += 1;
                time = time16.getText().toString();
                listTimeAdd.add("16");
                listTime.add(time);
            }
            if (time17.isChecked()) {
                value += 1;
                time = time17.getText().toString();
                listTimeAdd.add("17");
                listTime.add(time);
            }
            if (time18.isChecked()) {
                value += 1;
                time = time18.getText().toString();
                listTimeAdd.add("18");
                listTime.add(time);
            }
            if (time19.isChecked()) {
                value += 1;
                time = time19.getText().toString();
                listTimeAdd.add("19");
                listTime.add(time);
            }
            if (value > 3) {
                listTime.clear();
                Toast.makeText(getApplicationContext(), "ส่วนกลางนี้จองได้ไม่เกินคนละ 3 ชม.ต่อครั้ง", Toast.LENGTH_SHORT).show();
            } else {
                if (value < 1) {
                    Toast.makeText(getApplicationContext(), "กรุณาระบุเวลา", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(getApplicationContext(), CentralReservation2.class);
                    intent.putExtra("central", title + "");
                    intent.putExtra("day", day + "");
                    intent.putExtra("month", month + "");
                    intent.putExtra("year", year + "");
                    intent.putExtra("value", value + "");
                    intent.putExtra("time", listTime + "");
                    intent.putStringArrayListExtra("timeAdd", (ArrayList<String>) listTimeAdd);
                    Log.e("time", listTime + "");
                    startActivity(intent);
                    listTime.clear();
                }
            }
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