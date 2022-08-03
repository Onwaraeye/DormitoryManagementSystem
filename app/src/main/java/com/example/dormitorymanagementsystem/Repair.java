package com.example.dormitorymanagementsystem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Repair extends AppCompatActivity {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRefUser = database.getReference("Users");
    DatabaseReference myRefRepair = database.getReference("Repair").push();
    DatabaseReference myRef = database.getReference("Repair");

    private Context mContext;
    private String userID;

    private static final int IMAGE_REQUEST = 1;
    private Uri imUri;
    private ImageView imageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repair);

        String typeUser = Login.getGbTypeUser();
        mContext = getApplication();
        userID = Login.getGbIdUser();

        Intent intent = getIntent();
        String getRoom = intent.getStringExtra("room");
        String getTime = intent.getStringExtra("time");
        String getDetail = intent.getStringExtra("detail");
        String getTitle = intent.getStringExtra("title");
        String getImage = intent.getStringExtra("image");
        String getPhone = intent.getStringExtra("phone");
        String getStatus = intent.getStringExtra("status");
        String getCost = intent.getStringExtra("cost");
        Long time = System.currentTimeMillis();

        TextView txTime = findViewById(R.id.txTime);
        EditText etRoom = findViewById(R.id.etRoom);
        EditText etTitleRepair = findViewById(R.id.etTitleRepair);
        EditText etDetail = findViewById(R.id.etDetail);
        EditText etPhone = findViewById(R.id.etPhone);
        TextView txCost = findViewById(R.id.txCost);
        EditText etCost = findViewById(R.id.etCost);
        TextView txRepairman = findViewById(R.id.txRepairman);
        EditText etRepairman = findViewById(R.id.etRepairman);
        Button btConfirm = findViewById(R.id.btConfirm);
        Button btAdminConfirm = findViewById(R.id.btAdminConfirm);
        Button btAdminForwardWork = findViewById(R.id.btAdminForwardWork);
        Button btChat = findViewById(R.id.btChat);
        Button btConfirmRepair = findViewById(R.id.btConfirmRepair);
        LinearLayout repairman = findViewById(R.id.repairman);
        imageView = findViewById(R.id.imageView);

        btConfirm.setVisibility(View.GONE);
        btAdminConfirm.setVisibility(View.GONE);
        btAdminForwardWork.setVisibility(View.GONE);
        repairman.setVisibility(View.GONE);

        //ส่วนของUser
        if (typeUser.equals("User")) {

            txTime.setVisibility(View.GONE);
            btConfirm.setVisibility(View.VISIBLE);
            etCost.setVisibility(View.GONE);
            txCost.setVisibility(View.GONE);
            etRepairman.setVisibility(View.GONE);
            txRepairman.setVisibility(View.GONE);
            myRefUser.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.hasChild(userID)) {
                        String numroom = snapshot.child(userID).child("numroom").getValue(String.class);
                        String phone = snapshot.child(userID).child("phone").getValue(String.class);
                        etRoom.setText(numroom);
                        etPhone.setText(phone);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openImage();
                }
            });
            btConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String inputRoom = etRoom.getText().toString();
                    String inputRepair = etTitleRepair.getText().toString();
                    String inputDetail = etDetail.getText().toString();
                    String inputPhone = etPhone.getText().toString();
                    if (inputRoom.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "กรุณาระบุห้อง", Toast.LENGTH_SHORT).show();
                    } else if (inputRepair.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "กรุณาระบุหัวข้อ", Toast.LENGTH_SHORT).show();
                    } else if (inputDetail.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "กรุณาระบุรายละเอียด", Toast.LENGTH_SHORT).show();
                    } else if (inputPhone.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "กรุณาระบุเบอร์โทรศัพท์", Toast.LENGTH_SHORT).show();
                    } else {
                        myRefRepair.child("userID").setValue(userID);
                        myRefRepair.child("numroom").setValue(inputRoom);
                        myRefRepair.child("titleRepair").setValue(inputRepair);
                        myRefRepair.child("detail").setValue(inputDetail);
                        myRefRepair.child("phone").setValue(inputPhone);
                        myRefRepair.child("status").setValue("0");
                        myRefRepair.child("timestamp").setValue(String.valueOf(System.currentTimeMillis()));
                        myRefRepair.child("cost").setValue("0");
                        uploadImage();
                        finish();
                    }
                }
            });
        }
        //ส่วนของช่าง
        else if (typeUser.equals("Repairman")){

            SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yy / HH:mm");
            String date = formatter.format(new Date(Long.parseLong(getTime)));
            txTime.setText(date);

            repairman.setVisibility(View.VISIBLE);

            etRoom.setText(getRoom);
            etRoom.setEnabled(false);
            etDetail.setText(getDetail);
            etDetail.setEnabled(false);
            etTitleRepair.setText(getTitle);
            etTitleRepair.setEnabled(false);
            etPhone.setText(getPhone);
            etPhone.setEnabled(false);
            etRepairman.setText(userID);
            etRepairman.setEnabled(false);

            Glide.with(getApplicationContext()).load(getImage).fitCenter().centerCrop().into(imageView);

            btChat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /*Intent intent = new Intent(getApplicationContext(), Chat.class);
                    intent.putExtra("userID","Msg01");
                    startActivity(intent);*/
                }
            });


        }
        //ส่วนของAdmin
        else {
            SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yy / HH:mm");
            String date = formatter.format(new Date(Long.parseLong(getTime)));
            txTime.setText(date);

            if(getStatus.equals("1")){
                etCost.setEnabled(false);
                etCost.setText(getCost);
            }else if (getStatus.equals("2")){
                txCost.setVisibility(View.GONE);
                etCost.setVisibility(View.GONE);
            }else if (getStatus.equals("3")){

            }else {
                btAdminForwardWork.setVisibility(View.VISIBLE);
                txCost.setVisibility(View.GONE);
                etCost.setVisibility(View.GONE);
            }

            etRoom.setText(getRoom);
            etRoom.setEnabled(false);
            etDetail.setText(getDetail);
            etDetail.setEnabled(false);
            etTitleRepair.setText(getTitle);
            etTitleRepair.setEnabled(false);
            etPhone.setText(getPhone);
            etPhone.setEnabled(false);

            myRefRepair.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                    etRepairman.setText(snapshot.child("repairman").getValue(String.class));
                    etRepairman.setEnabled(false);
                }

                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) {

                }
            });

            Glide.with(getApplicationContext()).load(getImage).fitCenter().centerCrop().into(imageView);

            String timeUp = String.valueOf(System.currentTimeMillis());
            btAdminConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Query query = myRef.orderByChild("timestamp").equalTo(getTime);
                    query.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                            for (DataSnapshot ds : snapshot.getChildren()) {
                                ds.getRef().child("status").setValue("1");
                                ds.getRef().child("cost").setValue(etCost.getText().toString());
                                ds.getRef().child("timestampComplete").setValue(timeUp);
                                finish();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull @NotNull DatabaseError error) {
                        }
                    });
                }
            });
            btAdminForwardWork.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Query query = myRef.orderByChild("timestamp").equalTo(getTime);
                    query.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                            for (DataSnapshot ds : snapshot.getChildren()) {
                                ds.getRef().child("status").setValue("2");
                                ds.getRef().child("timestampComplete").setValue(timeUp);
                                ds.getRef().child("repairman").setValue(etRepairman.getText().toString());
                                finish();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull @NotNull DatabaseError error) {
                        }
                    });
                }
            });
        }

        ImageView arrow_back = findViewById(R.id.ic_arrow_back);
        arrow_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void openImage() {
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
            Glide.with(getApplication()).load(imUri).fitCenter().centerCrop().into(imageView);
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void uploadImage() {
        ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("กำลังอัพโหลด");
        pd.show();

        if (imUri != null) {
            StorageReference fileRef = FirebaseStorage.getInstance().getReference().child("uploadsRepair").child(System.currentTimeMillis() + "." + getFileExtension(imUri));

            fileRef.putFile(imUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String imageURL = uri.toString();
                            Toast.makeText(getApplicationContext(), "อัพโหลดสำเร็จ", Toast.LENGTH_SHORT).show();
                            myRefRepair.child("imageUrl").setValue(imageURL);
                        }
                    });
                }
            });
        }
    }
}