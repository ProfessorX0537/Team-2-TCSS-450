package com.example.chatapp.ui.main.weather;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.chatapp.R;
import com.example.chatapp.databinding.FragmentWeatherBinding;
import com.example.chatapp.model.WeatherInfoViewModel;
import com.google.android.material.tabs.TabLayout;

public class WeatherFragment extends Fragment {

    private FragmentWeatherBinding mBinding;
    private WeatherInfoViewModel mModel;

    public WeatherFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mModel = new ViewModelProvider(getActivity()).get(WeatherInfoViewModel.class);

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

        TabLayout tabLayout = mBinding.tabLayout;
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //Log.d("TEST", "onTabSelected: ");
                int curTab = tabLayout.getSelectedTabPosition();
                switch (curTab) {
                    case 0:
                        Log.d("TEST", "Today Tab Selected");
                        WeatherTodayFragment fragment = new WeatherTodayFragment();
                        requireActivity()
                            .getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.weather_fragment_container, fragment)
                            .commit();


                        break;
                    case 1:
                        Log.d("TEST", "24 Hour Tab Selected");
                        Weather24HourFragment fragment2 = new Weather24HourFragment();
                        requireActivity()
                                .getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.weather_fragment_container, fragment2)
                                .commit();

                        break;
                    case 2:
                        Log.d("TEST", "10 Day Tab Selected");
                        Weather10DayFragment fragment3 = new Weather10DayFragment();
                        requireActivity()
                                .getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.weather_fragment_container, fragment3)
                                .commit();
                        updateLocation();

                }

                mBinding.buttonLocation.setOnClickListener(button -> {
                    updateLocation();
                });
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBinding = null;
    }

    public void updateLocation() {
        mModel.setmLocation(mBinding.textLocation.getText().toString());
    }

}