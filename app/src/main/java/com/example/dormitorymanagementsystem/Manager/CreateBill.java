package com.example.dormitorymanagementsystem.Manager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.icu.text.DecimalFormat;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dormitorymanagementsystem.Login;
import com.example.dormitorymanagementsystem.Model.BillModel;
import com.example.dormitorymanagementsystem.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

public class CreateBill extends AppCompatActivity {

    private DatabaseReference refTypeRooms = FirebaseDatabase.getInstance().getReference("TypeRooms");
    private DatabaseReference refRoom = FirebaseDatabase.getInstance().getReference("Room");
    private DatabaseReference refBill = FirebaseDatabase.getInstance().getReference("Bills");

    private String monthThai = "";
    private String yearThai = "";
    private String year = "";
    private String room = "";
    private double sum = 0;
    private double fee = 0;
    private double internet = 0;
    private double discount = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_bill);

        TextView txRoom = findViewById(R.id.txRoom);
        TextView txDate = findViewById(R.id.txDate);
        TextView txStatus = findViewById(R.id.txStatus);
        TextView txRoomPrice = findViewById(R.id.txRoomPrice);
        TextView txElectricity = findViewById(R.id.txElectricity);
        TextView txWater = findViewById(R.id.txWater);
        EditText etDiscount = findViewById(R.id.etDiscount);
        EditText etFee = findViewById(R.id.etFee);
        EditText etInternet = findViewById(R.id.etInternet);
        TextView txSum = findViewById(R.id.txSum);
        TextView txElecUnit = findViewById(R.id.txElecUnit);
        TextView txWaterUnit = findViewById(R.id.txWaterUnit);
        Button btConfirm = findViewById(R.id.btConfirm);
        ImageView btEditDiscount = findViewById(R.id.btEditDiscount);
        ImageView btSaveDiscount = findViewById(R.id.btSaveDiscount);
        ImageView btEditInternet = findViewById(R.id.btEditInternet);
        ImageView btSaveInternet = findViewById(R.id.btSaveInternet);
        ImageView btEditFee = findViewById(R.id.btEditFee);
        ImageView btSaveFee = findViewById(R.id.btSaveFee);

        BillModel billModel = (BillModel) getIntent().getSerializableExtra("bill");
        String date = getIntent().getStringExtra("date");
        String status = billModel.getStatus();
        String roombills = getIntent().getStringExtra("room");

        String[] dateBill = date.split(" ");
        yearThai = dateBill[1];
        int yearCv = Integer.parseInt(yearThai) - 543;
        year = String.valueOf(yearCv);
        monthThai = dateBill[0];

        txRoom.setText(roombills);
        txDate.setText(date);
        if (status == null) {
            txStatus.setText("ยังไม่ได้ส่งใบแจ้งหนี้");
        }

        String unitElecAf = billModel.getElecafter();
        String unitElecBe = billModel.getElecbefore();
        String unitWaterAf = billModel.getWaterafter();
        String unitWaterBe = billModel.getWaterbefore();


        refTypeRooms.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshotType) {
                refRoom.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshotRoom) {
                        String typeRoom = snapshotRoom.child(roombills).child("Type").getValue(String.class);
                        String datapriceRoom = snapshotType.child(typeRoom).getValue(String.class);
                        String temppriceRoom = datapriceRoom.replace(",", "");
                        double priceRoom = Double.parseDouble(temppriceRoom);
                        refBill = refBill.child(yearCv + "").child(getMonth(monthThai)).child(roombills);
                        refBill.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                txRoomPrice.setText(datapriceRoom + " บาท");
                                if (snapshot.hasChild("discount")) {
                                    etDiscount.setText(snapshot.child("discount").getValue(String.class));
                                } else if (snapshot.child("discount").getValue() == null || Double.parseDouble(snapshot.child("discount").getValue(String.class)) == 0) {
                                    etDiscount.setText("0");
                                }
                                if (snapshot.hasChild("fee")) {
                                    etFee.setText(snapshot.child("fee").getValue(String.class));
                                } else if (snapshot.child("fee").getValue() == null || Double.parseDouble(snapshot.child("fee").getValue(String.class)) == 0) {
                                    etFee.setText("0");
                                }
                                if (snapshot.hasChild("internet")) {
                                    etInternet.setText(snapshot.child("internet").getValue(String.class));
                                } else if (snapshot.child("internet").getValue() == null || Double.parseDouble(snapshot.child("internet").getValue(String.class)) == 0) {
                                    etInternet.setText("0");
                                }

                                try {
                                    if (unitWaterAf == null || unitWaterBe == null) {
                                        txWaterUnit.setText("ค่าน้ำ\nยังไม่ได้จดหน่วยน้ำ");
                                        txWater.setText("0 บาท");
                                        int unitElecAfter = Integer.parseInt(billModel.getElecafter());
                                        int unitElecBefore = Integer.parseInt(billModel.getElecbefore());
                                        int sumEl = 0;
                                        if (unitElecAfter > unitElecBefore) {
                                            sumEl = (unitElecAfter - unitElecBefore);
                                        } else if (unitElecAfter < unitElecBefore) {
                                            sumEl = ((9999 - unitElecBefore) + unitElecAfter);
                                        }
                                        txElecUnit.setText("ค่าไฟ\n(" + unitElecBefore + " - " + unitElecAfter + " = " + sumEl + " หน่วย)");
                                        String dataelectricity = snapshot.child("electricity").getValue(String.class);
                                        String tempelectricity = dataelectricity.replace(",", "");
                                        txElectricity.setText(dataelectricity + " บาท");

                                        btConfirm.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Toast.makeText(CreateBill.this, "กรุณาจดหน่วยน้ำก่อน", Toast.LENGTH_SHORT).show();
                                            }
                                        });

                                    }else if (unitElecAf == null || unitElecBe == null) {
                                        txElecUnit.setText("ค่าไฟ\nยังไม่ได้จดหน่วยไฟ");
                                        txElectricity.setText("0 บาท");
                                        int unitWaterAfter = Integer.parseInt(billModel.getWaterafter());
                                        int unitWaterBefore = Integer.parseInt(billModel.getWaterbefore());
                                        int sumWt = 0;
                                        if (unitWaterAfter > unitWaterBefore) {
                                            sumWt = (unitWaterAfter - unitWaterBefore);
                                        } else if (unitWaterAfter < unitWaterBefore) {
                                            sumWt = ((9999 - unitWaterBefore) + unitWaterAfter);
                                        }
                                        txWaterUnit.setText("ค่าน้ำ\n(" + unitWaterBefore + " - " + unitWaterAfter + " = " + sumWt + " หน่วย)");
                                        String datawater = snapshot.child("water").getValue(String.class);
                                        String tempwater = datawater.replace(",", "");
                                        txWater.setText(datawater + " บาท");

                                        btConfirm.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Toast.makeText(CreateBill.this, "กรุณาจดหน่วยไฟก่อน", Toast.LENGTH_SHORT).show();
                                            }
                                        });

                                    } else {
                                        int unitElecAfter = Integer.parseInt(billModel.getElecafter());
                                        int unitElecBefore = Integer.parseInt(billModel.getElecbefore());
                                        int sumEl = 0;
                                        if (unitElecAfter > unitElecBefore) {
                                            sumEl = (unitElecAfter - unitElecBefore);
                                        } else if (unitElecAfter < unitElecBefore) {
                                            sumEl = ((9999 - unitElecBefore) + unitElecAfter);
                                        }
                                        txElecUnit.setText("ค่าไฟ\n(" + unitElecBefore + " - " + unitElecAfter + " = " + sumEl + " หน่วย)");

                                        int unitWaterAfter = Integer.parseInt(billModel.getWaterafter());
                                        int unitWaterBefore = Integer.parseInt(billModel.getWaterbefore());
                                        int sumWt = 0;
                                        if (unitWaterAfter > unitWaterBefore) {
                                            sumWt = (unitWaterAfter - unitWaterBefore);
                                        } else if (unitWaterAfter < unitWaterBefore) {
                                            sumWt = ((9999 - unitWaterBefore) + unitWaterAfter);
                                        }
                                        txWaterUnit.setText("ค่าน้ำ\n(" + unitWaterBefore + " - " + unitWaterAfter + " = " + sumWt + " หน่วย)");

                                        String dataelectricity = snapshot.child("electricity").getValue(String.class);
                                        String tempelectricity = dataelectricity.replace(",", "");
                                        double electricity = Double.parseDouble(tempelectricity);

                                        String datawater = snapshot.child("water").getValue(String.class);
                                        String tempwater = datawater.replace(",", "");
                                        double water = Double.parseDouble(tempwater);

                                        txElectricity.setText(dataelectricity + " บาท");
                                        txWater.setText(datawater + " บาท");

                                        sum = priceRoom + electricity + water;
                                        txSum.setText(DoubleToString(sum) + " บาท");

                                        if (btEditFee.getVisibility() == View.VISIBLE) {
                                            btEditFee.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    etFee.setEnabled(true);
                                                    btEditFee.setVisibility(View.GONE);
                                                    btSaveFee.setVisibility(View.VISIBLE);
                                                }
                                            });
                                        }
                                        btSaveFee.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                etFee.setEnabled(false);
                                                btEditFee.setVisibility(View.VISIBLE);
                                                btSaveFee.setVisibility(View.GONE);
                                                fee = Double.parseDouble(etFee.getText().toString());
                                                sum += fee;
                                                txSum.setText(DoubleToString(sum) + " บาท");
                                            }
                                        });

                                        if (btEditInternet.getVisibility() == View.VISIBLE) {
                                            btEditInternet.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    etInternet.setEnabled(true);
                                                    btEditInternet.setVisibility(View.GONE);
                                                    btSaveInternet.setVisibility(View.VISIBLE);
                                                }
                                            });
                                        }

                                        btSaveInternet.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                etInternet.setEnabled(false);
                                                btEditInternet.setVisibility(View.VISIBLE);
                                                btSaveInternet.setVisibility(View.GONE);
                                                internet = Double.parseDouble(etInternet.getText().toString());
                                                sum += internet;
                                                txSum.setText(DoubleToString(sum) + " บาท");
                                            }
                                        });

                                        if (btEditDiscount.getVisibility() == View.VISIBLE) {
                                            btEditDiscount.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    etDiscount.setEnabled(true);
                                                    btEditDiscount.setVisibility(View.GONE);
                                                    btSaveDiscount.setVisibility(View.VISIBLE);
                                                }
                                            });
                                        }
                                        btSaveDiscount.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                etDiscount.setEnabled(false);
                                                btEditDiscount.setVisibility(View.VISIBLE);
                                                btSaveDiscount.setVisibility(View.GONE);
                                                discount = Double.parseDouble(etDiscount.getText().toString());
                                                sum -= discount;
                                                txSum.setText(DoubleToString(sum) + " บาท");
                                            }
                                        });

                                        btConfirm.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Toast.makeText(CreateBill.this, "สำเร็จ", Toast.LENGTH_SHORT).show();
                                                refBill.child("roomprice").setValue(datapriceRoom);
                                                refBill.child("discount").setValue(DoubleToString(discount));
                                                refBill.child("fee").setValue(DoubleToString(fee));
                                                refBill.child("internet").setValue(DoubleToString(internet));
                                                refBill.child("sum").setValue(DoubleToString(sum));
                                                refBill.child("status").setValue("0");
                                                finish();
                                            }
                                        });
                                    }
                                }catch (Exception e){
                                    Toast.makeText(CreateBill.this, ""+e.toString(), Toast.LENGTH_SHORT).show();
                                }


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

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

        ImageView arrow_back = findViewById(R.id.ic_arrow_back);
        arrow_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public String DoubleToString(double data) {
        DecimalFormat formatter = new DecimalFormat("###,###,###.00");
        String formatted = formatter.format(data);
        return formatted;
    }

    public String getMonth(String monthThai) {
        String month = "";
        switch (monthThai) {
            case "มกราคม":
                month = "1";
                break;
            case "กุมภาพันธ์":
                month = "2";
                break;
            case "มีนาคม":
                month = "3";
                break;
            case "เมษายน":
                month = "4";
                break;
            case "พฤษภาคม":
                month = "5";
                break;
            case "มิถุนายน":
                month = "6";
                break;
            case "กรกฎาคม":
                month = "7";
                break;
            case "สิงหาคม":
                month = "8";
                break;
            case "กันยายน":
                month = "9";
                break;
            case "ตุลาคม":
                month = "10";
                break;
            case "พฤศจิกายน":
                month = "11";
                break;
            case "ธันวาคม":
                month = "12";
                break;
        }
        return month;
    }
}