package com.example.dormitorymanagementsystem.ChatNew;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.dormitorymanagementsystem.Adapter.AdapterTabLayout;
import com.example.dormitorymanagementsystem.Login;
import com.example.dormitorymanagementsystem.R;
import com.example.dormitorymanagementsystem.notifications.Token;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

public class ContactActivity extends AppCompatActivity {

    String mUID = Login.getGbIdUser();
    String userID = Login.getGbIdUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        ImageView arrow_back = findViewById(R.id.ic_arrow_back);
        arrow_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        TabLayout tabLayout = findViewById(R.id.tabChat);
        ViewPager viewPager = findViewById(R.id.vpChat);
        tabLayout.setupWithViewPager(viewPager);

        AdapterTabLayout adapterTabLayout = new AdapterTabLayout(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        adapterTabLayout.addFragment(new UsersFragment(),"ผู้ใช้งาน");
        adapterTabLayout.addFragment(new ChatListFragment(),"แชท");
        viewPager.setAdapter(adapterTabLayout);

        /*com.google.firebase.messaging.FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<String> task) {
                if (!task.isSuccessful()){
                    return;
                }
                mUID = task.getResult();
                String user = Login.getGbIdUser();
                updateToken(mUID);
                checkUserStatus();
            }
        });*/


        //updateToken(mUID);
        //checkUserStatus();

    }

    /*@Override
    protected void onResume() {
        checkUserStatus();
        super.onResume();
    }

    private void checkUserStatus(){

        SharedPreferences sp = getSharedPreferences("SP_USER", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("Current_USERID", mUID);
        editor.apply();
    }

    public void updateToken(String token) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Tokens");
        Token mToken = new Token(token);
        ref.child(userID).setValue(mToken);
    }*/
}