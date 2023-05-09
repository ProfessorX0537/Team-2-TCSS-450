package com.example.chatapp.model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class WeatherInfoViewModel extends AndroidViewModel {

    private ArrayList<String> mToday;
    // Time in 24 hour integer form -> ArrayList with 4 items:
    // [Temperature, ConditionType(Rain,sunny,etc), Precipitation, Wind Speed]
    private HashMap<Integer, ArrayList<String>> m24Hours;

    public WeatherInfoViewModel(@NonNull Application application) {
        super(application);
        m24Hours = new HashMap<>();

    }

    //@Todo: Pull information from web service, delete random data
    public void pullWeatherUpdates() {

        Random rand = new Random();
        for (int i = 1; i <= 24; i++) {

            //Get values for each item
            String temp = "" + (68 +rand.nextInt(10));
            String weatherCond = "Rainy";
            String precipitation = "" + rand.nextInt(100);
            String windSpeed = "" + rand.nextInt(14);

            ArrayList<String> itemList = new ArrayList<>();
            itemList.add(temp);
            itemList.add(weatherCond);
            itemList.add(precipitation);
            itemList.add(windSpeed);

            m24Hours.put(i, itemList);


        }
    }
}
