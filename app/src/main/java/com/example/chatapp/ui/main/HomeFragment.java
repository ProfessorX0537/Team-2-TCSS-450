package com.example.chatapp.ui.main;

import android.app.UiModeManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.chatapp.R;
import com.example.chatapp.databinding.FragmentHomeBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeFragment extends Fragment {
    public HomeFragment() {
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
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    //pop off stack
    //use bottom  nav to move
    //((AppCompatActivity)getActivity()).findViewById(R.id.nav_view)

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FragmentHomeBinding binding = FragmentHomeBinding.bind(requireView());

//        BottomNavigationView temp = ((AppCompatActivity)getActivity()).findViewById(R.id.nav_view);
//        temp.setSelectedItemId(R.id.navigation_weather);

//        binding.cardView.setOnClickListener(button ->
//                temp.setSelectedItemId(R.id.navigation_weather));

//        binding.cardView.setOnClickListener(button ->
//                Navigation.findNavController(requireView()).navigate(
//                        HomeFragmentDirections
//                                .actionNavigationHomeToNavigationWeather()));

        binding.homeWeatherCard.setOnClickListener(button -> {
            BottomNavigationView temp = ((AppCompatActivity) getActivity()).findViewById(R.id.nav_view);
            temp.setSelectedItemId(R.id.navigation_weather);
        });

        binding.homeMessageCard.setOnClickListener(button -> {
            BottomNavigationView temp = ((AppCompatActivity) getActivity()).findViewById(R.id.nav_view);
            temp.setSelectedItemId(R.id.navigation_chat);
        });

        binding.homeRequestCard.setOnClickListener(button -> {
            BottomNavigationView temp = ((AppCompatActivity) getActivity()).findViewById(R.id.nav_view);
            temp.setSelectedItemId(R.id.navigation_connections);
        });
    }


//    @Override
//    public void onResume() {
//        super.onResume();
//        ((AppCompatActivity)getActivity()).getSupportActionBar().;
//    }
//    @Override
//    public void onStop() {
//        super.onStop();
//        ((AppCompatActivity)getActivity()).getSupportActionBar().show();
//    }
}