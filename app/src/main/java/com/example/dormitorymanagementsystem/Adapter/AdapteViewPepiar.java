package com.example.dormitorymanagementsystem.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dormitorymanagementsystem.Login;
import com.example.dormitorymanagementsystem.Manager.Post;
import com.example.dormitorymanagementsystem.Model.PostModel;
import com.example.dormitorymanagementsystem.Model.RepairModel;
import com.example.dormitorymanagementsystem.R;
import com.example.dormitorymanagementsystem.Repair;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class AdapteViewPepiar extends RecyclerView.Adapter<AdapteViewPepiar.MyViewHolder> {

    private DatabaseReference ref = FirebaseDatabase.getInstance().getReference();

    private Context mContext;
    List<RepairModel> list;
    String typeUser = Login.getGbTypeUser();


    public AdapteViewPepiar(Context mContext, List<RepairModel> list) {
        this.mContext = mContext;
        this.list = list;
    }

    @NonNull
    @Override
    public AdapteViewPepiar.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_repair, parent, false);
        return new AdapteViewPepiar.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapteViewPepiar.MyViewHolder holder, int position) {
        try {
            String status = list.get(position).getStatus();

            String title = list.get(position).getTitleRepair();
            String time = list.get(position).getTimestamp();
            String detail = list.get(position).getDetail();
            String room = list.get(position).getNumroom();
            String image = list.get(position).getImageUrl();
            String phone = list.get(position).getPhone();
            String cost = list.get(position).getCost();
            String repairman = list.get(position).getRepairman();

            SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yy" + " / " + "HH:mm");
            String date = formatter.format(new Date(Long.parseLong(time)));

            if (status.equals("1")){
                String timestampComplete = list.get(position).getTimestampComplete();
                if (timestampComplete != null){
                    String dateComplete = formatter.format(new Date(Long.parseLong(timestampComplete)));
                    holder.txTime.setText("สำเร็จเมื่อ "+dateComplete);
                }
            }
            else if (status.equals("2")){
                String timestampForward = list.get(position).getTimestampForward();
                if (timestampForward != null){
                    String dateForward = formatter.format(new Date(Long.parseLong(timestampForward)));
                    holder.txTime.setText("มอบหมายเมื่อ "+dateForward);
                }
            }
            else if (status.equals("3")){
                String timestampRepair = list.get(position).getTimestampRepair();
                if (timestampRepair != null){
                    String dateRepair = formatter.format(new Date(Long.parseLong(timestampRepair)));
                    holder.txTime.setText("ซ่อมเสร็จเมื่อ "+dateRepair);
                }
            }
            else {
                holder.txTime.setText("");
            }

            if (status.equals("3") && typeUser.equals("Repairman")){
                holder.txTitle.setText(title+" กำลังตรวจสอบ");
                holder.txTitle.setTextColor(Color.RED);
            }else {
                holder.txTitle.setText(title);
            }

            holder.txDetail.setText(date);
            holder.txRoom.setText(room);

            holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, Repair.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("title", title);
                    intent.putExtra("time", time);
                    intent.putExtra("detail", detail);
                    intent.putExtra("room", room);
                    intent.putExtra("image", image);
                    intent.putExtra("phone", phone);
                    intent.putExtra("status", status);
                    intent.putExtra("cost", cost);
                    intent.putExtra("repairman", repairman);
                    mContext.startActivity(intent);
                }
            });

        } catch (NullPointerException e) {
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txTitle, txTime, txDetail, txRoom;
        LinearLayout linearLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            txTitle = itemView.findViewById(R.id.txTitle);
            txTime = itemView.findViewById(R.id.txTime);
            txDetail = itemView.findViewById(R.id.txDetail);
            txRoom = itemView.findViewById(R.id.txRoom);
            linearLayout = itemView.findViewById(R.id.linearLayout);
        }
    }

}