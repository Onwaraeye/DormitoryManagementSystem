package com.example.dormitorymanagementsystem.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dormitorymanagementsystem.Login;
import com.example.dormitorymanagementsystem.Manager.Chat;
import com.example.dormitorymanagementsystem.Model.Messages;
import com.example.dormitorymanagementsystem.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    private Context mContext;
    private List<Messages> userMessagesList;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference();


    public MessageAdapter(Context mContext, List<Messages> userMessagesList) {
        this.mContext = mContext;
        this.userMessagesList = userMessagesList;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
    {
        return new MessageViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.messeges_adapter_layout, null));
    }

    @Override
    public void onBindViewHolder(@NonNull final MessageViewHolder holder, int position) {

        Messages list = userMessagesList.get(position);
        holder.txUserID.setText(list.getUserID());
        holder.txlassMessage.setText(list.getLastMessage());

        if (list.getUnseenMessages() == 0){
            holder.unseenMessages.setVisibility(View.INVISIBLE);
            holder.txlassMessage.setTextColor(Color.parseColor("#959595"));
        }else {
            holder.unseenMessages.setVisibility(View.VISIBLE);
            holder.unseenMessages.setText(list.getUnseenMessages()+"");
            holder.txlassMessage.setTextColor(mContext.getResources().getColor(R.color.black));
        }
        holder.rootLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, Chat.class);
                intent.putExtra("userID",list.getUserID());
                intent.putExtra("chat_key",list.getUserID());
                mContext.startActivity(intent);
            }
        });

    }

    public void updateData(List<Messages> userMessagesList){
        this.userMessagesList = userMessagesList;
        notifyDataSetChanged();;
    }

    @Override
    public int getItemCount()
    {
        return userMessagesList.size();
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder
    {
        public TextView txUserID, txlassMessage,unseenMessages;
        private LinearLayout rootLayout;


        public MessageViewHolder(@NonNull View itemView)
        {
            super(itemView);

            txUserID = itemView.findViewById(R.id.txRoom);
            txlassMessage = itemView.findViewById(R.id.txlastMessage);
            unseenMessages = itemView.findViewById(R.id.unseenMessages);
            rootLayout = itemView.findViewById(R.id.rootLayout);
        }
    }
}
