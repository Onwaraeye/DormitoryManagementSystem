package com.example.dormitorymanagementsystem.Manager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.dormitorymanagementsystem.ChatNew.ChatActivity;
import com.example.dormitorymanagementsystem.Login;
import com.example.dormitorymanagementsystem.Model.ImageURL;
import com.example.dormitorymanagementsystem.ParcelDetail;
import com.example.dormitorymanagementsystem.R;
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
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Post extends AppCompatActivity {

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRefPost = database.getReference("Posts");

    private ImageView imageView;
    private String getStatus;
    private String getTime;
    private String uid = Login.getGbIdUser();
    private String name = Login.getGbFNameUser() +" "+Login.getGbLNameUser();
    String pTimestamps = "";
    String getImage;

    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        requestQueue = Volley.newRequestQueue(Post.this);

        pTimestamps = String.valueOf(System.currentTimeMillis());

        Intent intent = getIntent();
        String getTitle = intent.getStringExtra("titlePost");
        String getDetail = intent.getStringExtra("detailPost");
        getImage = intent.getStringExtra("imageUrl");
        getTime = intent.getStringExtra("time");
        getStatus = intent.getStringExtra("status");

        EditText etTitle = findViewById(R.id.etTitle);
        EditText etDetail = findViewById(R.id.etDetail);
        Button btConfirm = findViewById(R.id.btConfirm);
        imageView = findViewById(R.id.imageView);
        LinearLayout linearLayoutEdit = findViewById(R.id.linearLayoutEdit);
        Button bt_edit = findViewById(R.id.bt_edit);
        Button bt_del = findViewById(R.id.bt_del);
        LinearLayout attachLayout = findViewById(R.id.attachLayout);
        TextView cameraBtn = findViewById(R.id.cameraBtn);
        TextView galleryBtn = findViewById(R.id.galleryBtn);

        etTitle.setText(getTitle);
        etDetail.setText(getDetail);
        Glide.with(getApplicationContext()).load(getImage).fitCenter().centerCrop().into(imageView);

        if (getStatus.equals("1")) {
            btConfirm.setVisibility(View.GONE);
            linearLayoutEdit.setVisibility(View.VISIBLE);
            bt_edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Query query = myRefPost.orderByChild("timestamp").equalTo(getTime);
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                            for (DataSnapshot ds : snapshot.getChildren()) {
                                ds.getRef().child("title").setValue(etTitle.getText().toString());
                                ds.getRef().child("detail").setValue(etDetail.getText().toString());
                                uploadImageToFirebase();
                                finish();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull @NotNull DatabaseError error) {
                        }
                    });
                }
            });
            bt_del.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDialogDelete();
                }
            });
        } else {
            imageView.setVisibility(View.GONE);
            btConfirm.setVisibility(View.VISIBLE);
            linearLayoutEdit.setVisibility(View.GONE);
            btConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String inputTitle = etTitle.getText().toString();
                    String inputDetail = etDetail.getText().toString();
                    myRefPost.child(pTimestamps).child("title").setValue(inputTitle);
                    myRefPost.child(pTimestamps).child("detail").setValue(inputDetail);
                    myRefPost.child(pTimestamps).child("timestamp").setValue(pTimestamps);
                    myRefPost.child(pTimestamps).child("uid").setValue(uid);
                    uploadImageToFirebase();
                    prepareNotification(
                            ""+pTimestamps,
                            name+" เพิ่มโพสใหม่",
                            inputTitle+"\n"+inputDetail,
                            "PostNotification",
                            "POST");
                    finish();
                }
            });
        }

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
        if (contentUri != null) {
            StorageReference fileRef = FirebaseStorage.getInstance().getReference().child("uploadsPost").child(System.currentTimeMillis() + "." + getFileExtension(contentUri));
            fileRef.putFile(contentUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String imageURL = uri.toString();
                            Toast.makeText(getApplicationContext(), "อัพโหลดสำเร็จ", Toast.LENGTH_SHORT).show();
                            if (getStatus.equals("1")) {
                                Query query = myRefPost.orderByChild("timestamp").equalTo(getTime);
                                query.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                        for (DataSnapshot ds : snapshot.getChildren()) {
                                            ds.getRef().child("imageUrl").setValue(imageURL);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                    }
                                });
                            } else {
                                myRefPost.child(pTimestamps).child("imageUrl").setValue(imageURL);
                            }
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

    /*private void uploadImage() {

        if (imUri != null) {
            StorageReference fileRef = FirebaseStorage.getInstance().getReference().child("uploadsPost").child(System.currentTimeMillis() + "." + getFileExtension(imUri));

            fileRef.putFile(imUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String imageURL = uri.toString();
                            if (getStatus.equals("1")) {
                                Query query = myRefPost.orderByChild("timestamp").equalTo(getTime);
                                query.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                        for (DataSnapshot ds : snapshot.getChildren()) {
                                            ds.getRef().child("imageUrl").setValue(imageURL);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                    }
                                });
                            } else {
                                Toast.makeText(getApplicationContext(), "อัพโหลดสำเร็จ", Toast.LENGTH_SHORT).show();
                                myRefPost.child(pTimestamps).child("imageUrl").setValue(imageURL);
                            }
                        }
                    });
                }
            });
        }
    }*/

    private void showDialogDelete() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(Post.this);
        dialog.setTitle("ลบ");
        dialog.setMessage("คุณแน่ใจที่จะลบหรือไม่");
        dialog.setPositiveButton("ตกลง", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (getImage!=null){
                    FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
                    StorageReference photoRef = firebaseStorage.getReferenceFromUrl(getImage);
                    photoRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            // File deleted successfully
                            Log.d("TAGDelete", "onSuccess: deleted file");
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Uh-oh, an error occurred!
                            Log.d("TAGDelete", "onFailure: did not delete file");
                        }
                    });
                }
                Query query = myRefPost.orderByChild("timestamp").equalTo(getTime);
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            Toast.makeText(Post.this, "ลบสำเร็จ", Toast.LENGTH_SHORT).show();
                            ds.getRef().removeValue();
                            finish();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                    }
                });
            }
        });
        dialog.setNegativeButton("ยกเลิก", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = dialog.create();
        alertDialog.show();
    }

    private void prepareNotification(String pId, String title, String description, String notificationType,String notificationTopic) {
        DatabaseReference allToken = FirebaseDatabase.getInstance().getReference("Tokens");
        allToken.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Token token = ds.getValue(Token.class);
                    if (!token.getRole().equals("Repairman")){
                        String NOTIFICATION_TOPIC = token.getToken();
                        String NOTIFICATION_TITLE = title;
                        String NOTIFICATION_MESSAGE = description;
                        String NOTIFICATION_TYPE = notificationType;

                        JSONObject notificationJo = new JSONObject();
                        JSONObject notificationBodyJo = new JSONObject();

                        try {
                            notificationBodyJo.put("notificationType", NOTIFICATION_TYPE);
                            notificationBodyJo.put("sender", uid);
                            notificationBodyJo.put("pId", pId);
                            notificationBodyJo.put("pTitle", NOTIFICATION_TITLE);
                            notificationBodyJo.put("pDescription", NOTIFICATION_MESSAGE);

                            notificationJo.put("to", NOTIFICATION_TOPIC);
                            notificationJo.put("data", notificationBodyJo);

                        } catch (Exception e) {
                            //Toast.makeText(this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        Log.e("notificationJo", notificationJo.toString());
                        sendPostNotification(notificationJo);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });


    }

    private void sendPostNotification(JSONObject notificationJo) {
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
                        Toast.makeText(Post.this, ""+error.toString(), Toast.LENGTH_SHORT).show();
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
        requestQueue.add(jsonObjectRequest);
    }

}