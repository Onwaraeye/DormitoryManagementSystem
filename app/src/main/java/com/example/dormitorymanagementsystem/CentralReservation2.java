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

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("Room");
    DatabaseReference myRefUser = database.getReference("Users");
    DatabaseReference myRefCentral = database.getReference("Central");
    String userID,title;
    List<String> listBooking = new ArrayList<>();
    List<String> listCentral = new ArrayList<>();
    List<List<String>> myList = new ArrayList<>();

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
        String value = intent.getStringExtra("value");
        String time = intent.getStringExtra("time");

        ArrayList<String> timeAdd = intent.getStringArrayListExtra("timeAdd");

        Set<String> set = new HashSet<String>(timeAdd);

        List<String> list = new ArrayList<>();
        list.addAll(set);

        TextView txDate = findViewById(R.id.txDate);
        txDate.setText(day+"/"+month+"/"+year);
        TextView txTime = findViewById(R.id.txTime);
        txTime.setText(time);

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
}