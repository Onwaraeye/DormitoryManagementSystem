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
import androidx.recyclerview.widget.RecyclerView;

import com.example.dormitorymanagementsystem.Model.NewParcelModel;
import com.example.dormitorymanagementsystem.ParcelDetail;
import com.example.dormitorymanagementsystem.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class AdapterNewParcel extends RecyclerView.Adapter<AdapterNewParcel.MyViewHolder> {

    private DatabaseReference ref = FirebaseDatabase.getInstance().getReference();

    private Context mContext;
    private List<NewParcelModel> list;


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
            if (list.get(position).getStatus().equals("0")) {
                String fname = list.get(position).getFirstname();
                String lname = list.get(position).getLastname();
                String room = list.get(position).getNumroom();
                String timestamp = list.get(position).getTimestamp();
                String imageUrl = list.get(position).getImageUrl();

                SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yy" + " / " + "HH:mm");
                String date = formatter.format(new Date(Long.parseLong(timestamp)));

                String name = fname + " " + lname;

                holder.txName.setText(name);
                holder.txRoom.setText("ห้อง " + room);
                holder.txTime.setText(date);
                Picasso.get().load(imageUrl).fit().centerCrop().into(holder.imageView);

                holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        NewParcelModel newParcelModel = new NewParcelModel();
                        newParcelModel = list.get(position);
                        Intent intent = new Intent(mContext, ParcelDetail.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("parcel",(NewParcelModel) newParcelModel);
                        mContext.startActivity(intent);
                    }
                });

            } else {
                String fname = list.get(position).getFirstname();
                String lname = list.get(position).getLastname();
                String name = fname + " " + lname;
                String nameReceiver = list.get(position).getNameReceiver();
                String room = list.get(position).getNumroom();
                String timestamp = list.get(position).getTimestampReceiver();
                String imageUrl = list.get(position).getImageUrl();

                SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yy" + " / " + "HH:mm");
                String dateReceiver = formatter.format(new Date(Long.parseLong(timestamp)));

                holder.txName.setText(name);
                String[] fnameReceiver = nameReceiver.split(" ");
                holder.txReceiver.setText("รับโดย " + fnameReceiver[0]);
                holder.txReceiver.setVisibility(View.VISIBLE);
                holder.txRoom.setText("ห้อง " + room);
                holder.txTime.setText(dateReceiver);
                Picasso.get().load(imageUrl).fit().centerCrop().into(holder.imageView);

                holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        NewParcelModel newParcelModel = new NewParcelModel();
                        newParcelModel = list.get(position);
                        Intent intent = new Intent(mContext, ParcelDetail.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("parcel",(NewParcelModel) newParcelModel);
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

}