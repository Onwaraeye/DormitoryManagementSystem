package com.example.dormitorymanagementsystem.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dormitorymanagementsystem.BookingDetails;
import com.example.dormitorymanagementsystem.Central;
import com.example.dormitorymanagementsystem.Model.CentralModel;
import com.example.dormitorymanagementsystem.R;
import com.squareup.picasso.Picasso;

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
            holder.txTime.setText(list.get(position).getTime());
            if (list.get(position).getCentral().equals("Fitness")){
                Picasso.get().load("https://firebasestorage.googleapis.com/v0/b/dekhordemo-23dde.appspot.com/o/fitness.jpg?alt=media&token=65a9ae5b-209d-421a-b3f7-99ed0ce886d9").fit().centerCrop().into(holder.imageView);
            }else {
                Picasso.get().load("https://firebasestorage.googleapis.com/v0/b/dekhordemo-23dde.appspot.com/o/tutoringroom.jpg?alt=media&token=3213b984-1fc9-4d08-8140-3a8682c14529").fit().centerCrop().into(holder.imageView);
            }
            holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, BookingDetails.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
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
}
