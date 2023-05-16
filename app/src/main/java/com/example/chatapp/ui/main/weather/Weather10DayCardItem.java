package com.example.chatapp.ui.main.weather;

import android.graphics.drawable.Drawable;

public class Weather10DayCardItem {
    private final String mDay;

    private final String mTemperatureMax;
    private final String mTemperatureMin;

    private final Drawable mIcon;

    private final String mPrecipitation;

    //TODO add condition image and text
    /**
     * Constructor Weather24HourCardItem
     * @param mDay the hour of the day (1:00 pm)
     * @param mTemperatureMax the temperature at the given time
     * @param mPrecipitation the precipitation at the given time
     */
    public Weather10DayCardItem(String mDay, String mTemperatureMax, String mTemperatureMin,
                                String mPrecipitation, Drawable mIcon) {
        //TODO parse data & format
        this.mDay = mDay;
        this.mTemperatureMax = mTemperatureMax;
        this.mTemperatureMin = mTemperatureMin;
        this.mIcon = mIcon;
        this.mPrecipitation = mPrecipitation;
    }

    /**
     * returns this Precipitation
     * @return mPrecipitation
     */
    public String getmPrecipitation() {
        return mPrecipitation;
    }

//    public Drawable getmIcon() {
//        return mIcon;
//    }

    /**
     * returns this Time
     * @return mTime
     */
    public String getmDay() {
        return mDay;
    }

    /**
     * returns max temperature for the day
     * @return mTemperature
     */
    public String getmTemperatureMax() {
        return mTemperatureMax;
    }

    /**
     * returns min temperature for the day
     * @return mTemperature
     */
    public String getmTemperatureMin () {
        return mTemperatureMin;
    }

    /**
     * Returns drawable for weather condition icon
     * @return weather condition icon
     */
    public Drawable getmIcon() {
        return mIcon;
    }


}
