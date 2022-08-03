package com.example.dormitorymanagementsystem.Repairman;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dormitorymanagementsystem.Adapter.AdapteViewPepiar;
import com.example.dormitorymanagementsystem.Login;
import com.example.dormitorymanagementsystem.Model.RepairModel;
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

public class RepairSuccessful extends Fragment {

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference("Repair");
    private List<RepairModel> list = new ArrayList<>();
    private RepairModel repairModel;
    private RecyclerView recyclerView;
    private AdapteViewPepiar adapter;

    private View view;
    private Context mContext;

    private String numroom = Login.getGbNumroom();
    private String userID = Login.getGbIdUser();
    private String typeUser = Login.getGbTypeUser();


    public RepairSuccessful() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_repair_successful, container, false);

        mContext = getActivity().getApplication();

        recyclerView = view.findViewById(R.id.list_item);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));

        repairModel = new RepairModel();

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                Query query = myRef.orderByChild("status").equalTo("3");
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        list.clear();
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            String status = ds.child("status").getValue(String.class);
                            String idRepairman = ds.child("repairman").getValue(String.class);
                            if (typeUser.equals("Admin")) {
                                if (status.equals("3")) {
                                    repairModel = ds.getValue(RepairModel.class);
                                    list.add(0, repairModel);
                                }
                            }else {
                                if (status.equals("3") && idRepairman.equals(userID)) {
                                    repairModel = ds.getValue(RepairModel.class);
                                    list.add(0, repairModel);
                                }
                            }
                        }
                        adapter = new AdapteViewPepiar(mContext, list);
                        recyclerView.setAdapter(adapter);
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

        return view;
    }
}