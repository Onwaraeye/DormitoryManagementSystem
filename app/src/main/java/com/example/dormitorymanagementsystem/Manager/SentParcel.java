package com.example.dormitorymanagementsystem.Manager;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.dormitorymanagementsystem.ChatNew.ChatActivity;
import com.example.dormitorymanagementsystem.Login;
import com.example.dormitorymanagementsystem.Model.ImageURL;
import com.example.dormitorymanagementsystem.R;
import com.example.dormitorymanagementsystem.notifications.Data;
import com.example.dormitorymanagementsystem.notifications.Sender;
import com.example.dormitorymanagementsystem.notifications.Token;
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
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;


import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SentParcel extends AppCompatActivity {

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRefParcel = database.getReference("Parcel").push();
    private DatabaseReference myRef = database.getReference("Parcel");
    private DatabaseReference myRefRoom = database.getReference("Room");

    private int day, month, year;
    String monthThai = "";

    private static final int CAMERA_PERM_CODE = 101;
    private static final int CAMERA_REQUEST_CODE = 102;
    private static final int GALLERY_REQUEST_CODE = 103;
    private ImageView imageView;

    String room;
    String from, getTime;
    ArrayAdapter<String> adapter;

    String inputetFName;
    String inputetLName;

    String myUid = Login.getGbIdUser();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sent_parcel);

        String fname = Login.getGbFNameUser();
        String lname = Login.getGbLNameUser();
        String name = fname + " " + lname;
        EditText etFName = findViewById(R.id.etFName);
        EditText etLName = findViewById(R.id.etLName);
        TextView etDate = findViewById(R.id.txDate);
        Button btConfirm = findViewById(R.id.btConfirm);
        TextView cameraBtn = findViewById(R.id.cameraBtn);
        TextView galleryBtn = findViewById(R.id.galleryBtn);
        imageView = findViewById(R.id.imageView);
        Spinner spinner = findViewById(R.id.spinner);

        from = getIntent().getStringExtra("from");
        String getFname = getIntent().getStringExtra("fname");
        String getLname = getIntent().getStringExtra("lname");
        String numroom = getIntent().getStringExtra("room");
        String image = getIntent().getStringExtra("image");
        getTime = getIntent().getStringExtra("timestamp");

        ArrayList<String> timeAdd = getIntent().getStringArrayListExtra("timeAdd");
        Set<String> set = new HashSet<String>(timeAdd);
        List<String> listRoom = new ArrayList<>();
        listRoom.addAll(set);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, listRoom);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                room = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        month = Calendar.getInstance().get(Calendar.MONTH);
        year = Calendar.getInstance().get(Calendar.YEAR) + 543;
        etDate.setText(day + " " + getMonth(month) + " " + year);


        if (from != null && from.equals("edit")) {
            etFName.setText(getFname);
            etLName.setText(getLname);
            room = numroom;
            Glide.with(getApplicationContext()).load(image).fitCenter().centerCrop().into(imageView);
            imageView.setVisibility(View.VISIBLE);
            spinner.setSelection(adapter.getPosition(room));

            btConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Query query = myRef.orderByChild("timestamp").equalTo(getTime);
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                            inputetFName = etFName.getText().toString();
                            inputetLName = etLName.getText().toString();
                            for (DataSnapshot ds : snapshot.getChildren()) {
                                ds.getRef().child("numroom").setValue(room);
                                ds.getRef().child("firstname").setValue(inputetFName);
                                ds.getRef().child("lastname").setValue(inputetLName);
                                ds.getRef().child("status").setValue("0");
                                ds.getRef().child("nameImporter").setValue(name);
                                ds.getRef().child("nameReceiver").setValue("");
                                ds.getRef().child("timestampReceiver").setValue("");
                                uploadImageToFirebase();
                                prepareNotification(
                                        "" + System.currentTimeMillis(),
                                        "พัสดุใหม่",
                                        "คุณ" + getFname + " " + getLname,
                                        "ParcelNotification",
                                        "POST",
                                        "" + room);
                                finish();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull @NotNull DatabaseError error) {

                        }
                    });
                }
            });


        } else {
            btConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    inputetFName = etFName.getText().toString();
                    inputetLName = etLName.getText().toString();
                    imageView.setVisibility(View.GONE);
                    myRefParcel.child("numroom").setValue(room);
                    myRefParcel.child("firstname").setValue(inputetFName);
                    myRefParcel.child("lastname").setValue(inputetLName);
                    myRefParcel.child("status").setValue("0");
                    myRefParcel.child("timestamp").setValue(String.valueOf(System.currentTimeMillis()));
                    myRefParcel.child("nameImporter").setValue(name);
                    myRefParcel.child("nameReceiver").setValue("");
                    myRefParcel.child("timestampReceiver").setValue("");
                    uploadImageToFirebase();
                    prepareNotification(
                            "" + System.currentTimeMillis(),
                            "พัสดุใหม่",
                            "คุณ" + inputetFName + " " + inputetLName,
                            "ParcelNotification",
                            "POST",
                            "" + room);
                    //etRoom.setText("");
                    etFName.setText("");
                    etLName.setText("");
                    int id = getResources().getIdentifier("@drawable/ic_baseline_image_24", "drawable", getPackageName());
                    imageView.setImageResource(id);

                }
            });
        }

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

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //askCameraPermissions();
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
        imageView.setVisibility(View.VISIBLE);
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            File f = new File(currentPhotoPath);
            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            contentUri = Uri.fromFile(f);
            Log.e("contentUri", contentUri + "");
            //imageView.setImageURI(contentUri);

            Glide.with(getApplication()).load(contentUri).fitCenter().centerCrop().into(imageView);
            mediaScanIntent.setData(contentUri);
            this.sendBroadcast(mediaScanIntent);
            //uploadImageToFilrebase();
        }
        if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK) {
            contentUri = data.getData();
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
        if (contentUri != null) {
            StorageReference fileRef = FirebaseStorage.getInstance().getReference().child("uploadsParcel").child(System.currentTimeMillis() + "." + getFileExtension(contentUri));
            fileRef.putFile(contentUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String imageURL = uri.toString();
                            Toast.makeText(getApplicationContext(), "อัพโหลดสำเร็จ", Toast.LENGTH_SHORT).show();
                            if (from != null && from.equals("edit")) {
                                Log.e("getTime", getTime);
                                Query query = myRef.orderByChild("timestamp").equalTo(getTime);
                                query.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                        for (DataSnapshot ds : snapshot.getChildren()) {
                                            Log.e("imageUrl2", imageURL);
                                            ds.getRef().child("imageUrl").setValue(imageURL);
                                            ds.getRef().child("timestamp").setValue(String.valueOf(System.currentTimeMillis()));
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                    }
                                });
                            } else {
                                myRefParcel.child("imageUrl").setValue(imageURL);
                            }
                        }

                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull @NotNull Exception e) {
                    Toast.makeText(SentParcel.this, "Upload Failled.", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void prepareNotification(String pId, String title, String description, String notificationType, String notificationTopic, String room) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        Query query = ref.orderByChild("numroom").equalTo(room);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    DatabaseReference refTo = FirebaseDatabase.getInstance().getReference("Tokens");
                    refTo.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                            if (snapshot.child(ds.getKey()).getKey().equals(ds.getKey())) {
                                String NOTIFICATION_TOPIC = snapshot.child(ds.getKey()).child("token").getValue(String.class);
                                String NOTIFICATION_TITLE = title;
                                String NOTIFICATION_MESSAGE = description;
                                String NOTIFICATION_TYPE = notificationType;

                                JSONObject notificationJo = new JSONObject();
                                JSONObject notificationBodyJo = new JSONObject();

                                try {
                                    notificationBodyJo.put("notificationType", NOTIFICATION_TYPE);
                                    notificationBodyJo.put("sender", myUid);
                                    notificationBodyJo.put("pId", pId);
                                    notificationBodyJo.put("pTitle", NOTIFICATION_TITLE);
                                    notificationBodyJo.put("pDescription", NOTIFICATION_MESSAGE);

                                    notificationJo.put("to", NOTIFICATION_TOPIC);
                                    notificationJo.put("data", notificationBodyJo);

                                } catch (Exception e) {
                                    //Toast.makeText(this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                                Log.e("notificationJo", notificationJo.toString());
                                sendParcelNotification(notificationJo);
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

    private void sendParcelNotification(JSONObject notificationJo) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, "https://fcm.googleapis.com/fcm/send", notificationJo,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("FCM_RESPONSE", "onResponse" + response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(Post.this, ""+error.toString(), Toast.LENGTH_SHORT).show();
                        Log.e("FCM_RESPONSE", error.toString() + " onResponseError " + error.getMessage());
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("Authorization", "key=AAAAfLk6hVI:APA91bGmlTXFRoHjFSfv_qpaQw1NmIi0B5p-lluErCtQtY1TCbMkCoF8pr73q3_rE33rgOhhl0o_Os4vAY4x3oLeKpiO8LlilnvyOQ8SeIad6Byti4yTHlGyZLOeDdF7PwllZyxQ8clP");
                return headers;
            }
        };
        Volley.newRequestQueue(SentParcel.this).add(jsonObjectRequest);
    }

    public String getMonth(int month) {
        switch (month) {
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

    /*private void openImage(){
        Intent intent = new Intent();
        intent.setType("image/");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,IMAGE_REQUEST);
    }*/

    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_REQUEST && resultCode == RESULT_OK){
            imUri = data.getData();
            Glide.with(getApplicationContext()).load(imUri).fitCenter().centerCrop().into(imageParcel);
        }

    }*/



    /*private void PickImage() {
        //CropImage.activity().start(this);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //intent.setType("image/");
        //intent.setAction(Intent.ACTION_GET_CONTENT);

        //startActivity(intent);
        //resultUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,intent);
        //intent.setAction(Intent.ACTION_GET_CONTENT);
        //startActivityForResult(intent, IMAGE_REQUEST);
    }

    private void requestStoragePermission() {
        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
    }

    private void requestCameraPermission() {
        requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
    }

    private boolean checkStoragePermission() {
        boolean res2 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        return res2;
    }

    private boolean checkCameraPermission() {
        boolean res1 = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
        boolean res2 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        return res1 && res2;
    }

    private void uploadImage() {
        if (resultUri != null) {
            StorageReference fileRef = FirebaseStorage.getInstance().getReference().child("uploadsParcel").child(System.currentTimeMillis() + "");

            fileRef.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String imageURL = uri.toString();
                            Toast.makeText(getApplicationContext(), "อัพโหลดสำเร็จ", Toast.LENGTH_SHORT).show();
                            myRefParcel.child("imageUrl").setValue(imageURL);
                        }
                    });
                }
            });
        }
    }*/

}
