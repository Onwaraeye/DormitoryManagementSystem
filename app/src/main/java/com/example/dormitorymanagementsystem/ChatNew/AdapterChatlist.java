package com.example.dormitorymanagementsystem.ChatNew;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.dormitorymanagementsystem.MainActivity;
import com.example.dormitorymanagementsystem.R;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;

public class AdapterChatlist extends RecyclerView.Adapter<AdapterChatlist.MyHolder> {

    Context context;
    List<ModelUser> userList;
    private HashMap<String,String> lastMessageMap;
    private HashMap<String,String> typeMessageMap;
    private HashMap<String,String> isSeenMessageMap;



    public AdapterChatlist(Context context, List<ModelUser> userList) {
        this.context = context;
        this.userList = userList;
        lastMessageMap = new HashMap<>();
        typeMessageMap = new HashMap<>();
        isSeenMessageMap = new HashMap<>();
    }

    @NonNull
    @NotNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_chatlist, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MyHolder holder, int position) {
        String hisUid = userList.get(position).getId();
        String userImage = userList.get(position).getPictureUserUrl();
        String userName = userList.get(position).getFirstname()+" "+userList.get(position).getLastname();
        String lastMessage = lastMessageMap.get(hisUid);
        String userRoom = userList.get(position).getNumroom();
        String typeMessage = typeMessageMap.get(hisUid);
        String isSeenMessage = isSeenMessageMap.get(hisUid);

        holder.nameTv.setText(userName);
        if (lastMessage==null || lastMessage.equals("default")){
            holder.lastMessageTv.setVisibility(View.GONE);
        } else {
            if (typeMessage==null || typeMessage.equals("default")){
                holder.lastMessageTv.setVisibility(View.GONE);
            }else {
                if (typeMessage.equals("image")){
                    holder.lastMessageTv.setVisibility(View.VISIBLE);
                    holder.lastMessageTv.setText("ส่งรูปภาพ");
                }else {
                    holder.lastMessageTv.setVisibility(View.VISIBLE);
                    holder.lastMessageTv.setText(lastMessage);
                }
            }

        }
        if (isSeenMessage != null && isSeenMessage.equals("0")){
            holder.isSeenBadge.setVisibility(View.VISIBLE);
        }else {
            holder.isSeenBadge.setVisibility(View.INVISIBLE);
        }


        if (userImage.isEmpty()){
            holder.profileIv.setImageResource(R.drawable.ic_bx_bxs_user_circle);
        }
        else {
            Glide.with(context).load(userImage).fitCenter().centerCrop().into(holder.profileIv);
        }

        if (userRoom == null){
            if (userList.get(position).getRole().equals("Admin")){
                holder.mRoomTv.setText("นิติบุลคล");
            }else {
                holder.mRoomTv.setText("ช่างซ่อม");
            }
        }else {
            userRoom = "ห้อง "+userList.get(position).getNumroom();
            holder.mRoomTv.setText(userRoom);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChatActivity.class);
                intent.putExtra("hisUid", hisUid);
                context.startActivity(intent);
            }
        });
    }

    public  void setLastMessageMap(String userId, String lastMessage){
        lastMessageMap.put(userId, lastMessage);
    }

    public  void setTypeMessageMap(String userId, String typeMessage){
        typeMessageMap.put(userId, typeMessage);
    }

    public  void setIsSeenMessageMap(String userId, String isSeenMessage){
        isSeenMessageMap.put(userId, isSeenMessage);
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {

        ImageView profileIv;
        TextView nameTv,lastMessageTv,mRoomTv;
        View isSeenBadge;

        public MyHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            profileIv = itemView.findViewById(R.id.profileIv);
            nameTv = itemView.findViewById(R.id.nameTv);
            lastMessageTv = itemView.findViewById(R.id.lastMessageTv);
            mRoomTv = itemView.findViewById(R.id.roomTv);
            isSeenBadge = itemView.findViewById(R.id.isSeenBadge);
        }
    }
}
