package com.example.dormitorymanagementsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.dormitorymanagementsystem.Adapter.AdapterBookingDetails;
import com.example.dormitorymanagementsystem.Fragment.CentralReservationFragment;
import com.example.dormitorymanagementsystem.Manager.Post;
import com.example.dormitorymanagementsystem.Model.CentralModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.Calendar;
import java.util.List;

public class BookingDetails extends AppCompatActivity {

    Context mContext;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRefUser = database.getReference("Users");
    DatabaseReference myRefCentral = database.getReference("Central");

    private String monthThai = "";
    String day;
    String month;
    String year;
    private int yearThai;
    private String userID = "";
    private String nameCentral;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_details);

        mContext = getApplicationContext();

        day = getIntent().getStringExtra("day");
        month = getIntent().getStringExtra("month");
        year = getIntent().getStringExtra("year");

        yearThai = Integer.parseInt(year) + 543;
        String userType = Login.getGbTypeUser();
        userID = getIntent().getStringExtra("userID");

        String central = getIntent().getStringExtra("central");
        String image = getIntent().getStringExtra("image");
        String name = getIntent().getStringExtra("name");
        String date = getIntent().getStringExtra("date");

        String time = getIntent().getStringExtra("time");
        String phone = getIntent().getStringExtra("phone");
        String position = getIntent().getStringExtra("position");

        TextView txCentral = findViewById(R.id.txCentral);
        TextView txDate = findViewById(R.id.txDate);
        TextView txName = findViewById(R.id.txName);
        TextView txNumroom = findViewById(R.id.txNumroom);
        TextView txTime = findViewById(R.id.txTime);
        TextView txPhone = findViewById(R.id.txPhone);


        ImageView imageView = findViewById(R.id.imageView);
        Glide.with(mContext).load(image).fitCenter().centerCrop().into(imageView);


        if (central.equals("พื้นที่ออกกำลังกาย")){
            nameCentral = "fitness";
        }else {
            nameCentral = "tutoringRoom";
        }


        myRefCentral.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //String time = snapshot.child(nameCentral).child(year).child(month).child(day).child(userID).child("timeShow").getValue(String.class);

                myRefUser.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String numroom = snapshot.child(userID).child("numroom").getValue(String.class);
                        txPhone.setText("เบอร์โทร " + phone);
                        txCentral.setText(central);
                        txDate.setText(date);
                        txName.setText(name);
                        txNumroom.setText(numroom);
                        txTime.setText(convertTime(time));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

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

        Button btCancle = findViewById(R.id.btCancle);
        btCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogDelete(time);
            }
        });
    }

    public String getMonth(int month) {
        switch (month) {
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

    private void showDialogDelete(String time) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(BookingDetails.this);
        dialog.setTitle("ลบ");
        dialog.setMessage("คุณแน่ใจที่จะลบหรือไม่");
        dialog.setPositiveButton("ตกลง", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                myRefCentral.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if (snapshot.child(nameCentral).child(year).child(month).child(day).child(time).hasChild(userID)) {
                            myRefCentral.child(nameCentral).child(year).child(month).child(day).child(time).child(userID).getRef().removeValue();
                            finish();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
            }
        });
        dialog.setNegativeButton("ยกเลิก", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = dialog.create();
        alertDialog.show();
    }

    public String convertTime(String time) {
        String cvTime = "";
        switch (time) {
            case "8":
                cvTime = "08:00 - 09:00 น.";
                break;
            case "9":
                cvTime = "09:00 - 10:00 น.";
                break;
            case "10":
                cvTime = "10:00 - 11:00 น.";
                break;
            case "11":
                cvTime = "11:00 - 12:00 น.";
                break;
            case "12":
                cvTime = "12:00 - 13:00 น.";
                break;
            case "13":
                cvTime = "13:00 - 14:00 น.";
                break;
            case "14":
                cvTime = "14:00 - 15:00 น.";
                break;
            case "15":
                cvTime = "15:00 - 16:00 น.";
                break;
            case "16":
                cvTime = "16:00 - 17:00 น.";
                break;
            case "17":
                cvTime = "17:00 - 18:00 น.";
                break;
            case "18":
                cvTime = "18:00 - 19:00 น.";
                break;
            case "19":
                cvTime = "19:00 - 20:00 น.";
                break;
        }
        return cvTime;
    }
}