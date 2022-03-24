package com.example.dormitorymanagementsystem.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.dormitorymanagementsystem.CentralReservation;
import com.example.dormitorymanagementsystem.Parcel;
import com.example.dormitorymanagementsystem.R;

public class CentralFragment extends Fragment {

    private TextView menu_fitness,menu_tutoringRoom;
    private View view;

    public CentralFragment() {

    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_central, container, false);

        menu_fitness = view.findViewById(R.id.menu_fitness);
        menu_fitness.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CentralReservation.class);
                intent.putExtra("central","fitness");
                getActivity().startActivity(intent);
            }
        });

        menu_tutoringRoom = view.findViewById(R.id.menu_tutoringRoom);
        menu_tutoringRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CentralReservation.class);
                intent.putExtra("central","tutoringRoom");
                getActivity().startActivity(intent);
            }
        });
        return view;
    }
}