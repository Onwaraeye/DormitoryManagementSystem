package com.example.dormitorymanagementsystem.Fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;

import com.example.dormitorymanagementsystem.Adapter.AdapterBookingDetails;
import com.example.dormitorymanagementsystem.Adapter.AdapterNameMember;
import com.example.dormitorymanagementsystem.Central;
import com.example.dormitorymanagementsystem.Login;
import com.example.dormitorymanagementsystem.Model.CentralModel;
import com.example.dormitorymanagementsystem.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Set;

public class CentralReservationFragment extends Fragment {

    private View view;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRefCentral = database.getReference("Central");
    List<CentralModel> listCentral = new ArrayList<>();

    private Context mContext;
    RecyclerView recyclerView;
    AdapterBookingDetails adapterBookingDetails;

    String monthThai = "";

    public CentralReservationFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_central_reservation, container, false);

        String userID = Login.getGbIdUser();
        String userFName = Login.getGbFNameUser();
        String userLName = Login.getGbLNameUser();
        String name = userFName + " " + userLName;

        String day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH) + "";
        String month = Calendar.getInstance().get(Calendar.MONTH) + "";
        String year = Calendar.getInstance().get(Calendar.YEAR) + "";
        String date = day + " " + getMonth(Integer.parseInt(month)) + " " + year;

        mContext = getActivity().getApplication();
        recyclerView = view.findViewById(R.id.list_item);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));

        myRefCentral.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                listCentral.clear();
                GenericTypeIndicator<List<String>> genericTypeIndicator = new GenericTypeIndicator<List<String>>() {
                };
                if (snapshot.child("fitness").hasChild(year)) {
                    if (snapshot.child("fitness").child(year).hasChild(month)) {
                        if (snapshot.child("fitness").child(year).child(month).hasChild(day)) {
                            if (snapshot.child("fitness").child(year).child(month).child(day).hasChild(userID)) {
                                List<String> timefitness = snapshot.child("fitness").child(year).child(month).child(day).child(userID).child("time").getValue(genericTypeIndicator);
                                for (int i = 0; i < timefitness.size(); i++) {
                                    CentralModel centralModel = new CentralModel("Fitness", name, date, convertTime(timefitness.get(i)));
                                    listCentral.add(centralModel);
                                }
                            }
                        }
                    }
                }
                if (snapshot.child("tutoringRoom").hasChild(year)) {
                    if (snapshot.child("tutoringRoom").child(year).hasChild(month)) {
                        if (snapshot.child("tutoringRoom").child(year).child(month).hasChild(day)) {
                            if (snapshot.child("tutoringRoom").child(year).child(month).child(day).hasChild(userID)) {
                                List<String> timetutoringRoom = snapshot.child("tutoringRoom").child(year).child(month).child(day).child(userID).child("time").getValue(genericTypeIndicator);
                                for (int i = 0; i < timetutoringRoom.size(); i++) {
                                    CentralModel centralModel = new CentralModel("Tutoring Room", name, date, convertTime(timetutoringRoom.get(i)));
                                    listCentral.add(centralModel);
                                }
                            }
                        }
                    }
                }
                adapterBookingDetails = new AdapterBookingDetails(mContext, listCentral);
                recyclerView.setAdapter(adapterBookingDetails);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        return view;
    }

    public String getMonth(int month) {
        switch (month) {
            case 0:
                monthThai = "??????????????????";
                break;
            case 1:
                monthThai = "??????????????????????????????";
                break;
            case 2:
                monthThai = "??????????????????";
                break;
            case 3:
                monthThai = "??????????????????";
                break;
            case 4:
                monthThai = "?????????????????????";
                break;
            case 5:
                monthThai = "????????????????????????";
                break;
            case 6:
                monthThai = "?????????????????????";
                break;
            case 7:
                monthThai = "?????????????????????";
                break;
            case 8:
                monthThai = "?????????????????????";
                break;
            case 9:
                monthThai = "??????????????????";
                break;
            case 10:
                monthThai = "???????????????????????????";
                break;
            case 11:
                monthThai = "?????????????????????";
                break;
        }
        return monthThai;
    }

    public String convertTime(String time) {
        String cvTime = "";
        switch (time) {
            case "0":
                cvTime = "08:00 - 09:00 ???.";
                break;
            case "1":
                cvTime = "09:00 - 10:00 ???.";
                break;
            case "2":
                cvTime = "10:00 - 11:00 ???.";
                break;
            case "3":
                cvTime = "11:00 - 12:00 ???.";
                break;
            case "4":
                cvTime = "12:00 - 13:00 ???.";
                break;
            case "5":
                cvTime = "13:00 - 14:00 ???.";
                break;
            case "6":
                cvTime = "14:00 - 15:00 ???.";
                break;
            case "7":
                cvTime = "15:00 - 16:00 ???.";
                break;
            case "8":
                cvTime = "16:00 - 17:00 ???.";
                break;
            case "9":
                cvTime = "17:00 - 18:00 ???.";
                break;
            case "10":
                cvTime = "18:00 - 19:00 ???.";
                break;
            case "11":
                cvTime = "19:00 - 20:00 ???.";
                break;
        }
        return cvTime;
    }
}