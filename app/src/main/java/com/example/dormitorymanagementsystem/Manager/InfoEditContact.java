package com.example.dormitorymanagementsystem.Manager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.dormitorymanagementsystem.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

public class InfoEditContact extends AppCompatActivity {

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference("Contact");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_edit_contact);

        EditText etPhone = findViewById(R.id.etPhone);
        EditText etNamePhone = findViewById(R.id.etNamePhone);
        EditText etEmail = findViewById(R.id.etEmail);
        EditText etNameEmail = findViewById(R.id.etNameEmail);
        Button btConfirm = findViewById(R.id.btConfirm);
        EditText etNameDormitory = findViewById(R.id.etNameDormitory);


        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if (snapshot.child("nameDormitory").getValue(String.class) == null){
                    btConfirm.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            myRef.child("contactPhone").child("name").setValue(etNamePhone.getText().toString());
                            myRef.child("contactPhone").child("phone").setValue(etPhone.getText().toString());
                            myRef.child("contactEmail").child("email").setValue(etEmail.getText().toString());
                            myRef.child("contactEmail").child("name").setValue(etNameEmail.getText().toString());
                            myRef.child("nameDormitory").setValue(etNameDormitory.getText().toString());
                            finish();
                        }
                    });
                }else {
                    etPhone.setText(snapshot.child("contactPhone").child("phone").getValue(String.class));
                    etNamePhone.setText(snapshot.child("contactPhone").child("name").getValue(String.class));
                    etEmail.setText(snapshot.child("contactEmail").child("email").getValue(String.class));
                    etNameEmail.setText(snapshot.child("contactEmail").child("name").getValue(String.class));
                    etNameDormitory.setText(snapshot.child("nameDormitory").getValue(String.class));
                    btConfirm.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            myRef.child("contactPhone").child("name").setValue(etNamePhone.getText().toString());
                            myRef.child("contactPhone").child("phone").setValue(etPhone.getText().toString());
                            myRef.child("contactEmail").child("email").setValue(etEmail.getText().toString());
                            myRef.child("contactEmail").child("name").setValue(etNameEmail.getText().toString());
                            myRef.child("nameDormitory").setValue(etNameDormitory.getText().toString());
                            finish();
                        }
                    });
                }
            }
            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

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