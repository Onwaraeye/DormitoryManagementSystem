package com.example.dormitorymanagementsystem.Manager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.dormitorymanagementsystem.Adapter.MessageAdapter;
import com.example.dormitorymanagementsystem.Login;
import com.example.dormitorymanagementsystem.MainActivity;
import com.example.dormitorymanagementsystem.Model.MemoryData;
import com.example.dormitorymanagementsystem.Model.Messages;
import com.example.dormitorymanagementsystem.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ChatManager extends AppCompatActivity {

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference();
    private String userID;

    private boolean dataSet = false;
    private RecyclerView messagesRecycleView;
    private MessageAdapter messageAdapter;
    private List<Messages> messagesList = new ArrayList<>();

    private String chatKey = "";
    private String lastMessege = "";
    private int unseenMessages = 0;
    private int readCount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_manager);

        userID = Login.getGbIdUser();

        messagesRecycleView = findViewById(R.id.list_item);
        messagesRecycleView.setHasFixedSize(true);
        messagesRecycleView.setLayoutManager(new LinearLayoutManager(this));

        messageAdapter = new MessageAdapter(ChatManager.this, messagesList);
        messagesRecycleView.setAdapter(messageAdapter);

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                messagesList.clear();
                unseenMessages = 0;
                lastMessege = "";
                chatKey = userID;
                //for (DataSnapshot dataSnapshot : snapshot.child("Users").getChildren()) {
                for (DataSnapshot dataSnapshot : snapshot.child("Users").getChildren()) {
                    String getUserID = dataSnapshot.getKey();
                    //dataSet = false;
                    if (!getUserID.equals(userID)) {

                        myRef.child("Chats").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                                int getChatCounts = (int) snapshot.getChildrenCount();

                                if (getChatCounts > 0) {

                                    for (DataSnapshot dataSnapshot1 : snapshot.getChildren()) {

                                        String getKey = dataSnapshot1.getKey();
                                        chatKey = getKey;

                                        if (dataSnapshot1.hasChild("user_1") && dataSnapshot1.hasChild("user_2") && dataSnapshot1.hasChild("messages")) {

                                            String getUserOne = dataSnapshot1.child("user_1").getValue(String.class);
                                            String getUserTwo = dataSnapshot1.child("user_2").getValue(String.class);


                                            if ((getUserOne.equals(getUserID) && getUserTwo.equals(userID)) || (getUserOne.equals(userID) && getUserTwo.equals(getUserID))) {

                                                Log.e("readCount", readCount + "");
                                                for (DataSnapshot chatDataSnapshop : dataSnapshot1.child("messages").getChildren()) {

                                                    long getMessageKey = Long.parseLong(chatDataSnapshop.getKey());
                                                    long getLastSeenMessage = Long.parseLong(MemoryData.getLastMsgTST(ChatManager.this, getKey));
                                                    lastMessege = chatDataSnapshop.child("msg").getValue(String.class);
                                                    if (getMessageKey > getLastSeenMessage) {
                                                        unseenMessages++;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                                //if (!dataSet){
                                //dataSet = true;
                                Messages messages = new Messages(getUserID, lastMessege,chatKey, unseenMessages);
                                messagesList.add(messages);
                                messageAdapter.updateData(messagesList);

                                //}

                            }

                            @Override
                            public void onCancelled(@NonNull @NotNull DatabaseError error) {

                            }
                        });
                    }
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
    }
}