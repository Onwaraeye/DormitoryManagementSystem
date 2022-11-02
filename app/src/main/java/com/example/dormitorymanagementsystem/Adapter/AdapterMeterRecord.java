package com.example.dormitorymanagementsystem.Adapter;

import android.app.Activity;
import android.content.Context;
import android.icu.text.DecimalFormat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dormitorymanagementsystem.Model.RoomModel;
import com.example.dormitorymanagementsystem.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class AdapterMeterRecord extends RecyclerView.Adapter<AdapterMeterRecord.MyViewHolder> {

    private DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Bills");
    private DatabaseReference refView = FirebaseDatabase.getInstance().getReference("View");

    Context mContext;
    public static List<RoomModel> listRoom;
    private String year;
    private String month;
    //private String unitBefore;

    //Button btSave;

    View rootView;

    public AdapterMeterRecord(Context mContext, List<RoomModel> listRoom, String year, String month) {
        this.mContext = mContext;
        this.listRoom = listRoom;
        this.year = year;
        this.month = month;
    }

    /*public AdapterMeterRecord(Context mContext, List<RoomModel> listRoom) {
        this.mContext = mContext;
        this.listRoom = listRoom;
    }*/

    @NonNull
    @NotNull
    @Override
    public AdapterMeterRecord.MyViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_metor, parent, false);

        mContext = parent.getContext();
        rootView = ((Activity) mContext).getWindow().getDecorView().findViewById(android.R.id.content);
        //btSave = (Button) rootView.findViewById(R.id.btSave);

        return new AdapterMeterRecord.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull AdapterMeterRecord.MyViewHolder holder, int position) {
        try {
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                    int mo = Integer.parseInt(month) - 1;
                    int mo2 = Integer.parseInt(month);
                    Log.e("mmo", mo + " " + mo2);
                    String unitBefore = snapshot.child(year).child(mo + "").child(listRoom.get(position).getNumroom()).child("elecafter").getValue(String.class);
                    Log.e("unitBefore", listRoom.get(position).getNumroom() + " " + unitBefore);
                    String unitAfter = snapshot.child(year).child(mo2 + "").child(listRoom.get(position).getNumroom()).child("elecafter").getValue(String.class);

                    if (unitAfter != null) {
                        holder.etUnitAfter.setText(unitAfter);
                        holder.etUnitAfter.setEnabled(false);
                        holder.btSave.setVisibility(View.GONE);
                        holder.btEdit.setVisibility(View.VISIBLE);
                    }
                    if (unitBefore == null) {
                        holder.btSave.setVisibility(View.GONE);
                        holder.btEdit.setVisibility(View.VISIBLE);
                        holder.etUnitAfter.setEnabled(false);
                        holder.btEdit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                holder.btSave.setVisibility(View.VISIBLE);
                                holder.btEdit.setVisibility(View.GONE);
                                holder.txUnitBefore.setEnabled(true);
                                holder.txUnitBefore.setHint("");
                                holder.txUnitBefore.addTextChangedListener(new TextWatcher() {
                                    @Override
                                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                    }

                                    @Override
                                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                                        String elecAfter = s.toString();
                                        holder.btSave.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                ref.child(year).child(mo + "").child(listRoom.get(position).getNumroom()).child("elecafter").setValue(elecAfter);
                                                ref.child(year).child(mo2 + "").child(listRoom.get(position).getNumroom()).child("elecbefore").setValue(elecAfter);
                                                holder.txUnitBefore.setEnabled(false);
                                                holder.btSave.setVisibility(View.GONE);
                                                holder.btEdit.setVisibility(View.VISIBLE);
                                            }
                                        });
                                    }

                                    @Override
                                    public void afterTextChanged(Editable s) {

                                    }
                                });
                            }
                        });

                    }
                    else {
                        holder.txUnitBefore.setText(unitBefore);
                        holder.etUnitAfter.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                                holder.btSave.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        holder.etUnitAfter.setEnabled(false);
                                        holder.btSave.setVisibility(View.GONE);
                                        holder.btEdit.setVisibility(View.VISIBLE);
                                    }
                                });
                            }

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {
                                String elecAfter = s.toString();
                                if (holder.btSave.getVisibility() == View.VISIBLE) {
                                    holder.btSave.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            refView.addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                                    double unitEl = Double.parseDouble(snapshot.child("electricitybill").child("Elec").getValue(String.class));
                                                    //if (unitBefore != null) {
                                                    Log.e("check", "1");
                                                    if (Integer.parseInt(elecAfter) > Integer.parseInt(unitBefore)) {
                                                        Log.e("check", "2");
                                                        double sumEl = (Integer.parseInt(elecAfter) - Integer.parseInt(unitBefore)) * unitEl;
                                                        DecimalFormat formatter = new DecimalFormat("###.00");
                                                        String formatted = formatter.format(sumEl);
                                                        ref.child(year).child(mo2 + "").child(listRoom.get(position).getNumroom()).child("electricity").setValue(formatted);
                                                        ref.child(year).child(mo2 + "").child(listRoom.get(position).getNumroom()).child("elecafter").setValue(elecAfter);
                                                        ref.child(year).child(mo2 + "").child(listRoom.get(position).getNumroom()).child("elecbefore").setValue(unitBefore);
                                                    } else if (Integer.parseInt(elecAfter) < Integer.parseInt(unitBefore)) {
                                                        Log.e("check", "3");
                                                        double sumEl = ((9999 - Integer.parseInt(unitBefore)) + Integer.parseInt(elecAfter)) * unitEl;
                                                        DecimalFormat formatter = new DecimalFormat("###.00");
                                                        String formatted = formatter.format(sumEl);
                                                        ref.child(year).child(mo2 + "").child(listRoom.get(position).getNumroom()).child("electricity").setValue(formatted);
                                                        ref.child(year).child(mo2 + "").child(listRoom.get(position).getNumroom()).child("elecafter").setValue(elecAfter);
                                                        ref.child(year).child(mo2 + "").child(listRoom.get(position).getNumroom()).child("elecbefore").setValue(unitBefore);
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                                }
                                            });
                                            holder.etUnitAfter.setEnabled(false);
                                            holder.btSave.setVisibility(View.GONE);
                                            holder.btEdit.setVisibility(View.VISIBLE);
                                        }
                                    });
                                }
                            }

                            @Override
                            public void afterTextChanged(Editable s) {

                            }
                        });

                        holder.btEdit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                holder.etUnitAfter.setEnabled(true);
                                holder.etUnitAfter.setHint("");
                                holder.btSave.setVisibility(View.VISIBLE);
                                holder.btEdit.setVisibility(View.GONE);
                            }
                        });

                        holder.btSave.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                holder.etUnitAfter.setEnabled(false);
                                holder.etUnitAfter.setHint("หน่วยล่าสุด");
                                holder.btSave.setVisibility(View.GONE);
                                holder.btEdit.setVisibility(View.VISIBLE);
                            }
                        });
                    }
                }

                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) {

                }
            });

            /*if (holder.btEdit.getVisibility() == View.VISIBLE) {
                holder.btEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        holder.etUnitAfter.setEnabled(true);
                        holder.btSave.setVisibility(View.VISIBLE);
                        holder.btEdit.setVisibility(View.GONE);
                    }
                });
            }else {
                holder.btSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        holder.etUnitAfter.setEnabled(false);
                        holder.btSave.setVisibility(View.GONE);
                        holder.btEdit.setVisibility(View.VISIBLE);
                    }
                });
            }*/

            int count = listRoom.get(position).getListMember().size();
            if (count < 1) {
                holder.txRoom.setTextColor(ContextCompat.getColor(mContext, R.color.red));
            }
            holder.txRoom.setText(listRoom.get(position).getNumroom());

        } catch (NullPointerException e) {
        }
    }

    @Override
    public int getItemCount() {
        return listRoom.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txRoom;
        EditText txUnitBefore, etUnitAfter;
        ImageView btSave, btEdit;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            txRoom = itemView.findViewById(R.id.txRoom);
            txUnitBefore = itemView.findViewById(R.id.txUnitBefore);
            etUnitAfter = itemView.findViewById(R.id.etUnitAfter);
            btSave = itemView.findViewById(R.id.btSave);
            btEdit = itemView.findViewById(R.id.btEdit);
        }
    }

    public void save(int position) {

    }
}