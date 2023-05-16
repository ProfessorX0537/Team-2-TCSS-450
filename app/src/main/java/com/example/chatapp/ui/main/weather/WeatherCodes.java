package com.example.chatapp.ui.main.weather;

import android.graphics.drawable.Drawable;

import com.example.chatapp.R;

import java.util.HashMap;

public class WeatherCodes {

    /**
     * Uses a hashmap to get Icon file name that corresponds with weathercode
     * @param weatherCode integer code obtained from weather API representing weather condition
     * @return file name of eather condition icon
     */
    public static int getWeatherIconName(int weatherCode) {

        HashMap<Integer, Integer> weatherIcons = new HashMap<>();
        weatherIcons.put(0, R.drawable.day_clear);
        weatherIcons.put(1, R.drawable.day_partial_cloud);
        weatherIcons.put(2, R.drawable.day_partial_cloud);
        weatherIcons.put(3, R.drawable.overcast);
        weatherIcons.put(45, R.drawable.fog);
        weatherIcons.put(48, R.drawable.fog);
        weatherIcons.put(51, R.drawable.rain);
        weatherIcons.put(53, R.drawable.rain);
        weatherIcons.put(55, R.drawable.rain);
        weatherIcons.put(61, R.drawable.rain);
        weatherIcons.put(63, R.drawable.rain);
        weatherIcons.put(65, R.drawable.rain);
        weatherIcons.put(80, R.drawable.rain);
        weatherIcons.put(81, R.drawable.rain);
        weatherIcons.put(82, R.drawable.rain);
        weatherIcons.put(71, R.drawable.snow);
        weatherIcons.put(73, R.drawable.snow);
        weatherIcons.put(75, R.drawable.snow);
        weatherIcons.put(85, R.drawable.snow);
        weatherIcons.put(86, R.drawable.snow);
        weatherIcons.put(95, R.drawable.thunder);
        weatherIcons.put(96, R.drawable.snow_thunder);
        weatherIcons.put(99, R.drawable.snow_thunder);

        if (weatherIcons.containsKey(weatherCode)) {
            return weatherIcons.get(weatherCode);
        } else {
            return R.drawable.mist; //@Todo Default image return, add images that can cover all weather codes
        }



    }

}
