package com.example.dormitorymanagementsystem.ChatNew;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.example.dormitorymanagementsystem.Login;
import com.example.dormitorymanagementsystem.R;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


public class UsersFragment extends Fragment {

    RecyclerView recyclerView;
    AdapterUsers adapterUsers;
    List<ModelUser> userList;
    String userId = Login.getGbIdUser();
    Context context;

    public UsersFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_users, container, false);
        context = getActivity().getApplication();
        recyclerView = view.findViewById(R.id.user_recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        SearchView searchView = view.findViewById(R.id.action_search);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (!TextUtils.isEmpty(query.trim())) {
                    searchUsers(query);
                } else {
                    getAllUsers();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                if (!TextUtils.isEmpty(newText.trim())) {
                    searchUsers(newText);
                } else {
                    getAllUsers();
                }

                return false;
            }
        });

        userList = new ArrayList<>();

        getAllUsers();

        return view;
    }

    private void getAllUsers() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                userList.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    ModelUser modelUser = ds.getValue(ModelUser.class);
                    if (modelUser.getRole() != null && !modelUser.getRole().equals("Admin") && !modelUser.getRole().equals("Superadmin")) {
                        userList.add(modelUser);
                    }
                    adapterUsers = new AdapterUsers(context, userList, "UsersFragment");
                    recyclerView.setAdapter(adapterUsers);
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    private void searchUsers(String query) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                userList.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    ModelUser modelUser = ds.getValue(ModelUser.class);
                    if (modelUser.getId() != null && !modelUser.getId().equals(userId)) {
                        if (modelUser.getNumroom() != null) {
                            if (modelUser.getNumroom().toLowerCase().contains(query.toLowerCase()) ||
                                    modelUser.getFirstname().toLowerCase().contains(query.toLowerCase()) ||
                                    modelUser.getLastname().toLowerCase().contains(query.toLowerCase()) || modelUser.getRole().toLowerCase().contains(query.toLowerCase())) {
                                userList.add(modelUser);
                            }
                        }
                    }
                    adapterUsers = new AdapterUsers(context, userList, "UsersFragment");
                    adapterUsers.notifyDataSetChanged();
                    recyclerView.setAdapter(adapterUsers);
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }
}