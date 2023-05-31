package com.example.chatapp.ui.main.weather;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;


public class Weather24HourFragment extends Fragment {

    /**
     * Field variable to access info about weather
     */
    private WeatherInfoViewModel mViewModel;

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
        mViewModel = new ViewModelProvider(getActivity()).get(WeatherInfoViewModel.class);

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
        mViewModel.addResponseObserver(
                getViewLifecycleOwner(),
                this::observeData
        );

        mBinding.rootRecycler.setAdapter(new Weather24HourRecycleViewAdapter(mViewModel.mToday));
    }

    private void observeData(JSONObject result) {


        //Clear mToday is previous data is inside it
        if (!mViewModel.mToday.isEmpty()) {
            mViewModel.mToday.clear();
        }

        try {
            JSONObject currentWeather = result.getJSONObject("current_weather");
            //Set the current time
            mViewModel.mTime = currentWeather.getString("time");

            JSONObject hourlyUnits = result.getJSONObject("hourly_units");
            JSONObject hourly = result.getJSONObject("hourly");

            String tempUnit = hourlyUnits.getString("temperature_2m");

            //Find starting point in hourly time array, index position will be used for
            //all other data arrays.
            JSONArray times = hourly.getJSONArray("time");

            int startingIndex = 0;
            while (startingIndex < times.length()) {
                if (times.get(startingIndex).equals(mViewModel.mTime)) {
                    Log.d("Weather", "Found the time at index " + startingIndex);
                    Log.d("Weather", "Current time: " + times.get(startingIndex));
                    break;
                }
                startingIndex++;
            }

            //Instantiate arrays with data we need
            JSONArray temperatures = hourly.getJSONArray("temperature_2m");
            JSONArray precipitationChances = hourly.getJSONArray("precipitation_probability");
            JSONArray weatherCodes = hourly.getJSONArray("weathercode");

            //Now for the next 24 hours from our starting time, get the data from the arrays for
            //times, temperatures, precipitation chance, weather codes for icons
            for (int i = 1; i <= 24; i++) {

                //Get time string
                String time = times.getString(startingIndex + i);
                time = time.substring(11, time.length() - 3);

                //Reformat to 12 hour clock
                int twelveHour = Integer.parseInt(time);
                if (twelveHour == 0) {
                    time = "12 AM";
                } else if (twelveHour > 12) {
                    time = (twelveHour - 12) + " PM";
                } else {
                    time = twelveHour + " AM";
                }

                //Get temperature given the time
                int temperature = temperatures.getInt(startingIndex + i);

                //Get precipitation chance given the time
                String precipitationChance = precipitationChances.getString(startingIndex + i);

                //Get Drawable icon for weather code
                int weatherCode = weatherCodes.getInt(startingIndex + i);
                int iconID = WeatherCodes.getWeatherIconName(weatherCode);
                Drawable icon = AppCompatResources.getDrawable(getContext(), iconID);

                //Construct card for the hour
                Weather24HourCardItem curr = new Weather24HourCardItem(
                        time,
                        (temperature + tempUnit),
                        ("Precipitation Chance: " + precipitationChance + "%"),
                        icon
                );
                mViewModel.mToday.add(curr);
            }

        } catch (JSONException e) {
        Log.e("Weather Update JSON Error", "handleResultError: " + e);
        }


    }
}