package com.example.dormitorymanagementsystem.ChatNew;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.dormitorymanagementsystem.Login;
import com.example.dormitorymanagementsystem.Parcel;
import com.example.dormitorymanagementsystem.R;
import com.example.dormitorymanagementsystem.notifications.Data;
import com.example.dormitorymanagementsystem.notifications.Sender;
import com.example.dormitorymanagementsystem.notifications.Token;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.internal.FirebaseInstanceIdInternal;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ImageView sendBtn,attachBtn;
    TextView nameTv,roomTv;
    EditText messageEt;

    ValueEventListener seenListener;
    DatabaseReference userRefForSeen;

    List<ModelChat> chatList;
    AdapterChat adapterChat;

    String hisUid;
    String myUid;
    String hisImage;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference("Users");
    private DatabaseReference myRefChat = database.getReference("Chats").push();

    int notify = 0;

    private static final int IMAGE_REQUEST = 1;
    private Uri imUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        recyclerView = findViewById(R.id.chat_recyclerView);
        nameTv = findViewById(R.id.nameTv);
        roomTv = findViewById(R.id.roomTv);
        sendBtn = findViewById(R.id.sendBtn);
        messageEt = findViewById(R.id.messageEt);
        attachBtn = findViewById(R.id.attachBtn);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);


        Intent intent = getIntent();
        hisUid = intent.getStringExtra("hisUid");
        myUid = Login.getGbIdUser();



        Query userQuery = myRef.orderByChild("id").equalTo(hisUid);
        //get user name
        userQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                for (DataSnapshot ds: snapshot.getChildren()){
                    String name = ds.child("firstname").getValue(String.class) + " "+ds.child("lastname").getValue(String.class);
                    String room = "ห้อง "+ds.child("numroom").getValue(String.class);
                    hisImage = ds.child("pictureUserUrl").getValue(String.class);

                    nameTv.setText(name);
                    roomTv.setText(room);

                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notify = 1;
                String message = messageEt.getText().toString().trim();
                if (TextUtils.isEmpty(message)){
                    Toast.makeText(ChatActivity.this,"กรุณากรอกข้อความ",Toast.LENGTH_SHORT).show();
                }
                else {
                    sendMessage(message);
                }
                messageEt.setText("");
            }
        });

        attachBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImage();
            }
        });

        readMessages();

        seenMessages();

        ImageView arrow_back = findViewById(R.id.ic_arrow_back);
        arrow_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void seenMessages() {
        userRefForSeen = FirebaseDatabase.getInstance().getReference("Chats");
        seenListener = userRefForSeen.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                for (DataSnapshot ds: snapshot.getChildren()){
                    ModelChat chat = ds.getValue(ModelChat.class);
                    if (chat.getReceiver().equals(myUid) && chat.getSender().equals(hisUid)){
                        HashMap<String, Object> hasSeenHashMap = new HashMap<>();
                        hasSeenHashMap.put("isSeen", 1);
                        ds.getRef().updateChildren(hasSeenHashMap);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    private void readMessages() {
        chatList = new ArrayList<>();
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("Chats");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                chatList.clear();
                for (DataSnapshot ds: snapshot.getChildren()){
                    ModelChat chat = ds.getValue(ModelChat.class);
                    if (chat.getReceiver().equals(myUid) && chat.getSender().equals(hisUid) || chat.getReceiver().equals(hisUid) && chat.getSender().equals(myUid)){
                        chatList.add(chat);
                    }
                    adapterChat = new AdapterChat(ChatActivity.this, chatList, hisImage);
                    adapterChat.notifyDataSetChanged();

                    recyclerView.setAdapter(adapterChat);
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

    }

    private void sendMessage(String message) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        String timestamp = String.valueOf(System.currentTimeMillis());

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender",myUid);
        hashMap.put("receiver", hisUid);
        hashMap.put("message", message);
        hashMap.put("timestamp", timestamp);
        hashMap.put("isSeen", 0);
        hashMap.put("type", "text");
        databaseReference.child("Chats").push().setValue(hashMap);



        String msg = message;
        DatabaseReference database = FirebaseDatabase.getInstance().getReference("Users").child(myUid);
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                ModelUser user = snapshot.getValue(ModelUser.class);
                if (notify==1) {
                    senNotification(hisUid, user.getFirstname()+" "+user.getLastname(), message);
                }
                notify = 0;
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    private void sendImageMessage(String message) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        String timestamp = String.valueOf(System.currentTimeMillis());

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender",myUid);
        hashMap.put("receiver", hisUid);
        hashMap.put("message", message);
        hashMap.put("timestamp", timestamp);
        hashMap.put("isSeen", 0);
        hashMap.put("type", "image");
        databaseReference.child("Chats").push().setValue(hashMap);

        String msg = message;
        DatabaseReference database = FirebaseDatabase.getInstance().getReference("Users").child(myUid);
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                ModelUser user = snapshot.getValue(ModelUser.class);
                if (notify==1) {
                    senNotification(hisUid, user.getFirstname()+" "+user.getLastname(), message);
                }
                notify = 0;
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    private void senNotification(String hisUid, String name, String message) {
        DatabaseReference allToken = FirebaseDatabase.getInstance().getReference("Token");
        Query query = allToken.orderByKey().equalTo(hisUid);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Token token = ds.getValue(Token.class);
                    Data data = new Data(myUid, name + " : " + message, "New_Message", hisUid, R.drawable.ic_bx_bxs_user_circle);

                    Sender sender = new Sender(data, token.getToken());

                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        userRefForSeen.removeEventListener(seenListener);
    }

    private void openImage(){
        Intent intent = new Intent();
        intent.setType("image/");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_REQUEST && resultCode == RESULT_OK){
            imUri = data.getData();
            uploadImage();
        }

    }

    private String getFileExtension(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void uploadImage(){
        ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("กำลังอัพโหลด");
        pd.show();

        if (imUri != null){
            StorageReference fileRef = FirebaseStorage.getInstance().getReference().child("uploadsChars").child(System.currentTimeMillis()+"."+getFileExtension(imUri));

            fileRef.putFile(imUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String imageURL = uri.toString();
                            Toast.makeText(getApplicationContext(),"อัพโหลดสำเร็จ",Toast.LENGTH_SHORT).show();
                            sendImageMessage(imageURL);
                            pd.dismiss();
                        }
                    });
                }
            });
        }
    }
}