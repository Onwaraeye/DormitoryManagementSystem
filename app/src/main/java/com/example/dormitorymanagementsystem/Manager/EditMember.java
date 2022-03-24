package com.example.dormitorymanagementsystem.Manager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dormitorymanagementsystem.Adapter.AdapterEditMember;
import com.example.dormitorymanagementsystem.Model.EditMemberModel;
import com.example.dormitorymanagementsystem.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class EditMember extends AppCompatActivity {

    private List<String> list = new ArrayList<>();
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference("Users");
    private AdapterEditMember adapter;
    private RecyclerView recyclerView;
    private List<EditMemberModel> listNameMember = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_member);

        Intent intent = getIntent();
        list = intent.getStringArrayListExtra("listMember");
        String getRoom = intent.getStringExtra("room");

        Context mContext = getApplication();
        recyclerView = findViewById(R.id.userList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));

        TextView txRoom = findViewById(R.id.numRoom);
        txRoom.setText(getRoom);
        TextView txMember = findViewById(R.id.txMember);

        if (list.isEmpty()){
            txMember.setText("จำนวนสมาชิก 0 คน");
        }else {
            txMember.setText("จำนวนสมาชิก "+list.size()+" คน");
        }

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                listNameMember.clear();
                for (String userID : list){
                    if (snapshot.hasChild(userID)){
                        final String fName = snapshot.child(userID).child("firstname").getValue(String.class);
                        final String lName = snapshot.child(userID).child("lastname").getValue(String.class);
                        final String name = fName+" "+lName;
                        EditMemberModel editMemberModel = new EditMemberModel(userID,name,getRoom);
                        listNameMember.add(editMemberModel);
                    }
                }
                adapter = new AdapterEditMember(EditMember.this,listNameMember);
                recyclerView.setAdapter(adapter);
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