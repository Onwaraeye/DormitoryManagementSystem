package com.example.dormitorymanagementsystem.ChatNew;

import android.app.Activity;
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

import com.bumptech.glide.Glide;
import com.example.dormitorymanagementsystem.R;



import org.jetbrains.annotations.NotNull;

import java.util.List;

public class AdapterUsers extends  RecyclerView.Adapter<AdapterUsers.Myholder> {

    Context context;
    List<ModelUser> userList;
    String page;
    View view;

    TextView etRepairman;
    RecyclerView recyclerView;
    LinearLayout repairPage;
    String repairmanID;

    View rootView;


    public AdapterUsers(Context context, List<ModelUser> userList,String page) {
        this.context = context;
        this.userList = userList;
        this.page = page;
    }

    @NonNull
    @NotNull
    @Override
    public Myholder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_user,parent,false);

        if (page.equals("ViewRepairman")){
            rootView = ((Activity) context).getWindow().getDecorView().findViewById(android.R.id.content);
            etRepairman = (TextView) rootView.findViewById(R.id.etRepairman);
            recyclerView = (RecyclerView) rootView.findViewById(R.id.list_item);
            repairPage = (LinearLayout) rootView.findViewById(R.id.repairPage);
        }

        return new Myholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull Myholder holder, int i) {
        String hisUID = userList.get(i).getId();
        String userName = userList.get(i).getFirstname() + " " + userList.get(i).getLastname();
        String userRoom = "";
        if (userList.get(i).getNumroom() == null){
            if (userList.get(i).getRole().equals("Admin")){
                holder.mRoomTv.setText("นิติบุลคล");
            }else if (userList.get(i).getRole().equals("Repairman")){
                holder.mRoomTv.setText("ช่างซ่อม");
            }else {
                holder.mRoomTv.setText("");
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

        if (page.equals("UsersFragment")){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ChatActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("hisUid", hisUID);
                    context.startActivity(intent);
                }
            });
        }else {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    etRepairman.setText(hisUID);
                    recyclerView.setVisibility(View.GONE);
                    repairPage.setVisibility(View.VISIBLE);


                    /*Intent intent = new Intent(context, InfoEditBank.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("hisUid", hisUID);
                    context.startActivity(intent);
                    ((Activity)context).finish();*/
                }
            });
        }

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
