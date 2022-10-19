package com.example.dormitorymanagementsystem;

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

import java.util.ArrayList;
import java.util.List;

public class CentralReservation1 extends AppCompatActivity {

    private Button getValue;
    private CheckBox time8am_9am,time9am_10am,time10am_11am,time11am_12am,time12am_13am,time13am_14am,time14am_15am,time15am_16am,time16am_17am,time17am_18am,time18am_19am,time19am_20am;
    private int value;
    private String time = "",timeshow = "";
    private String monthThai;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_central_reservation1);

        Intent intent = getIntent();
        String title = intent.getStringExtra("central");
        String day = intent.getStringExtra("day");
        String month = intent.getStringExtra("month");
        String year = intent.getStringExtra("year");
        int yearTh = Integer.parseInt(year)+543;

        TextView txDate = findViewById(R.id.txDate);
        txDate.setText("วันที่ "+day+" "+getMonth(Integer.parseInt(month))+" "+yearTh);
        TextView txCentral = findViewById(R.id.txCentral);
        String central = "";
        if (title.equals("fitness")){
            central = "พื้นที่ออกกำลังกาย";
        }else {
            central = "ห้องติวหนังสือ";
        }
        txCentral.setText(central);

        TimePicker timePicker = findViewById(R.id.timePicker);
        timePicker.setIs24HourView(false);

        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

            }
        };



        time8am_9am = findViewById(R.id.time8am_9am);
        time9am_10am = findViewById(R.id.time9am_10am);
        time10am_11am = findViewById(R.id.time10am_11am);
        time11am_12am = findViewById(R.id.time11am_12am);
        time12am_13am = findViewById(R.id.time12am_13am);
        time13am_14am = findViewById(R.id.time13am_14am);
        time14am_15am = findViewById(R.id.time14am_15am);
        time15am_16am = findViewById(R.id.time15am_16am);
        time16am_17am = findViewById(R.id.time16am_17am);
        time17am_18am = findViewById(R.id.time17am_18am);
        time18am_19am = findViewById(R.id.time18am_19am);
        time19am_20am = findViewById(R.id.time19am_20am);
        List<String> listTime = new ArrayList<>();
        List<String> listTimeAdd = new ArrayList<>();

        getValue = findViewById(R.id.btNext);
        getValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                value = 0;
                if (time8am_9am.isChecked()){
                    value += 1;
                    time = time8am_9am.getText().toString();
                    listTimeAdd.add("0");
                    listTime.add(time);
                }
                if (time9am_10am.isChecked()){
                    value += 1;
                    time = time9am_10am.getText().toString();
                    listTimeAdd.add("1");
                    listTime.add(time);
                }
                if (time10am_11am.isChecked()){
                    value += 1;
                    time = time10am_11am.getText().toString();
                    listTimeAdd.add("2");
                    listTime.add(time);
                }
                if (time11am_12am.isChecked()){
                    value += 1;
                    time = time11am_12am.getText().toString();
                    listTimeAdd.add("3");
                    listTime.add(time);
                }
                if (time12am_13am.isChecked()){
                    value += 1;
                    time = time12am_13am.getText().toString();
                    listTimeAdd.add("4");
                    listTime.add(time);
                }
                if (time13am_14am.isChecked()){
                    value += 1;
                    time = time13am_14am.getText().toString();
                    listTimeAdd.add("5");
                    listTime.add(time);
                }
                if (time14am_15am.isChecked()){
                    value += 1;
                    time = time14am_15am.getText().toString();
                    listTimeAdd.add("6");
                    listTime.add(time);
                }
                if (time15am_16am.isChecked()){
                    value += 1;
                    time = time15am_16am.getText().toString();
                    listTimeAdd.add("7");
                    listTime.add(time);
                }
                if (time16am_17am.isChecked()){
                    value += 1;
                    time = time16am_17am.getText().toString();
                    listTimeAdd.add("8");
                    listTime.add(time);
                }
                if (time17am_18am.isChecked()){
                    value += 1;
                    time = time17am_18am.getText().toString();
                    listTimeAdd.add("9");
                    listTime.add(time);
                }
                if (time18am_19am.isChecked()){
                    value += 1;
                    time = time18am_19am.getText().toString();
                    listTimeAdd.add("10");
                    listTime.add(time);
                }
                if (time19am_20am.isChecked()){
                    value += 1;
                    time = time19am_20am.getText().toString();
                    listTimeAdd.add("11");
                    listTime.add(time);
                }
                if (value>3){
                    listTime.clear();
                    Toast.makeText(getApplicationContext(),"ส่วนกลางนี้จองได้ไม่เกินคนละ 3 ชม.ต่อครั้ง",Toast.LENGTH_SHORT).show();
                }else {
                    if (value<1){
                        Toast.makeText(getApplicationContext(),"กรุณาระบุเวลา",Toast.LENGTH_SHORT).show();
                    }else {
                        Intent intent = new Intent(getApplicationContext(),CentralReservation2.class);
                        intent.putExtra("central",title+"");
                        intent.putExtra("day",day+"");
                        intent.putExtra("month",month+"");
                        intent.putExtra("year",year+"");
                        intent.putExtra("value",value+"");
                        intent.putExtra("time",listTime+"");
                        intent.putStringArrayListExtra("timeAdd",(ArrayList<String>) listTimeAdd);
                        Log.e("time",listTime+"");
                        startActivity(intent);
                        listTime.clear();
                    }
                }

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
}