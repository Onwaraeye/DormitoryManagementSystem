package com.example.dormitorymanagementsystem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.dormitorymanagementsystem.ChatNew.AdapterChatlist;
import com.example.dormitorymanagementsystem.Fragment.AnnounceFragment;
import com.example.dormitorymanagementsystem.Fragment.HomeFragment;
import com.example.dormitorymanagementsystem.Fragment.SettingFragment;
import com.example.dormitorymanagementsystem.Manager.SentParcel;
import com.example.dormitorymanagementsystem.notifications.Token;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    String mUID = "";
    String userID = Login.getGbIdUser();
    String typeUser = Login.getGbTypeUser();
    String fromNoti = "";
    String room = Login.getGbNumroom();
    String notify = "0";
    private int day,month,year;
    private String monthThai = "";

    private BottomNavigationView bottomNavigationView;

    String isSeen;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        month = Calendar.getInstance().get(Calendar.MONTH)+1;
        year = Calendar.getInstance().get(Calendar.YEAR);

        bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setOnNavigationItemSelectedListener(bottomNavMethod);
        fromNoti = getIntent().getStringExtra("postFromNotification");

        if (typeUser.equals("Repairman")){
            bottomNavigationView.getMenu().removeItem(R.id.announce);
        }

        if (fromNoti!=null && fromNoti.equals("postFromNotification")){
            getSupportFragmentManager().beginTransaction().replace(R.id.container,new AnnounceFragment()).commit();
        }else {
            getSupportFragmentManager().beginTransaction().replace(R.id.container,new HomeFragment()).commit();
        }


        com.google.firebase.messaging.FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<String> task) {
                if (!task.isSuccessful()){
                    return;
                }
                mUID = task.getResult();
                updateToken(mUID);
                checkUserStatus();
            }
        });

        prepareNotification(
                ""+System.currentTimeMillis(),
                "แจ้งเตือนบิล",
                "เดือน"+getMonth(month),
                "BillNotification",
                "POST");

    }

    @Override
    protected void onResume() {
        checkUserStatus();
        super.onResume();
    }

    private void prepareNotification(String pId, String title, String description, String notificationType, String notificationTopic) {
        DatabaseReference refBill = FirebaseDatabase.getInstance().getReference("Bills");
        refBill.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                Log.e("Data", "onChildAdded");
            }
            @Override
            public void onChildChanged(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                Log.e("Data", "onChildChanged");
                if (snapshot.child(""+month).child(room).child("status").getValue(String.class) != null && snapshot.child(""+month).child(room).child("status").getValue(String.class).equals("0")){
                    Log.e("Data", "DataBill");
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
                    Query query = ref.orderByChild("numroom").equalTo(room);
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull @NotNull DataSnapshot snapshotUser) {
                            Log.e("Data", "DataUser");
                            for (DataSnapshot ds : snapshotUser.getChildren()){
                                DatabaseReference refTo = FirebaseDatabase.getInstance().getReference("Tokens");
                                refTo.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshotTo) {
                                        Log.e("Data", "DataToken"+ds.getKey());
                                        if (snapshotTo.child(ds.getKey()).getKey().equals(userID)){
                                            String NOTIFICATION_TOPIC = snapshotTo.child(ds.getKey()).child("token").getValue(String.class);
                                            String NOTIFICATION_TITLE = title;
                                            String NOTIFICATION_MESSAGE = description;
                                            String NOTIFICATION_TYPE = notificationType;

                                            JSONObject notificationJo = new JSONObject();
                                            JSONObject notificationBodyJo = new JSONObject();

                                            try {
                                                notificationBodyJo.put("notificationType", NOTIFICATION_TYPE);
                                                notificationBodyJo.put("pId", pId);
                                                notificationBodyJo.put("pTitle", NOTIFICATION_TITLE);
                                                notificationBodyJo.put("pDescription", NOTIFICATION_MESSAGE);

                                                notificationJo.put("to", NOTIFICATION_TOPIC);
                                                notificationJo.put("data", notificationBodyJo);

                                            } catch (Exception e) {
                                                //Toast.makeText(this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                            Log.e("notibill",notificationJo.toString());
                                            sendBillNotification(notificationJo);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                    }
                                });
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull @NotNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onChildRemoved(@NonNull @NotNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    private void sendBillNotification(JSONObject notificationJo) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,"https://fcm.googleapis.com/fcm/send", notificationJo,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("FCM_RESPONSE", "onResponse" + response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(Post.this, ""+error.toString(), Toast.LENGTH_SHORT).show();
                        Log.e("FCM_RESPONSE",  error.toString()+" onResponseError "+ error.getMessage());
                    }
                })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("Authorization", "key=AAAAfLk6hVI:APA91bGmlTXFRoHjFSfv_qpaQw1NmIi0B5p-lluErCtQtY1TCbMkCoF8pr73q3_rE33rgOhhl0o_Os4vAY4x3oLeKpiO8LlilnvyOQ8SeIad6Byti4yTHlGyZLOeDdF7PwllZyxQ8clP");
                return headers;
            }
        };
        Volley.newRequestQueue(MainActivity.this).add(jsonObjectRequest);
    }

    public String getMonth(int month){
        switch(month) {
            case 1:
                monthThai = "มกราคม";
                break;
            case 2:
                monthThai = "กุมภาพันธ์";
                break;
            case 3:
                monthThai = "มีนาคม";
                break;
            case 4:
                monthThai = "เมษายน";
                break;
            case 5:
                monthThai = "พฤษภาคม";
                break;
            case 6:
                monthThai = "มิถุนายน";
                break;
            case 7:
                monthThai = "กรกฎาคม";
                break;
            case 8:
                monthThai = "สิงหาคม";
                break;
            case 9:
                monthThai = "กันยายน";
                break;
            case 10:
                monthThai = "ตุลาคม";
                break;
            case 11:
                monthThai = "พฤศจิกายน";
                break;
            case 12:
                monthThai = "ธันวาคม";
                break;
        }
        return monthThai;
    }

    private BottomNavigationView.OnNavigationItemSelectedListener bottomNavMethod = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            Fragment fragment = null;

            switch(item.getItemId()){
                case R.id.home:
                    fragment = new HomeFragment();
                    break;

                case R.id.announce:
                    fragment = new AnnounceFragment();
                    break;

                case R.id.setting:
                    fragment = new SettingFragment();
                    break;
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.container,fragment).commit();
            return true;
        }
    };

    private void checkUserStatus(){

        SharedPreferences sp = getSharedPreferences("SP_USER", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("Current_USERID", mUID);
        editor.apply();
    }

    public void updateToken(String token) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Tokens");
        Token mToken = new Token(token,typeUser);
        ref.child(userID).setValue(mToken);
    }
}