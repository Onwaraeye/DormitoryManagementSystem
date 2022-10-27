package com.example.dormitorymanagementsystem.Fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dormitorymanagementsystem.Adapter.AdapterBookingDetails;
import com.example.dormitorymanagementsystem.Adapter.AdapterManagerPhone;
import com.example.dormitorymanagementsystem.Adapter.AdapterNewParcel;
import com.example.dormitorymanagementsystem.Login;
import com.example.dormitorymanagementsystem.Manager.ManagerPhone;
import com.example.dormitorymanagementsystem.Model.CentralModel;
import com.example.dormitorymanagementsystem.Model.ManagerModel;
import com.example.dormitorymanagementsystem.Model.NewParcelModel;
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

public class NewParcelFragment extends Fragment {

    private View view;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("Parcel");
    List<NewParcelModel> list = new ArrayList<>();

    private Context mContext;
    RecyclerView recyclerView;
    AdapterNewParcel adapter;

    private NewParcelModel newParcelModel;
    private String numroom = Login.getGbNumroom();
    private String userID = Login.getGbIdUser();
    private String typeUser = Login.getGbTypeUser();

    public NewParcelFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_new_parcel, container, false);

        mContext = getActivity().getApplication();

        recyclerView = view.findViewById(R.id.list_item);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));

        newParcelModel = new NewParcelModel();
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if (typeUser.equals("User")) {
                    Query query = myRef.orderByChild("numroom").equalTo(numroom);
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                            list.clear();
                            for (DataSnapshot ds : snapshot.getChildren()) {
                                if (ds.child("status").getValue(String.class) != null && ds.child("status").getValue(String.class).equals("0")) {
                                    newParcelModel = ds.getValue(NewParcelModel.class);
                                    list.add(0,newParcelModel);
                                }
                            }
                            adapter = new AdapterNewParcel(mContext, list);
                            recyclerView.setAdapter(adapter);
                        }

                        @Override
                        public void onCancelled(@NonNull @NotNull DatabaseError error) {

                        }
                    });
                } else {
                    Query query = myRef.orderByChild("status").equalTo("0");
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                            list.clear();
                            for (DataSnapshot ds : snapshot.getChildren()) {
                                newParcelModel = ds.getValue(NewParcelModel.class);
                                list.add(0,newParcelModel);
                            }
                            adapter = new AdapterNewParcel(mContext, list);
                            recyclerView.setAdapter(adapter);
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

        return view;
    }
}