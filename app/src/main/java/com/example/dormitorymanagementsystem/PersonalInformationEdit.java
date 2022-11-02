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
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.dormitorymanagementsystem.Model.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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

public class PersonalInformationEdit extends AppCompatActivity {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("Room");
    DatabaseReference myRefUser = database.getReference("Users");

    RadioGroup radioGroup;
    RadioButton radMale, radFemale;

    String userID;
    ImageView imUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_information_edit);

        userID = Login.getGbIdUser();

        EditText etFName = findViewById(R.id.etFName);
        EditText etLName = findViewById(R.id.etLName);


        radioGroup = findViewById(R.id.radioGroup);
        radMale = findViewById(R.id.radMale);
        radFemale = findViewById(R.id.radFemale);
        imUser = findViewById(R.id.imUser);
        TextView cameraBtn = findViewById(R.id.cameraBtn);
        TextView galleryBtn = findViewById(R.id.galleryBtn);


        myRefUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(userID)) {
                    String fName = snapshot.child(userID).child("firstname").getValue(String.class);
                    String lName = snapshot.child(userID).child("lastname").getValue(String.class);
                    String gender = snapshot.child(userID).child("gender").getValue(String.class);
                    String imageURL = snapshot.child(userID).child("pictureUserUrl").getValue(String.class);
                    if (imageURL.isEmpty()){
                        int id = getResources().getIdentifier("@drawable/ic_bx_bxs_user_circle", "drawable", getPackageName());
                        imUser.setImageResource(id);
                    }else {
                        Glide.with(getApplicationContext()).load(imageURL).fitCenter().centerCrop().into(imUser);
                    }

                    int valueGender = 0;
                    if (gender.equals("ผู้ชาย")) {
                        valueGender = 0;
                        radMale.setChecked(true);
                    } else {
                        if (gender.equals("ผู้หญิง")) {
                            valueGender = 1;
                            radFemale.setChecked(true);
                        } else {
                            Log.e("gender", "ผิด");
                        }
                    }
                    Log.e("gender", valueGender + "");
                    etFName.setText(fName);
                    etLName.setText(lName);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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

        Button btConfirm = findViewById(R.id.btConfirm);
        btConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputFName = etFName.getText().toString();
                String inputLName = etLName.getText().toString();

                RadioButton inputradioButton;
                myRefUser.child(userID).child("firstname").setValue(inputFName);
                myRefUser.child(userID).child("lastname").setValue(inputLName);
                uploadImageToFirebase();

                int selectedID = radioGroup.getCheckedRadioButtonId();
                inputradioButton = findViewById(selectedID);
                String gender = inputradioButton.getText().toString();
                myRefUser.child(userID).child("gender").setValue(gender);
                finish();
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
            Glide.with(getApplication()).load(contentUri).fitCenter().centerCrop().into(imUser);
            mediaScanIntent.setData(contentUri);
            this.sendBroadcast(mediaScanIntent);
            //uploadImageToFirebase();
        }
        if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK) {
            contentUri = data.getData();
            Glide.with(getApplication()).load(contentUri).fitCenter().centerCrop().into(imUser);
            //uploadImageToFirebase();
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void uploadImageToFirebase() {
        if (contentUri != null) {
            StorageReference fileRef = FirebaseStorage.getInstance().getReference().child("uploadsUser").child(userID+"."+getFileExtension(contentUri));
            fileRef.putFile(contentUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String imageURL = uri.toString();
                            Toast.makeText(getApplicationContext(), "อัพโหลดสำเร็จ", Toast.LENGTH_SHORT).show();
                            myRefUser.child(userID).child("pictureUserUrl").setValue(imageURL);
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
}