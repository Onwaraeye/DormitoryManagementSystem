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
import com.example.dormitorymanagementsystem.Model.NewParcelModel;
import com.example.dormitorymanagementsystem.ParcelDetail;
import com.example.dormitorymanagementsystem.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class AdapterNewParcel extends RecyclerView.Adapter<AdapterNewParcel.MyViewHolder> {

    private DatabaseReference ref = FirebaseDatabase.getInstance().getReference();

    private Context mContext;
    private List<NewParcelModel> list;
    String monthThai;


    public AdapterNewParcel(Context mContext, List<NewParcelModel> list) {
        this.mContext = mContext;
        this.list = list;
    }

    @NonNull
    @Override
    public AdapterNewParcel.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_parcel, parent, false);
        return new AdapterNewParcel.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterNewParcel.MyViewHolder holder, int position) {
        try {
            String timestamp = list.get(position).getTimestamp();
            SimpleDateFormat dFormatter = new SimpleDateFormat("dd");
            SimpleDateFormat mFormatter = new SimpleDateFormat("MM");
            SimpleDateFormat yFormatter = new SimpleDateFormat("yy");
            SimpleDateFormat tFormatter = new SimpleDateFormat("HH:mm");
            String day = dFormatter.format(new Date(Long.parseLong(timestamp)));
            String month = mFormatter.format(new Date(Long.parseLong(timestamp)));
            String year = yFormatter.format(new Date(Long.parseLong(timestamp)));
            String time = tFormatter.format(new Date(Long.parseLong(timestamp)));
            int mo = Integer.valueOf(month);
            int ye = Integer.valueOf(year)+43;
            if (ye>=100){
                ye -= 100;
            }
            String date = day+" "+getMonth(mo)+" "+ye+" "+time+" น.";
            if (list.get(position).getStatus().equals("0")) {
                String fname = list.get(position).getFirstname();
                String lname = list.get(position).getLastname();
                String room = list.get(position).getNumroom();
                String imageUrl = list.get(position).getImageUrl();

                String name = fname + " " + lname;

                holder.txName.setText(name);
                holder.txRoom.setText("ห้อง " + room);
                holder.txTime.setText(date);
                Glide.with(mContext).load(imageUrl).fitCenter().centerCrop().into(holder.imageView);

                holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        NewParcelModel newParcelModel = new NewParcelModel();
                        newParcelModel = list.get(position);
                        Intent intent = new Intent(mContext, ParcelDetail.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("parcel",(NewParcelModel) newParcelModel);
                        intent.putExtra("date", date);
                        intent.putExtra("timestamp",timestamp);
                        mContext.startActivity(intent);
                    }
                });

            } else {
                String fname = list.get(position).getFirstname();
                String lname = list.get(position).getLastname();
                String name = fname + " " + lname;
                String nameReceiver = list.get(position).getNameReceiver();
                String room = list.get(position).getNumroom();
                String timestampReceiver = list.get(position).getTimestampReceiver();
                String imageUrl = list.get(position).getImageUrl();

                String dayRe = dFormatter.format(new Date(Long.parseLong(timestampReceiver)));
                String monthRe = mFormatter.format(new Date(Long.parseLong(timestampReceiver)));
                String yearRe = yFormatter.format(new Date(Long.parseLong(timestampReceiver)));
                String timeRe = tFormatter.format(new Date(Long.parseLong(timestampReceiver)));
                int moRe = Integer.valueOf(monthRe);
                int yeRe = Integer.valueOf(yearRe)+43;
                if (ye>=100){
                    yeRe -= 100;
                }
                String dateReceiver = dayRe+" "+getMonth(moRe)+" "+yeRe+" "+timeRe+" น.";

                holder.txName.setText(name);
                String[] fnameReceiver = nameReceiver.split(" ");
                holder.txReceiver.setText("รับโดย " + fnameReceiver[0]);
                holder.txReceiver.setVisibility(View.VISIBLE);
                holder.txRoom.setText("ห้อง " + room);
                holder.txTime.setText(dateReceiver);
                Glide.with(mContext).load(imageUrl).fitCenter().centerCrop().into(holder.imageView);

                holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        NewParcelModel newParcelModel = new NewParcelModel();
                        newParcelModel = list.get(position);
                        Intent intent = new Intent(mContext, ParcelDetail.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("parcel",(NewParcelModel) newParcelModel);
                        intent.putExtra("date", date);
                        intent.putExtra("dateReceiver",dateReceiver);
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

        TextView txName, txRoom, txTime, txReceiver;
        ImageView imageView;
        LinearLayout linearLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            txName = itemView.findViewById(R.id.txName);
            txRoom = itemView.findViewById(R.id.txRoom);
            txTime = itemView.findViewById(R.id.txTime);
            imageView = itemView.findViewById(R.id.imageView);
            txReceiver = itemView.findViewById(R.id.txReceiver);
            linearLayout = itemView.findViewById(R.id.linearLayout);
        }
    }

    public String getMonth(int month) {
        switch (month) {
            case 1:
                monthThai = "ม.ค.";
                break;
            case 2:
                monthThai = "ก.พ.";
                break;
            case 3:
                monthThai = "มี.ค.";
                break;
            case 4:
                monthThai = "เม.ย.";
                break;
            case 5:
                monthThai = "พ.ค.";
                break;
            case 6:
                monthThai = "มิ.ย.";
                break;
            case 7:
                monthThai = "ก.ค.";
                break;
            case 8:
                monthThai = "ส.ค.";
                break;
            case 9:
                monthThai = "ก.ย.";
                break;
            case 10:
                monthThai = "ต.ค.";
                break;
            case 11:
                monthThai = "พ.ย.";
                break;
            case 12:
                monthThai = "ธ.ค.";
                break;
        }
        return monthThai;
    }

}