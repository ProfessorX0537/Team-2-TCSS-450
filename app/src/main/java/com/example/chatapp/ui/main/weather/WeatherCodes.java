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

    /**
     * Return the string id of the weather condition from the weathercode that is passed
     * @param weatherCode code from api to find weather conditions
     * @return string id
     */
    public static int getWeatherCodeName(int weatherCode) {
        HashMap<Integer, Integer> weatherIcons = new HashMap<>();
        weatherIcons.put(0, R.string.weathercode_clear);
        weatherIcons.put(1, R.string.weathercode_partly_clear);
        weatherIcons.put(2, R.string.weathercode_partly_cloudy);
        weatherIcons.put(3, R.string.weathercode_overcast);
        weatherIcons.put(45, R.string.weathercode_fog);
        weatherIcons.put(48, R.string.weathercode_rime_fog);
        weatherIcons.put(51, R.string.weathercode_drizzle_light);
        weatherIcons.put(53, R.string.weathercode_drizzle_med);
        weatherIcons.put(55, R.string.weathercode_drizzle_high);
        weatherIcons.put(56, R.string.weathercode_freezing_drizzle_light);
        weatherIcons.put(57, R.string.weathercode_freezing_drizzle_high);
        weatherIcons.put(61, R.string.weathercode_rain_light);
        weatherIcons.put(63, R.string.weathercode_rain_moderate);
        weatherIcons.put(65, R.string.weathercode_rain_heavy);
        weatherIcons.put(66, R.string.weathercode_freezing_rain_light);
        weatherIcons.put(67, R.string.weathercode_freezing_rain_heavy);
        weatherIcons.put(71, R.string.weathercode_light_snow);
        weatherIcons.put(73, R.string.weathercode_moderate_snow);
        weatherIcons.put(75, R.string.weathercode_heavy_snow);
        weatherIcons.put(77, R.string.weathercode_snow_grains);
        weatherIcons.put(80, R.string.weathercode_rain_showers_light);
        weatherIcons.put(81, R.string.weathercode_rain_showers_moderate);
        weatherIcons.put(82, R.string.weathercode_rain_showers_heavy);
        weatherIcons.put(85, R.string.weathercode_snow_showers_light);
        weatherIcons.put(86, R.string.weathercode_snow_showers_heavy);
        weatherIcons.put(95, R.string.weathercode_storm);
        weatherIcons.put(96, R.string.weathercode_storm_hail);
        weatherIcons.put(99, R.string.weathercode_storm_hail_heavy);

        if (weatherIcons.containsKey(weatherCode)) {
            return weatherIcons.get(weatherCode);
        } else {
            return weatherIcons.get(0);
        }
    }
}
