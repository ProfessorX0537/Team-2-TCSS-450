package com.example.chatapp.ui.main.weather;

import android.Manifest;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

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

        ActivityResultLauncher<String[]> locationPermissionRequest =
                registerForActivityResult(new ActivityResultContracts
                                .RequestMultiplePermissions(), result -> {
                            Boolean fineLocationGranted = result.getOrDefault(
                                    Manifest.permission.ACCESS_FINE_LOCATION, false);
                            Boolean coarseLocationGranted = result.getOrDefault(
                                    Manifest.permission.ACCESS_COARSE_LOCATION,false);
                            if (fineLocationGranted != null && fineLocationGranted) {
                                // Precise location access granted.
                            } else if (coarseLocationGranted != null && coarseLocationGranted) {
                                // Only approximate location access granted.
                            } else {
                                // No location access granted.
                            }
                        }
                );

// ...

// Before you perform the actual permission request, check whether your app
// already has the permissions, and whether your app needs to show a permission
// rationale dialog. For more details, see Request permissions.
        locationPermissionRequest.launch(new String[] {
                //Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        });

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

        mBinding.buttonLocation.setOnClickListener(button -> {
            updateLocation();
        });

        mBinding.buttonTextbox.setOnClickListener(button -> {
            mBinding.buttonTextbox.setVisibility(View.GONE);
            mBinding.weatherFragmentLocationsStatic.setVisibility(View.VISIBLE);
            mBinding.idButtonExitLocationView.setVisibility(View.VISIBLE);
            mBinding.idButtonGetPhoneLocation.setVisibility(View.VISIBLE);
        });

        mBinding.idButtonExitLocationView.setOnClickListener(button -> {
            mBinding.buttonTextbox.setVisibility(View.VISIBLE);
            mBinding.weatherFragmentLocationsStatic.setVisibility(View.GONE);
            mBinding.idButtonExitLocationView.setVisibility(View.GONE);
            mBinding.idButtonGetPhoneLocation.setVisibility(View.GONE);
        });

        mBinding.idButtonGetPhoneLocation.setOnClickListener(button -> {

        });


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
                        break;
                }


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

    public void ToggleOffLocationsView() {
        //mBinding.weatherFragmentLocationsStatic.setVisibility(View.GONE);
        //mBinding.buttonLocation.setVisibility(View.VISIBLE);
    }

}