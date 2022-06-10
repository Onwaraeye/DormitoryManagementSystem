package com.example.dormitorymanagementsystem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.dormitorymanagementsystem.Model.BillModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.jetbrains.annotations.NotNull;

public class AttachPayment extends AppCompatActivity {

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference("Bills");

    private Context mContext;

    private static final int IMAGE_REQUEST = 1;
    private Uri imUri;
    private ImageView imageView,imageViewZoom;

    private String year = "";
    private String room = "";
    private String monthThai = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attach_payment);

        mContext = getApplication();

        BillModel billModel = (BillModel) getIntent().getSerializableExtra("bill");
        String date = getIntent().getStringExtra("date");
        String status = billModel.getStatus();
        String typeUser = Login.getGbTypeUser();
        String roombills = getIntent().getStringExtra("room");

        String[] dateBill = date.split("/");
        year = dateBill[1];
        monthThai = dateBill[0];
        if (typeUser.equals("User")){
            room = Login.getGbNumroom();
        }else {
            room = roombills;
        }


        TextView txRoom = findViewById(R.id.txRoom);
        TextView txDate = findViewById(R.id.txDate);
        TextView txStatus = findViewById(R.id.txStatus);
        TextView txRoomPrice = findViewById(R.id.txRoomPrice);
        TextView txElectricity = findViewById(R.id.txElectricity);
        TextView txWater = findViewById(R.id.txWater);
        TextView txDiscount = findViewById(R.id.txDiscount);
        TextView txSum = findViewById(R.id.txSum);
        imageView = findViewById(R.id.imageView);
        imageViewZoom = findViewById(R.id.imageViewZoom);
        LinearLayout linearLayout3 = findViewById(R.id.linearLayout3);
        Button btConfirm = findViewById(R.id.btConfirm);

        txRoom.setText(room);
        txDate.setText(date);
        if (status.equals("0")){
            btConfirm.setVisibility(View.VISIBLE);
            txStatus.setText("ยังไม่ได้ชำระ");
            txStatus.setTextColor(ContextCompat.getColor(mContext,R.color.red));
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openImage();
                }
            });
            btConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myRef.child(year).child(getMonth(monthThai)).child(room).child("status").setValue("1");
                    uploadImage();
                    finish();
                }
            });

        }else if (status.equals("1")){
            LinearLayout buttonAdmin = findViewById(R.id.buttonAdmin);
            Button btCorrect = findViewById(R.id.btCorrect);
            Button btCancel = findViewById(R.id.btCancel);
            if (typeUser.equals("Admin")){
                buttonAdmin.setVisibility(View.VISIBLE);
                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        btCorrect.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                myRef.child(year).child(getMonth(monthThai)).child(room).child("status").setValue("2");
                                finish();
                            }
                        });
                        btCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                myRef.child(year).child(getMonth(monthThai)).child(room).child("status").setValue("0");
                                finish();
                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                    }
                });

            }else {
                buttonAdmin.setVisibility(View.GONE);
            }
            txStatus.setText("รอการตรวจสอบ");
            txStatus.setTextColor(ContextCompat.getColor(mContext,R.color.orange));
            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                    String imageURL = snapshot.child(year).child(getMonth(monthThai)).child(room).child("imageUrl").getValue(String.class);
                    if (imageURL.isEmpty()){
                        int id = getResources().getIdentifier("@drawable/ic_baseline_image_24", "drawable", getPackageName());
                        imageView.setImageResource(id);
                    }else {
                        Glide.with(getApplicationContext()).load(imageURL).fitCenter().centerCrop().into(imageView);
                        btConfirm.setVisibility(View.GONE);
                        imageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                linearLayout3.setVisibility(View.GONE);
                                imageViewZoom.setVisibility(View.VISIBLE);
                                Glide.with(getApplicationContext()).load(imageURL).apply(new RequestOptions().override(600, 600)).fitCenter().centerCrop().into(imageViewZoom);
                                imageViewZoom.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        imageViewZoom.setVisibility(View.GONE);
                                        linearLayout3.setVisibility(View.VISIBLE);
                                    }
                                });
                            }
                        });
                    }
                }
                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) {

                }
            });
        }else {

            txStatus.setText("ชำระแล้ว");
            txStatus.setTextColor(ContextCompat.getColor(mContext,R.color.green));
            imageView.setVisibility(View.GONE);
            btConfirm.setVisibility(View.GONE);
        }
        txRoomPrice.setText(billModel.getRoomprice());
        txElectricity.setText(billModel.getElectricity());
        txWater.setText(billModel.getWater());
        txDiscount.setText(billModel.getDiscount());
        txSum.setText(billModel.getSum());


        ImageView arrow_back = findViewById(R.id.ic_arrow_back);
        arrow_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public String getMonth(String monthThai){
        String month = "";
        switch(monthThai) {
            case "มกราคม":
                month = "0";
                break;
            case "กุมภาพันธ์":
                month = "1";
                break;
            case "มีนาคม":
                month = "2";
                break;
            case "เมษายน":
                month = "3";
                break;
            case "พฤษภาคม":
                month = "4";
                break;
            case "มิถุนายน":
                month = "5";
                break;
            case "กรกฎาคม":
                month = "6";
                break;
            case "สิงหาคม":
                month = "7";
                break;
            case "กันยายน":
                month = "8";
                break;
            case "ตุลาคม":
                month = "9";
                break;
            case "พฤศจิกายน":
                month = "10";
                break;
            case "ธันวาคม":
                month = "11";
                break;
        }
        return month;
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
            Glide.with(getApplicationContext()).load(imUri).fitCenter().centerCrop().into(imageView);

        }

    }

    private String getFileExtension(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void uploadImage(){

        if (imUri != null){
            StorageReference fileRef = FirebaseStorage.getInstance().getReference().child("uploadsParcel").child(System.currentTimeMillis()+"."+getFileExtension(imUri));

            fileRef.putFile(imUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String imageURL = uri.toString();
                            Toast.makeText(getApplicationContext(),"อัพโหลดสำเร็จ",Toast.LENGTH_SHORT).show();
                            myRef.child(year).child(getMonth(monthThai)).child(room).child("imageUrl").setValue(imageURL);
                        }
                    });
                }
            });
        }
    }
}