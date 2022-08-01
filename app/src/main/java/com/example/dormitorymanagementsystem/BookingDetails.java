package com.example.dormitorymanagementsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
    private String day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH) + "";
    private String month = Calendar.getInstance().get(Calendar.MONTH) + "";
    private String year = Calendar.getInstance().get(Calendar.YEAR) + "";
    private int yearThai = Integer.parseInt(year) + 543;
    private String date = day + " " + getMonth(Integer.parseInt(month)) + " " + yearThai;
    private String userID = "";
    private String nameCentral;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_details);

        mContext = getApplicationContext();

        String userType = Login.getGbTypeUser();
        userID = getIntent().getStringExtra("userID");

        String central = getIntent().getStringExtra("central");
        String image = getIntent().getStringExtra("image");
        String name = getIntent().getStringExtra("name");
        String date = getIntent().getStringExtra("date");
        String time = getIntent().getStringExtra("time");


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
                        String phone = snapshot.child(userID).child("phone").getValue(String.class);
                        String numroom = snapshot.child(userID).child("numroom").getValue(String.class);
                        txPhone.setText("เบอร์โทร " + phone);
                        txCentral.setText(central);
                        txDate.setText(date);
                        txName.setText(name);
                        txNumroom.setText(numroom);
                        txTime.setText(time);
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
                showDialogDelete();
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

    private void showDialogDelete() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(BookingDetails.this);
        dialog.setTitle("ลบ");
        dialog.setMessage("คุณแน่ใจที่จะลบหรือไม่");
        dialog.setPositiveButton("ตกลง", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                myRefCentral.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.child(nameCentral).child(year).child(month).child(day).hasChild(userID)) {
                            myRefCentral.child(nameCentral).child(year).child(month).child(day).child(userID).getRef().removeValue();
                            /*Intent intent = new Intent(mContext, Central.class);
                            startActivity(intent);*/
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
}