package com.example.chatapp.ui.main.weather;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.chatapp.R;
import com.example.chatapp.databinding.FragmentWeatherBinding;

public class WeatherFragment extends Fragment {

    private FragmentWeatherBinding mBinding;

    public WeatherFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //android:name="com.example.chatapp.ui.main.weather.WeatherTodayFragment"
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mBinding = FragmentWeatherBinding.inflate(inflater, container, false);

        //mBinding.getRoot().setTag(R.id.fragmentContainerView, fragment);
//        mBinding.fragmentContainerView.addView(mBinding.getRoot().);

        WeatherTodayFragment fragment = new WeatherTodayFragment();
        requireActivity()
                .getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.weather_fragment_container, fragment)
                .commit();

        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated (@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //Use a Lamda expression to add the OnClickListener

        mBinding.buttonViewToday.setOnClickListener(button -> {
            Log.d("TEST", "Today View");
            WeatherTodayFragment fragment = new WeatherTodayFragment();
            requireActivity()
                    .getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.weather_fragment_container, fragment)
                    .commit();

        });

        mBinding.buttonView24hour.setOnClickListener(button -> {
            Log.d("TEST", "24 Hour View");
            Weather24HourFragment fragment = new Weather24HourFragment();
            requireActivity()
                    .getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.weather_fragment_container, fragment)
                    .commit();

        });
        mBinding.buttonView10day.setOnClickListener(button -> {
            Log.d("TEST", "10 Day View");
            Weather10DayFragment fragment = new Weather10DayFragment();
            requireActivity()
                    .getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.weather_fragment_container, fragment)
                    .commit();

        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBinding = null;
    }



}