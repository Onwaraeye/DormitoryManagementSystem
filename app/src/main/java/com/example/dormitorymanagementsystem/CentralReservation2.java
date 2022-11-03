package com.example.dormitorymanagementsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
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
    private String userID, title;
    private List<String> listBooking = new ArrayList<>();
    private List<String> listCentral = new ArrayList<>();
    private List<List<String>> myList = new ArrayList<>();
    private String monthThai;
    List<String> listCheckUser = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_central_reservation2);

        userID = Login.getGbIdUser();

        Intent intent = getIntent();
        title = intent.getStringExtra("central");
        String day = intent.getStringExtra("day");
        String month = intent.getStringExtra("month");
        int mo = Integer.valueOf(month) + 1;
        String year = intent.getStringExtra("year");
        int yearTh = Integer.parseInt(year) + 543;
        String value = intent.getStringExtra("value");
        String time = intent.getStringExtra("time");
        String check = intent.getStringExtra("check");

        ArrayList<String> timeAdd = intent.getStringArrayListExtra("timeAdd");

        Set<String> set = new HashSet<String>(timeAdd);

        List<String> list = new ArrayList<>();
        list.addAll(set);

        List<String> listCheck = new ArrayList<>();
        List<String> listUser = new ArrayList<>();


        TextView txDate = findViewById(R.id.txDate);
        txDate.setText("วันที่ " + day + " " + getMonth(mo) + " " + yearTh);
        TextView txTime = findViewById(R.id.txTime);
        txTime.setText(time);
        TextView txCentral = findViewById(R.id.txCentral);
        String central = "";
        if (title.equals("fitness")) {
            central = "พื้นที่ออกกำลังกาย";
        } else {
            central = "ห้องติวหนังสือ";
        }
        txCentral.setText(central);

        TextView etNumroom = findViewById(R.id.etNumroom);
        EditText etPhone = findViewById(R.id.etPhone);
        Button btConfirm = findViewById(R.id.btConfirm);


        myRefUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshotUser) {
                try {
                    String numroom = snapshotUser.child(userID).child("numroom").getValue(String.class);
                    String phone = snapshotUser.child(userID).child("phone").getValue(String.class);
                    etNumroom.setText(numroom);
                    etPhone.setText(phone);

                    btConfirm.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            myRefCentral.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                   /* listCheck.clear();
                                    listCheckUser.clear();
                                    for (DataSnapshot ds : snapshot.child(title).child(year).child(mo + "").child(day).getChildren()) {
                                        listCheck.add(ds.getKey());
                                    }
                                    for (String t : listCheck) {
                                        for (int i = 0; i <= 31; i++) {
                                            for (DataSnapshot dataSnapshot : snapshot.child(title).child(year).child(mo + "").child(i + "").child(t).getChildren()) {
                                                listCheckUser.add(dataSnapshot.getKey());
                                                Log.e("listCheckUser", listCheckUser.get(i));
                                            }
                                        }
                                    }*/
                                    /*if (listCheckUser.size() != 0) {
                                        listUser.clear();
                                        for (int j = 0; j < listCheckUser.size(); j++) {
                                            if (listCheckUser.get(j).equals(userID)) {
                                                listUser.add(listCheckUser.get(j));
                                            }
                                        }
                                        if (listUser.size() != 0 && listUser.get(0).equals(userID)) {*/

                                                        if (check.equals("Limit")){
                                                            Toast.makeText(CentralReservation2.this, "ใช้สิทธิ์ครบแล้ว", Toast.LENGTH_SHORT).show();
                                                        }else {
                                                            String inputNumroom = etNumroom.getText().toString();
                                                            String inputPhone = etPhone.getText().toString();
                                                            try {
                                                                for (int i = 0; i < list.size(); i++) {
                                                                    Log.e("listCheck", list.toString());
                                                                    //myRefCentral.child(title).child(year).child(mo + "").child(day).child(list.get(i)).child(userID).child("value").setValue(value);
                                                                    myRefCentral.child(title).child(year).child(mo + "").child(day).child(list.get(i)).child(userID).child("numroom").setValue(inputNumroom);
                                                                    myRefCentral.child(title).child(year).child(mo + "").child(day).child(list.get(i)).child(userID).child("phone").setValue(inputPhone);
                                                                    //myRefCentral.child(title).child(year).child(mo + "").child(day).child(list.get(i)).child(userID).child("timeShow").setValue(time);
                                                                }
                                                                Intent intent = new Intent(getApplicationContext(), Central.class);
                                                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                                startActivity(intent);
                                                                finish();
                                                            } catch (Exception e) {
                                                                Log.e("central", e.toString());
                                                            }
                                                        }

                                        //}
                                        /*else {
                                            String inputNumroom = etNumroom.getText().toString();
                                            String inputPhone = etPhone.getText().toString();
                                            try {
                                                for (int i = 0; i < list.size(); i++) {
                                                    Log.e("listCheck", list.toString());
                                                    myRefCentral.child(title).child(year).child(mo + "").child(day).child(list.get(i)).child(userID).child("value").setValue(value);
                                                    myRefCentral.child(title).child(year).child(mo + "").child(day).child(list.get(i)).child(userID).child("numroom").setValue(inputNumroom);
                                                    myRefCentral.child(title).child(year).child(mo + "").child(day).child(list.get(i)).child(userID).child("phone").setValue(inputPhone);
                                                    myRefCentral.child(title).child(year).child(mo + "").child(day).child(list.get(i)).child(userID).child("timeShow").setValue(time);
                                                }
                                                Intent intent = new Intent(getApplicationContext(), Central.class);
                                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                startActivity(intent);
                                                finish();
                                            } catch (Exception e) {
                                                Log.e("central", e.toString());
                                            }
                                        }
                                    } else {
                                        String inputNumroom = etNumroom.getText().toString();
                                        String inputPhone = etPhone.getText().toString();

                                        try {
                                            for (int i = 0; i < list.size(); i++) {
                                                Log.e("listCheck", list.toString());
                                                myRefCentral.child(title).child(year).child(mo + "").child(day).child(list.get(i)).child(userID).child("value").setValue(value);
                                                myRefCentral.child(title).child(year).child(mo + "").child(day).child(list.get(i)).child(userID).child("numroom").setValue(inputNumroom);
                                                myRefCentral.child(title).child(year).child(mo + "").child(day).child(list.get(i)).child(userID).child("phone").setValue(inputPhone);
                                                myRefCentral.child(title).child(year).child(mo + "").child(day).child(list.get(i)).child(userID).child("timeShow").setValue(time);
                                            }
                                            Intent intent = new Intent(getApplicationContext(), Central.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            startActivity(intent);
                                            finish();
                                        } catch (Exception e) {
                                            Log.e("central", e.toString());
                                        }
                                    }*/
                                }

                                @Override
                                public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                }
                            });


                        }
                    });
                } catch (Exception e) {
                    Toast.makeText(CentralReservation2.this, e.toString(), Toast.LENGTH_SHORT).show();
                }

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