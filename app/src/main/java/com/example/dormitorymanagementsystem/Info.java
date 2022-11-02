package com.example.dormitorymanagementsystem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.dormitorymanagementsystem.Manager.InfoEditContact;
import com.example.dormitorymanagementsystem.Manager.SelectBank;
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
import java.text.SimpleDateFormat;
import java.util.Date;

public class Info extends AppCompatActivity {

    StorageReference databassStorage;
    private DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("Contact");
    private Context mContext;

    private static final int IMAGE_REQUEST = 1;
    private Uri resultUri;
    ImageView imageView;
    Button buttonConfirm;

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
        ImageView editImageBtn = findViewById(R.id.editImageBtn);
        LinearLayout attachLayout = findViewById(R.id.attachLayout);
        TextView cameraBtn = findViewById(R.id.cameraBtn);
        TextView galleryBtn = findViewById(R.id.galleryBtn);
        buttonConfirm = findViewById(R.id.buttonConfirm);


        TextView txBank = findViewById(R.id.txBank);
        TextView txAccName = findViewById(R.id.txAccName);
        TextView txAccNo = findViewById(R.id.txAccNo);
        ImageView imageBank = findViewById(R.id.imageBank);
        imageView = findViewById(R.id.imageDormitory);


        if (Login.getGbTypeUser().equals("User")){
            txEditContact.setVisibility(View.INVISIBLE);
            txEditBank.setVisibility(View.INVISIBLE);
            editImageBtn.setVisibility(View.INVISIBLE);
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
                    Glide.with(mContext).load(R.drawable.ic_baseline_image_24).into(imageView);
                }else {
                    Glide.with(mContext).load(image).fitCenter().centerCrop().into(imageView);
                }
            }
            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
            }
        });

        editImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (attachLayout.getVisibility() == View.GONE){
                    attachLayout.setVisibility(View.VISIBLE);
                }else {
                    attachLayout.setVisibility(View.GONE);
                }
            }
        });

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

        buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImageToFirebase();
                buttonConfirm.setVisibility(View.GONE);
                attachLayout.setVisibility(View.GONE);
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

        ImageView arrow_back = findViewById(R.id.ic_arrow_back);
        arrow_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
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
            buttonConfirm.setVisibility(View.VISIBLE);
            imageView.setVisibility(View.VISIBLE);
            Glide.with(getApplication()).load(contentUri).fitCenter().centerCrop().into(imageView);
            mediaScanIntent.setData(contentUri);
            this.sendBroadcast(mediaScanIntent);
            //uploadImageToFirebase();
        }
        if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK) {
            contentUri = data.getData();
            imageView.setVisibility(View.VISIBLE);
            buttonConfirm.setVisibility(View.VISIBLE);
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
        StorageReference fileRef = FirebaseStorage.getInstance().getReference().child("dormitory");
        fileRef.putFile(contentUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String imageURL = uri.toString();
                        Toast.makeText(getApplicationContext(), "อัพโหลดสำเร็จ", Toast.LENGTH_SHORT).show();
                        myRef.child("image").setValue(imageURL);
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

    /*private void uploadImage(){
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
    }*/
}