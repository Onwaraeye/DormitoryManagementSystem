package com.example.dormitorymanagementsystem.Fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dormitorymanagementsystem.Adapter.AdapterBookingDetails;
import com.example.dormitorymanagementsystem.Model.CentralModel;
import com.example.dormitorymanagementsystem.R;

import java.util.ArrayList;
import java.util.List;

public class ViewTutoringFragment extends Fragment {
    private View view;
    private Context mContext;
    RecyclerView recyclerView;
    AdapterBookingDetails adapterBookingDetails;
    private List<CentralModel> list = new ArrayList<>();
    public ViewTutoringFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_view_tutoring, container, false);
        mContext = getActivity().getApplication();
        recyclerView = view.findViewById(R.id.list_item);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));


        //list = (ArrayList) getArguments().getParcelableArrayList("viewTutoring");

        /*adapterBookingDetails = new AdapterBookingDetails(mContext, list);
        recyclerView.setAdapter(adapterBookingDetails);*/
        return view;
    }
}