package com.example.dormitorymanagementsystem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.dormitorymanagementsystem.Manager.InfoEditContact;
import com.example.dormitorymanagementsystem.Manager.SelectBank;
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

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Info extends AppCompatActivity {

    StorageReference databassStorage;
    private DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("Contact");
    private Context mContext;

    private static final int IMAGE_REQUEST = 1;
    private Uri resultUri;
    ImageView imageDormitory;

    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        mContext = getApplication();

        TextView txEditContact = findViewById(R.id.txEditContact);
        TextView txEditBank = findViewById(R.id.txEditBank);

        TextView txPhone = findViewById(R.id.txPhone);
        TextView txNamePhone = findViewById(R.id.txNamePhone);
        TextView txEmail = findViewById(R.id.txEmail);
        TextView txNameEmail = findViewById(R.id.txNameEmail);
        TextView txNameDormitory = findViewById(R.id.txNameDormitory);


        TextView txBank = findViewById(R.id.txBank);
        TextView txAccName = findViewById(R.id.txAccName);
        TextView txAccNo = findViewById(R.id.txAccNo);
        ImageView imageBank = findViewById(R.id.imageBank);
        imageDormitory = findViewById(R.id.imageDormitory);


        if (Login.getGbTypeUser().equals("User")){
            txEditContact.setVisibility(View.INVISIBLE);
            txEditBank.setVisibility(View.INVISIBLE);
        }

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                String icon = snapshot.child("contactBank").child("icon").getValue(String.class);
                String image = snapshot.child("image").getValue(String.class);
                if (icon == null){
                    Glide.with(mContext).load(R.drawable.ic_bank).fitCenter().centerCrop().into(imageBank);
                }else {
                    Glide.with(mContext).load(icon).fitCenter().centerCrop().into(imageBank);
                    txBank.setText(snapshot.child("contactBank").child("bank").getValue(String.class));
                    txAccName.setText(snapshot.child("contactBank").child("name").getValue(String.class));
                    txAccNo.setText(snapshot.child("contactBank").child("accNo").getValue(String.class));
                }
                if (snapshot.child("nameDormitory").getValue(String.class)!=null){
                    txPhone.setText(snapshot.child("contactPhone").child("phone").getValue(String.class));
                    txNamePhone.setText(snapshot.child("contactPhone").child("name").getValue(String.class));
                    txEmail.setText(snapshot.child("contactEmail").child("email").getValue(String.class));
                    txNameEmail.setText(snapshot.child("contactEmail").child("name").getValue(String.class));
                    txNameDormitory.setText(snapshot.child("nameDormitory").getValue(String.class));
                }
                if (image == null){
                    Glide.with(mContext).load(R.drawable.ic_baseline_image_24).into(imageDormitory);
                }else {
                    Glide.with(mContext).load(image).fitCenter().centerCrop().into(imageDormitory);
                }
            }
            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
            }
        });

        txEditBank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, SelectBank.class);
                startActivity(intent);
            }
        });

        txEditContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, InfoEditContact.class);
                startActivity(intent);
            }
        });

        if (Login.getGbTypeUser().equals("Admin")){
            imageDormitory.setOnClickListener(new View.OnClickListener() {
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
        }

        ImageView arrow_back = findViewById(R.id.ic_arrow_back);
        arrow_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
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
                uploadImage();
                //Glide.with(getApplicationContext()).load(resultUri).fitCenter().centerCrop().into(imageDormitory);
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
            StorageReference fileRef = FirebaseStorage.getInstance().getReference().child("dormitory");

            fileRef.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            pd.dismiss();
                            String imageURL = uri.toString();
                            Toast.makeText(getApplicationContext(),"อัพโหลดสำเร็จ",Toast.LENGTH_SHORT).show();
                            myRef.child("image").setValue(imageURL);
                        }
                    });
                }
            });
        }
    }
}