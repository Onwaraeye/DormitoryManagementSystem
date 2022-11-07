package com.example.dormitorymanagementsystem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
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
import com.example.dormitorymanagementsystem.ChatNew.AdapterUsers;
import com.example.dormitorymanagementsystem.ChatNew.ChatActivity;
import com.example.dormitorymanagementsystem.ChatNew.ModelUser;
import com.example.dormitorymanagementsystem.Manager.InfoEditBank;
import com.example.dormitorymanagementsystem.Manager.SelectBank;
import com.example.dormitorymanagementsystem.Manager.ViewRepairman;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
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

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Repair extends AppCompatActivity {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRefUser = database.getReference("Users");
    DatabaseReference myRefRepair = database.getReference("Repair").push();
    DatabaseReference myRef = database.getReference("Repair");

    AdapterUsers adapterUsers;
    List<ModelUser> userList;
    Context context;
    RecyclerView recyclerView;

    private Context mContext;
    private String userID;

    private static final int IMAGE_REQUEST = 1;
    private Uri resultUri;

    private ImageView imageView, imageViewZoom;
    LinearLayout linearLayout;


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
        String getRepairman = intent.getStringExtra("repairman");
        String getDate = intent.getStringExtra("date");

        TextView txTime = findViewById(R.id.txTime);
        TextView etRoom = findViewById(R.id.etRoom);
        EditText etTitleRepair = findViewById(R.id.etTitleRepair);
        EditText etDetail = findViewById(R.id.etDetail);
        EditText etPhone = findViewById(R.id.etPhone);
        TextView txCost = findViewById(R.id.txCost);
        EditText etCost = findViewById(R.id.etCost);
        TextView txRepairman = findViewById(R.id.txRepairman);
        TextView etRepairman = findViewById(R.id.etRepairman);
        Button btConfirm = findViewById(R.id.btConfirm);
        Button btAdminConfirm = findViewById(R.id.btAdminConfirm);
        Button btAdminForwardWork = findViewById(R.id.btAdminForwardWork);
        Button btChat = findViewById(R.id.btChat);
        Button btConfirmRepair = findViewById(R.id.btConfirmRepair);
        LinearLayout repairman = findViewById(R.id.repairman);
        imageView = findViewById(R.id.imageView);
        LinearLayout attachLayout = findViewById(R.id.attachLayout);
        TextView cameraBtn = findViewById(R.id.cameraBtn);
        TextView galleryBtn = findViewById(R.id.galleryBtn);
        LinearLayout repairPage = findViewById(R.id.repairPage);
        imageView = findViewById(R.id.imageView);
        imageViewZoom = findViewById(R.id.imageViewZoom);
        linearLayout = findViewById(R.id.linearLayout);

        userList = new ArrayList<>();

        recyclerView = findViewById(R.id.list_item);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        btConfirm.setVisibility(View.GONE);
        btAdminConfirm.setVisibility(View.GONE);
        btAdminForwardWork.setVisibility(View.GONE);
        repairman.setVisibility(View.GONE);

        txTime.setText(getDate);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ZoomImage(getImage);
            }
        });

        //ส่วนของUser
        if (typeUser.equals("User")) {
            if (getStatus != null){
                etRoom.setText(getRoom);
                etDetail.setText(getDetail);
                etDetail.setEnabled(false);
                etTitleRepair.setText(getTitle);
                etTitleRepair.setEnabled(false);
                etPhone.setText(getPhone);
                etPhone.setEnabled(false);
                btConfirm.setVisibility(View.GONE);
                Glide.with(getApplicationContext()).load(getImage).fitCenter().centerCrop().into(imageView);

                etCost.setVisibility(View.GONE);
                txCost.setVisibility(View.GONE);
                etRepairman.setVisibility(View.GONE);
                txRepairman.setVisibility(View.GONE);
            }
            else {
                txTime.setVisibility(View.GONE);
                btConfirm.setVisibility(View.VISIBLE);
                etCost.setVisibility(View.GONE);
                txCost.setVisibility(View.GONE);
                etRepairman.setVisibility(View.GONE);
                txRepairman.setVisibility(View.GONE);
                imageView.setVisibility(View.GONE);

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
                            uploadImageToFirebase();
                            finish();
                        }
                    }
                });
            }
        }
        //ส่วนของช่าง
        else if (typeUser.equals("Repairman")){

            etRoom.setText(getRoom);
            etDetail.setText(getDetail);
            etDetail.setEnabled(false);
            etTitleRepair.setText(getTitle);
            etTitleRepair.setEnabled(false);
            etPhone.setText(getPhone);
            etPhone.setEnabled(false);
            String name = Login.getGbFNameUser()+" "+Login.getGbLNameUser();
            etRepairman.setText(name);
            Glide.with(getApplicationContext()).load(getImage).fitCenter().centerCrop().into(imageView);

            if (getStatus.equals("2")){
                repairman.setVisibility(View.VISIBLE);
                btChat.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
                    intent.putExtra("hisUid","Mng01");
                    startActivity(intent);
                    }
                });
                btConfirmRepair.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String timeUp = String.valueOf(System.currentTimeMillis());
                        Query query = myRef.orderByChild("timestamp").equalTo(getTime);
                        query.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                for (DataSnapshot ds : snapshot.getChildren()) {
                                    ds.getRef().child("status").setValue("3");
                                    ds.getRef().child("cost").setValue(etCost.getText().toString());
                                    ds.getRef().child("timestampRepair").setValue(timeUp);
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
            else {
                etCost.setText(getCost);
                etCost.setEnabled(false);
            }

        }
        //ส่วนของAdmin
        else if (typeUser.equals("Admin")){

            etRoom.setText(getRoom);
            etDetail.setText(getDetail);
            etDetail.setEnabled(false);
            etTitleRepair.setText(getTitle);
            etTitleRepair.setEnabled(false);
            etPhone.setText(getPhone);
            etPhone.setEnabled(false);
            etCost.setText(getCost);
            etCost.setEnabled(false);

            //0คือแจ้งมา
            if(getStatus.equals("0")){
                etRepairman.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getUsers();
                        recyclerView.setVisibility(View.VISIBLE);
                        repairPage.setVisibility(View.GONE);
                    }
                });

                btAdminForwardWork.setVisibility(View.VISIBLE);
                txCost.setVisibility(View.GONE);
                etCost.setVisibility(View.GONE);
                btAdminForwardWork.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String timeUp = String.valueOf(System.currentTimeMillis());
                        Query query = myRef.orderByChild("timestamp").equalTo(getTime);
                        query.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                for (DataSnapshot ds : snapshot.getChildren()) {
                                    ds.getRef().child("status").setValue("2");
                                    ds.getRef().child("timestampForward").setValue(timeUp);
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
            //2คืองานที่นิติมอบหมายให้ช่าง
            else if (getStatus.equals("2")){
                txCost.setVisibility(View.GONE);
                etCost.setVisibility(View.GONE);
                repairman.setVisibility(View.VISIBLE);
                btChat.setText("คุยกับช่าง");
                btChat.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
                    intent.putExtra("hisUid",getRepairman);
                    startActivity(intent);
                    }
                });
                etRepairman.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getUsers();
                        recyclerView.setVisibility(View.VISIBLE);
                        repairPage.setVisibility(View.GONE);
                    }
                });

                btConfirmRepair.setText("แก้ไข");
                btConfirmRepair.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String timeUp = String.valueOf(System.currentTimeMillis());
                        Query query = myRef.orderByChild("timestamp").equalTo(getTime);
                        query.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                for (DataSnapshot ds : snapshot.getChildren()) {
                                    myRefUser.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                            if (snapshot.hasChild(etRepairman.getText().toString())){
                                                ds.getRef().child("repairman").setValue(etRepairman.getText().toString());
                                                Toast.makeText(Repair.this, "แก้ไขสำเร็จ", Toast.LENGTH_SHORT).show();
                                            }else {
                                                Toast.makeText(Repair.this, "กรุณากรอกไอดีของช่าง", Toast.LENGTH_SHORT).show();
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
                });
            }
            //3คืองานที่ช่างทำสำเร็จ
            else if (getStatus.equals("3")){
                String timeUp = String.valueOf(System.currentTimeMillis());
                btAdminConfirm.setVisibility(View.VISIBLE);
                btAdminConfirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Query query = myRef.orderByChild("timestamp").equalTo(getTime);
                        query.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                for (DataSnapshot ds : snapshot.getChildren()) {
                                    ds.getRef().child("status").setValue("1");
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
            }
            //1คือสำเร็จ
            else {

            }

            if (getRepairman != null){
                myRefUser.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        String name = snapshot.child(getRepairman).child("firstname").getValue(String.class)+" "+snapshot.child(getRepairman).child("lastname").getValue(String.class);
                        etRepairman.setText(name);
                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                    }
                });
            }
            Glide.with(getApplicationContext()).load(getImage).fitCenter().centerCrop().into(imageView);
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

    private static final int CAMERA_PERM_CODE = 101;
    private static final int CAMERA_REQUEST_CODE = 102;
    private static final int GALLERY_REQUEST_CODE = 103;
    private Uri contentUri;

    private void askCameraPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA}, CAMERA_PERM_CODE);
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
        String imageFileName = System.currentTimeMillis()+"";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName
                , ".jpg"
                , storageDir);
        currentPhotoPath = image.getAbsolutePath();
        Log.e("currentPhotoPath", currentPhotoPath+"");
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
            Log.e("contentUri", contentUri+"");
            imageView.setVisibility(View.VISIBLE);
            Glide.with(getApplication()).load(contentUri).fitCenter().centerCrop().into(imageView);
            mediaScanIntent.setData(contentUri);
            this.sendBroadcast(mediaScanIntent);
            //uploadImageToFilrebase();
        }
        if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK) {
            contentUri = data.getData();
            imageView.setVisibility(View.VISIBLE);
            Glide.with(getApplication()).load(contentUri).fitCenter().centerCrop().into(imageView);
            //uploadImageToFilrebase();
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void uploadImageToFirebase() {
        StorageReference fileRef = FirebaseStorage.getInstance().getReference().child("uploadsRepair").child(System.currentTimeMillis()+"."+getFileExtension(contentUri));
        fileRef.putFile(contentUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String imageURL = uri.toString();
                        Toast.makeText(getApplicationContext(), "อัพโหลดสำเร็จ", Toast.LENGTH_SHORT).show();
                        myRefRepair.child("imageUrl").setValue(imageURL);
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

    private void getUsers(){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                userList.clear();
                for (DataSnapshot ds: snapshot.getChildren()){
                    ModelUser modelUser = ds.getValue(ModelUser.class);
                    if (modelUser.getRole() != null && modelUser.getRole().equals("Repairman")){
                        userList.add(modelUser);
                    }
                    adapterUsers = new AdapterUsers(Repair.this, userList,"ViewRepairman");
                    recyclerView.setAdapter(adapterUsers);
                }
            }
            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    /*private void uploadImage(){
        ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("กำลังอัพโหลด");
        pd.show();

        if (resultUri != null){
            StorageReference fileRef = FirebaseStorage.getInstance().getReference().child("uploadsRepair").child(System.currentTimeMillis()+"");

            fileRef.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            pd.dismiss();
                            String imageURL = uri.toString();
                            Toast.makeText(getApplicationContext(),"อัพโหลดสำเร็จ",Toast.LENGTH_SHORT).show();
                            myRefRepair.child("imageUrl").setValue(imageURL);
                        }
                    });
                }
            });
        }
    }*/
}