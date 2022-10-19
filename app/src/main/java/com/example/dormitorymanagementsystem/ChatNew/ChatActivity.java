package com.example.dormitorymanagementsystem.ChatNew;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.dormitorymanagementsystem.Login;
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
import com.google.firebase.database.annotations.NotNull;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.theartofdev.edmodo.cropper.CropImage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ImageView sendBtn, attachBtn;
    TextView nameTv, roomTv;
    EditText messageEt;

    ValueEventListener seenListener;
    DatabaseReference userRefForSeen;

    List<ModelChat> chatList;
    AdapterChat adapterChat;

    String hisUid;
    String myUid;
    String hisImage;
    String imageAdmin;
    String typeUser = Login.getGbTypeUser();
    String nameUser = Login.getGbFNameUser()+" "+Login.getGbLNameUser();

    private RequestQueue requestQueue;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference("Users");
    private DatabaseReference refToken = database.getReference("Tokens");
    private DatabaseReference myRefChat = database.getReference("Chats").push();

    private int notify = 0;

    private static final int IMAGE_REQUEST = 1;
    private Uri resultUri;

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

        requestQueue = Volley.newRequestQueue(ChatActivity.this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        Intent intent = getIntent();
        hisUid = intent.getStringExtra("hisUid");

        myUid = Login.getGbIdUser();

        //if (typeUser.equals("User"))

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Contact");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @org.jetbrains.annotations.NotNull DataSnapshot snapshot) {
                imageAdmin = snapshot.child("image").getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull @org.jetbrains.annotations.NotNull DatabaseError error) {

            }
        });

        Query userQuery = myRef.orderByChild("id").equalTo(hisUid);
        //get user name
        userQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    String name = ds.child("firstname").getValue(String.class) + " " + ds.child("lastname").getValue(String.class);
                    String room = ds.child("numroom").getValue(String.class);
                    String role = ds.child("role").getValue(String.class);
                    if (room == null) {
                        if (role.equals("Admin")) {
                            roomTv.setVisibility(View.INVISIBLE);
                            nameTv.setText("นิติบุลคล");
                        } else if (role.equals("Repairman")) {
                            roomTv.setText("ช่างซ่อม");
                        }
                    } else {
                        roomTv.setText("ห้อง " + room);
                        nameTv.setText(name);
                    }
                    if (role != null && role.equals("Admin")){
                        hisImage = imageAdmin;
                    }else {
                        hisImage = ds.child("pictureUserUrl").getValue(String.class);
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
                notify = 1;
                String message = messageEt.getText().toString().trim();
                if (TextUtils.isEmpty(message)) {
                    Toast.makeText(ChatActivity.this, "กรุณากรอกข้อความ", Toast.LENGTH_SHORT).show();
                } else {
                    sendMessage(message);
                }
                messageEt.setText("");
            }
        });

        attachBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean pick=true;
                if (pick==true){
                    if (!checkCameraPermission()){
                        requestCameraPermission();
                    }else PickImage();
                }else {
                    if (!checkStoragePermission()){
                        requestStoragePermission();
                    }else PickImage();
                }
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
                for (DataSnapshot ds : snapshot.getChildren()) {
                    ModelChat chat = ds.getValue(ModelChat.class);
                    if (typeUser.equals("Admin")){
                        if (chat.getReceiver().equals("Mng") && chat.getSender().equals(hisUid)) {
                            HashMap<String, Object> hasSeenHashMap = new HashMap<>();
                            hasSeenHashMap.put("isSeen", 1);
                            ds.getRef().updateChildren(hasSeenHashMap);
                        }
                    }else {
                        if (chat.getReceiver().equals(myUid) && chat.getSender().equals("Mng")) {
                            HashMap<String, Object> hasSeenHashMap = new HashMap<>();
                            hasSeenHashMap.put("isSeen", 1);
                            ds.getRef().updateChildren(hasSeenHashMap);
                        }
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
                for (DataSnapshot ds : snapshot.getChildren()) {
                    ModelChat chat = ds.getValue(ModelChat.class);
                    if (typeUser.equals("Admin")){
                        if (chat.getReceiver().equals("Mng") && chat.getSender().equals(hisUid) || chat.getReceiver().equals(hisUid) && chat.getSender().equals("Mng")) {
                            chatList.add(chat);
                        }
                    }else {
                        if (chat.getReceiver().equals(myUid) && chat.getSender().equals("Mng") || chat.getReceiver().equals("Mng") && chat.getSender().equals(myUid)) {
                            chatList.add(chat);
                        }
                    }
                    /*if (chat.getReceiver().equals(myUid) && chat.getSender().equals(hisUid) || chat.getReceiver().equals(hisUid) && chat.getSender().equals(myUid)) {
                        chatList.add(chat);
                    }*/
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
        if (typeUser.equals("Admin")) {
            hashMap.put("sender", "Mng");
            hashMap.put("receiver", hisUid);
        } else {
            hashMap.put("sender", myUid);
            hashMap.put("receiver", "Mng");
        }
        /*hashMap.put("sender", myUid);
        hashMap.put("receiver", hisUid);*/
        hashMap.put("nameUser",nameUser);
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
                if (notify == 1) {
                    senNotification(hisUid, user.getFirstname() + " " + user.getLastname(), message);
                }
                notify = 0;
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

        //AddChatList
        if (typeUser.equals("Admin")) {
            DatabaseReference chatRef1 = FirebaseDatabase.getInstance().getReference("Chatlist").child("Mng").child(hisUid);
            chatRef1.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull @org.jetbrains.annotations.NotNull DataSnapshot snapshot) {
                    if (!snapshot.exists()) {
                        chatRef1.child("id").setValue(hisUid);

                    }
                }

                @Override
                public void onCancelled(@NonNull @org.jetbrains.annotations.NotNull DatabaseError error) {

                }
            });
            DatabaseReference chatRef2 = FirebaseDatabase.getInstance().getReference("Chatlist").child(hisUid).child("Mng");
            chatRef2.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull @org.jetbrains.annotations.NotNull DataSnapshot snapshot) {
                    if (!snapshot.exists()) {
                        chatRef2.child("id").setValue("Mng");

                    }
                }

                @Override
                public void onCancelled(@NonNull @org.jetbrains.annotations.NotNull DatabaseError error) {

                }
            });
        } else {
            DatabaseReference chatRef1 = FirebaseDatabase.getInstance().getReference("Chatlist").child(myUid).child("Mng");
            chatRef1.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull @org.jetbrains.annotations.NotNull DataSnapshot snapshot) {
                    if (!snapshot.exists()) {
                        chatRef1.child("id").setValue("Mng");

                    }
                }

                @Override
                public void onCancelled(@NonNull @org.jetbrains.annotations.NotNull DatabaseError error) {

                }
            });
            DatabaseReference chatRef2 = FirebaseDatabase.getInstance().getReference("Chatlist").child("Mng").child(myUid);
            chatRef2.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull @org.jetbrains.annotations.NotNull DataSnapshot snapshot) {
                    if (!snapshot.exists()) {
                        chatRef2.child("id").setValue(myUid);

                    }
                }

                @Override
                public void onCancelled(@NonNull @org.jetbrains.annotations.NotNull DatabaseError error) {

                }
            });
        }
    }

    private void sendImageMessage(String message) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        String timestamp = String.valueOf(System.currentTimeMillis());

        HashMap<String, Object> hashMap = new HashMap<>();
        if (typeUser.equals("Admin")) {
            hashMap.put("sender", "Mng");
            hashMap.put("receiver", hisUid);
            hashMap.put("admin",myUid);
        } else {
            hashMap.put("sender", myUid);
            hashMap.put("receiver", "Mng");
        }
        /*hashMap.put("sender", myUid);
        hashMap.put("receiver", hisUid);*/
        hashMap.put("nameUser",nameUser);
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
                if (notify == 1) {
                    senNotification(hisUid, user.getFirstname() + " " + user.getLastname(), message);
                }
                notify = 0;
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    private void senNotification(String hisUid, String name, String message) {
        DatabaseReference allToken = FirebaseDatabase.getInstance().getReference("Tokens");
        if (typeUser.equals("Admin")){
            Query query = allToken.orderByKey().equalTo(hisUid);
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        Token token = ds.getValue(Token.class);
                        Data data = new Data(myUid, name + " : " + message, "ข้อความใหม่", hisUid, "ChatNotification", R.drawable.ic_bx_bxs_user_circle);

                        Sender sender = new Sender(data, token.getToken());

                        try {
                            JSONObject senderJsonObj = new JSONObject(new Gson().toJson(sender));


                            Log.e("JSONObject", senderJsonObj.toString());
                            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, "https://fcm.googleapis.com/fcm/send", senderJsonObj,
                                    new com.android.volley.Response.Listener<JSONObject>() {
                                        @Override
                                        public void onResponse(JSONObject response) {
                                            //Toast.makeText(ChatActivity.this, "onResponse : " + response.toString(), Toast.LENGTH_SHORT).show();
                                            Log.d("JSON_RESPONSE", "onResponse : " + response.toString());
                                        }
                                    }, new com.android.volley.Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.d("JSON_RESPONSE", "onResponseError : " + error.toString());
                                }
                            }) {
                                @Override
                                public Map<String, String> getHeaders() throws AuthFailureError {
                                    Map<String, String> headers = new HashMap<>();
                                    headers.put("Content-Type", "application/json");
                                    headers.put("Authorization", "key=AAAAfLk6hVI:APA91bGmlTXFRoHjFSfv_qpaQw1NmIi0B5p-lluErCtQtY1TCbMkCoF8pr73q3_rE33rgOhhl0o_Os4vAY4x3oLeKpiO8LlilnvyOQ8SeIad6Byti4yTHlGyZLOeDdF7PwllZyxQ8clP");
                                    return headers;
                                }
                            };
                            requestQueue.add(jsonObjectRequest);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) {
                }
            });
        }else {
            Query query = allToken.orderByChild("role").equalTo("Admin");
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        Token token = ds.getValue(Token.class);
                        Data data = new Data(myUid, name + " : " + message, "ข้อความใหม่", ds.getKey(), "ChatNotification", R.drawable.ic_bx_bxs_user_circle);

                        Sender sender = new Sender(data, token.getToken());

                        try {
                            JSONObject senderJsonObj = new JSONObject(new Gson().toJson(sender));


                            Log.e("JSONObject", senderJsonObj.toString());
                            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, "https://fcm.googleapis.com/fcm/send", senderJsonObj,
                                    new com.android.volley.Response.Listener<JSONObject>() {
                                        @Override
                                        public void onResponse(JSONObject response) {
                                            //Toast.makeText(ChatActivity.this, "onResponse : " + response.toString(), Toast.LENGTH_SHORT).show();
                                            Log.d("JSON_RESPONSE", "onResponse : " + response.toString());
                                        }
                                    }, new com.android.volley.Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.d("JSON_RESPONSE", "onResponseError : " + error.toString());
                                }
                            }) {
                                @Override
                                public Map<String, String> getHeaders() throws AuthFailureError {
                                    Map<String, String> headers = new HashMap<>();
                                    headers.put("Content-Type", "application/json");
                                    headers.put("Authorization", "key=AAAAfLk6hVI:APA91bGmlTXFRoHjFSfv_qpaQw1NmIi0B5p-lluErCtQtY1TCbMkCoF8pr73q3_rE33rgOhhl0o_Os4vAY4x3oLeKpiO8LlilnvyOQ8SeIad6Byti4yTHlGyZLOeDdF7PwllZyxQ8clP");
                                    return headers;
                                }
                            };
                            requestQueue.add(jsonObjectRequest);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) {
                }
            });
        }


    }

    @Override
    protected void onPause() {
        super.onPause();
        userRefForSeen.removeEventListener(seenListener);
    }

    /*private void openImage() {
        Intent intent = new Intent();
        intent.setType("image/");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_REQUEST && resultCode == RESULT_OK) {
            imUri = data.getData();
        }

    }

    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void uploadImage() {


        if (imUri != null) {
            StorageReference fileRef = FirebaseStorage.getInstance().getReference().child("uploadsChats").child(System.currentTimeMillis() + "." + getFileExtension(imUri));

            fileRef.putFile(imUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String imageURL = uri.toString();
                            Toast.makeText(getApplicationContext(), "อัพโหลดสำเร็จ", Toast.LENGTH_SHORT).show();
                            sendImageMessage(imageURL);

                        }
                    });
                }
            });
        }
    }*/

    private void PickImage() {
        CropImage.activity().start(this);
    }

    private void requestStoragePermission() {
        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},100);
    }

    private void requestCameraPermission() {
        requestPermissions(new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE},100);
    }

    private boolean checkStoragePermission() {
        boolean res2 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED;
        return res2;
    }

    private boolean checkCameraPermission() {
        boolean res1 = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)== PackageManager.PERMISSION_GRANTED;
        boolean res2 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED;
        return res1 && res2;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                resultUri = result.getUri();
                /*try {
                    InputStream stream = getContentResolver().openInputStream(resultUri);
                    Bitmap bitmap = BitmapFactory.decodeStream(stream);
                    image.setImageBitmap(bitmap);
                }catch (Exception e){
                    e.printStackTrace();
                }*/
                uploadImage();
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    private void uploadImage(){
        ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("กำลังอัพโหลด");
        pd.show();

        if (resultUri != null){
            StorageReference fileRef = FirebaseStorage.getInstance().getReference().child("uploadsChats").child(System.currentTimeMillis()+"");

            fileRef.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            pd.dismiss();
                            String imageURL = uri.toString();
                            Toast.makeText(getApplicationContext(),"อัพโหลดสำเร็จ",Toast.LENGTH_SHORT).show();
                            sendImageMessage(imageURL);
                        }
                    });
                }
            });
        }
    }
}