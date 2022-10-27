package com.example.dormitorymanagementsystem.ChatNew;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.dormitorymanagementsystem.Login;
import com.example.dormitorymanagementsystem.R;

import org.jetbrains.annotations.NotNull;

import android.text.format.DateFormat;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AdapterChat extends RecyclerView.Adapter<AdapterChat.MyHoler>{

    private static final int MSG_TYPE_LEFT = 0;
    private static final int MSG_TYPE_RIGHT = 1;
    Context context;
    List<ModelChat> chatList;
    String imageUrl;
    String userID = Login.getGbIdUser();
    String typeUser = Login.getGbTypeUser();
    String monthThai;



    public AdapterChat(Context context, List<ModelChat> chatList, String imageUrl) {
        this.context = context;
        this.chatList = chatList;
        this.imageUrl = imageUrl;
    }

    @NonNull
    @NotNull
    @Override
    public MyHoler onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int position) {

        View view;
        if (position==MSG_TYPE_RIGHT){
            view = LayoutInflater.from(context).inflate(R.layout.row_chat_right, parent, false);
        }
        else {
            view = LayoutInflater.from(context).inflate(R.layout.row_chat_left, parent, false);
        }
        return new MyHoler(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MyHoler holder, int position) {
        String message = chatList.get(position).getMessage();
        String timeStamp = chatList.get(position).getTimestamp();
        String type = chatList.get(position).getType();

        SimpleDateFormat dFormatter = new SimpleDateFormat("dd");
        SimpleDateFormat mFormatter = new SimpleDateFormat("MM");
        SimpleDateFormat yFormatter = new SimpleDateFormat("yy");
        SimpleDateFormat tFormatter = new SimpleDateFormat("HH:mm");
        String day = dFormatter.format(new Date(Long.parseLong(timeStamp)));
        String month = mFormatter.format(new Date(Long.parseLong(timeStamp)));
        String year = yFormatter.format(new Date(Long.parseLong(timeStamp)));
        String times = tFormatter.format(new Date(Long.parseLong(timeStamp)));
        int ye = Integer.valueOf(year)+43;
        if (ye>=100){
            ye -= 100;
        }
        String dateTime = day+" "+getMonth(Integer.valueOf(month))+" "+ye+" "+times+"น.";

        if (type.equals("text")){
            holder.messageTv.setVisibility(View.VISIBLE);
            holder.messageIv.setVisibility(View.GONE);
            holder.messageTv.setText(message);
        }else {
            holder.messageTv.setVisibility(View.GONE);
            holder.messageIv.setVisibility(View.VISIBLE);
            Glide.with(context).load(message).fitCenter().into(holder.messageIv);
        }

        holder.nameUser.setText(chatList.get(position).getNameUser());
        holder.timeTv.setText(dateTime);
        if (imageUrl != null){
            if (imageUrl.isEmpty()){
                holder.profileIv.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_24);
            }else {
                Glide.with(context).load(imageUrl).fitCenter().centerCrop().into(holder.profileIv);
            }
        }


        if (position==chatList.size()-1){
            if (chatList.get(position).getIsSeen() == 1){
                holder.isSeenTv.setText("อ่านแล้ว");
            }else {
                holder.isSeenTv.setText("ส่งแล้ว");
            }
        }else {
            holder.isSeenTv.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (typeUser.equals("Admin")){
            if (chatList.get(position).getSender().equals("Mng")){
                return MSG_TYPE_RIGHT;
            }else {
                return MSG_TYPE_LEFT;
            }
        }else {
            if (chatList.get(position).getSender().equals(userID)){
                return MSG_TYPE_RIGHT;
            }else {
                return MSG_TYPE_LEFT;
            }
        }

    }

    class MyHoler extends RecyclerView.ViewHolder{

        ImageView profileIv,messageIv;
        TextView messageTv,timeTv,isSeenTv,nameUser;

        public MyHoler(@NonNull @NotNull View itemView) {
            super(itemView);

            profileIv = itemView.findViewById(R.id.profileIv);
            messageTv = itemView.findViewById(R.id.messageTv);
            messageIv = itemView.findViewById(R.id.messageIv);
            timeTv = itemView.findViewById(R.id.timeTv);
            isSeenTv = itemView.findViewById(R.id.isSeenTv);
            nameUser = itemView.findViewById(R.id.nameUser);
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
