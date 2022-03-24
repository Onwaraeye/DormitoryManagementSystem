package com.example.dormitorymanagementsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ChangePassword extends AppCompatActivity {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRefUser = database.getReference("Users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        String userID = Login.getGbIdUser();

        EditText etPass = findViewById(R.id.etPass);
        EditText etCFPass = findViewById(R.id.etCFPass);
        EditText etPreviousPass = findViewById(R.id.etPreviousPass);


        Button btConfirm = findViewById(R.id.btConfirm);
        btConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ipPass = etPass.getText().toString();
                String inputCFPass = etCFPass.getText().toString();
                String inputPreviousPass = etPreviousPass.getText().toString();
                if (inputCFPass.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "กรุณายืนยันพาสเวิร์ด", Toast.LENGTH_SHORT).show();
                } else {
                    myRefUser.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String password = snapshot.child(userID).child("password").getValue(String.class);
                            if (inputPreviousPass.equals(password)){
                                if (ipPass.equals(inputCFPass)) {

                                    myRefUser.child(userID).child("password").setValue(ipPass);

                                    finish();

                                } else {
                                    Toast.makeText(getApplicationContext(), "รหัสผ่านใหม่และรหัสยืนยันไม่ตรงกัน", Toast.LENGTH_SHORT).show();
                                }
                            }else {
                                Toast.makeText(getApplicationContext(), "รหัสผ่านไม่ถูกต้อง", Toast.LENGTH_SHORT).show();
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
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
}