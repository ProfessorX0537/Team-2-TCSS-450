package com.example.chatapp.ui.main.home;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.chatapp.R;
import com.example.chatapp.databinding.FragmentHomeMessagesBinding;
import com.example.chatapp.databinding.FragmentHomeRequestsBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeRequestsFragment extends Fragment {


    private FragmentHomeRequestsBinding mBinding;

    public HomeRequestsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_home_messages, container, false);
        mBinding = FragmentHomeRequestsBinding.inflate(inflater);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        mBinding.homeRequestCard.setOnClickListener(button -> {
//            BottomNavigationView temp = ((AppCompatActivity) getActivity()).findViewById(R.id.nav_view);
//            temp.setSelectedItemId(R.id.navigation_connections);
//        });
    }
}