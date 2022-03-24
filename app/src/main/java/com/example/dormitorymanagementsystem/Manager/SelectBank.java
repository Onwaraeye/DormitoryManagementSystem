package com.example.dormitorymanagementsystem.Manager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dormitorymanagementsystem.Adapter.AdapterBank;
import com.example.dormitorymanagementsystem.Adapter.AdapterManagerPhone;
import com.example.dormitorymanagementsystem.Adapter.AdapterNameMember;
import com.example.dormitorymanagementsystem.Model.BankModel;
import com.example.dormitorymanagementsystem.Model.ManagerModel;
import com.example.dormitorymanagementsystem.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class SelectBank extends AppCompatActivity {

    private List<BankModel> list = new ArrayList<>();
    private BankModel bankModel;
    private RecyclerView recyclerView;
    private AdapterBank adapter;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_bank);

        mContext = getApplication();

        recyclerView = findViewById(R.id.list_item);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));

        list.add(new BankModel(R.drawable.kbank,"กสิกรไทย"));
        list.add(new BankModel(R.drawable.bangkok,"กรุงเทพ"));
        list.add(new BankModel(R.drawable.krungthai,"กรุงไทย"));
        list.add(new BankModel(R.drawable.krungsri,"กรุงศรี"));
        list.add(new BankModel(R.drawable.tmb,"ทหารไทยธนชาต"));
        list.add(new BankModel(R.drawable.scb,"ไทยพาณิชย์"));
        list.add(new BankModel(R.drawable.government_savings,"ออมสิน"));

        adapter = new AdapterBank(SelectBank.this,list);
        recyclerView.setAdapter(adapter);

        ImageView arrow_back = findViewById(R.id.ic_arrow_back);
        arrow_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}