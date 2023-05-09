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

    private WeatherInfoViewModel mModel;
    private FragmentWeather24HourBinding mBinding;

    public Weather24HourFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mModel = new ViewModelProvider(getActivity()).get(WeatherInfoViewModel.class);
        mModel.pullWeatherUpdates();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = FragmentWeather24HourBinding.inflate(inflater);
        return mBinding.getRoot();
    }

    @SuppressLint("ResourceType")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setTables();

    }

    @SuppressLint("ResourceType")
    public void setTables() {
        //Get table layout object
        TableLayout temperatureTable = mBinding.tableTemperature;
        TableLayout precipitationTable = mBinding.tablePrecipitation;
        TableLayout windTable = mBinding.tableWind;

        //Grab rows
        TableRow tr_temperatures = mBinding.trTemperatures;
        TableRow tr_weatherConditions = mBinding.trWeatherConditions;
        TableRow tr_times = mBinding.trTimes;
        TableRow tr_precipitation = mBinding.trPrecipitation;

        //Set Table Row attributes
        //Row for temps
        //tr_temperatures.setId(10);
        //tr_temperatures.setLayoutParams(new TableLayout.LayoutParams(
//                TableLayout.LayoutParams.MATCH_PARENT,
//                TableLayout.LayoutParams.WRAP_CONTENT));

        //Row for conditions image (cloudy, sunny, etc)
        //tr_weatherConditions.setId(11);
        //tr_weatherConditions.setLayoutParams(new TableLayout.LayoutParams(
//                TableLayout.LayoutParams.MATCH_PARENT,
//                TableLayout.LayoutParams.WRAP_CONTENT));

        //Row for times
        //tr_times.setId(12);
        //tr_times.setLayoutParams(new TableLayout.LayoutParams(
//                TableLayout.LayoutParams.MATCH_PARENT,
//                TableLayout.LayoutParams.WRAP_CONTENT));

        //Fill rows with 24 hours worth of data
        //TODO: Fill with real data from weather API

        for (int i = 0; i < 24; i++) {

            Random rand = new Random();

            //TEMPERATURE TABLE

            //Set random degrees
            TextView label_temperature = new TextView(getContext());
            label_temperature.setId(20 + i);
            label_temperature.setText((68 + rand.nextInt(10)) + "°");
            label_temperature.setTextColor(Color.BLACK);
            label_temperature.setTextSize(20);
            label_temperature.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            label_temperature.setGravity(Gravity.CENTER);
            label_temperature.setPadding(5, 5, 5, 5);
            tr_temperatures.addView(label_temperature);// add the column to the table row here

            //Set condition images
            ImageView weatherCondition = new ImageView(getContext());
            weatherCondition.setId(50 + i);
            weatherCondition.setImageDrawable(getActivity().getDrawable(R.drawable.angry_clouds));
            weatherCondition.setAdjustViewBounds(true);
            weatherCondition.setMaxWidth(5);
            tr_weatherConditions.addView(weatherCondition);

            //Set times
            TextView label_time = new TextView(getContext());
            label_time.setId(40 + i);// define id that must be unique

            // set the text for the header
            if (i <= 12) {
                label_time.setText("" + (i + 1) + "AM");
            } else {
                label_time.setText("" + (i + 1 - 12) + "PM");
            }

            label_time.setTextColor(Color.BLACK); // set the color
            label_time.setTextSize(20);
            label_time.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            label_time.setGravity(Gravity.CENTER);
            label_time.setPadding(5, 5, 5, 5); // set the padding (if required)
            tr_times.addView(label_time); // add the column to the table row here

            //PRECIPITATION TABLE
            //SEt random precip values
            TextView label_precipitation = new TextView(getContext());
            label_precipitation.setId(60 + i);
            label_precipitation.setText((rand.nextInt(100)) + "%");
            label_precipitation.setTextColor(Color.BLACK);
            label_precipitation.setTextSize(20);
            label_precipitation.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            label_precipitation.setGravity(Gravity.CENTER);
            label_precipitation.setPadding(5, 5, 5, 5);
            tr_precipitation.addView(label_precipitation);// add the column to the table row here

        }



        //Add copy of time rows to tables

        TableRow trTimeCopy1 = new TableRow(tr_times.getContext());
        precipitationTable.addView(trTimeCopy1);
        //temperatureTable.addView(tr_weatherConditions);
        //temperatureTable.addView(tr_times);

    }
}