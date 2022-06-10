package com.example.dormitorymanagementsystem.Manager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.dormitorymanagementsystem.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

public class InfoEditBank extends AppCompatActivity {

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference("Contact");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_edit_bank);

        String bank = getIntent().getStringExtra("bank");
        int icon = Integer.parseInt(getIntent().getStringExtra("icon"));

        TextView txBank = findViewById(R.id.txBank);
        EditText etName = findViewById(R.id.etName);
        EditText etAccNo = findViewById(R.id.etAccNo);
        Button btConfirm = findViewById(R.id.btConfirm);
        ImageView imageView = findViewById(R.id.imageView);

        txBank.setText(bank);
        Glide.with(getApplicationContext()).load(icon).fitCenter().centerCrop().into(imageView);

        btConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        myRef.child("contactBank").child("bank").setValue(bank);
                        myRef.child("contactBank").child("icon").setValue(String.valueOf(icon));
                        myRef.child("contactBank").child("name").setValue(etName.getText().toString());
                        myRef.child("contactBank").child("accNo").setValue(etAccNo.getText().toString());
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