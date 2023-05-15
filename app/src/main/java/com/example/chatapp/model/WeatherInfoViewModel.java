package com.example.chatapp.model;

import android.app.Application;
import android.widget.ImageView;
import android.text.style.IconMarginSpan;
import android.util.Log;

import androidx.annotation.NonNull;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.Random;

public class WeatherInfoViewModel extends AndroidViewModel {

    public ArrayList<Weather24HourCardItem> mToday;
    public ArrayList<Weather10DayCardItem> mDays;
    // Time in 24 hour integer form -> ArrayList with 4 items:
    // [Temperature, ConditionType(Rain,sunny,etc), Precipitation, Wind Speed]
    private HashMap<Integer, ArrayList<String>> m24Hours;

    private MutableLiveData<JSONObject> mResponse;

    private String mDate;

    public WeatherInfoViewModel(@NonNull Application application) {
        super(application);
        mResponse = new MutableLiveData<>();
        mResponse.setValue(new JSONObject());
        mDate = ("");
        m24Hours = new HashMap<>();
    }

    public void addResponseObserver(@NonNull LifecycleOwner owner,
                                    @NonNull Observer<? super JSONObject> observer) {
        mResponse.observe(owner, observer);
    }

    private void handleResult(final JSONObject result) {

        Log.d("TEST", "handle result test");
        JSONObject currentWeather;
        JSONArray daily2;
        JSONObject time;
        try {
            //JSONArray temp = result.getJSONArray("daily");
            Log.d("TEST", "JSON: " + result);
            currentWeather = result.getJSONObject("current_weather");
            Log.d("TEST", "Current Weather time is: " + currentWeather);
            mDate = currentWeather.getString("time");
            Log.d("TEST", "Current time is: " + mDate);
            //daily2 = result.getJSONArray("daily_units");
            //time = currentWeather.getJSONObject("time");

//            Log.d("hi", hi.getJSONArray("time").toString());
//            Log.d("hi", time.getString("time"));
            //Log.d("hi", mDate);
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
        mToday = new ArrayList<>(24);
        mDays = new ArrayList<>(10);
    }

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

        String latitude = "&latitude=" + -87.244843;
        String longitude = "?longitude=" + -122.42595;

        url += longitude;
        url += latitude;



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

    //TEMP FOR FILLING DATA
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
