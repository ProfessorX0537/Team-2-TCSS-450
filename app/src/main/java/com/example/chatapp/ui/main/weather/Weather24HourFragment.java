package com.example.chatapp.ui.main.weather;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.chatapp.R;
import com.example.chatapp.databinding.FragmentWeather24HourBinding;
import com.example.chatapp.model.WeatherInfoViewModel;

import java.util.Random;


public class Weather24HourFragment extends Fragment {

    /**
     * Field variable to access info about weather
     */
    private WeatherInfoViewModel mModel;

    /**
     * Instance field variable for binding layout and this fragment.
     */
    private FragmentWeather24HourBinding mBinding;

    public Weather24HourFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //TODO may be creating two instances of weather info viewmodel
        mModel = new ViewModelProvider(getActivity()).get(WeatherInfoViewModel.class);
        //mModel.pullWeatherUpdates(); //replace with my fake data
        //mModel.setupWeather24HourCards();//TODO fix method in viewmodel to load actual data

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = FragmentWeather24HourBinding.inflate(inflater);
        return mBinding.getRoot();
    }

    /**
     * Fills the bound recycler view with data
     * @param view The View returned by {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mBinding.rootRecycler.setAdapter(new Weather24HourRecycleViewAdapter(mModel.mToday));
    }
}