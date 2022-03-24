package com.example.dormitorymanagementsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dormitorymanagementsystem.Manager.InfoEditContact;
import com.example.dormitorymanagementsystem.Manager.SelectBank;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Info extends AppCompatActivity {

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference("Contact");
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        mContext = getApplication();

        TextView txEditContact = findViewById(R.id.txEditContact);
        TextView txEditBank = findViewById(R.id.txEditBank);

        TextView txPhone = findViewById(R.id.txPhone);
        TextView txNamePhone = findViewById(R.id.txNamePhone);
        TextView txEmail = findViewById(R.id.txEmail);
        TextView txNameEmail = findViewById(R.id.txNameEmail);
        TextView txNameDormitory = findViewById(R.id.txNameDormitory);


        TextView txBank = findViewById(R.id.txBank);
        TextView txAccName = findViewById(R.id.txAccName);
        TextView txAccNo = findViewById(R.id.txAccNo);
        ImageView imageBank = findViewById(R.id.imageBank);


        if (Login.getGbTypeUser().equals("User")){
            txEditContact.setVisibility(View.INVISIBLE);
            txEditBank.setVisibility(View.INVISIBLE);
        }

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                String icon = snapshot.child("contactBank").child("icon").getValue(String.class);
                if (icon == null){
                    Picasso.get().load(R.drawable.ic_bank).into(imageBank);
                }else {
                    Picasso.get().load(Integer.parseInt(icon)).into(imageBank);
                    txBank.setText(snapshot.child("contactBank").child("bank").getValue(String.class));
                    txAccName.setText(snapshot.child("contactBank").child("name").getValue(String.class));
                    txAccNo.setText(snapshot.child("contactBank").child("accNo").getValue(String.class));
                }
                if (snapshot.child("nameDormitory").getValue(String.class)==null){

                }else {
                    txPhone.setText(snapshot.child("contactPhone").child("phone").getValue(String.class));
                    txNamePhone.setText(snapshot.child("contactPhone").child("name").getValue(String.class));
                    txEmail.setText(snapshot.child("contactEmail").child("email").getValue(String.class));
                    txNameEmail.setText(snapshot.child("contactEmail").child("name").getValue(String.class));
                    txNameDormitory.setText(snapshot.child("nameDormitory").getValue(String.class));
                }
            }
            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
            }
        });

        txEditBank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, SelectBank.class);
                startActivity(intent);
            }
        });

        txEditContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, InfoEditContact.class);
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