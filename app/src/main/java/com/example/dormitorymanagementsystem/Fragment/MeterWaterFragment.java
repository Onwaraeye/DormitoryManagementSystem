package com.example.dormitorymanagementsystem.Fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.dormitorymanagementsystem.Adapter.AdapterMeterRecord;
import com.example.dormitorymanagementsystem.Adapter.AdapterMeterWaterRecord;
import com.example.dormitorymanagementsystem.Model.RoomModel;
import com.example.dormitorymanagementsystem.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class MeterWaterFragment extends Fragment {

    DatabaseReference myRefRoom = FirebaseDatabase.getInstance().getReference("Room");

    List<RoomModel> listRoom = new ArrayList<>();
    AdapterMeterWaterRecord adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_meter_water, container, false);

        Context mContext = getActivity().getApplication();

        RecyclerView recyclerView = view.findViewById(R.id.list_item);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));

        if (getArguments() != null){
            String monthCurrent = getArguments().getString("monthCurrent");
            String yearCurrent = getArguments().getString("yearCurrent");
            String date = getArguments().getString("date");

            TextView textView = view.findViewById(R.id.textView);
            textView.setText(date);


            myRefRoom.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                    listRoom.clear();
                    for (DataSnapshot ds : snapshot.getChildren()){
                        if (snapshot.hasChild(ds.getKey())){
                            if(snapshot.child(ds.getKey()).getChildrenCount()==0){
                                List<String> listnull = new ArrayList<>();
                                listnull.add("0");
                                RoomModel roomModel = new RoomModel(ds.getKey(),listnull);
                                listRoom.add(roomModel);
                            }else{
                                List<String> listMember = new ArrayList<>();
                                listMember.clear();
                                for (int i=0 ; i<=3 ; i++){
                                    String member = snapshot.child(ds.getKey()).child(String.valueOf(i)).getValue(String.class);
                                    if (member == null){

                                    }else {
                                        listMember.add(member);
                                    }
                                }
                                RoomModel roomModel = new RoomModel(ds.getKey(),listMember);
                                listRoom.add(roomModel);
                            }
                        }
                    }
                    adapter = new AdapterMeterWaterRecord(mContext,listRoom,yearCurrent+"",monthCurrent+"");
                    recyclerView.setAdapter(adapter);
                }

                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) {

                }
            });
        }

        return view;
    }
}