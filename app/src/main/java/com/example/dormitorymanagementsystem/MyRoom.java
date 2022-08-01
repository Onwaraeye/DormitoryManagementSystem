package com.example.dormitorymanagementsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dormitorymanagementsystem.Adapter.AdapterNameMember;
import com.example.dormitorymanagementsystem.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MyRoom extends AppCompatActivity {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("Room");
    DatabaseReference myRefUser = database.getReference("Users");

    private Context mContext;

    RecyclerView recyclerView;
    AdapterNameMember adapterNameMember;
    List<String> listName = new ArrayList<>();;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_room);

        mContext = getApplication();

        String numroom = Login.getGbNumroom();

        TextView txNumroom = findViewById(R.id.numRoom);
        txNumroom.setText(numroom);
        TextView txMember = findViewById(R.id.txMember);

        recyclerView = findViewById(R.id.userList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(numroom)){
                    //GenericTypeIndicator<List<String>> genericTypeIndicator = new GenericTypeIndicator<List<String>>(){};
                    //final List<String> listUser = snapshot.child(numroom).getValue(genericTypeIndicator);
                    List<String> listMember = new ArrayList<>();
                    for (int i=0 ; i<=3 ; i++){
                        String member = snapshot.child(numroom).child(String.valueOf(i)).getValue(String.class);
                        if (member == null){

                        }else {
                            listMember.add(member);
                        }
                    }
                    if (listMember.size()!=0){
                        txMember.setText("สมาชิก "+listMember.size()+" คน");
                    }else {
                        txMember.setText("0");
                    }
                    myRefUser.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (int i = 0 ; i<listMember.size() ; i++){
                                if (snapshot.hasChild(listMember.get(i))){
                                    final String fName = snapshot.child(listMember.get(i)).child("firstname").getValue(String.class);
                                    final String lName = snapshot.child(listMember.get(i)).child("lastname").getValue(String.class);
                                    final String name = fName+" "+lName;
                                    Log.e("nameMyRoom",name);
                                    listName.add(name);
                                }
                            }
                            adapterNameMember = new AdapterNameMember(mContext,listName);
                            recyclerView.setAdapter(adapterNameMember);
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        ImageView arrow_back = findViewById(R.id.ic_arrow_back);
        arrow_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ( getFragmentManager().getBackStackEntryCount() > 0)
                {
                    getFragmentManager().popBackStack();
                    return;
                }
                onBackPressed();
            }
        });
    }
}