package com.example.chatapp.ui.main.weather;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.chatapp.R;
import com.example.chatapp.databinding.FragmentWeather10DayBinding;
import com.example.chatapp.databinding.FragmentWeather24HourBinding;
import com.example.chatapp.model.WeatherInfoViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Fragment for displaying weather for the next 10 days for each day
 *
 * @author Luca Smith
 */
public class Weather10DayFragment extends Fragment {
    /**
     * Field variable to access info about weather
     */
    private WeatherInfoViewModel mViewModel;

    /**
     * Instance field variable for binding layout and this fragment.
     */
    private FragmentWeather10DayBinding mBinding;

    public Weather10DayFragment() {
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
        mBinding = FragmentWeather10DayBinding.inflate(inflater);
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

        mViewModel.addWeatherResponseObserver(
                getViewLifecycleOwner(),
                this::observeData
        );

        mBinding.rootRecycler.setAdapter(new Weather10DayRecycleViewAdapter(mViewModel.mDays));
    }

    /**
     * Data observer for weather information from web service
     * @param result
     */
    private void observeData(JSONObject result) {
        if (result.length() != 0) {

            //Clear mDays is previous data is inside it
            if (!mViewModel.mDays.isEmpty()) {
                mViewModel.mDays.clear();
            }

            try {


                JSONObject dailyUnits = result.getJSONObject("daily_units");
                JSONObject daily = result.getJSONObject("daily");

                String tempUnit = dailyUnits.getString("temperature_2m_max");

                //Get dates
                JSONArray dates = daily.getJSONArray("time");

                //Get max temperature for day
                JSONArray maxTemps = daily.getJSONArray("temperature_2m_max");
                //Get min temperature for day
                JSONArray minTemps = daily.getJSONArray("temperature_2m_min");
                //Get precipitation chance for day
                JSONArray precipitationChances = daily.getJSONArray("precipitation_probability_max");
                //Get weather code
                JSONArray weatherCodes = daily.getJSONArray("weathercode");

                //Create weather cards for the next 10 days from above array data
                for (int i = 0; i < 10; i++) {

                    //Get date
                    String date = "";
                    if (i == 0) {
                        date = "Today";
                    } else {
                        date = dates.getString(i).substring(5);
                    }
                    //Get temps
                    int tempMax = maxTemps.getInt(i);
                    int tempMin = minTemps.getInt(i);

                    //Get Precipitation chance
                    String precipitationChance = precipitationChances.getString(i);

                    int iconID = WeatherCodes.getWeatherIconName(weatherCodes.getInt(i));
                    Drawable icon = AppCompatResources.getDrawable(getContext(), iconID);

                    Weather10DayCardItem curr = new Weather10DayCardItem(
                            date,
                            tempMax + tempUnit,
                            tempMin + tempUnit,
                            "Precipitation Chance: " + precipitationChance + "%",
                            icon
                    );
                    mViewModel.mDays.add(curr);
                }

            } catch (JSONException e) {
                Log.e("Weather Update JSON Error", "handleResultError: " + e);
            }

            mBinding.rootRecycler.getAdapter().notifyDataSetChanged();
        }
    }
}