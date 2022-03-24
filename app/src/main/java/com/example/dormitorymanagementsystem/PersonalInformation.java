package com.example.dormitorymanagementsystem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.dormitorymanagementsystem.Model.ImageURL;
import com.example.dormitorymanagementsystem.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class PersonalInformation extends AppCompatActivity {

    StorageReference databassStorage;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("Room");
    DatabaseReference myRefUser = database.getReference("Users");
    String userID;
    String url;


    private static final int IMAGE_REQUEST = 1;
    private Uri imUrl;
    ImageView imUser;
    String imageURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_information);

        userID = Login.getGbIdUser();
        databassStorage = FirebaseStorage.getInstance().getReference("uploadsUser").child(userID+".jpg");

        TextView txName = findViewById(R.id.txHeadName);
        TextView txHeadName = findViewById(R.id.txName);
        TextView txBirthday = findViewById(R.id.txBirthday);
        TextView txGender = findViewById(R.id.txGender);
        imUser = findViewById(R.id.imUser);

        myRefUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(userID)){
                    String fName = snapshot.child(userID).child("firstname").getValue(String.class);
                    String lName = snapshot.child(userID).child("lastname").getValue(String.class);
                    String name = fName+" "+lName;
                    String birthday = snapshot.child(userID).child("birthday").getValue(String.class);
                    String gender = snapshot.child(userID).child("gender").getValue(String.class);
                    String imageURL = snapshot.child(userID).child("pictureUserUrl").getValue(String.class);
                    if (imageURL.isEmpty()){
                        int id = getResources().getIdentifier("@drawable/ic_bx_bxs_user_circle", "drawable", getPackageName());
                        imUser.setImageResource(id);
                    }else {
                        Glide.with(getApplicationContext()).load(imageURL).fitCenter().centerCrop().into(imUser);
                        //Picasso.get().load(imageURL).fit().centerCrop().into(imUser);
                    }


                    /*try {
                        File localfile = File.createTempFile(userID,".jpg");
                        databassStorage.getFile(localfile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                Bitmap bitmap = BitmapFactory.decodeFile(localfile.getAbsolutePath());
                                imUser.setImageBitmap(bitmap);
                            }
                        });
                    }catch (IOException e){ }*/
                    txGender.setText(gender);
                    txHeadName.setText(name);
                    txName.setText(name);
                    txBirthday.setText(birthday);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        TextView ic_edit = findViewById(R.id.txEdit);
        ic_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),PersonalInformationEdit.class);
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
        imUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startCropActivity();
                openImage();
            }
        });

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
            imUrl = data.getData();
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

        if (imUrl != null){
            StorageReference fileRef = FirebaseStorage.getInstance().getReference().child("uploadsUser").child(userID+"."+getFileExtension(imUrl));

            fileRef.putFile(imUrl).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            imageURL = databassStorage.getDownloadUrl().toString();
                            url = uri.toString();
                            //Log.e("url",url);
                            pd.dismiss();
                            Picasso.get().load(url).fit().centerCrop().into(imUser);
                            //Picasso.get().load(imageURL).fit().centerCrop().into(imUser);
                            Log.e("imageURLLL",imageURL);
                            //imUser.setImageURI(imUrl);
                            Toast.makeText(getApplicationContext(),"อัพโหลดสำเร็จ",Toast.LENGTH_SHORT).show();
                            //myRefUser.child(userID).child("pictureUserUrl").setValue(userID+"."+getFileExtension(imUrl));
                            myRefUser.child(userID).child("pictureUserUrl").setValue(url);

                        }
                    });
                }
            });
        }
    }
}