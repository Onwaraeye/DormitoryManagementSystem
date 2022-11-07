package com.example.dormitorymanagementsystem.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.dormitorymanagementsystem.MainActivity;
import com.example.dormitorymanagementsystem.Manager.EditAddMember;
import com.example.dormitorymanagementsystem.Manager.EditMember;
import com.example.dormitorymanagementsystem.Model.EditMemberModel;
import com.example.dormitorymanagementsystem.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class AdapterEditMember extends RecyclerView.Adapter<AdapterEditMember.AdapterEditMemberHolder> {

    DatabaseReference ref = FirebaseDatabase.getInstance().getReference();

    private Context mContext;
    List<EditMemberModel> list;

    public AdapterEditMember(Context mContext, List<EditMemberModel> list) {
        this.mContext = mContext;
        this.list = list;
    }

    @NonNull
    @Override
    public AdapterEditMember.AdapterEditMemberHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_edit_member,parent,false);

        return new AdapterEditMemberHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterEditMember.AdapterEditMemberHolder holder, int position) {
        try {
            holder.text_name.setText(list.get(position).getName());
            if (list.get(position).getOwner().equals("owner")){
                holder.txOwn.setText("ผู้เช่า");
            } else{
                holder.txOwn.setText("ผู้อยู่อาศัย");
            }
            if (!list.get(position).getImage().isEmpty()){
                Glide.with(mContext).load(list.get(position).getImage()).fitCenter().centerCrop().into(holder.imageView);
            }else {
                Glide.with(mContext).load(R.drawable.ic_carbon_user_avatar).fitCenter().into(holder.imageView);
            }
            holder.bt_del.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDialogDelete(list.get(position).getUserId(),list.get(position).getRoom(),position);
                }
            });
            holder.bt_edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, EditAddMember.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("from", "edit");
                    intent.putExtra("userID", list.get(position).getUserId());
                    intent.putExtra("role", list.get(position).getOwner());
                    mContext.startActivity(intent);
                }
            });

        }catch (NullPointerException e){ }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class AdapterEditMemberHolder extends RecyclerView.ViewHolder {

        TextView text_name,bt_del,txOwn,bt_edit;
        ImageView imageView;

        public AdapterEditMemberHolder(@NonNull View itemView) {
            super(itemView);

            text_name = itemView.findViewById(R.id.textName);
            txOwn = itemView.findViewById(R.id.txOwn);
            bt_del = itemView.findViewById(R.id.bt_del);
            imageView = itemView.findViewById(R.id.imageView);
            bt_edit = itemView.findViewById(R.id.bt_edit);
        }
    }

    public void delete(int position){
        list.remove(position);
        notifyItemRemoved(position);
    }

    private void showDialogDelete(String id,String room,int position){
        AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
        dialog.setTitle("ลบ");
        dialog.setMessage("คุณแน่ใจที่จะลบหรือไม่");
        dialog.setPositiveButton("ตกลง", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ref.child("Users").child(id).child("numroom").setValue("");
                ref.child("Room").child(room).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        for (DataSnapshot ds : snapshot.getChildren()){
                            try {
                                if (ds.getValue().equals(id)){
                                    ds.getRef().removeValue();
                                    // delete(position);
                                    list.remove(position);
                                    notifyDataSetChanged();
                                    Toast.makeText(mContext, "ลบสำเร็จ", Toast.LENGTH_SHORT).show();
                                }
                            }catch (Exception e){

                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                    }
                });
            }
        });
        dialog.setNegativeButton("ยกเลิก", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = dialog.create();
        alertDialog.show();
    }
}
