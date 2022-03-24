package com.example.dormitorymanagementsystem.Manager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dormitorymanagementsystem.Login;
import com.example.dormitorymanagementsystem.Model.ImageURL;
import com.example.dormitorymanagementsystem.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.DateFormatSymbols;
import java.util.Calendar;

public class SentParcel extends AppCompatActivity {

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRefParcel = database.getReference("Parcel").push();

    private int day,month,year;
    String monthThai = "";

    private static final int IMAGE_REQUEST = 1;
    private Uri imUri;
    private ImageView imageParcel;
    //private String imageURL;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sent_parcel);

        String fname = Login.getGbFNameUser();
        String lname = Login.getGbLNameUser();
        String name = fname+" "+lname;
        EditText etRoom = findViewById(R.id.etRoom);
        EditText etFName = findViewById(R.id.etFName);
        EditText etLName = findViewById(R.id.etLName);
        TextView etDate = findViewById(R.id.txDate);
        Button btConfirm = findViewById(R.id.btConfirm);
        imageParcel = findViewById(R.id.imageView);

        day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        month = Calendar.getInstance().get(Calendar.MONTH);
        year = Calendar.getInstance().get(Calendar.YEAR)+543;
        etDate.setText(day+" "+getMonth(month)+" "+year);

        btConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputetRoom = etRoom.getText().toString();
                String inputetFName = etFName.getText().toString();
                String inputetLName = etLName.getText().toString();

                myRefParcel.child("numroom").setValue(inputetRoom);
                myRefParcel.child("firstname").setValue(inputetFName);
                myRefParcel.child("lastname").setValue(inputetLName);
                myRefParcel.child("status").setValue("0");
                myRefParcel.child("timestamp").setValue(String.valueOf(System.currentTimeMillis()));
                myRefParcel.child("nameImporter").setValue(name);
                myRefParcel.child("nameReceiver").setValue("");
                myRefParcel.child("timestampReceiver").setValue("");
                uploadImage();

                etRoom.setText("");
                etFName.setText("");
                etLName.setText("");
                int id = getResources().getIdentifier("@drawable/ic_baseline_image_24", "drawable", getPackageName());
                imageParcel.setImageResource(id);
            }
        });

        imageParcel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImage();
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

    public String getMonth(int month){
        switch(month) {
            case 0:
                monthThai = "มกราคม";
                break;
            case 1:
                monthThai = "กุมภาพันธ์";
                break;
            case 2:
                monthThai = "มีนาคม";
                break;
            case 3:
                monthThai = "เมษายน";
                break;
            case 4:
                monthThai = "พฤษภาคม";
                break;
            case 5:
                monthThai = "มิถุนายน";
                break;
            case 6:
                monthThai = "กรกฎาคม";
                break;
            case 7:
                monthThai = "สิงหาคม";
                break;
            case 8:
                monthThai = "กันยายน";
                break;
            case 9:
                monthThai = "ตุลาคม";
                break;
            case 10:
                monthThai = "พฤศจิกายน";
                break;
            case 11:
                monthThai = "ธันวาคม";
                break;
        }
        return monthThai;
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
            Picasso.get().load(imUri).fit().centerCrop().into(imageParcel);

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
                            myRefParcel.child("imageUrl").setValue(imageURL);
                        }
                    });
                }
            });
        }
    }

}
