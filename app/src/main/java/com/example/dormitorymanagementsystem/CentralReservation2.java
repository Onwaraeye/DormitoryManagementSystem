package com.example.dormitorymanagementsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CentralReservation2 extends AppCompatActivity {

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference("Room");
    private DatabaseReference myRefUser = database.getReference("Users");
    private DatabaseReference myRefCentral = database.getReference("Central");
    private String userID,title;
    private List<String> listBooking = new ArrayList<>();
    private List<String> listCentral = new ArrayList<>();
    private List<List<String>> myList = new ArrayList<>();
    private String monthThai;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_central_reservation2);

        userID = Login.getGbIdUser();

        Intent intent = getIntent();
        title = intent.getStringExtra("central");
        String day = intent.getStringExtra("day");
        String month = intent.getStringExtra("month");
        String year = intent.getStringExtra("year");
        int yearTh = Integer.parseInt(year)+543;
        String value = intent.getStringExtra("value");
        String time = intent.getStringExtra("time");

        ArrayList<String> timeAdd = intent.getStringArrayListExtra("timeAdd");

        Set<String> set = new HashSet<String>(timeAdd);

        List<String> list = new ArrayList<>();
        list.addAll(set);

        TextView txDate = findViewById(R.id.txDate);
        txDate.setText("วันที่ "+day+" "+getMonth(Integer.parseInt(month))+" "+yearTh);
        TextView txTime = findViewById(R.id.txTime);
        txTime.setText(time);
        TextView txCentral = findViewById(R.id.txCentral);
        String central = "";
        if (title.equals("fitness")){
            central = "พื้นที่ออกกำลังกาย";
        }else {
            central = "ห้องติวหนังสือ";
        }
        txCentral.setText(central);

        EditText etNumroom = findViewById(R.id.etNumroom);
        EditText etPhone = findViewById(R.id.etPhone);

        myRefUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(userID)){
                    String numroom = snapshot.child(userID).child("numroom").getValue(String.class);
                    String phone = snapshot.child(userID).child("phone").getValue(String.class);
                    etNumroom.setText(numroom);
                    etPhone.setText(phone);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        Button btConfirm = findViewById(R.id.btConfirm);
        btConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myRefUser.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        String inputNumroom = etNumroom.getText().toString();
                        String inputPhone = etPhone.getText().toString();

                        myRefCentral.child(title).child(year).child(month).child(day).child(userID).child("value").setValue(value);
                        myRefCentral.child(title).child(year).child(month).child(day).child(userID).child("time").setValue(list);
                        myRefCentral.child(title).child(year).child(month).child(day).child(userID).child("numroom").setValue(inputNumroom);
                        myRefCentral.child(title).child(year).child(month).child(day).child(userID).child("phone").setValue(inputPhone);
                        myRefCentral.child(title).child(year).child(month).child(day).child(userID).child("timeShow").setValue(time);

                        Intent intent = new Intent(getApplicationContext(),Central.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {
                    }
                });
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