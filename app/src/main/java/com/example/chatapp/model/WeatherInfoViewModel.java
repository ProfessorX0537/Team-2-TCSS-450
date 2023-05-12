package com.example.chatapp.model;

import android.app.Application;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.Random;

public class WeatherInfoViewModel extends AndroidViewModel {

    private ArrayList<String> mToday;
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
//        try {
//            mResponse.setValue(result);
//            JSONObject temp = result.getJSONObject("timezone");
//        } catch (JSONException e) {
//            throw new RuntimeException(e);
//        }
        //mResponse.setValue(result);
        JSONObject daily;
        JSONArray daily2;
        JSONObject time;
        try {
            //JSONArray temp = result.getJSONArray("daily");

            daily = result.getJSONObject("daily");
            mDate = daily.getJSONArray("time").getString(0);
            //daily2 = result.getJSONArray("daily_units");
            time = result.getJSONObject("daily_units");

//            Log.d("hi", hi.getJSONArray("time").toString());
//            Log.d("hi", time.getString("time"));
            Log.d("hi", mDate);
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

        Request request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null, //no body for this get request
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
}
