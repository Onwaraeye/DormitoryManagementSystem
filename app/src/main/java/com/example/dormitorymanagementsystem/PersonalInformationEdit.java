package com.example.dormitorymanagementsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dormitorymanagementsystem.Model.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PersonalInformationEdit extends AppCompatActivity {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("Room");
    DatabaseReference myRefUser = database.getReference("Users");

    RadioGroup radioGroup;
    RadioButton radMale,radFemale;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_information_edit);

        String userID = Login.getGbIdUser();

        EditText etFName = findViewById(R.id.etFName);
        EditText etLName = findViewById(R.id.etLName);
        EditText etDate = findViewById(R.id.etDate);


        radioGroup = findViewById(R.id.radioGroup);
        radMale = findViewById(R.id.radMale);
        radFemale = findViewById(R.id.radFemale);



        myRefUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(userID)){
                    String fName = snapshot.child(userID).child("firstname").getValue(String.class);
                    String lName = snapshot.child(userID).child("lastname").getValue(String.class);
                    String birthday = snapshot.child(userID).child("birthday").getValue(String.class);
                    String gender = snapshot.child(userID).child("gender").child("gender").getValue(String.class);
                    int valueGender = 0;
                    if (gender.equals("ผู้ชาย")){
                        valueGender = 0;
                        radMale.setChecked(true);
                    }else {
                        if (gender.equals("ผู้หญิง")){
                            valueGender = 1;
                            radFemale.setChecked(true);
                        }else {
                            Log.e("gender","ผิด");
                        }
                    }
                    Log.e("gender",valueGender+"");
                    etFName.setText(fName);
                    etLName.setText(lName);
                    etDate.setText(birthday);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        Button btConfirm = findViewById(R.id.btConfirm);
        btConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputFName = etFName.getText().toString();
                String inputLName = etLName.getText().toString();
                String inputDate = etDate.getText().toString();

                RadioButton inputradioButton;
                myRefUser.child(userID).child("firstname").setValue(inputFName);
                myRefUser.child(userID).child("lastname").setValue(inputLName);
                myRefUser.child(userID).child("birthday").setValue(inputDate);

                int selectedID = radioGroup.getCheckedRadioButtonId();
                inputradioButton = findViewById(selectedID);
                String gender = inputradioButton.getText().toString();
                myRefUser.child(userID).child("gender").child("gender").setValue(gender);/*.addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Intent intent = new Intent(getApplicationContext(),PersonalInformation.class);
                                startActivity(intent);
                                finish();
                            }
                        });*/
                //startActivity(new Intent(getApplicationContext(),PersonalInformation.class));
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
}