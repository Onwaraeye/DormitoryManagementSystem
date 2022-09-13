package com.example.dormitorymanagementsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.dormitorymanagementsystem.Model.BillModel;
import com.example.dormitorymanagementsystem.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Login extends AppCompatActivity {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("Users");
    DatabaseReference myRefRoom = database.getReference("Room");

    private EditText etUser;
    private EditText etPass;
    private String inputUser;
    private String inputPass;

    private static String gbroom;
    public static String getGbNumroom() {
        return gbroom;
    }
    public static void setGbNumroom(String gbNumroom) {
        gbroom = gbNumroom;
    }

    private static String gbUserID;
    public static String getGbIdUser() {
        return gbUserID;
    }
    public static void setGbIdUser(String gbIdUser) {
        gbUserID = gbIdUser;
    }

    private static String gbFNameUser;
    public static String getGbFNameUser() {
        return gbFNameUser;
    }
    public static void setGbFNameUser(String gbNameID) {
        Login.gbFNameUser = gbNameID;
    }

    private static String gbLNameUser;
    public static String getGbLNameUser() {
        return gbLNameUser;
    }
    public static void setGbLNameUser(String gbLNameUser) {
        Login.gbLNameUser = gbLNameUser;
    }

    private static String gbTypeUser;
    public static String getGbTypeUser() {
        return gbTypeUser;
    }
    public static void setGbTypeUser(String gbTypeUser) {
        Login.gbTypeUser = gbTypeUser;
    }

    List<List<String>> myList;
    List<String> user,user2;
    List<String> listRoom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        myList = new ArrayList<>();
        user = new ArrayList<>();
        user2 = new ArrayList<>();
        listRoom = new ArrayList<>();

        Button buttonAddData = findViewById(R.id.buttonAddData);
        buttonAddData.setVisibility(View.GONE);
        buttonAddData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRefadd = database.getReference("Users/Rpm02");

                myRefadd.setValue(new User("Rpm02","123456","05/05/2555","eye@gmail.com","ผู้ชาย","คุณ","ตอง","none",
                        "1234567891234","Repairman","0812345678",""));
                //uploadList();
            }
        });


        etUser = findViewById(R.id.etUsername);
        etPass = findViewById(R.id.etPassword);
        //CheckBox remember = findViewById(R.id.remember);
        Button btLogin = findViewById(R.id.btLogin);

        SharedPreferences keep = PreferenceManager.getDefaultSharedPreferences(this);
        String user = keep.getString("memuser",inputUser);
        etUser.setText(user);
        String pass = keep.getString("pass",inputPass);
        etPass.setText(pass);

        SharedPreferences preferences = getSharedPreferences("checkbox",MODE_PRIVATE);
        String checkbox = preferences.getString("remember","");
        if (checkbox.equals("true")){
            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(intent);
            finish();
        }else if (checkbox.equals("false")){

        }

        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputUser = etUser.getText().toString();
                inputPass = etPass.getText().toString();

                SharedPreferences keep = PreferenceManager.getDefaultSharedPreferences(getApplication());
                SharedPreferences.Editor editorkp = keep.edit();
                editorkp.putString("memuser",inputUser);
                editorkp.putString("pass",inputPass);
                editorkp.apply();
                if (inputUser.isEmpty() || inputPass.isEmpty()){
                    Toast.makeText(getApplicationContext(),"Enter details",Toast.LENGTH_SHORT).show();
                }else {
                    myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            if (snapshot.hasChild(inputUser)){
                                final String getPass = snapshot.child(inputUser).child("password").getValue(String.class);
                                if (getPass.equals(inputPass)){
                                    String numroom = snapshot.child(inputUser).child("numroom").getValue(String.class);
                                    String userId = snapshot.child(inputUser).child("id").getValue(String.class);
                                    String userFName = snapshot.child(inputUser).child("firstname").getValue(String.class);
                                    String userLName = snapshot.child(inputUser).child("lastname").getValue(String.class);
                                    String userType = snapshot.child(inputUser).child("role").getValue(String.class);
                                    Login.setGbNumroom(numroom);
                                    Login.setGbIdUser(userId);
                                    Login.setGbFNameUser(userFName);
                                    Login.setGbLNameUser(userLName);
                                    Login.setGbTypeUser(userType);
                                    Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }else {
                                    Toast.makeText(getApplicationContext(),"Incorrect Password",Toast.LENGTH_SHORT).show();
                                }
                            }else {
                                Toast.makeText(getApplicationContext(),"Incorrect User",Toast.LENGTH_SHORT).show();
                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError error) {

                        }
                    });
                }
            }
        });

        /*remember.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isChecked()){
                    SharedPreferences preferences = getSharedPreferences("checkbox",MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("remember","true");
                    editor.apply();
                }else if (!compoundButton.isChecked()){
                    SharedPreferences preferences = getSharedPreferences("checkbox",MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("remember","false");
                    editor.apply();
                }
            }
        });*/
    }

    public void uploadList(){

        listRoom.add("101");
        listRoom.add("102");

        user.add("10101");
        user.add("10102");
        user2.add("10201");
        user2.add("10202");

        myList.add(user);
        myList.add(user2);

        for (int i = 0 ; i<listRoom.size() ; i++){
            myRefRoom.child(listRoom.get(i)).setValue(myList.get(i)).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                }
            });
        }
    }

    public void uploadBill(){

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRefadd = database.getReference("Bills/2022/1/101");

        myRefadd.setValue(new BillModel("0","1400","100","4500","6000","0",""));
    }
}