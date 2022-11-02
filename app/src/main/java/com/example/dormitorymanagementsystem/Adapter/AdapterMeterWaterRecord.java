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
import android.widget.ImageView;
import android.widget.TextView;

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

public class AdapterMeterWaterRecord extends RecyclerView.Adapter<AdapterMeterWaterRecord.MyViewHolder> {

    private DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Bills");
    private DatabaseReference refView = FirebaseDatabase.getInstance().getReference("View");
    private DatabaseReference refTypeRoom = FirebaseDatabase.getInstance().getReference("TypeRooms");
    private DatabaseReference refRoom = FirebaseDatabase.getInstance().getReference("Room");

    Context mContext;
    public static List<RoomModel> listRoom;
    private String year;
    private String month;


    //Button btSave;

    View rootView;

    public AdapterMeterWaterRecord(Context mContext, List<RoomModel> listRoom, String year, String month) {
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
    public AdapterMeterWaterRecord.MyViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_metor, parent, false);

        mContext = parent.getContext();
        rootView = ((Activity) mContext).getWindow().getDecorView().findViewById(android.R.id.content);
        //btSave = (Button) rootView.findViewById(R.id.btSave);

        return new AdapterMeterWaterRecord.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull AdapterMeterWaterRecord.MyViewHolder holder, int position) {
        try {

            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                    int mo = Integer.parseInt(month) - 1;
                    int mo2 = Integer.parseInt(month);
                    String unitBefore = snapshot.child(year).child(mo + "").child(listRoom.get(position).getNumroom()).child("waterafter").getValue(String.class);
                    String unitAfter = snapshot.child(year).child(mo2 + "").child(listRoom.get(position).getNumroom()).child("waterafter").getValue(String.class);
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
                                                refRoom.addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                                        String typeRoom = snapshot.child(listRoom.get(position).getNumroom()).child("Type").getValue(String.class);
                                                        refTypeRoom.addListenerForSingleValueEvent(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                                                String priceRoom = snapshot.child(typeRoom).getValue(String.class);
                                                                ref.child(year).child(mo2 + "").child(listRoom.get(position).getNumroom()).child("roomprice").setValue(priceRoom);
                                                            }

                                                            @Override
                                                            public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                                            }
                                                        });
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                                    }
                                                });
                                                ref.child(year).child(mo + "").child(listRoom.get(position).getNumroom()).child("waterafter").setValue(elecAfter);
                                                ref.child(year).child(mo2 + "").child(listRoom.get(position).getNumroom()).child("waterbefore").setValue(elecAfter);

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
                    } else {
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
                                                    double unitEl = Double.parseDouble(snapshot.child("electricitybill").child("Water").getValue(String.class));
                                                    //if (unitBefore != null) {
                                                    Log.e("check", "1");
                                                    if (Integer.parseInt(elecAfter) > Integer.parseInt(unitBefore)) {
                                                        Log.e("check", "2");
                                                        double sumEl = (Integer.parseInt(elecAfter) - Integer.parseInt(unitBefore)) * unitEl;
                                                        DecimalFormat formatter = new DecimalFormat("###,###,###.00");
                                                        String formatted = formatter.format(sumEl);
                                                        ref.child(year).child(mo2 + "").child(listRoom.get(position).getNumroom()).child("water").setValue(formatted);
                                                        ref.child(year).child(mo2 + "").child(listRoom.get(position).getNumroom()).child("waterafter").setValue(elecAfter);
                                                        ref.child(year).child(mo2 + "").child(listRoom.get(position).getNumroom()).child("waterbefore").setValue(unitBefore);
                                                        refRoom.addListenerForSingleValueEvent(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                                                String typeRoom = snapshot.child(listRoom.get(position).getNumroom()).child("Type").getValue(String.class);
                                                                refTypeRoom.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                    @Override
                                                                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                                                        String priceRoom = snapshot.child(typeRoom).getValue(String.class);
                                                                        ref.child(year).child(mo2 + "").child(listRoom.get(position).getNumroom()).child("roomprice").setValue(priceRoom);
                                                                    }

                                                                    @Override
                                                                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                                                    }
                                                                });
                                                            }

                                                            @Override
                                                            public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                                            }
                                                        });
                                                    } else if (Integer.parseInt(elecAfter) < Integer.parseInt(unitBefore)) {
                                                        Log.e("check", "3");
                                                        double sumEl = ((9999 - Integer.parseInt(unitBefore)) + Integer.parseInt(elecAfter)) * unitEl;
                                                        DecimalFormat formatter = new DecimalFormat("###,###,###.00");
                                                        String formatted = formatter.format(sumEl);
                                                        ref.child(year).child(mo2 + "").child(listRoom.get(position).getNumroom()).child("water").setValue(formatted);
                                                        ref.child(year).child(mo2 + "").child(listRoom.get(position).getNumroom()).child("waterafter").setValue(elecAfter);
                                                        ref.child(year).child(mo2 + "").child(listRoom.get(position).getNumroom()).child("waterbefore").setValue(unitBefore);
                                                        refRoom.addListenerForSingleValueEvent(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                                                String typeRoom = snapshot.child(listRoom.get(position).getNumroom()).child("Type").getValue(String.class);
                                                                refTypeRoom.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                    @Override
                                                                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                                                        String priceRoom = snapshot.child(typeRoom).getValue(String.class);
                                                                        ref.child(year).child(mo2 + "").child(listRoom.get(position).getNumroom()).child("roomprice").setValue(priceRoom);
                                                                    }

                                                                    @Override
                                                                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                                                    }
                                                                });
                                                            }

                                                            @Override
                                                            public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                                            }
                                                        });
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
                                holder.btSave.setVisibility(View.VISIBLE);
                                holder.btEdit.setVisibility(View.GONE);
                            }
                        });

                        holder.btSave.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                holder.etUnitAfter.setEnabled(false);
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

        TextView txRoom, txUnitBefore, etUnitAfter;
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
