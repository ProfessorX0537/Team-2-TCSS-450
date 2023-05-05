package com.example.chatapp.ui.main.weather;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.chatapp.R;

import java.util.Random;


public class Weather24HourFragment extends Fragment {


    public Weather24HourFragment() {
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
        return inflater.inflate(R.layout.fragment_weather24_hour, container, false);
    }

    @SuppressLint("ResourceType")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setTemperatueTable();

    }

    @SuppressLint("ResourceType")
    public void setTemperatueTable() {
        //Get table layout object
        TableLayout tl = (TableLayout) getActivity().findViewById(R.id.table_temp);

        //Create rows
        TableRow tr_temperatures = new TableRow(getContext());
        TableRow tr_times = new TableRow(getContext());

        //Set Table Row attributes
        tr_temperatures.setId(10);
        tr_temperatures.setLayoutParams(new TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT));

        tr_times.setId(11);
        tr_times.setLayoutParams(new TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT));

        //Fill rows with 24 hours worth of data
        //TODO: Fill with real data from weather API

        for (int i = 0; i < 24; i++) {

            Random rand = new Random();

            //Create textlabels
            TextView label_temperature = new TextView(getContext());
            label_temperature.setId(20 + i);
            label_temperature.setText((68 + rand.nextInt(10)) + "°");
            label_temperature.setTextColor(Color.BLACK);
            label_temperature.setTextSize(20);
            label_temperature.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            label_temperature.setGravity(Gravity.CENTER);
            label_temperature.setPadding(5, 5, 5, 5);
            tr_temperatures.addView(label_temperature);// add the column to the table row here

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


        }

        //Add rows to table
        tl.addView(tr_temperatures);
        tl.addView(tr_times);

    }
}