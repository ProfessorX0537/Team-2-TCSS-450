package com.example.chatapp.ui.main.home;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.chatapp.R;
import com.example.chatapp.databinding.FragmentHomeBinding;
import com.example.chatapp.model.UserInfoViewModel;
import com.example.chatapp.model.WeatherInfoViewModel;
import com.example.chatapp.ui.main.weather.WeatherCodes;
import com.example.chatapp.ui.main.weather.WeatherFragment;
import com.example.chatapp.ui.main.weather.WeatherLocationsCardItem;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class HomeFragment extends Fragment {

    private WeatherInfoViewModel mViewModel;

    private FragmentHomeBinding mBinding;

    private UserInfoViewModel mUserInfoViewModel;

    private int currentTabPos;

    private WeatherInfoViewModel mWeatherInfoViewModel;


    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(getActivity()).get(WeatherInfoViewModel.class);
        //mViewModel.connectGet();

        mUserInfoViewModel = new ViewModelProvider(getActivity()).get(UserInfoViewModel.class);

        mWeatherInfoViewModel = new ViewModelProvider(getActivity()).get(WeatherInfoViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                  Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = FragmentHomeBinding.inflate(inflater);
//        return inflater.inflate(R.layout.fragment_home, container, false);
//        HomeMessagesFragment fragment = new HomeMessagesFragment();
//        requireActivity()
//                .getSupportFragmentManager()
//                .beginTransaction()
//                .replace(R.id.HomeMessagesContainerView, fragment)
//                .commit();
        return mBinding.getRoot();
    }

    //pop off stack
    //use bottom  nav to move
    //((AppCompatActivity)getActivity()).findViewById(R.id.nav_view)

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //FragmentHomeBinding binding = FragmentHomeBinding.bind(requireView());

        mViewModel.addWeatherResponseObserver(
                getViewLifecycleOwner(),
                this::observeData
        );

        mViewModel.addLocationResponseObserver(getViewLifecycleOwner(), list -> {
            ArrayList<WeatherLocationsCardItem> pastLoc = mWeatherInfoViewModel.mPastLocations;
            if (pastLoc.size() != 0) {
                mBinding.homeTextLocation.setText(pastLoc.get(0).getCity());
            } else {
                mBinding.homeTextLocation.setText("");
            }
        });

        mBinding.textUsernameHome.setText(mUserInfoViewModel.getUsername());

        TabLayout tabLayout = mBinding.tabLayout4;
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int currentTab = tabLayout.getSelectedTabPosition();
                switch (currentTab) {
                    case 0:
                        HomeMessagesFragment fragment = new HomeMessagesFragment();
                        requireActivity()
                                .getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.home_containerView, fragment)
                                .commit();
                        break;
                    case 1:
                        HomeRequestsFragment fragment2 = new HomeRequestsFragment();
                        requireActivity()
                                .getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.home_containerView, fragment2)
                                .commit();
                        break;
                }
                mBinding.homeContainerView.removeAllViews();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

//        mViewModel.addResponseObserver(
//                getViewLifecycleOwner(),
//                result ->
//                {
//                    if (result.length() == 0) {
//                        return;
//                    } else {
//                        try {
//                            Log.d("hi2", result.toString());
//                            //binding.textDate.setText(result.getString("time"));
//                            binding.textDate.setText(result.getJSONArray("time").getString(0));
//                        } catch (Exception e) {
//                            throw new RuntimeException(e);
//                        }
//                    }
//                }
//                this::observeData
//        );

        //mViewModel.addResponseObserver(getViewLifecycleOwner(), result ->
                //result.getJSONObject("daily").getJSONArray("time").getString(0);
                //Log.d("hi2", "hi"));
                //binding.textDate.setText(result.toString());
//        mViewModel.addResponseObserver(
//                getViewLifecycleOwner(),
//                this::observeData
//        );

//        BottomNavigationView temp = ((AppCompatActivity)getActivity()).findViewById(R.id.nav_view);
//        temp.setSelectedItemId(R.id.navigation_weather);

//        binding.cardView.setOnClickListener(button ->
//                temp.setSelectedItemId(R.id.navigation_weather));

//        binding.cardView.setOnClickListener(button ->
//                Navigation.findNavController(requireView()).navigate(
//                        HomeFragmentDirections
//                                .actionNavigationHomeToNavigationWeather()));

        mBinding.weatherTodayCard.setOnClickListener(button -> {
            BottomNavigationView temp = ((AppCompatActivity) getActivity()).findViewById(R.id.nav_view);
            temp.setSelectedItemId(R.id.navigation_weather);
        });

//        mBinding.homeContainerView.setOnClickListener(button -> {
//            BottomNavigationView temp = ((AppCompatActivity) getActivity()).findViewById(R.id.nav_view);
//            temp.setSelectedItemId(R.id.navigation_chat);
//        });
//
//        binding.homeRequestCard.setOnClickListener(button -> {
//            BottomNavigationView temp = ((AppCompatActivity) getActivity()).findViewById(R.id.nav_view);
//            temp.setSelectedItemId(R.id.navigation_connections);
//        });
    }

//    private void observeData(JSONObject result) {
//        JSONObject daily;
//        String date;
//        //JSONArray temp = result.getJSONArray("daily");
//
////            daily = result.getJSONObject("daily");
////            date = daily.getJSONArray("time").getString(0);
////            Log.d("hi", hi.getJSONArray("time").toString());
//
//        JSONArray jsonArray = new JSONArray();
//        jsonArray.put(result);
//
//        //daily = result.getJSONObject("time");
//        Log.d("hi3", result.toString());
//        binding.textDate.setText("hi");
//        //binding.textDate.setText(result.getJSONArray("timezone").toString());
//    }
    private void observeData(JSONObject result) {
        if (result.length() != 0) {
//            try {
//                Date date = new Date();   // given date
//                Calendar calendar = GregorianCalendar.getInstance(); // creates a new calendar instance
//                calendar.setTime(date);   // assigns calendar to given date
//                int currentHour = calendar.get(Calendar.HOUR_OF_DAY); // gets hour in 24h format
//                int currentMonth = calendar.get(Calendar.MONTH);       // gets month number, NOTE this is zero based!
//                Log.d("time", currentHour + "");
//                //Log.d("time", Calendar.getInstance().getTime().toString());
//                JSONObject currentDate = result.getJSONObject("daily");
//                JSONObject currentTemp = result.getJSONObject("hourly");
//                JSONArray temp = currentDate.getJSONArray("time");
//                Log.d("temp", currentTemp.getJSONArray("temperature_2m").getString(currentHour));
//                binding.textDate.setText(currentDate.getJSONArray("time").getString(0));
//                binding.textTemperature.setText(currentTemp.getJSONArray("temperature_2m").getString(currentHour));
//            } catch (Exception e) {
//                throw new RuntimeException(e);
//            }
            try {
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
                mBinding.imageWeathercondtion1.setImageDrawable(icon);
                mBinding.textDate.setText(currentDate);

//                Log.d("hi", temp+"");
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mBinding.tabLayout4.selectTab(mBinding.tabLayout4.getTabAt(currentTabPos));
    }
    @Override
    public void onStop() {
        super.onStop();
        currentTabPos = mBinding.tabLayout4.getSelectedTabPosition();
    }

}