package com.example.chatapp.model;

import android.app.Application;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import android.text.style.IconMarginSpan;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.chatapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.Charset;
import com.example.chatapp.R;
import com.example.chatapp.ui.main.chat.chatlist.ChatListItem;
import com.example.chatapp.ui.main.weather.Weather10DayCardItem;
import com.example.chatapp.ui.main.weather.Weather24HourCardItem;
import com.example.chatapp.ui.main.weather.WeatherCodes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.Random;

public class WeatherInfoViewModel extends AndroidViewModel {

    public ArrayList<Weather24HourCardItem> mToday;
    public ArrayList<Weather10DayCardItem> mDays;
    // Time in 24 hour integer form -> ArrayList with 4 items:
    // [Temperature, ConditionType(Rain,sunny,etc), Precipitation, Wind Speed]

    private MutableLiveData<JSONObject> mResponse;

    private String mTime;

    private String mLocation;
    private HashMap<Integer, String> mWeatherCodeIcons;

    public WeatherInfoViewModel(@NonNull Application application) {
        super(application);
        mResponse = new MutableLiveData<>();
        mResponse.setValue(new JSONObject());
        mTime = ("");
        mToday = new ArrayList<>(24);
        mDays = new ArrayList<>(10);
        mWeatherCodeIcons = new HashMap<>();


    }

    public void addResponseObserver(@NonNull LifecycleOwner owner,
                                    @NonNull Observer<? super JSONObject> observer) {
        mResponse.observe(owner, observer);
    }

    private void handleResult(final JSONObject result) {

        JSONObject currentWeather;
        JSONObject houryUnits;
        JSONObject hourly;
        JSONObject dailyUnits;
        JSONObject daily;
        try {
            currentWeather = result.getJSONObject("current_weather");
            //Set the current time
            mTime = currentWeather.getString("time");
            //mTime = mTime.substring(11, mTime.length() - 3);
            Log.d("WEATHER", "Time is: " + mTime);

            houryUnits = result.getJSONObject("hourly_units");
            hourly = result.getJSONObject("hourly");
            dailyUnits = result.getJSONObject("daily_units");
            daily = result.getJSONObject("daily");

            setupWeather24HourCards(houryUnits, hourly);

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }


        mResponse.setValue(result);
    }

    private void handleError(final VolleyError error) {

        if (Objects.isNull(error.networkResponse)) {
            try {
                mResponse.setValue(new JSONObject("{" +
                        "error:\"" + error.getMessage() +
                        "\"}"));
            } catch (JSONException e) {
                Log.e("JSON PARSE", "JSON Parse Error in handleError");
            }
        }
        else {
            String data = new String(error.networkResponse.data, Charset.defaultCharset())
                    .replace('\"', '\'');

            Log.e("Bad Request", "handleError " + data);

            try {
                mResponse.setValue(new JSONObject("{" +
                        "code:" + error.networkResponse.statusCode +
                        ", data:\"" + data +
                        "\"}"));
            } catch (JSONException e) {
                Log.e("JSON PARSE", "JSON Parse Error in handleError");
            }
        }
    }

    public void connectGet() {
        String url = getApplication().getString(R.string.url_webservices) + "weather";
        //String url = "http://192.168.1.123:5000/weather";

//        String latitude = "&latitude=" + -87.244843;
//        String longitude = "?longitude=" + -122.42595;
//
//        url += longitude;
//        url += latitude;

        url += "?zipcode=98402";

        Request request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                this::handleResult,
                this::handleError);

        request.setRetryPolicy(new DefaultRetryPolicy(
                10_000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //Instantiate the RequestQueue and add the request to the queue
        Volley.newRequestQueue(getApplication().getApplicationContext())
                .add(request);
    }


    /**
     * fills arraylist with fake data for 24 hour weather cards
     * @author Xavier Hines
     */
    public void setupWeather24HourCards(JSONObject hourlyUnit, JSONObject hourly) throws JSONException { //TODO remove for webservice

        String tempUnit = hourlyUnit.getString("temperature_2m");

        //Find starting point in hourly time array, index position will be used for
        //all other data arrays.
        JSONArray times = hourly.getJSONArray("time");

        int startingIndex = 0;
        while (startingIndex < times.length()) {
            if (times.get(startingIndex).equals(mTime)) {
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
            }
            else if (twelveHour > 12) {
                time = (twelveHour - 12) + " PM";
            } else {
                time = twelveHour + " AM";
            }

            //Get temperature given the time
            String temperature = temperatures.getString(startingIndex + i);

            //Get precipitation chance given the time
            String precipitationChance = precipitationChances.getString(startingIndex + i);

            //Get Drawable icon for weather code
            int weatherCode = weatherCodes.getInt(startingIndex + i);
            int iconID = WeatherCodes.getWeatherIconName(weatherCode);
            Drawable icon = AppCompatResources.getDrawable(getApplication(), iconID);

            //Construct card for the hour
            Weather24HourCardItem curr = new Weather24HourCardItem(
                    time,
                    (temperature + tempUnit),
                    ("Precipitation Chance: " + precipitationChance + "%"),
                    icon
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
