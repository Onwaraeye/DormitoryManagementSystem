package com.example.dormitorymanagementsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.dormitorymanagementsystem.Model.NewParcelModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ParcelDetail extends AppCompatActivity {

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference("Parcel");

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
        ImageView imageView = findViewById(R.id.imageView);

        String name = newParcelModel.getFirstname()+" "+newParcelModel.getLastname();
        SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yy" + " / " + "HH:mm");
        String date = formatter.format(new Date(Long.parseLong(newParcelModel.getTimestamp())));


        if (newParcelModel.getStatus().equals("0")){
            receiver.setVisibility(View.GONE);
            txRoom.setText(newParcelModel.getNumroom());
            txName.setText(name);
            txTime.setText(date);
            txNameImporter.setText(newParcelModel.getNameImporter());
            Picasso.get().load(newParcelModel.getImageUrl()).fit().centerCrop().into(imageView);

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
        }else {
            SimpleDateFormat formatter2 = new SimpleDateFormat("dd MMM yy" + " / " + "HH:mm");
            String dateReceiver = formatter2.format(new Date(Long.parseLong(newParcelModel.getTimestampReceiver())));

            txRoom.setText(newParcelModel.getNumroom());
            txName.setText(name);
            txTime.setText(date);
            txNameImporter.setText(newParcelModel.getNameImporter());
            Picasso.get().load(newParcelModel.getImageUrl()).fit().centerCrop().into(imageView);

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
}