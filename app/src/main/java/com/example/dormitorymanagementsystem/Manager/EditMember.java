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
    private DatabaseReference myRefRoom = database.getReference("Room");
    private AdapterEditMember adapter;
    private RecyclerView recyclerView;
    private List<EditMemberModel> listNameMember = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_member);

        list = getIntent().getStringArrayListExtra("listMember");
        String getRoom = getIntent().getStringExtra("room");

        Context mContext = getApplication();
        recyclerView = findViewById(R.id.userList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));

        TextView txRoom = findViewById(R.id.numRoom);
        txRoom.setText(getRoom);
        TextView txMember = findViewById(R.id.txMember);

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                myRefRoom.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshotRoom) {
                        listNameMember.clear();
                        for (String userID : list) {
                            if (!userID.equals("")) {
                                if (snapshot.hasChild(userID)) {
                                    final String fName = snapshot.child(userID).child("firstname").getValue(String.class);
                                    final String lName = snapshot.child(userID).child("lastname").getValue(String.class);
                                    final String name = fName + " " + lName;
                                    String roleRoom = snapshot.child(userID).child("roleRoom").getValue(String.class);
                                    String imageUser = snapshot.child(userID).child("pictureUserUrl").getValue(String.class);
                                    EditMemberModel editMemberModel = new EditMemberModel(userID, name, getRoom,roleRoom,imageUser);
                                    listNameMember.add(editMemberModel);
                                    txMember.setText("จำนวนสมาชิก " + list.size() + " คน");
                                }
                            }
                            adapter = new AdapterEditMember(EditMember.this, listNameMember);
                            adapter.notifyDataSetChanged();
                            recyclerView.setAdapter(adapter);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                    }
                });
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

        List<String> listMember = new ArrayList<>();
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    if (snapshot.child(ds.getKey()).child("numroom").getValue(String.class)!= null &&snapshot.child(ds.getKey()).child("numroom").getValue(String.class).isEmpty()){
                        listMember.add(ds.getKey());
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

        ImageView menu_add = findViewById(R.id.menu_add);
        menu_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), EditAddMember.class);
                intent.putStringArrayListExtra("timeAdd", (ArrayList<String>) listMember);
                intent.putExtra("room", getRoom);
                intent.putExtra("from","");
                startActivity(intent);
            }
        });

    }
}