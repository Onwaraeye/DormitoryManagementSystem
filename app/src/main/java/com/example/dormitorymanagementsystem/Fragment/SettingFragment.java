package com.example.dormitorymanagementsystem.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.dormitorymanagementsystem.ChangePassword;
import com.example.dormitorymanagementsystem.Login;
import com.example.dormitorymanagementsystem.MonthlyBill;
import com.example.dormitorymanagementsystem.PersonalInformation;
import com.example.dormitorymanagementsystem.R;

public class SettingFragment extends Fragment {

    private LinearLayout menu_info,menu_logout,menu_change_pass;
    private View view;


    public SettingFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_setting, container, false);

        menu_info = view.findViewById(R.id.menu_info);
        menu_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PersonalInformation.class);
                getActivity().startActivity(intent);
            }
        });

        menu_change_pass = view.findViewById(R.id.menu_change_pass);
        menu_change_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ChangePassword.class);
                getActivity().startActivity(intent);
            }
        });

        menu_logout = view.findViewById(R.id.menu_logout);
        menu_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplicationContext(), Login.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        return view;
    }
}