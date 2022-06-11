package com.example.dormitorymanagementsystem.Manager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.dormitorymanagementsystem.Adapter.AdapterRoom;
import com.example.dormitorymanagementsystem.Model.RoomModel;
import com.example.dormitorymanagementsystem.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ViewRoom extends AppCompatActivity {

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference("Room");
    private DatabaseReference myRefUser = database.getReference("Users");
    private List<RoomModel> list = new ArrayList<>();
    private List<String> stringList = new ArrayList<>();
    private AdapterRoom adapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_room);

        Context mContext = getApplication();
        recyclerView = findViewById(R.id.list_item);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));


        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot ds : snapshot.getChildren()){
                    if (snapshot.hasChild(ds.getKey())){
                        if(snapshot.child(ds.getKey()).getChildrenCount()==0){
                            List<String> listnull = new ArrayList<>();
                            listnull.add("0");
                            RoomModel roomModel = new RoomModel(ds.getKey(),listnull);
                            list.add(roomModel);
                        }else{
                            GenericTypeIndicator<List<String>> genericTypeIndicator = new GenericTypeIndicator<List<String>>(){};
                            final List<String> listMember = snapshot.child(ds.getKey()).getValue(genericTypeIndicator);
                            RoomModel roomModel = new RoomModel(ds.getKey(),listMember);
                            list.add(roomModel);
                        }

                    }
                }
                adapter = new AdapterRoom(mContext,list);
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