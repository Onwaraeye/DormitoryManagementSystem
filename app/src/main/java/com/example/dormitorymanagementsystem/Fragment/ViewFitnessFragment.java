package com.example.dormitorymanagementsystem.Fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dormitorymanagementsystem.Adapter.AdapterBookingDetails;
import com.example.dormitorymanagementsystem.Adapter.AdapterParcel;
import com.example.dormitorymanagementsystem.Model.CentralManagerModel;
import com.example.dormitorymanagementsystem.Model.CentralModel;
import com.example.dormitorymanagementsystem.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ViewFitnessFragment extends Fragment {
    private View view;
    private Context mContext;
    private RecyclerView recyclerView;
    private AdapterBookingDetails adapterBookingDetails;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRefCentral = database.getReference("Central");
    private List<CentralModel> list = new ArrayList<>();

    public ViewFitnessFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_view_fitness, container, false);
        mContext = getActivity().getApplication();
        recyclerView = view.findViewById(R.id.list_item);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        Bundle bundle = getArguments();
        Handler handler = new Handler();
        int delay = 1000;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                list = bundle.getParcelableArrayList("viewFitness");
                if(!list.isEmpty()) {
                    adapterBookingDetails = new AdapterBookingDetails(mContext, list);
                    recyclerView.setAdapter(adapterBookingDetails);
                }
                else
                    handler.postDelayed(this, delay);
            }
        },delay);







        return view;

    }
}