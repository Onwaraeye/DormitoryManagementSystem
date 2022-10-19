package com.example.dormitorymanagementsystem.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dormitorymanagementsystem.BookingDetails;
import com.example.dormitorymanagementsystem.Manager.EditMember;
import com.example.dormitorymanagementsystem.Model.CentralModel;
import com.example.dormitorymanagementsystem.Model.RoomModel;
import com.example.dormitorymanagementsystem.R;

import java.util.ArrayList;
import java.util.List;

public class AdapterRoom extends RecyclerView.Adapter<AdapterRoom.AdapterRoomHolder> {
    private Context mContext;
    List<RoomModel> list;

    public AdapterRoom(Context mContext, List<RoomModel> list) {
        this.mContext = mContext;
        this.list = list;
    }

    @NonNull
    @Override
    public AdapterRoom.AdapterRoomHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_room, parent, false);
        return new AdapterRoom.AdapterRoomHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterRoom.AdapterRoomHolder holder, int position) {
        try {
            int count = list.get(position).getListMember().size();
            if (count < 1) {
                holder.txRoom.setTextColor(ContextCompat.getColor(mContext, R.color.red));
            }else {

            }

            holder.txRoom.setText("ห้อง " + list.get(position).getNumroom());
            holder.menu_edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, EditMember.class);
                    intent.putStringArrayListExtra("listMember", (ArrayList<String>) list.get(position).getListMember());
                    intent.putExtra("room", list.get(position).getNumroom());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
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

    public class AdapterRoomHolder extends RecyclerView.ViewHolder {

        TextView txRoom, menu_edit;

        public AdapterRoomHolder(@NonNull View itemView) {
            super(itemView);

            txRoom = itemView.findViewById(R.id.txRoom);
            menu_edit = itemView.findViewById(R.id.menu_edit);
        }
    }
}
