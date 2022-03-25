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

import com.example.dormitorymanagementsystem.Central;
import com.example.dormitorymanagementsystem.Login;
import com.example.dormitorymanagementsystem.Manager.Chat;
import com.example.dormitorymanagementsystem.Info;
import com.example.dormitorymanagementsystem.Manager.ChatManager;
import com.example.dormitorymanagementsystem.Manager.ViewRoom;
import com.example.dormitorymanagementsystem.Manager.ManagerPhone;
import com.example.dormitorymanagementsystem.Manager.Post;
import com.example.dormitorymanagementsystem.Manager.SentParcel;
import com.example.dormitorymanagementsystem.Manager.ViewCentralUse;
import com.example.dormitorymanagementsystem.Manager.ViewRepair;
import com.example.dormitorymanagementsystem.MonthlyBill;
import com.example.dormitorymanagementsystem.MyRoom;
import com.example.dormitorymanagementsystem.Parcel;
import com.example.dormitorymanagementsystem.R;
import com.example.dormitorymanagementsystem.Repair;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

public class HomeFragment extends Fragment {



    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference("Users");
    private DatabaseReference myRefContact = database.getReference("Contact");

    private LinearLayout menu_bill,menu_parcel,menu_central,menu_my_room,menu_phone,menu_info,menu_repair,menu_chat;
    private LinearLayout menu_sent_parcel,menu_edit_phone,menu_edit_room_member,menu_chat_manager,menu_view_central,menu_post,menu_view_repair,menu_edit_info;
    private LinearLayout user1,user2,user3,user4,admin1,admin2,admin3,admin4;
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
                    int id = getResources().getIdentifier("@drawable/ic_bx_bxs_user_circle", "drawable", getActivity().getPackageName());
                    imUser.setImageResource(id);
                }else {
                    Picasso.get().load(imageURL).fit().centerCrop().into(imUser);
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
                Intent intent = new Intent(getActivity(), Repair.class);
                getActivity().startActivity(intent);
            }
        });
        menu_chat = view.findViewById(R.id.menu_chat);
        menu_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Chat.class);
                if (getType.equals("User")){
                    intent.putExtra("userID","Msg01");
                }
                getActivity().startActivity(intent);

            }
        });
        menu_sent_parcel = view.findViewById(R.id.menu_sent_parcel);
        menu_sent_parcel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SentParcel.class);
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
                Intent intent = new Intent(getActivity(), ChatManager.class);
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
        menu_post = view.findViewById(R.id.menu_post);
        menu_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Post.class);
                intent.putExtra("status","0");
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

        if (Login.getGbTypeUser().equals("User")){
            admin1 = view.findViewById(R.id.admin1);
            admin1.setVisibility(View.GONE);
            admin2 = view.findViewById(R.id.admin2);
            admin2.setVisibility(View.GONE);
            admin3 = view.findViewById(R.id.admin3);
            admin3.setVisibility(View.GONE);
            admin4 = view.findViewById(R.id.admin4);
            admin4.setVisibility(View.GONE);
        }else {
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