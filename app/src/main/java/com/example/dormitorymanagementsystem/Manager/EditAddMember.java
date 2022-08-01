package com.example.dormitorymanagementsystem.Manager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.dormitorymanagementsystem.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class EditAddMember extends AppCompatActivity {

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference("Room");
    private DatabaseReference myRefUsers = database.getReference("Users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_add_member);

        String numroom = getIntent().getStringExtra("room");
        EditText etUser = findViewById(R.id.etUser);
        List<String> listuser;

        Button btAdd = findViewById(R.id.btAdd);
        btAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputUser = etUser.getText().toString();
                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        long addchild = snapshot.child(numroom).getChildrenCount()+1;
                        if (snapshot.child(numroom).getChildrenCount() >= 4){
                            Toast.makeText(getApplicationContext(), "มีสมาชิกได้ไม่เกิน 3 คน", Toast.LENGTH_SHORT).show();
                        }else {
                            if (snapshot.child(numroom).child("1").getValue(String.class) == null){
                                myRef.child(numroom).child("1").setValue(inputUser);
                            }else {
                                if(snapshot.child(numroom).child("1").getValue(String.class).equals("")){
                                    myRef.child(numroom).child("1").setValue(inputUser);
                                }else {
                                    if (snapshot.child(numroom).child("2").getValue(String.class) == null){
                                        myRef.child(numroom).child("2").setValue(inputUser);
                                    }else {
                                        myRef.child(numroom).child(addchild + "").setValue(inputUser);
                                    }
                                }
                            }
                        }
                        myRefUsers.child(inputUser).child("numroom").setValue(numroom);
                        finish();
                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                    }
                });
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
}