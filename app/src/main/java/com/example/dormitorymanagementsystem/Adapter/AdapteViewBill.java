package com.example.dormitorymanagementsystem.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dormitorymanagementsystem.AttachPayment;
import com.example.dormitorymanagementsystem.Manager.CreateBill;
import com.example.dormitorymanagementsystem.Model.BillModel;
import com.example.dormitorymanagementsystem.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class AdapteViewBill extends RecyclerView.Adapter<AdapteViewBill.MyViewHolder> {

    private Context mContext;
    private List<BillModel> list;
    private List<String> room;
    private String date;

    public AdapteViewBill(Context mContext, List<BillModel> list, List<String> room, String date) {
        this.mContext = mContext;
        this.list = list;
        this.room = room;;
        this.date = date;
    }

    @NonNull
    @Override
    public AdapteViewBill.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_room_bill, parent, false);

        return new AdapteViewBill.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapteViewBill.MyViewHolder holder, int position) {
        try {
            String roombill = room.get(position);
            String status = list.get(position).getStatus();

            holder.txRoom.setText(roombill);
            if (status != null){
                if (status.equals("0")){
                    holder.txstatus.setText("ยังไม่ได้ชำระ");
                    holder.txstatus.setTextColor(ContextCompat.getColor(mContext,R.color.red));
                    holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            BillModel billModel = new BillModel();
                            billModel = list.get(position);
                            Intent intent = new Intent(mContext, CreateBill.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.putExtra("room", roombill);
                            intent.putExtra("date", date);
                            intent.putExtra("bill",(BillModel) billModel);
                            mContext.startActivity(intent);
                        }
                    });
                }else if (status.equals("1")){
                    holder.txstatus.setText("รอการตรวจสอบ");
                    holder.txstatus.setTextColor(ContextCompat.getColor(mContext,R.color.orange));
                    holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            BillModel billModel = new BillModel();
                            billModel = list.get(position);
                            Intent intent = new Intent(mContext, AttachPayment.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.putExtra("room", roombill);
                            intent.putExtra("date", date);
                            intent.putExtra("bill",(BillModel) billModel);
                            mContext.startActivity(intent);
                        }
                    });
                }else {
                    holder.txstatus.setText("ชำระแล้ว");
                    holder.txstatus.setTextColor(ContextCompat.getColor(mContext,R.color.green));
                    holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            BillModel billModel = new BillModel();
                            billModel = list.get(position);
                            Intent intent = new Intent(mContext, AttachPayment.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.putExtra("room", roombill);
                            intent.putExtra("date", date);
                            intent.putExtra("bill",(BillModel) billModel);
                            mContext.startActivity(intent);
                        }
                    });
                }
            }else {
                holder.txstatus.setText("ยังไม่ได้ส่งใบแจ้งหนี้");
                holder.txstatus.setTextColor(ContextCompat.getColor(mContext,R.color.darkergray));
                holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        BillModel billModel = new BillModel();
                        billModel = list.get(position);
                        Intent intent = new Intent(mContext, CreateBill.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("room", roombill);
                        intent.putExtra("date", date);
                        intent.putExtra("bill",(BillModel) billModel);
                        mContext.startActivity(intent);
                    }
                });
            }


        } catch (NullPointerException e) {
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txRoom, txstatus;
        LinearLayout linearLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            txRoom = itemView.findViewById(R.id.txRoom);
            txstatus = itemView.findViewById(R.id.txstatus);
            linearLayout = itemView.findViewById(R.id.linearLayout);
        }
    }
}