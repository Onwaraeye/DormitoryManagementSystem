package com.example.dormitorymanagementsystem.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.dormitorymanagementsystem.Adapter.AdapterPost;
import com.example.dormitorymanagementsystem.Login;
import com.example.dormitorymanagementsystem.Manager.Post;
import com.example.dormitorymanagementsystem.Model.PostModel;
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

public class AnnounceFragment extends Fragment {

    private View view;
    private Context mContext;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference("Posts");
    private List<PostModel> list = new ArrayList<>();
    private PostModel postModel;
    private RecyclerView recyclerView;
    private AdapterPost adapter;

    public AnnounceFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_announce, container, false);

        mContext = getActivity().getApplication();

        recyclerView = view.findViewById(R.id.list_item);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));

        postModel = new PostModel();

        String typeUser = Login.getGbTypeUser();


        ImageView addPost = view.findViewById(R.id.addPost);
        addPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Post.class);
                intent.putExtra("status","0");
                getActivity().startActivity(intent);
            }
        });

        if (typeUser.equals("User") || typeUser.equals("Repairman")){
            addPost.setVisibility(View.INVISIBLE);
        }

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot ds : snapshot.getChildren()){
                    postModel = ds.getValue(PostModel.class);
                    list.add(0,postModel);
                }
                adapter = new AdapterPost(mContext,list);
                recyclerView.setAdapter(adapter);
            }
            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
            }
        });
        return view;
    }
}