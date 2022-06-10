package com.example.dormitorymanagementsystem.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dormitorymanagementsystem.Model.ChatList;
import com.example.dormitorymanagementsystem.Model.MemoryData;
import com.example.dormitorymanagementsystem.R;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.MyViewHolder> {

    private List<ChatList> chatLists;
    private Context context;
    private String userID;

    public ChatAdapter(List<ChatList> chatLists, Context context) {
        this.chatLists = chatLists;
        this.context = context;
        this.userID = MemoryData.getData(context);
    }

    @NonNull
    @NotNull
    @Override
    public ChatAdapter.MyViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_adapter,null));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ChatAdapter.MyViewHolder holder, int position) {
        ChatList list = chatLists.get(position);
        if (list.getUserID().equals(userID)){
            holder.myLayout.setVisibility(View.VISIBLE);
            holder.oppoLayout.setVisibility(View.INVISIBLE);

            holder.myMessage.setText(list.getMessage());
            holder.myTime.setText(list.getDate()+" "+list.getTime());
            holder.myName.setText(list.getUserID());
        }else {
            holder.myLayout.setVisibility(View.INVISIBLE);
            holder.oppoLayout.setVisibility(View.VISIBLE);

            holder.oppoMessage.setText(list.getMessage());
            holder.oppoTime.setText(list.getDate()+" "+list.getTime());
            holder.oppoName.setText(list.getUserID());

        }
    }

    public  void updateChatList(List<ChatList> chatLists){
        this.chatLists = chatLists;
    }

    @Override
    public int getItemCount() {
        return chatLists.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        private LinearLayout oppoLayout,myLayout;
        private TextView oppoMessage,myMessage;
        private TextView oppoTime,myTime;
        private TextView oppoName,myName;

        public MyViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            oppoLayout = itemView.findViewById(R.id.oppoLayout);
            myLayout = itemView.findViewById(R.id.myLayout);
            oppoMessage = itemView.findViewById(R.id.oppoMessage);
            myMessage = itemView.findViewById(R.id.myMessage);
            oppoTime = itemView.findViewById(R.id.oppoMsgTime);
            myTime = itemView.findViewById(R.id.myMsgTime);
            oppoName = itemView.findViewById(R.id.oppoName);
            myName = itemView.findViewById(R.id.myName);
        }
    }
}
