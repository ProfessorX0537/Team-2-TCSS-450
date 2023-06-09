package com.example.chatapp.ui.main.weather;

import android.Manifest;
import android.content.Context;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.chatapp.MainActivity;
import com.example.chatapp.R;
import com.example.chatapp.databinding.FragmentWeatherBinding;
import com.example.chatapp.model.WeatherInfoViewModel;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;

/**
 * Weather fragment class used for displaying weather forecasts
 *
 * @author Luca Smith
 */
public class WeatherFragment extends Fragment {

    private FragmentWeatherBinding mBinding;
    private WeatherInfoViewModel mModel;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationRequest locationRequest;

    public WeatherFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mModel = new ViewModelProvider(getActivity()).get(WeatherInfoViewModel.class);
        mModel.connectGet();

        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(2000);



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mBinding = FragmentWeatherBinding.inflate(inflater, container, false);

        WeatherTodayFragment fragment = new WeatherTodayFragment();
        requireActivity()
                .getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.weather_fragment_container, fragment)
                .commit();

        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //BUTTONS
        mBinding.idButtonGetPhoneLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getCurrentLocation();
            }
        });

        mBinding.buttonLocation.setOnClickListener(button -> {
            updateLocation();

            //@Todo: Does not update properly do to the asynchronous calls
            //User input errors
            if (mModel.mInvalidLocationFormatting) {
                mBinding.textLocation.setError("Invalid formatting");
            }
            if (mModel.mInvalidLocationRequest) {
                mBinding.textLocation.setError("Invalid Location");
            }
            else {
                mBinding.textLocation.setError(null);
            }
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

        //Text box selected hint
        mBinding.textLocation.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                mBinding.textLocation.setHint(getString(R.string.hint_weather_location_focused));
            } else {
                mBinding.textLocation.setHint(getString(R.string.hint_weather_location));
            }
        });


        mModel.addWeatherResponseObserver(getViewLifecycleOwner(), list -> {
            if (mModel.mPastLocations.size() != 0) {
                mBinding.textLocationCurrWeather.setText(mModel.mPastLocations.get(0).getCity());
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBinding = null;
    }

    /**
     * Takes user input and sends it to the weather viewmodel for processing
     */
    public void updateLocation() {
        mModel.setLocation(mBinding.textLocation.getText().toString());
    }

    /**
     * Gets current device location
     */
    private void getCurrentLocation() {


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                if (isGPSEnabled()) {

                    LocationServices.getFusedLocationProviderClient(getActivity())
                            .requestLocationUpdates(locationRequest, new LocationCallback() {
                                @Override
                                public void onLocationResult(@NonNull LocationResult locationResult) {
                                    super.onLocationResult(locationResult);

                                    LocationServices.getFusedLocationProviderClient(getActivity())
                                            .removeLocationUpdates(this);

                                    if (locationResult != null && locationResult.getLocations().size() >0){

                                        int index = locationResult.getLocations().size() - 1;
                                        double latitude = locationResult.getLocations().get(index).getLatitude();
                                        double longitude = locationResult.getLocations().get(index).getLongitude();

                                        Log.d("PHONE LOCATION", "GOT LOCATION: " + latitude + ", " + longitude);
                                        mModel.setLocation(latitude + "," + longitude);
                                        //AddressText.setText("Latitude: "+ latitude + "\n" + "Longitude: "+ longitude);
                                    }
                                }
                            }, Looper.getMainLooper());

                } else {
                    turnOnGPS();
                }

            } else {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }
    }

    /**
     * Check GPS location is enabled in device services
     * @return if location service is on
     */
    private boolean isGPSEnabled() {
        LocationManager locationManager = null;
        boolean isEnabled = false;

        if (locationManager == null) {
            locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        }

        isEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        return isEnabled;

    }

    /**
     * Turns on device location service
     */
    private void turnOnGPS() {



        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        Task<LocationSettingsResponse> result = LocationServices.getSettingsClient(getContext())
                .checkLocationSettings(builder.build());

        result.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
            @Override
            public void onComplete(@NonNull Task<LocationSettingsResponse> task) {

                try {
                    LocationSettingsResponse response = task.getResult(ApiException.class);
                    Toast.makeText(getContext(), "GPS is already tured on", Toast.LENGTH_SHORT).show();

                } catch (ApiException e) {

                    switch (e.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:

                            try {
                                ResolvableApiException resolvableApiException = (ResolvableApiException) e;
                                resolvableApiException.startResolutionForResult(getActivity(), 2);
                            } catch (IntentSender.SendIntentException ex) {
                                ex.printStackTrace();
                            }
                            break;

                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            //Device does not have location
                            break;
                    }
                }
            }
        });

    }




}