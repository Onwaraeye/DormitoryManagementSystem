package com.example.dormitorymanagementsystem.Manager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dormitorymanagementsystem.Adapter.ChatAdapter;
import com.example.dormitorymanagementsystem.Adapter.MessageAdapter;
import com.example.dormitorymanagementsystem.Login;
import com.example.dormitorymanagementsystem.MainActivity;
import com.example.dormitorymanagementsystem.Model.ChatList;
import com.example.dormitorymanagementsystem.Model.MemoryData;
import com.example.dormitorymanagementsystem.Model.Messages;
import com.example.dormitorymanagementsystem.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Chat extends AppCompatActivity {

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference();

    private List<ChatList> chatLists = new ArrayList<>();
    private String chatKey;
    private String getUserID = "";
    private RecyclerView chattingRecycleView;
    private ChatAdapter chatAdapter;
    private boolean loadingFirstTime = true;
    private String getType = Login.getGbTypeUser();
    private int readCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        TextView txUserID = findViewById(R.id.txUserID);
        EditText messageEditTxt = findViewById(R.id.messageEditTxt);
        ImageView sendBtn = findViewById(R.id.sendBtn);

        chattingRecycleView = findViewById(R.id.chattingRecycleView);

        getUserID = Login.getGbIdUser();
        String getID = getIntent().getStringExtra("userID");
        if (getType.equals("Admin")) {
            chatKey = getID;
        } else {
            chatKey = getUserID;
        }

        txUserID.setText(getID);

        chattingRecycleView.setHasFixedSize(true);
        chattingRecycleView.setLayoutManager(new LinearLayoutManager(Chat.this));

        chatAdapter = new ChatAdapter(chatLists, Chat.this);
        chattingRecycleView.setAdapter(chatAdapter);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {


                if (snapshot.hasChild("Chats")) {
                    if (getType.equals("Admin")) {
                        chatKey = getID;
                        Log.e("getType", getType);
                        Log.e("getID", getID);
                    } else {
                        chatKey = getUserID;

                    }
                }

                if (snapshot.hasChild("Chats")) {

                    if (snapshot.child("Chats").child(chatKey).hasChild("messages")) {

                        chatLists.clear();

                        for (DataSnapshot messagesSnapshot : snapshot.child("Chats").child(chatKey).child("messages").getChildren()) {

                            if (messagesSnapshot.hasChild("msg") && messagesSnapshot.hasChild("userID")) {

                                String messageTimestamps = messagesSnapshot.getKey();
                                String getID = messagesSnapshot.child("userID").getValue(String.class);
                                String getMsg = messagesSnapshot.child("msg").getValue(String.class);

                                Timestamp timestamp = new Timestamp(Long.parseLong(messageTimestamps));
                                Date date = new Date(timestamp.getTime());
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yy", Locale.getDefault());
                                SimpleDateFormat simpleTimeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());

                                ChatList chatList = new ChatList(getID, getMsg, simpleDateFormat.format(date), simpleTimeFormat.format(date));
                                chatLists.add(chatList);

                                if (loadingFirstTime || Long.parseLong(messageTimestamps) > Long.parseLong(MemoryData.getLastMsgTST(Chat.this, chatKey))) {

                                    loadingFirstTime = false;
                                    MemoryData.saveLastMsgTST(messageTimestamps, chatKey, Chat.this);
                                    chatAdapter.updateChatList(chatLists);

                                    chattingRecycleView.scrollToPosition(chatLists.size() - 1);

                                }
                            }

                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String getTxtMessage = messageEditTxt.getText().toString();
                String currentTimestamp = String.valueOf(System.currentTimeMillis());
                //String currentTimestamp = String.valueOf(System.currentTimeMillis()).substring(0, 10);

                myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        Log.e("11",snapshot.child("Chats").child(chatKey).child("sendMsg").getValue(Long.class)+"");
                        Long read = snapshot.child("Chats").child(chatKey).child("sendMsg").getValue(Long.class);
                        if (read.intValue() == 0){
                            readCount = 1;
                            myRef.child("Chats").child(chatKey).child("user_1").setValue(getUserID);
                            myRef.child("Chats").child(chatKey).child("user_2").setValue(getID);
                            myRef.child("Chats").child(chatKey).child("messages").child(currentTimestamp).child("msg").setValue(getTxtMessage);
                            myRef.child("Chats").child(chatKey).child("messages").child(currentTimestamp).child("userID").setValue(getUserID);
                            myRef.child("Chats").child(chatKey).child("sendMsg").setValue(readCount);
                        }else {
                            myRef.child("Chats").child(chatKey).child("user_1").setValue(getUserID);
                            myRef.child("Chats").child(chatKey).child("user_2").setValue(getID);
                            myRef.child("Chats").child(chatKey).child("messages").child(currentTimestamp).child("msg").setValue(getTxtMessage);
                            myRef.child("Chats").child(chatKey).child("messages").child(currentTimestamp).child("userID").setValue(getUserID);
                            myRef.child("Chats").child(chatKey).child("sendMsg").setValue(readCount);
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {
                    }
                });
                messageEditTxt.setText("");
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