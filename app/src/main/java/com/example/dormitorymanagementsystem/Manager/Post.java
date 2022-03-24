package com.example.dormitorymanagementsystem.Manager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dormitorymanagementsystem.Model.ImageURL;
import com.example.dormitorymanagementsystem.R;
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
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

public class Post extends AppCompatActivity {

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRefPost = database.getReference("Posts").push();
    private DatabaseReference myRef = database.getReference("Posts");

    private static final int IMAGE_REQUEST = 1;
    private Uri imUri;
    private ImageView imageView;
    private String getStatus;
    private String getTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        Intent intent = getIntent();
        String getTitle = intent.getStringExtra("titlePost");
        String getDetail = intent.getStringExtra("detailPost");
        String getImage = intent.getStringExtra("imageUrl");
        getTime = intent.getStringExtra("time");
        getStatus = intent.getStringExtra("status");

        EditText etTitle = findViewById(R.id.etTitle);
        EditText etDetail = findViewById(R.id.etDetail);
        Button btConfirm = findViewById(R.id.btConfirm);
        imageView = findViewById(R.id.imageView);
        LinearLayout linearLayoutEdit = findViewById(R.id.linearLayoutEdit);
        Button bt_edit = findViewById(R.id.bt_edit);
        Button bt_del = findViewById(R.id.bt_del);

        etTitle.setText(getTitle);
        etDetail.setText(getDetail);
        Picasso.get().load(getImage).fit().centerCrop().into(imageView);

        if (getStatus.equals("1")){
            btConfirm.setVisibility(View.GONE);
            linearLayoutEdit.setVisibility(View.VISIBLE);
            bt_edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Query query = myRef.orderByChild("timestamp").equalTo(getTime);
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                            for (DataSnapshot ds : snapshot.getChildren()) {
                                ds.getRef().child("title").setValue(etTitle.getText().toString());
                                ds.getRef().child("detail").setValue(etDetail.getText().toString());
                                uploadImage();
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
        }else {
            btConfirm.setVisibility(View.VISIBLE);
            linearLayoutEdit.setVisibility(View.GONE);
            btConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String inputTitle = etTitle.getText().toString();
                    String inputDetail = etDetail.getText().toString();
                    myRefPost.child("title").setValue(inputTitle);
                    myRefPost.child("detail").setValue(inputDetail);
                    myRefPost.child("timestamp").setValue(String.valueOf(System.currentTimeMillis()));
                    uploadImage();
                    finish();
                }
            });
        }

        imageView.setOnClickListener(new View.OnClickListener() {
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
            Picasso.get().load(imUri).fit().centerCrop().into(imageView);
        }

    }

    private String getFileExtension(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void uploadImage(){
        /*ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("กำลังอัพโหลด");
        pd.show();*/

        if (imUri != null){
            StorageReference fileRef = FirebaseStorage.getInstance().getReference().child("uploadsPost").child(System.currentTimeMillis()+"."+getFileExtension(imUri));

            fileRef.putFile(imUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String imageURL = uri.toString();
                            //pd.dismiss();
                            if (getStatus.equals("1")){
                                Query query = myRef.orderByChild("timestamp").equalTo(getTime);
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
                            }else {
                                Toast.makeText(getApplicationContext(),"อัพโหลดสำเร็จ",Toast.LENGTH_SHORT).show();
                                myRefPost.child("imageUrl").setValue(imageURL);
                            }
                        }
                    });
                }
            });
        }
    }

    private void showDialogDelete(){
        AlertDialog.Builder dialog = new AlertDialog.Builder(Post.this);
        dialog.setTitle("ลบ");
        dialog.setMessage("คุณแน่ใจที่จะลบหรือไม่");
        dialog.setPositiveButton("ตกลง", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Query query = myRef.orderByChild("timestamp").equalTo(getTime);
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        for (DataSnapshot ds : snapshot.getChildren()) {
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
}