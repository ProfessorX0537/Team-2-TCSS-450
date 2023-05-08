package com.example.chatapp.ui.main;

import android.app.UiModeManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.chatapp.R;
import com.example.chatapp.databinding.FragmentHomeBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.Map;

public class HomeFragment extends Fragment {

    private WeatherApiViewModel mViewModel;

    private FragmentHomeBinding binding;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(getActivity()).get(WeatherApiViewModel.class);
        mViewModel.connectGet();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                  Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater);
//        return inflater.inflate(R.layout.fragment_home, container, false);
        return binding.getRoot();
    }

    //pop off stack
    //use bottom  nav to move
    //((AppCompatActivity)getActivity()).findViewById(R.id.nav_view)

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //FragmentHomeBinding binding = FragmentHomeBinding.bind(requireView());


//        mViewModel.addResponseObserver(getViewLifecycleOwner(), result ->
//                //result.getJSONObject("daily").getJSONArray("time").getString(0);
//                binding.textDate.setText(result.toString()));
        mViewModel.addResponseObserver(
                getViewLifecycleOwner(),
                this::observeData
        );

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

    private void observeData(JSONObject result) {
        JSONObject daily;
        String date;
        //JSONArray temp = result.getJSONArray("daily");

//            daily = result.getJSONObject("daily");
//            date = daily.getJSONArray("time").getString(0);
//            Log.d("hi", hi.getJSONArray("time").toString());

        JSONArray jsonArray = new JSONArray();
        jsonArray.put(result);

        Log.d("hi3", jsonArray.toString());
        binding.textDate.setText("hi");
        //binding.textDate.setText(result.getJSONArray("timezone").toString());
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