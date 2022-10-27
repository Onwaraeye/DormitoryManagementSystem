package com.example.dormitorymanagementsystem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.icu.text.DecimalFormat;
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
import com.theartofdev.edmodo.cropper.CropImage;

import org.jetbrains.annotations.NotNull;

public class AttachPayment extends AppCompatActivity {

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference("Bills");

    private Context mContext;

    private static final int IMAGE_REQUEST = 1;
    private Uri resultUri;
    private ImageView imageView, imageViewZoom;

    private String year = "";
    private String room = "";
    private String monthThai = "";
    private String yearThai = "";


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

        String[] dateBill = date.split(" ");
        yearThai = dateBill[1];
        int yearCv = Integer.parseInt(yearThai)-543;
        year = String.valueOf(yearCv);
        monthThai = dateBill[0];
        if (typeUser.equals("User")) {
            room = Login.getGbNumroom();
        } else {
            room = roombills;
        }

        TextView txRoom = findViewById(R.id.txRoom);
        TextView txDate = findViewById(R.id.txDate);
        TextView txStatus = findViewById(R.id.txStatus);
        TextView txRoomPrice = findViewById(R.id.txRoomPrice);
        TextView txElectricity = findViewById(R.id.txElectricity);
        TextView txWater = findViewById(R.id.txWater);
        TextView txDiscount = findViewById(R.id.txDiscount);
        TextView txFee = findViewById(R.id.txFee);
        TextView txInternet = findViewById(R.id.txInternet);
        TextView txSum = findViewById(R.id.txSum);
        TextView txElecUnit = findViewById(R.id.txElecUnit);
        TextView txWaterUnit = findViewById(R.id.txWaterUnit);
        imageView = findViewById(R.id.imageView);
        imageViewZoom = findViewById(R.id.imageViewZoom);
        LinearLayout linearLayout3 = findViewById(R.id.linearLayout3);
        Button btConfirm = findViewById(R.id.btConfirm);

        int unitElecAfter = Integer.parseInt(billModel.getElecafter());
        int unitElecBefore = Integer.parseInt(billModel.getElecbefore());
        int sumEl = 0;
        if (unitElecAfter>unitElecBefore){
            sumEl = (unitElecAfter-unitElecBefore);
        }else if (unitElecAfter<unitElecBefore){
            sumEl = ((9999-unitElecBefore)+unitElecAfter);
        }
        txElecUnit.setText("ค่าไฟ\n("+unitElecBefore+" - "+unitElecAfter+" = "+sumEl+" หน่วย)");

        int unitWaterAfter = Integer.parseInt(billModel.getWaterafter());
        int unitWaterBefore = Integer.parseInt(billModel.getWaterbefore());
        int sumWt = 0;
        if (unitWaterAfter>unitWaterBefore){
            sumWt = (unitWaterAfter-unitWaterBefore);
        }else if (unitWaterAfter<unitWaterBefore){
            sumWt = ((9999-unitWaterBefore)+unitWaterAfter);
        }
        txWaterUnit.setText("ค่าน้ำ\n("+unitWaterBefore+" - "+unitWaterAfter+" = "+sumWt+" หน่วย)");

        txRoomPrice.setText(billModel.getRoomprice()+" บาท");
        txElectricity.setText(billModel.getElectricity()+" บาท");
        txWater.setText(billModel.getWater()+" บาท");
        txDiscount.setText(billModel.getDiscount()+" บาท");
        txSum.setText(billModel.getSum()+" บาท");
        txFee.setText(billModel.getFee()+" บาท");
        txInternet.setText(billModel.getInternet()+" บาท");

        txRoom.setText(room);
        txDate.setText(date);
        if (status.equals("0")) {
            if (typeUser.equals("Admin")) {
                btConfirm.setVisibility(View.GONE);
                imageView.setVisibility(View.GONE);
            } else {
                btConfirm.setVisibility(View.VISIBLE);
            }
            txStatus.setText("ยังไม่ได้ชำระ");
            txStatus.setTextColor(ContextCompat.getColor(mContext, R.color.red));
            imageView.setOnClickListener(new View.OnClickListener() {
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
            btConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myRef.child(year).child(getMonth(monthThai)).child(room).child("status").setValue("1");
                    uploadImage();
                    finish();
                }
            });
        } else if (status.equals("1")) {
            LinearLayout buttonAdmin = findViewById(R.id.buttonAdmin);
            Button btCorrect = findViewById(R.id.btCorrect);
            Button btCancel = findViewById(R.id.btCancel);
            if (typeUser.equals("Admin")) {
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

            } else {
                buttonAdmin.setVisibility(View.GONE);
            }

            txStatus.setText("รอการตรวจสอบ");
            txStatus.setTextColor(ContextCompat.getColor(mContext, R.color.orange));
            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                    String imageURL = snapshot.child(year).child(getMonth(monthThai)).child(room).child("imageUrl").getValue(String.class);
                    if (imageURL.isEmpty()) {
                        int id = getResources().getIdentifier("@drawable/ic_baseline_image_24", "drawable", getPackageName());
                        imageView.setImageResource(id);
                    } else {
                        Glide.with(getApplicationContext()).load(imageURL).fitCenter().centerCrop().into(imageView);
                        btConfirm.setVisibility(View.GONE);
                        imageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                linearLayout3.setVisibility(View.GONE);
                                imageViewZoom.setVisibility(View.VISIBLE);
                                Glide.with(getApplicationContext()).load(imageURL).apply(new RequestOptions().override(600, 600)).fitCenter().into(imageViewZoom);
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
        } else {
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                    String imageURL = snapshot.child(year).child(getMonth(monthThai)).child(room).child("imageUrl").getValue(String.class);
                    Glide.with(getApplicationContext()).load(imageURL).fitCenter().centerCrop().into(imageView);
                    txStatus.setText("ชำระแล้ว");
                    txStatus.setTextColor(ContextCompat.getColor(mContext, R.color.green));
                    btConfirm.setVisibility(View.GONE);
                }

                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) {

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

    public String getMonth(String monthThai) {
        String month = "";
        switch (monthThai) {
            case "มกราคม":
                month = "1";
                break;
            case "กุมภาพันธ์":
                month = "2";
                break;
            case "มีนาคม":
                month = "3";
                break;
            case "เมษายน":
                month = "4";
                break;
            case "พฤษภาคม":
                month = "5";
                break;
            case "มิถุนายน":
                month = "6";
                break;
            case "กรกฎาคม":
                month = "7";
                break;
            case "สิงหาคม":
                month = "8";
                break;
            case "กันยายน":
                month = "9";
                break;
            case "ตุลาคม":
                month = "10";
                break;
            case "พฤศจิกายน":
                month = "11";
                break;
            case "ธันวาคม":
                month = "12";
                break;
        }
        return month;
    }

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
                Glide.with(getApplicationContext()).load(resultUri).fitCenter().centerCrop().into(imageView);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    private void uploadImage(){
        if (resultUri != null){
            StorageReference fileRef = FirebaseStorage.getInstance().getReference().child("uploadsSlips").child(System.currentTimeMillis()+"");

            fileRef.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
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