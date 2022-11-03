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
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.dormitorymanagementsystem.Login;
import com.example.dormitorymanagementsystem.Model.RepairModel;
import com.example.dormitorymanagementsystem.R;
import com.example.dormitorymanagementsystem.Repair;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class AdapteViewRepiar extends RecyclerView.Adapter<AdapteViewRepiar.MyViewHolder> {

    private DatabaseReference ref = FirebaseDatabase.getInstance().getReference();

    private Context mContext;
    List<RepairModel> list;
    String typeUser = Login.getGbTypeUser();
    String monthThai;

    public AdapteViewRepiar(Context mContext, List<RepairModel> list) {
        this.mContext = mContext;
        this.list = list;
    }

    @NonNull
    @Override
    public AdapteViewRepiar.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_repair, parent, false);
        return new AdapteViewRepiar.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapteViewRepiar.MyViewHolder holder, int position) {
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

            SimpleDateFormat dFormatter = new SimpleDateFormat("dd");
            SimpleDateFormat mFormatter = new SimpleDateFormat("MM");
            SimpleDateFormat yFormatter = new SimpleDateFormat("yy");
            SimpleDateFormat tFormatter = new SimpleDateFormat("HH:mm");
            String day = dFormatter.format(new Date(Long.parseLong(time)));
            String month = mFormatter.format(new Date(Long.parseLong(time)));
            String year = yFormatter.format(new Date(Long.parseLong(time)));
            String times = tFormatter.format(new Date(Long.parseLong(time)));
            int ye = Integer.valueOf(year)+43;
            if (ye>=100){
                ye -= 100;
            }
            String date = day+" "+getMonth(Integer.valueOf(month))+" "+ye+" "+times+" น.";

            Glide.with(mContext).load(image).fitCenter().centerCrop().into(holder.imageView);

            if (status.equals("1")) {
                String timestampComplete = list.get(position).getTimestampComplete();
                if (timestampComplete != null) {
                    String dayCP = dFormatter.format(new Date(Long.parseLong(timestampComplete)));
                    String monthCP = mFormatter.format(new Date(Long.parseLong(timestampComplete)));
                    String yearCP = yFormatter.format(new Date(Long.parseLong(timestampComplete)));
                    String timesCP = tFormatter.format(new Date(Long.parseLong(timestampComplete)));
                    int yeCP = Integer.valueOf(yearCP)+43;
                    if (yeCP>=100){
                        yeCP -= 100;
                    }
                    String dateComplete = dayCP+" "+getMonth(Integer.parseInt(monthCP))+" "+yeCP+" "+timesCP+" น.";
                    holder.txTime.setText("สำเร็จเมื่อ " + dateComplete);
                }
            } else if (status.equals("2")) {
                String timestampForward = list.get(position).getTimestampForward();
                String dayFW = dFormatter.format(new Date(Long.parseLong(time)));
                String monthFW = mFormatter.format(new Date(Long.parseLong(time)));
                String yearFW = yFormatter.format(new Date(Long.parseLong(time)));
                String timesFW = tFormatter.format(new Date(Long.parseLong(time)));
                int yeFW = Integer.valueOf(yearFW)+43;
                if (yeFW>=100){
                    yeFW -= 100;
                }
                String dateForward = dayFW+" "+getMonth(Integer.valueOf(monthFW))+" "+yeFW+" "+timesFW+" น.";
                if (timestampForward != null) {
                    if (typeUser.equals("User")) {
                        holder.txTime.setVisibility(View.GONE);
                    } else {
                        holder.txTime.setText("มอบหมายเมื่อ "+dateForward);
                    }
                }
            } else if (status.equals("3")) {
                String timestampRepair = list.get(position).getTimestampRepair();
                String dayRP = dFormatter.format(new Date(Long.parseLong(time)));
                String monthRP = mFormatter.format(new Date(Long.parseLong(time)));
                String yearRP = yFormatter.format(new Date(Long.parseLong(time)));
                String timesRP = tFormatter.format(new Date(Long.parseLong(time)));
                int yeRP = Integer.valueOf(year)+43;
                if (yeRP>=100){
                    yeRP -= 100;
                }
                String dateRepair = dayRP+" "+getMonth(Integer.valueOf(monthRP))+" "+yeRP+" "+timesRP+" น.";
                if (timestampRepair != null) {
                    if (typeUser.equals("User")) {
                        holder.txTime.setVisibility(View.GONE);
                    } else {
                        holder.txTime.setText("ซ่อมเสร็จเมื่อ " + dateRepair);
                    }
                }
            } else {
                holder.txTime.setVisibility(View.GONE);
            }

            if (status.equals("3") && typeUser.equals("Repairman")) {
                holder.txTitle.setText(title + " (กำลังตรวจสอบ)");
                holder.txTitle.setTextColor(ContextCompat.getColor(mContext,R.color.red));
            } else if (status.equals("3")  || status.equals("2") && typeUser.equals("User")){
                holder.txTitle.setText(title+" (กำลังดำเนินการ)");
                holder.txTitle.setTextColor(ContextCompat.getColor(mContext,R.color.orange));
            }else {
                holder.txTitle.setText(title);
            }

            holder.txDetail.setText(date);
            holder.txRoom.setText("ห้อง "+room);


            holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, Repair.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("title", title);
                    intent.putExtra("time", time);
                    intent.putExtra("detail", detail);
                    intent.putExtra("room", room);
                    intent.putExtra("date", date);
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
        ImageView imageView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            txTitle = itemView.findViewById(R.id.txTitle);
            txTime = itemView.findViewById(R.id.txTime);
            txDetail = itemView.findViewById(R.id.txDetail);
            txRoom = itemView.findViewById(R.id.txRoom);
            linearLayout = itemView.findViewById(R.id.linearLayout);
            imageView = itemView.findViewById(R.id.imageView);
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