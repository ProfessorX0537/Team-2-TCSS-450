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
import com.example.chatapp.databinding.FragmentHomeBinding;
import com.example.chatapp.databinding.FragmentWeatherTodayBinding;
import com.example.chatapp.model.WeatherInfoViewModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


public class WeatherTodayFragment extends Fragment {

    private WeatherInfoViewModel mViewModel;
    private FragmentWeatherTodayBinding mBinding;

    public WeatherTodayFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(getActivity()).get(WeatherInfoViewModel.class);
//        mViewModel.connectGet();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = FragmentWeatherTodayBinding.inflate(inflater);

        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mViewModel.addWeatherResponseObserver(
                getViewLifecycleOwner(),
                this::observeData
        );

    }

    private void observeData(JSONObject result) {
        if (result.length() != 0) {
            try {

                if (result.has("current_weather")) {
                    Date date = new Date();   // given date
                    Calendar calendar = GregorianCalendar.getInstance(); // creates a new calendar instance
                    calendar.setTime(date);   // assigns calendar to given date
                    int currentHour = calendar.get(Calendar.HOUR_OF_DAY); // gets hour in 24h format
                    int currentMonth = calendar.get(Calendar.MONTH);       // gets month number, NOTE this is zero based!
                    int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
                    int currentYear = calendar.get(Calendar.YEAR);

                    JSONObject current = result.getJSONObject("current_weather");
                    String currentTemp = Math.round(Float.parseFloat(current.getString("temperature"))) + "°F";
                    String currentDate = mViewModel.mMonthName[currentMonth] + " " + currentDay + ", " + currentYear;

                    int currentWeatherCode = Integer.parseInt(current.getString("weathercode"));
                    int iconID = WeatherCodes.getWeatherIconName(currentWeatherCode);
                    Drawable icon = AppCompatResources.getDrawable(getContext(), iconID);

                    mBinding.textTemperature.setText(currentTemp);
                    mBinding.textDaycondition.setText(WeatherCodes.getWeatherCodeName(currentWeatherCode));
                    mBinding.imageWeathercondtion.setImageDrawable(icon);
                    mBinding.textDate.setText(currentDate);
                }
            } catch (JSONException e) {
                Log.e("Weather Update JSON Error", "handleResultError: " + e);
            }
        }
    }
}