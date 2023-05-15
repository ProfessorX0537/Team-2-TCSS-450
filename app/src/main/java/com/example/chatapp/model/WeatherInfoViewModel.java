package com.example.chatapp.model;

import android.app.Application;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.chatapp.R;
import com.example.chatapp.ui.main.chat.chatlist.ChatListItem;
import com.example.chatapp.ui.main.weather.Weather10DayCardItem;
import com.example.chatapp.ui.main.weather.Weather24HourCardItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class WeatherInfoViewModel extends AndroidViewModel {

    public ArrayList<Weather24HourCardItem> mToday;
    public ArrayList<Weather10DayCardItem> mDays;
    // Time in 24 hour integer form -> ArrayList with 4 items:
    // [Temperature, ConditionType(Rain,sunny,etc), Precipitation, Wind Speed]
    private HashMap<Integer, ArrayList<String>> m24Hours;

    public WeatherInfoViewModel(@NonNull Application application) {
        super(application);
        m24Hours = new HashMap<>();
        mToday = new ArrayList<>(24);
        mDays = new ArrayList<>(10);
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

    /**
     * fills arraylist with fake data for 24 hour weather cards
     * @author Xavier Hines
     */
    public void setupWeather24HourCards() { //TODO remove for webservice

        for (int i = 0; i < 24; i++) {
            Weather24HourCardItem curr = new Weather24HourCardItem(
                    "Time " + i,
                    "Temp " +i,

                    "Precipitation " + i
            );
            mToday.add(curr);
        }
    }

    /**
     * fills arraylist with fake data for 10 day weather cards
     * @author Xavier Hines
     */
    public void setupWeather10DayCards() { //TODO remove for webservice

        for (int i = 0; i < 10; i++) {
            Weather10DayCardItem curr = new Weather10DayCardItem(
                    "Day " + i,
                    "Temp " +i,

                    "Precipitation " + i
            );
            mDays.add(curr);
        }
    }
}
