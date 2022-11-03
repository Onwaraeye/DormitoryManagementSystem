package com.example.dormitorymanagementsystem.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.dormitorymanagementsystem.Central;
import com.example.dormitorymanagementsystem.ChatNew.ChatActivity;
import com.example.dormitorymanagementsystem.ChatNew.ContactActivity;
import com.example.dormitorymanagementsystem.Login;
import com.example.dormitorymanagementsystem.Info;
import com.example.dormitorymanagementsystem.Manager.MeterRecord;
import com.example.dormitorymanagementsystem.Manager.ViewBills;
import com.example.dormitorymanagementsystem.Manager.ViewRoom;
import com.example.dormitorymanagementsystem.Manager.ManagerPhone;
import com.example.dormitorymanagementsystem.Manager.SentParcel;
import com.example.dormitorymanagementsystem.Manager.ViewCentralUse;
import com.example.dormitorymanagementsystem.Manager.ViewRepair;
import com.example.dormitorymanagementsystem.MonthlyBill;
import com.example.dormitorymanagementsystem.MyRoom;
import com.example.dormitorymanagementsystem.Parcel;
import com.example.dormitorymanagementsystem.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {



    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference("Users");
    private DatabaseReference myRefContact = database.getReference("Contact");

    private LinearLayout menu_bill,menu_parcel,menu_central,menu_my_room,menu_phone,menu_info,menu_repair,menu_chat;
    private LinearLayout menu_sent_parcel,menu_edit_phone,menu_edit_room_member,menu_chat_manager,menu_view_central,menu_view_repair,menu_edit_info,menu_view_bill,menu_add_unit;
    private LinearLayout user1,user2,user3,user4,admin1,admin2,admin3,admin4,admin5,repair;
    private View view;
    private final String getType = Login.getGbTypeUser();

    public HomeFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);

        TextView txNameDormitory = view.findViewById(R.id.txNameDormitory);
        myRefContact.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                String nameDor = snapshot.child("nameDormitory").getValue(String.class);
                if (nameDor != null){
                    txNameDormitory.setText(nameDor);
                }
            }
            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
            }
        });


        String userID = Login.getGbIdUser();
        ImageView imUser = view.findViewById(R.id.imUser);
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                String imageURL = snapshot.child(userID).child("pictureUserUrl").getValue(String.class);
                if (imageURL.isEmpty()){
                    int id = getResources().getIdentifier("@drawable/ic_bx_bxs_user_home", "drawable", getActivity().getPackageName());
                    imUser.setImageResource(id);
                }else {
                    Glide.with(getActivity().getApplicationContext()).load(imageURL).fitCenter().centerCrop().into(imUser);
                }

            }
            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
            }
        });


        menu_bill = view.findViewById(R.id.menu_bill);
        menu_bill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MonthlyBill.class);
                getActivity().startActivity(intent);
            }
        });
        menu_parcel = view.findViewById(R.id.menu_parcel);
        menu_parcel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Parcel.class);
                getActivity().startActivity(intent);
            }
        });
        menu_central = view.findViewById(R.id.menu_central);
        menu_central.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Central.class);
                getActivity().startActivity(intent);
            }
        });
        menu_my_room = view.findViewById(R.id.menu_my_room);
        menu_my_room.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MyRoom.class);
                getActivity().startActivity(intent);
            }
        });
        menu_phone = view.findViewById(R.id.menu_phone);
        menu_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ManagerPhone.class);
                getActivity().startActivity(intent);
            }
        });
        menu_info = view.findViewById(R.id.menu_info);
        menu_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Info.class);
                getActivity().startActivity(intent);
            }
        });
        menu_repair = view.findViewById(R.id.menu_repair);
        menu_repair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ViewRepair.class);
                getActivity().startActivity(intent);
            }
        });
        menu_chat = view.findViewById(R.id.menu_chat);
        menu_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ChatActivity.class);
                if (getType.equals("User")){
                    intent.putExtra("hisUid","Mng01");
                }
                getActivity().startActivity(intent);
            }
        });
        menu_sent_parcel = view.findViewById(R.id.menu_sent_parcel);
        menu_sent_parcel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Parcel.class);
                getActivity().startActivity(intent);
            }
        });
        menu_edit_phone = view.findViewById(R.id.menu_edit_phone);
        menu_edit_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ManagerPhone.class);
                getActivity().startActivity(intent);
            }
        });

        menu_edit_room_member = view.findViewById(R.id.menu_edit_room_member);
        menu_edit_room_member.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ViewRoom.class);
                getActivity().startActivity(intent);
            }
        });

        menu_chat_manager = view.findViewById(R.id.menu_chat_manager);
        menu_chat_manager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ContactActivity.class);
                getActivity().startActivity(intent);
            }
        });
        menu_view_central = view.findViewById(R.id.menu_view_central);
        menu_view_central.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ViewCentralUse.class);
                getActivity().startActivity(intent);
            }
        });

        menu_view_repair = view.findViewById(R.id.menu_view_repair);
        menu_view_repair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ViewRepair.class);
                getActivity().startActivity(intent);
            }
        });

        menu_edit_info = view.findViewById(R.id.menu_edit_info);
        menu_edit_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Info.class);
                getActivity().startActivity(intent);
            }
        });

        menu_view_bill = view.findViewById(R.id.menu_view_bill);
        menu_view_bill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ViewBills.class);
                getActivity().startActivity(intent);
            }
        });

        LinearLayout menu_view_repair_work = view.findViewById(R.id.menu_view_repair_work);
        menu_view_repair_work.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ViewRepair.class);
                getActivity().startActivity(intent);
            }
        });

        LinearLayout menu_chat_manager_rpm = view.findViewById(R.id.menu_chat_manager_rpm);
        menu_chat_manager_rpm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ChatActivity.class);
                intent.putExtra("hisUid","Mng01");
                getActivity().startActivity(intent);
            }
        });

        menu_add_unit = view.findViewById(R.id.menu_add_unit);
        menu_add_unit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MeterRecord.class);
                getActivity().startActivity(intent);
            }
        });

        if (Login.getGbTypeUser().equals("User")){
            admin1 = view.findViewById(R.id.admin1);
            admin1.setVisibility(View.GONE);
            admin2 = view.findViewById(R.id.admin2);
            admin2.setVisibility(View.GONE);
            admin3 = view.findViewById(R.id.admin3);
            admin3.setVisibility(View.GONE);
            admin4 = view.findViewById(R.id.admin4);
            admin4.setVisibility(View.GONE);
            admin5 = view.findViewById(R.id.admin5);
            admin5.setVisibility(View.GONE);
            repair = view.findViewById(R.id.repair);
            repair.setVisibility(View.GONE);
        }else if (Login.getGbTypeUser().equals("Admin")){
            user1 = view.findViewById(R.id.user1);
            user1.setVisibility(View.GONE);
            user2 = view.findViewById(R.id.user2);
            user2.setVisibility(View.GONE);
            user3 = view.findViewById(R.id.user3);
            user3.setVisibility(View.GONE);
            user4 = view.findViewById(R.id.user4);
            user4.setVisibility(View.GONE);
            repair = view.findViewById(R.id.repair);
            repair.setVisibility(View.GONE);
        }else {
            admin1 = view.findViewById(R.id.admin1);
            admin1.setVisibility(View.GONE);
            admin2 = view.findViewById(R.id.admin2);
            admin2.setVisibility(View.GONE);
            admin3 = view.findViewById(R.id.admin3);
            admin3.setVisibility(View.GONE);
            admin4 = view.findViewById(R.id.admin4);
            admin4.setVisibility(View.GONE);
            admin5 = view.findViewById(R.id.admin5);
            admin5.setVisibility(View.GONE);
            user1 = view.findViewById(R.id.user1);
            user1.setVisibility(View.GONE);
            user2 = view.findViewById(R.id.user2);
            user2.setVisibility(View.GONE);
            user3 = view.findViewById(R.id.user3);
            user3.setVisibility(View.GONE);
            user4 = view.findViewById(R.id.user4);
            user4.setVisibility(View.GONE);
        }

        return view;
    }
}