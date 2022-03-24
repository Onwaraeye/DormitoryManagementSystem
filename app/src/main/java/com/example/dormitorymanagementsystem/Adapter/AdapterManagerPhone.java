package com.example.dormitorymanagementsystem.Adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dormitorymanagementsystem.BookingDetails;
import com.example.dormitorymanagementsystem.Login;
import com.example.dormitorymanagementsystem.Manager.ManagerPhone;
import com.example.dormitorymanagementsystem.Manager.ManagerPhoneEdit;
import com.example.dormitorymanagementsystem.Model.CentralModel;
import com.example.dormitorymanagementsystem.Model.ManagerModel;
import com.example.dormitorymanagementsystem.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class AdapterManagerPhone extends RecyclerView.Adapter<AdapterManagerPhone.AdapterManagerPhoneHolder> {

    DatabaseReference ref = FirebaseDatabase.getInstance().getReference();

    private Context mContext;
    List<ManagerModel> list;


    public AdapterManagerPhone(Context mContext, List<ManagerModel> list) {
        this.mContext = mContext;
        this.list = list;
    }

    @NonNull
    @Override
    public AdapterManagerPhone.AdapterManagerPhoneHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_manager_phone,parent,false);
        return new AdapterManagerPhone.AdapterManagerPhoneHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterManagerPhone.AdapterManagerPhoneHolder holder, int position) {
        try {
            String name = list.get(position).getName();
            String phone = list.get(position).getPhone();

            holder.txName.setText(name);
            holder.txPhone.setText(phone);
            if (Login.getGbTypeUser().equals("User")){
                holder.btDel.setVisibility(View.INVISIBLE);
                holder.btEdit.setVisibility(View.INVISIBLE);
            }else {
                holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, ManagerPhoneEdit.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("nameMng",name);
                        intent.putExtra("phoneMng",phone);
                        mContext.startActivity(intent);
                    }
                });
                holder.btDel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showDialogDelete(list.get(position).getPhone());
                    }
                });
            }


        }catch (NullPointerException e){ }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class AdapterManagerPhoneHolder extends RecyclerView.ViewHolder {

        TextView txName,txPhone,btDel,btEdit;
        LinearLayout linearLayout;

        public AdapterManagerPhoneHolder(@NonNull View itemView) {
            super(itemView);

            txName = itemView.findViewById(R.id.txName);
            txPhone = itemView.findViewById(R.id.txPhone);
            linearLayout = itemView.findViewById(R.id.linearLayout);
            btDel = itemView.findViewById(R.id.bt_del);
            btEdit = itemView.findViewById(R.id.bt_edit);
        }
    }

    private void showDialogDelete(String phone){
        AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
        dialog.setTitle("ลบ");
        dialog.setMessage("คุณแน่ใจที่จะลบหรือไม่");
        dialog.setPositiveButton("ตกลง", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Query query = ref.child("Managers").orderByChild("phone").equalTo(phone);
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        for (DataSnapshot ds : snapshot.getChildren()){
                            ds.getRef().removeValue();
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