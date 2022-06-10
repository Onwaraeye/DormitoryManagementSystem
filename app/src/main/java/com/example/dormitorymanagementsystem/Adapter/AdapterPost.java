package com.example.dormitorymanagementsystem.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.dormitorymanagementsystem.Login;
import com.example.dormitorymanagementsystem.Manager.Post;
import com.example.dormitorymanagementsystem.Model.PostModel;
import com.example.dormitorymanagementsystem.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class AdapterPost extends RecyclerView.Adapter<AdapterPost.MyViewHolder> {

    private DatabaseReference ref = FirebaseDatabase.getInstance().getReference();

    private Context mContext;
    List<PostModel> list;


    public AdapterPost(Context mContext, List<PostModel> list) {
        this.mContext = mContext;
        this.list = list;
    }

    @NonNull
    @Override
    public AdapterPost.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_post,parent,false);
        return new AdapterPost.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterPost.MyViewHolder holder, int position) {
        try {
            String title = list.get(position).getTitle();
            String time = list.get(position).getTimestamp();
            String detail = list.get(position).getDetail();
            String imageUrl = list.get(position).getImageUrl();
            String status = "0";

            SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yy" + " / " + "HH:mm");
            String date = formatter.format(new Date(Long.parseLong(time)));

            holder.txTitle.setText(title);
            holder.txTime.setText(date);
            holder.txDetail.setText(detail);
            Glide.with(mContext).load(imageUrl).fitCenter().centerCrop().into(holder.imageView);

            if (Login.getGbTypeUser().equals("User")){
                holder.btEdit.setVisibility(View.GONE);
            }else {
                holder.btEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, Post.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("titlePost",title);
                        intent.putExtra("detailPost",detail);
                        intent.putExtra("imageUrl",imageUrl);
                        intent.putExtra("time",time);
                        intent.putExtra("status","1");
                        mContext.startActivity(intent);
                    }
                });
            }


        }catch (NullPointerException e){ }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txTitle,txTime,txDetail,btEdit;
        ImageView imageView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            txTitle = itemView.findViewById(R.id.txTitle);
            txTime = itemView.findViewById(R.id.txTime);
            txDetail = itemView.findViewById(R.id.txDetail);
            btEdit = itemView.findViewById(R.id.bt_edit);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }

}