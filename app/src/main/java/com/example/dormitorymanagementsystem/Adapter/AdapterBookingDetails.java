package com.example.dormitorymanagementsystem.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.dormitorymanagementsystem.BookingDetails;
import com.example.dormitorymanagementsystem.Central;
import com.example.dormitorymanagementsystem.Model.CentralModel;
import com.example.dormitorymanagementsystem.R;

import java.util.List;

public class AdapterBookingDetails extends RecyclerView.Adapter<AdapterBookingDetails.AdapterNameMemberHolder> {

    private Context mContext;
    List<CentralModel> list;

    public AdapterBookingDetails(Context mContext, List<CentralModel> list) {
        this.mContext = mContext;
        this.list = list;
    }

    @NonNull
    @Override
    public AdapterBookingDetails.AdapterNameMemberHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_booking_details,parent,false);
        return new AdapterNameMemberHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterBookingDetails.AdapterNameMemberHolder holder, int position) {
        try {

            holder.txCentral.setText(list.get(position).getCentral());
            holder.txName.setText(list.get(position).getName());
            holder.txDate.setText(list.get(position).getDate());
            holder.txTime.setText(convertTime(list.get(position).getTime()));
            String imageFitness = "https://firebasestorage.googleapis.com/v0/b/dekhordemo-23dde.appspot.com/o/fitness.jpg?alt=media&token=65a9ae5b-209d-421a-b3f7-99ed0ce886d9";
            String imageTutoringRoom = "https://firebasestorage.googleapis.com/v0/b/dekhordemo-23dde.appspot.com/o/tutoringroom.jpg?alt=media&token=3213b984-1fc9-4d08-8140-3a8682c14529";
            if (list.get(position).getCentral().equals("พื้นที่ออกกำลังกาย")){
                Glide.with(mContext).load(imageFitness).fitCenter().centerCrop().into(holder.imageView);
            }else {
                Glide.with(mContext).load(imageTutoringRoom).fitCenter().centerCrop().into(holder.imageView);
            }
            holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, BookingDetails.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("central",list.get(position).getCentral());
                    intent.putExtra("name", list.get(position).getName());
                    intent.putExtra("date", list.get(position).getDate());
                    intent.putExtra("time", list.get(position).getTime());
                    intent.putExtra("phone", list.get(position).getPhone());
                    intent.putExtra("userID", list.get(position).getUserID());
                    intent.putExtra("position", position);
                    if (list.get(position).getCentral().equals("พื้นที่ออกกำลังกาย")){
                        intent.putExtra("image",imageFitness);
                    }else {
                        intent.putExtra("image",imageTutoringRoom);
                    }
                    mContext.startActivity(intent);
                }
            });

        }catch (NullPointerException e){ }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class AdapterNameMemberHolder extends RecyclerView.ViewHolder {

        TextView txCentral,txName,txDate,txTime;
        LinearLayout linearLayout;
        ImageView imageView;

        public AdapterNameMemberHolder(@NonNull View itemView) {
            super(itemView);

            txCentral = itemView.findViewById(R.id.txCentral);
            txName = itemView.findViewById(R.id.txName);
            txDate = itemView.findViewById(R.id.txDate);
            txTime = itemView.findViewById(R.id.txTime);
            linearLayout = itemView.findViewById(R.id.linearLayout);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }

    public String convertTime(String time) {
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
    }
}
