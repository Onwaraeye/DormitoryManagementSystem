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

        list.add(new BankModel("https://firebasestorage.googleapis.com/v0/b/dekhordemo-23dde.appspot.com/o/Bank%2Fkbank.png?alt=media&token=7c8d6aff-dec5-4741-952c-196c60c7063e","กสิกรไทย"));
        list.add(new BankModel("https://firebasestorage.googleapis.com/v0/b/dekhordemo-23dde.appspot.com/o/Bank%2Fbangkok.png?alt=media&token=33cee91e-0103-4a92-9d4d-bf961aa5660b","กรุงเทพ"));
        list.add(new BankModel("https://firebasestorage.googleapis.com/v0/b/dekhordemo-23dde.appspot.com/o/Bank%2Fkrungthai.png?alt=media&token=0633e88e-b25d-4fda-b014-3ca383fd2cb2","กรุงไทย"));
        list.add(new BankModel("https://firebasestorage.googleapis.com/v0/b/dekhordemo-23dde.appspot.com/o/Bank%2Fkrungsri.png?alt=media&token=3bb51409-49be-4e7a-a661-028fb9ae52ac","กรุงศรี"));
        list.add(new BankModel("https://firebasestorage.googleapis.com/v0/b/dekhordemo-23dde.appspot.com/o/Bank%2Ftmb.png?alt=media&token=dd5f7e08-6b0e-43f7-ba2c-b0151a638a52","ทหารไทยธนชาต"));
        list.add(new BankModel("https://firebasestorage.googleapis.com/v0/b/dekhordemo-23dde.appspot.com/o/Bank%2Fscb.png?alt=media&token=a09e8939-7582-48ca-881d-2fb569452c94","ไทยพาณิชย์"));
        list.add(new BankModel("https://firebasestorage.googleapis.com/v0/b/dekhordemo-23dde.appspot.com/o/Bank%2Fgovernment_savings.png?alt=media&token=4fcbe342-4fe2-44d8-a250-f2b07ad13e31","ออมสิน"));

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