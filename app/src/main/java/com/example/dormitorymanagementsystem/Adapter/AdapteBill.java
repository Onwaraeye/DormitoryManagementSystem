package com.example.dormitorymanagementsystem.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dormitorymanagementsystem.AttachPayment;
import com.example.dormitorymanagementsystem.Model.BillModel;
import com.example.dormitorymanagementsystem.Model.RepairModel;
import com.example.dormitorymanagementsystem.MonthlyBill;
import com.example.dormitorymanagementsystem.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class AdapteBill extends RecyclerView.Adapter<AdapteBill.MyViewHolder> {

    private DatabaseReference ref = FirebaseDatabase.getInstance().getReference();

    private Context mContext;
    private List<BillModel> list;
    private List<String> date;


    public AdapteBill(Context mContext, List<BillModel> list, List<String> date) {
        this.mContext = mContext;
        this.list = list;
        this.date = date;
    }

    @NonNull
    @Override
    public AdapteBill.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_bill, parent, false);

        return new AdapteBill.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapteBill.MyViewHolder holder, int position) {
        try {
            String datebill = date.get(position);
            String status = list.get(position).getStatus();

            holder.txBill.setText(datebill);
            if (status.equals("0")){
                holder.txstatus.setText("ยังไม่ได้ชำระ");
                holder.txstatus.setTextColor(ContextCompat.getColor(mContext,R.color.red));
            }else if (status.equals("1")){
                holder.txstatus.setText("รอการตรวจสอบ");
                holder.txstatus.setTextColor(ContextCompat.getColor(mContext,R.color.orange));
            }else {
                holder.txstatus.setText("ชำระแล้ว");
                holder.txstatus.setTextColor(ContextCompat.getColor(mContext,R.color.green));
            }


            holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BillModel billModel = new BillModel();
                    billModel = list.get(position);
                    Intent intent = new Intent(mContext, AttachPayment.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("date", datebill);
                    intent.putExtra("bill",(BillModel) billModel);
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

        TextView txBill, txstatus;
        LinearLayout linearLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            txBill = itemView.findViewById(R.id.txBill);
            txstatus = itemView.findViewById(R.id.txstatus);
            linearLayout = itemView.findViewById(R.id.linearLayout);
        }
    }
    /*public void updateData(List list) {
        this.list = list;
        notifyDataSetChanged();
    }*/
}