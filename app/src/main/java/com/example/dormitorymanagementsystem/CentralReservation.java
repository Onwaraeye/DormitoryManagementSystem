package com.example.dormitorymanagementsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CentralReservation extends AppCompatActivity {

    private Button getValue;
    private int day,month,year;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_central_reservation);

        Intent intent = getIntent();
        String title = intent.getStringExtra("central");

        TextView txCentral = findViewById(R.id.txCentral);
        String central = "";
        if (title.equals("fitness")){
            central = "พื้นที่ออกกำลังกาย";
        }else {
            central = "ห้องติวหนังสือ";
        }
        txCentral.setText(central);


        CalendarView calendarView = (CalendarView) findViewById(R.id.calendarView);
        day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        month = Calendar.getInstance().get(Calendar.MONTH);
        year = Calendar.getInstance().get(Calendar.YEAR);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int y, int m, int d) {
                day = d;
                month = m;
                year = y;
                
            }
        });

        getValue = findViewById(R.id.btNext);
        getValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),CentralReservation1.class);
                intent.putExtra("central",title+"");
                intent.putExtra("day",day+"");
                intent.putExtra("month",month+"");
                intent.putExtra("year",year+"");
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
    }
}