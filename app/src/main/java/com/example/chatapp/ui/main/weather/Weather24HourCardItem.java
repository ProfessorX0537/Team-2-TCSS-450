package com.example.chatapp.ui.main.weather;

import android.graphics.drawable.Drawable;

import androidx.fragment.app.Fragment;

/**
 * Class that will hold data for each 24 hour item card.
 * @author Xavier Hines
 */
public class Weather24HourCardItem extends Fragment {
    private final String mTime;

    private final String mTemperature;

    private final Drawable mIcon;

    private final String mPrecipitation;

    //TODO add condition image and text
    /**
     * Constructor Weather24HourCardItem
     * @param mTime the hour of the day (1:00 pm)
     * @param mTemperature the temperature at the given time
     * @param mPrecipitation the precipitation at the given time
     */
    public Weather24HourCardItem(String mTime, String mTemperature, String mPrecipitation, Drawable mIcon) {
        //TODO parse data & format
        this.mTime = mTime;
        this.mTemperature = mTemperature;
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

    public Drawable getmIcon() {
        return mIcon;
    }

    /**
     * returns this Time
     * @return mTime
     */
    public String getmTime() {
        return mTime;
    }

    /**
     * returns this Temperature
     * @return mTemperature
     */
    public String getmTemperature() {
        return mTemperature;
    }
}
