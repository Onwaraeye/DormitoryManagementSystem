package com.example.dormitorymanagementsystem.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.dormitorymanagementsystem.Model.User;
import com.example.dormitorymanagementsystem.R;

import java.util.List;

public class AdapterNameMember extends RecyclerView.Adapter<AdapterNameMember.AdapterNameMemberHolder> {

    private Context mContext;
    private List<String> list;
    private String owner;
    private List<String> listImage;
    private List<String> listMember;

    public AdapterNameMember(Context mContext, List<String> list, String owner, List<String> listImage, List<String> listMember) {
        this.mContext = mContext;
        this.list = list;
        this.owner = owner;
        this.listImage = listImage;
        this.listMember = listMember;
    }

    @NonNull
    @Override
    public AdapterNameMember.AdapterNameMemberHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_member,parent,false);
        return new AdapterNameMemberHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterNameMember.AdapterNameMemberHolder holder, int position) {
        try {
            holder.text_name.setText(list.get(position));
            if (listMember.get(position).equals(owner)){
                holder.txOwn.setVisibility(View.VISIBLE);
            }
            if (!listImage.get(position).isEmpty()){
                Glide.with(mContext).load(listImage.get(position)).fitCenter().centerCrop().into(holder.image);
            }else {
                Glide.with(mContext).load(R.drawable.ic_carbon_user_avatar).fitCenter().into(holder.image);
            }


        }catch (NullPointerException e){ }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class AdapterNameMemberHolder extends RecyclerView.ViewHolder {

        TextView text_name,txOwn;
        ImageView image;

        public AdapterNameMemberHolder(@NonNull View itemView) {
            super(itemView);

            text_name = itemView.findViewById(R.id.textName);
            txOwn = itemView.findViewById(R.id.txOwn);
            image = itemView.findViewById(R.id.imageView);

        }
    }
}
