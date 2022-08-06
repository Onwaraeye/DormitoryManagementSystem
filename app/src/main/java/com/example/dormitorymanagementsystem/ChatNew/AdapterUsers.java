package com.example.dormitorymanagementsystem.ChatNew;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.dormitorymanagementsystem.R;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class AdapterUsers extends  RecyclerView.Adapter<AdapterUsers.Myholder> {

    Context context;
    List<ModelUser> userList;
    View view;

    public AdapterUsers(Context context, List<ModelUser> userList) {
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @NotNull
    @Override
    public Myholder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_user,parent,false);

        return new Myholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull Myholder holder, int i) {
        String hisUID = userList.get(i).getId();
        String userName = userList.get(i).getFirstname() + " " + userList.get(i).getLastname();
        String userRoom = "";
        if (userList.get(i).getNumroom().equals("none")){
            if (userList.get(i).getRole().equals("Admin")){
                holder.mRoomTv.setText("นิติบุลคล");
            }else {
                holder.mRoomTv.setText("ช่างซ่อม");
            }
        }else {
            userRoom = "ห้อง "+userList.get(i).getNumroom();
            holder.mRoomTv.setText(userRoom);
        }

        String userImage = userList.get(i).getPictureUserUrl();

        holder.mNameTv.setText(userName);

        if (userImage.isEmpty()){
            holder.mAvatarIv.setImageResource(R.drawable.ic_bx_bxs_user_circle);
        }else {
            Glide.with(context).load(userImage).fitCenter().centerCrop().into(holder.mAvatarIv);
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChatActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("hisUid", hisUID);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    class Myholder extends RecyclerView.ViewHolder{

        ImageView mAvatarIv;
        TextView mNameTv, mRoomTv;

        public Myholder(@NonNull @NotNull View itemView) {
            super(itemView);

            mAvatarIv = itemView.findViewById(R.id.avatarIv);
            mNameTv = itemView.findViewById(R.id.nameTv);
            mRoomTv = itemView.findViewById(R.id.roomTv);

        }
    }

}
