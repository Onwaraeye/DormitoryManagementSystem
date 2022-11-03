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
import android.widget.Toast;

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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Set;

public class CentralReservationFragment extends Fragment {

    private View view;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRefCentral = database.getReference("Central");
    List<CentralModel> listCentral = new ArrayList<>();
    List<String> list = new ArrayList<>();
    List<String> listTitle = new ArrayList<>();

    private Context mContext;
    RecyclerView recyclerView;
    AdapterBookingDetails adapterBookingDetails;

    String monthThai = "";
    String userID = "";
    String sentTitle = "";

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

        userID = Login.getGbIdUser();
        String userFName = Login.getGbFNameUser();
        String userLName = Login.getGbLNameUser();
        String name = userFName + " " + userLName;

        String day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH) + "";
        String month = Calendar.getInstance().get(Calendar.MONTH) + "";
        int mo = Integer.valueOf(month) + 1;

        String year = Calendar.getInstance().get(Calendar.YEAR) + "";
        int ye = Integer.valueOf(year)+543;
        //String date = day + " " + getMonth(mo) + " " + ye;

        mContext = getActivity().getApplication();
        recyclerView = view.findViewById(R.id.list_item);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));

        myRefCentral.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                /*listCentral.clear();
                GenericTypeIndicator<List<String>> genericTypeIndicator = new GenericTypeIndicator<List<String>>() {
                };
                String phone = snapshot.child("fitness").child(year).child(month).child(day).child(userID).child("phone").getValue(String.class);
                if (snapshot.child("fitness").hasChild(year)) {
                    if (snapshot.child("fitness").child(year).hasChild(month)) {
                        if (snapshot.child("fitness").child(year).child(month).hasChild(day)) {
                            if (snapshot.child("fitness").child(year).child(month).child(day).hasChild(userID)) {
                                List<String> timefitness = snapshot.child("fitness").child(year).child(month).child(day).child(userID).child("time").getValue(genericTypeIndicator);
                                if (snapshot.child("fitness").child(year).child(month).child(day).child(userID).child("time").getChildrenCount() == 0){

                                }else {
                                    for (int i = 0; i < timefitness.size(); i++) {
                                        CentralModel centralModel = new CentralModel("พื้นที่ออกกำลังกาย", name, date, convertTime(timefitness.get(i)),userID,phone);
                                        listCentral.add(centralModel);
                                    }
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
                                if (snapshot.child("tutoringRoom").child(year).child(month).child(day).child(userID).child("time").getChildrenCount() == 0){
                                }else {
                                    for (int i = 0; i < timetutoringRoom.size(); i++) {
                                        CentralModel centralModel = new CentralModel("ห้องติวหนังสือ", name, date, convertTime(timetutoringRoom.get(i)),userID,phone);
                                        listCentral.add(centralModel);
                                    }
                                }

                            }
                        }
                    }
                }
                adapterBookingDetails = new AdapterBookingDetails(mContext, listCentral);
                recyclerView.setAdapter(adapterBookingDetails);*/
                //
                try {
                    for (DataSnapshot dsTitle : snapshot.getChildren()){
                        Log.e("dsTitle", dsTitle.getKey());
                        String title = dsTitle.getKey();
                        list.clear();
                        listCentral.clear();
                        for (int d=Integer.parseInt(day); d<31;d++){
                            for (DataSnapshot ds : snapshot.child(dsTitle.getKey()).child(year).child(mo + "").child(d+"").getChildren()) {
                                list.add(ds.getKey());
                            }
                            Log.e("listtime", list.toString());
                            for (String t : list) {
                                Query query = myRefCentral.child(dsTitle.getKey()).child(year).child(mo+"").child(d+"").child(t).orderByKey().equalTo(userID);
                                String date = d + " " + getMonth(mo) + " " + ye;
                                query.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                            if (title!=null && title.equals("fitness")){
                                                sentTitle = "พื้นที่ออกกำลังกาย";
                                            }else {
                                                sentTitle = "ห้องอเนกประสงค์";
                                            }
                                            String phone = dataSnapshot.child("phone").getValue(String.class);
                                            CentralModel centralModel = new CentralModel(sentTitle, name, date, t,userID,phone);
                                            listCentral.add(centralModel);
                                            Log.e("listCentral", dataSnapshot.getKey());
                                        }
                                        adapterBookingDetails = new AdapterBookingDetails(mContext, listCentral);
                                        recyclerView.setAdapter(adapterBookingDetails);
                                        //adapterBookingDetails.notifyDataSetChanged();
                                    }
                                    @Override
                                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                    }
                                });
                            }
                        }


                    }
                }catch (Exception e){
                    Toast.makeText(mContext, e.toString(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        return view;
    }

    public String getMonth(int month) {
        switch (month) {
            case 1:
                monthThai = "มกราคม";
                break;
            case 2:
                monthThai = "กุมภาพันธ์";
                break;
            case 3:
                monthThai = "มีนาคม";
                break;
            case 4:
                monthThai = "เมษายน";
                break;
            case 5:
                monthThai = "พฤษภาคม";
                break;
            case 6:
                monthThai = "มิถุนายน";
                break;
            case 7:
                monthThai = "กรกฎาคม";
                break;
            case 8:
                monthThai = "สิงหาคม";
                break;
            case 9:
                monthThai = "กันยายน";
                break;
            case 10:
                monthThai = "ตุลาคม";
                break;
            case 11:
                monthThai = "พฤศจิกายน";
                break;
            case 12:
                monthThai = "ธันวาคม";
                break;
        }
        return monthThai;
    }

    /*public String convertTime(String time) {
        String cvTime = "";
        switch (time) {
            case "8":
                cvTime = "08:00 - 09:00 น.";
                break;
            case "9":
                cvTime = "09:00 - 10:00 น.";
                break;
            case "10":
                cvTime = "10:00 - 11:00 น.";
                break;
            case "11":
                cvTime = "11:00 - 12:00 น.";
                break;
            case "12":
                cvTime = "12:00 - 13:00 น.";
                break;
            case "13":
                cvTime = "13:00 - 14:00 น.";
                break;
            case "14":
                cvTime = "14:00 - 15:00 น.";
                break;
            case "15":
                cvTime = "15:00 - 16:00 น.";
                break;
            case "16":
                cvTime = "16:00 - 17:00 น.";
                break;
            case "17":
                cvTime = "17:00 - 18:00 น.";
                break;
            case "18":
                cvTime = "18:00 - 19:00 น.";
                break;
            case "19":
                cvTime = "19:00 - 20:00 น.";
                break;
        }
        return cvTime;
    }*/
}