package com.example.chatapp.ui.main.weather;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.chatapp.R;

public class WeatherLocationsCardItem extends Fragment {

    private String mCity;
    private String mState;
    private String mZipCode;



    public WeatherLocationsCardItem(String city, String state, String zipcode) {
        this.mCity = city;
        this.mState = state;
        this.mZipCode = zipcode;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_weather_locations_card_item, container, false);
    }

    public String getCity() {
        return mCity;
    }

    public String getState() {
        return mState;
    }

    public String getZipCode() {
        return mZipCode;
    }
}