package com.example.chatapp.ui.main.weather;

public class Weather10DayCardItem {
    private final String mDay;

    private final String mTemperature;

    //private final Drawable mIcon;

    private final String mPrecipitation;

    //TODO add condition image and text
    /**
     * Constructor Weather24HourCardItem
     * @param mDay the hour of the day (1:00 pm)
     * @param mTemperature the temperature at the given time
     * @param mPrecipitation the precipitation at the given time
     */
    public Weather10DayCardItem(String mDay, String mTemperature, String mPrecipitation) {
        //TODO parse data & format
        this.mDay = mDay;
        this.mTemperature = mTemperature;
        //this.mIcon = mIcon;
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
     * returns this Temperature
     * @return mTemperature
     */
    public String getmTemperature() {
        return mTemperature;
    }
}