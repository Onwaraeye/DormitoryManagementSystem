package com.example.dormitorymanagementsystem.Manager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class SentParcel extends AppCompatActivity {

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRefParcel = database.getReference("Parcel").push();

    private int day,month,year;
    String monthThai = "";

    private static final int IMAGE_REQUEST = 1;
    private Uri resultUri;
    private ImageView image;
    String myUid = Login.getGbIdUser();


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
        image = findViewById(R.id.imageView);

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
                prepareNotification(
                        ""+System.currentTimeMillis(),
                        "พัสดุใหม่",
                        "คุณ"+inputetFName+" "+inputetLName,
                        "ParcelNotification",
                        "POST",
                        ""+inputetRoom);
                etRoom.setText("");
                etFName.setText("");
                etLName.setText("");
                int id = getResources().getIdentifier("@drawable/ic_baseline_image_24", "drawable", getPackageName());
                image.setImageResource(id);

            }
        });
        image.setOnClickListener(new View.OnClickListener() {
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

        ImageView arrow_back = findViewById(R.id.ic_arrow_back);
        arrow_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }

    private void prepareNotification(String pId, String title, String description, String notificationType,String notificationTopic,String room) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        Query query = ref.orderByChild("numroom").equalTo(room);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()){
                    DatabaseReference refTo = FirebaseDatabase.getInstance().getReference("Tokens");
                    refTo.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                            if (snapshot.child(ds.getKey()).getKey().equals(ds.getKey())){
                                String NOTIFICATION_TOPIC = snapshot.child(ds.getKey()).child("token").getValue(String.class);
                                String NOTIFICATION_TITLE = title;
                                String NOTIFICATION_MESSAGE = description;
                                String NOTIFICATION_TYPE = notificationType;

                                JSONObject notificationJo = new JSONObject();
                                JSONObject notificationBodyJo = new JSONObject();

                                try {
                                    notificationBodyJo.put("notificationType", NOTIFICATION_TYPE);
                                    notificationBodyJo.put("sender",myUid );
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
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,"https://fcm.googleapis.com/fcm/send", notificationJo,
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
                        Log.e("FCM_RESPONSE",  error.toString()+" onResponseError "+ error.getMessage());
                    }
                })
        {
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

    /*private String getFileExtension(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }*/

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
                Glide.with(getApplicationContext()).load(resultUri).fitCenter().centerCrop().into(image);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    private void uploadImage(){
        if (resultUri != null){
            StorageReference fileRef = FirebaseStorage.getInstance().getReference().child("uploadsParcel").child(System.currentTimeMillis()+"");

            fileRef.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
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
