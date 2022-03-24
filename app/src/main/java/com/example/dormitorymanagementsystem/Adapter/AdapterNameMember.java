package com.example.dormitorymanagementsystem.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dormitorymanagementsystem.Model.User;
import com.example.dormitorymanagementsystem.R;

import java.util.List;

public class AdapterNameMember extends RecyclerView.Adapter<AdapterNameMember.AdapterNameMemberHolder> {

    private Context mContext;
    List<String> list;

    public AdapterNameMember(Context mContext, List<String> list) {
        this.mContext = mContext;
        this.list = list;
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


        }catch (NullPointerException e){ }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class AdapterNameMemberHolder extends RecyclerView.ViewHolder {

        TextView text_name;
        ImageView image;

        public AdapterNameMemberHolder(@NonNull View itemView) {
            super(itemView);

            text_name = itemView.findViewById(R.id.textName);
            image = itemView.findViewById(R.id.image);
        }
    }
}
