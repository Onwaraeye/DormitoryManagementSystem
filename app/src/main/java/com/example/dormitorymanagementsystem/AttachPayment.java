package com.example.dormitorymanagementsystem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.icu.text.DecimalFormat;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
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
import com.example.dormitorymanagementsystem.Manager.SentParcel;
import com.example.dormitorymanagementsystem.Model.BillModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
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

import java.io.File;
import java.io.IOException;

public class AttachPayment extends AppCompatActivity {

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference("Bills");

    private Context mContext;

    private Uri resultUri;
    private ImageView imageView, imageViewZoom;
    LinearLayout linearLayout;

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
        int yearCv = Integer.parseInt(yearThai) - 543;
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
        linearLayout = findViewById(R.id.linearLayout);
        Button btConfirm = findViewById(R.id.btConfirm);
        LinearLayout attachLayout = findViewById(R.id.attachLayout);
        TextView cameraBtn = findViewById(R.id.cameraBtn);
        TextView galleryBtn = findViewById(R.id.galleryBtn);

        int unitElecAfter = Integer.parseInt(billModel.getElecafter());
        int unitElecBefore = Integer.parseInt(billModel.getElecbefore());
        int sumEl = 0;
        if (unitElecAfter > unitElecBefore) {
            sumEl = (unitElecAfter - unitElecBefore);
        } else if (unitElecAfter < unitElecBefore) {
            sumEl = ((9999 - unitElecBefore) + unitElecAfter);
        }
        txElecUnit.setText("ค่าไฟ\n(" + unitElecBefore + " - " + unitElecAfter + " = " + sumEl + " หน่วย)");

        int unitWaterAfter = Integer.parseInt(billModel.getWaterafter());
        int unitWaterBefore = Integer.parseInt(billModel.getWaterbefore());
        int sumWt = 0;
        if (unitWaterAfter > unitWaterBefore) {
            sumWt = (unitWaterAfter - unitWaterBefore);
        } else if (unitWaterAfter < unitWaterBefore) {
            sumWt = ((9999 - unitWaterBefore) + unitWaterAfter);
        }
        txWaterUnit.setText("ค่าน้ำ\n(" + unitWaterBefore + " - " + unitWaterAfter + " = " + sumWt + " หน่วย)");

        txRoomPrice.setText(billModel.getRoomprice() + " บาท");
        txElectricity.setText(billModel.getElectricity() + " บาท");
        txWater.setText(billModel.getWater() + " บาท");
        txDiscount.setText(billModel.getDiscount() + " บาท");
        txSum.setText(billModel.getSum() + " บาท");
        txFee.setText(billModel.getFee() + " บาท");
        txInternet.setText(billModel.getInternet() + " บาท");

        txRoom.setText(room);
        txDate.setText(date);
        if (status.equals("0")) {
            if (typeUser.equals("Admin")) {
                btConfirm.setVisibility(View.GONE);
                imageView.setVisibility(View.GONE);
            } else {
                imageView.setVisibility(View.GONE);
                btConfirm.setVisibility(View.VISIBLE);
            }
            txStatus.setText("ยังไม่ได้ชำระ");
            txStatus.setTextColor(ContextCompat.getColor(mContext, R.color.red));
            attachLayout.setVisibility(View.VISIBLE);
            cameraBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    askCameraPermissions();
                }
            });
            galleryBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, GALLERY_REQUEST_CODE);
                }
            });
            btConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myRef.child(year).child(getMonth(monthThai)).child(room).child("status").setValue("1");
                    uploadImageToFirebase();
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
                    if (imageURL == null || imageURL.isEmpty()) {
                        int id = getResources().getIdentifier("@drawable/ic_baseline_image_24", "drawable", getPackageName());
                        imageView.setImageResource(id);
                    } else {
                        Glide.with(getApplicationContext()).load(imageURL).fitCenter().centerCrop().into(imageView);
                        btConfirm.setVisibility(View.GONE);

                        imageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ZoomImage(imageURL);
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
                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ZoomImage(imageURL);
                        }
                    });
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

    private void ZoomImage(String imageURL) {
        linearLayout.setVisibility(View.GONE);
        imageViewZoom.setVisibility(View.VISIBLE);
        Glide.with(getApplicationContext()).load(imageURL).apply(new RequestOptions().override(600, 600)).fitCenter().into(imageViewZoom);
        imageViewZoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageViewZoom.setVisibility(View.GONE);
                linearLayout.setVisibility(View.VISIBLE);
            }
        });
    }

    public String DoubleToString(double data) {
        DecimalFormat formatter = new DecimalFormat("###,###,###.00");
        String formatted = formatter.format(data);
        return formatted;
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


    private static final int CAMERA_PERM_CODE = 101;
    private static final int CAMERA_REQUEST_CODE = 102;
    private static final int GALLERY_REQUEST_CODE = 103;
    private Uri contentUri;

    private void askCameraPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERM_CODE);
        } else {
            dispatchTakePictureIntent();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERM_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                dispatchTakePictureIntent();
            } else {
                Toast.makeText(this, "Camera Permission is Required to Use camera.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    String currentPhotoPath;

    private File createImageFile() throws IOException {
        String imageFileName = System.currentTimeMillis() + "";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName
                , ".jpg"
                , storageDir);
        currentPhotoPath = image.getAbsolutePath();
        Log.e("currentPhotoPath", currentPhotoPath + "");
        return image;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {

            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            File f = new File(currentPhotoPath);
            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            contentUri = Uri.fromFile(f);
            Log.e("contentUri", contentUri + "");
            imageView.setVisibility(View.VISIBLE);
            Glide.with(getApplication()).load(contentUri).fitCenter().centerCrop().into(imageView);
            mediaScanIntent.setData(contentUri);
            this.sendBroadcast(mediaScanIntent);
            //uploadImageToFirebase();
        }
        if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK) {
            contentUri = data.getData();
            imageView.setVisibility(View.VISIBLE);
            Glide.with(getApplication()).load(contentUri).fitCenter().centerCrop().into(imageView);
            //uploadImageToFirebase();
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void uploadImageToFirebase() {
        StorageReference fileRef = FirebaseStorage.getInstance().getReference().child("uploadsSlips").child(System.currentTimeMillis() + "." + getFileExtension(contentUri));
        fileRef.putFile(contentUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String imageURL = uri.toString();
                        Toast.makeText(getApplicationContext(), "อัพโหลดสำเร็จ", Toast.LENGTH_SHORT).show();
                        myRef.child(year).child(getMonth(monthThai)).child(room).child("imageUrl").setValue(imageURL);
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                Toast.makeText(getApplicationContext(), "Upload Failled.", Toast.LENGTH_SHORT).show();
            }
        });
    }

}