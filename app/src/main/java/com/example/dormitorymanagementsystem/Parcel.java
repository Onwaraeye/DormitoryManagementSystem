package com.example.dormitorymanagementsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.dormitorymanagementsystem.Adapter.AdapterTabLayout;
import com.example.dormitorymanagementsystem.Fragment.HistoryPacelFragment;
import com.example.dormitorymanagementsystem.Fragment.NewParcelFragment;
import com.example.dormitorymanagementsystem.Manager.SentParcel;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class Parcel extends AppCompatActivity {

    private DatabaseReference myRefRoom = FirebaseDatabase.getInstance().getReference("Room");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parcel);

        //Context mContext = getApplication();

        String typeUser = Login.getGbTypeUser();

        List<String> listRoom = new ArrayList<>();

        myRefRoom.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    listRoom.add(ds.getKey());
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

        TabLayout tabLayout = findViewById(R.id.tabParcel);
        ViewPager viewPager = findViewById(R.id.vpParcel);
        tabLayout.setupWithViewPager(viewPager);


        AdapterTabLayout adapterTabLayout = new AdapterTabLayout(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);

        adapterTabLayout.addFragment(new NewParcelFragment(), "พัสดุใหม่");
        adapterTabLayout.addFragment(new HistoryPacelFragment(), "ประวัติพัสดุ");
        if (typeUser.equals("Admin")){
            Button button = findViewById(R.id.button);
            button.setVisibility(View.VISIBLE);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), SentParcel.class);
                    intent.putStringArrayListExtra("timeAdd", (ArrayList<String>) listRoom);
                    startActivity(intent);
                }
            });
        }

        viewPager.setAdapter(adapterTabLayout);
    }
}