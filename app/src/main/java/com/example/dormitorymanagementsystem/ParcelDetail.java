package com.example.dormitorymanagementsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.dormitorymanagementsystem.Manager.Post;
import com.example.dormitorymanagementsystem.Manager.SentParcel;
import com.example.dormitorymanagementsystem.Model.NewParcelModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ParcelDetail extends AppCompatActivity {

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference("Parcel");
    private DatabaseReference myRefRoom = database.getReference("Room");

    private ImageView imageView, imageViewZoom;
    LinearLayout linearLayout,linearLayoutEdit;
    Button bt_edit,bt_del;
    String imageUp;
    String getTime,monthThai;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parcel_detail);

        NewParcelModel newParcelModel = (NewParcelModel) getIntent().getSerializableExtra("parcel");

        TextView txRoom = findViewById(R.id.txRoom);
        TextView txName = findViewById(R.id.txName);
        TextView txTime = findViewById(R.id.txTime);
        TextView txNameImporter = findViewById(R.id.txNameImporter);
        LinearLayout receiver = findViewById(R.id.receiver);
        TextView txTimeReceiver = findViewById(R.id.txTimeReceiver);
        TextView txNameReceiver = findViewById(R.id.txNameReceiver);
        Button btConfirm = findViewById(R.id.btConfirm);
        imageView = findViewById(R.id.imageView);
        imageViewZoom = findViewById(R.id.imageViewZoom);
        linearLayout = findViewById(R.id.linearLayout);
        linearLayoutEdit = findViewById(R.id.linearLayoutEdit);
        bt_edit =findViewById(R.id.bt_edit);
        bt_del =findViewById(R.id.bt_del);

        String date = getIntent().getStringExtra("date");
        getTime = getIntent().getStringExtra("timestamp");
        String name = newParcelModel.getFirstname()+" "+newParcelModel.getLastname();

        imageUp = newParcelModel.getImageUrl();

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ZoomImage(newParcelModel.getImageUrl());
            }
        });

        List<String> listRoom = new ArrayList<>();
        myRefRoom.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    listRoom.add(ds.getKey());
                }
            }
            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

        if (newParcelModel.getStatus().equals("0")){
            if (Login.getGbTypeUser().equals("Admin")){
                btConfirm.setVisibility(View.GONE);
                linearLayoutEdit.setVisibility(View.VISIBLE);
            }
            receiver.setVisibility(View.GONE);
            txRoom.setText(newParcelModel.getNumroom());
            txName.setText(name);
            txTime.setText(date);
            txNameImporter.setText(newParcelModel.getNameImporter());
            Glide.with(getApplicationContext()).load(newParcelModel.getImageUrl()).fitCenter().centerCrop().into(imageView);
            btConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                            Query query = myRef.orderByChild("timestamp").equalTo(newParcelModel.getTimestamp());
                            query.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                    for (DataSnapshot ds : snapshot.getChildren()){
                                        ds.getRef().child("nameReceiver").setValue(Login.getGbFNameUser()+" "+Login.getGbLNameUser());
                                        ds.getRef().child("timestampReceiver").setValue(String.valueOf(System.currentTimeMillis()));
                                        ds.getRef().child("status").setValue("1");
                                        finish();
                                    }
                                }
                                @Override
                                public void onCancelled(@NonNull @NotNull DatabaseError error) {
                                }
                            });
                        }
                        @Override
                        public void onCancelled(@NonNull @NotNull DatabaseError error) {
                        }
                    });
                }
            });
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                    Query query = myRef.orderByChild("timestamp").equalTo(getTime);
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                            for (DataSnapshot ds : snapshot.getChildren()) {
                                String numroom = ds.child("numroom").getValue(String.class);
                                String fname = ds.child("firstname").getValue(String.class);
                                String lname = ds.child("lastname").getValue(String.class);
                                String imageUrl = ds.child("imageUrl").getValue(String.class);
                                String time = ds.child("timestamp").getValue(String.class);
                                imageUp = imageUrl;
                                txRoom.setText(numroom);
                                txName.setText(fname+" "+lname);
                                Glide.with(getApplicationContext()).load(imageUrl).fitCenter().centerCrop().into(imageView);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull @NotNull DatabaseError error) {

                        }
                    });

                }

                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) {

                }
            });

            bt_edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), SentParcel.class);
                    intent.putStringArrayListExtra("timeAdd", (ArrayList<String>) listRoom);
                    intent.putExtra("fname", newParcelModel.getFirstname());
                    intent.putExtra("lname", newParcelModel.getLastname());
                    intent.putExtra("from","edit");
                    intent.putExtra("room",newParcelModel.getNumroom());
                    intent.putExtra("image",imageUp);
                    intent.putExtra("timestamp", getTime);
                    startActivity(intent);
                }
            });

            bt_del.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDialogDelete();
                }
            });

        }else {
            String dateReceiver = getIntent().getStringExtra("dateReceiver");
            txRoom.setText(newParcelModel.getNumroom());
            txName.setText(name);
            txTime.setText(date);
            txNameImporter.setText(newParcelModel.getNameImporter());
            Glide.with(getApplicationContext()).load(newParcelModel.getImageUrl()).fitCenter().centerCrop().into(imageView);

            receiver.setVisibility(View.VISIBLE);
            txTimeReceiver.setText(dateReceiver);
            txNameReceiver.setText(newParcelModel.getNameReceiver());

            btConfirm.setVisibility(View.GONE);
        }

        ImageView arrow_back = findViewById(R.id.ic_arrow_back);
        arrow_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
    private void showDialogDelete() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(ParcelDetail.this);
        dialog.setTitle("ลบ");
        dialog.setMessage("คุณแน่ใจที่จะลบหรือไม่");
        dialog.setPositiveButton("ตกลง", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (imageUp!=null){
                    FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
                    StorageReference photoRef = firebaseStorage.getReferenceFromUrl(imageUp);
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
                Query query = myRef.orderByChild("timestamp").equalTo(getTime);
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            Toast.makeText(ParcelDetail.this, "ลบสำเร็จ", Toast.LENGTH_SHORT).show();
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

    private void ZoomImage(String imageURL) {
        linearLayout.setVisibility(View.VISIBLE);
        //imageViewZoom.setVisibility(View.VISIBLE);
        Glide.with(getApplicationContext()).load(imageURL).apply(new RequestOptions().override(600, 600)).fitCenter().into(imageViewZoom);
        imageViewZoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //imageViewZoom.setVisibility(View.GONE);
                linearLayout.setVisibility(View.GONE);
            }
        });
    }

    public String getMonth(int month) {
        switch (month) {
            case 1:
                monthThai = "ม.ค.";
                break;
            case 2:
                monthThai = "ก.พ.";
                break;
            case 3:
                monthThai = "มี.ค.";
                break;
            case 4:
                monthThai = "เม.ย.";
                break;
            case 5:
                monthThai = "พ.ค.";
                break;
            case 6:
                monthThai = "มิ.ย.";
                break;
            case 7:
                monthThai = "ก.ค.";
                break;
            case 8:
                monthThai = "ส.ค.";
                break;
            case 9:
                monthThai = "ก.ย.";
                break;
            case 10:
                monthThai = "ต.ค.";
                break;
            case 11:
                monthThai = "พ.ย.";
                break;
            case 12:
                monthThai = "ธ.ค.";
                break;
        }
        return monthThai;
    }

}