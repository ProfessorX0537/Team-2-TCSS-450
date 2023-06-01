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

    public String[] mMonthName;

    private MutableLiveData<JSONObject> mResponse;

    public String mTime;

    private String mLocation;
    //Used for reverting to previous location is to be updated location is invalid
    private String mLocationBackup;


    public WeatherInfoViewModel(@NonNull Application application) {
        super(application);
        mResponse = new MutableLiveData<>();
        mResponse.setValue(new JSONObject());
        mTime = ("");
        mToday = new ArrayList<>(24);
        mDays = new ArrayList<>(10);
        mMonthName = new String[]{"Jan", "Feb", "Mar", "April",
                                    "May", "June", "July", "Aug",
                                    "Sept", "Oct", "Nov", "Dec"};
        mLocation = "98402"; //Default location
        mLocationBackup = mLocation;


    }

    public void addResponseObserver(@NonNull LifecycleOwner owner,
                                    @NonNull Observer<? super JSONObject> observer) {
        mResponse.observe(owner, observer);
    }

    private void handleResult(final JSONObject result) {

        mLocationBackup = mLocation;
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

        //Set location to backup due to error
        mLocation = mLocationBackup;
        connectGet();
    }

    public void connectGet() {
        String url = getApplication().getString(R.string.url_webservices) + "weather";

        //Todo: Pull zipcode/coords from user input

//        String latitude = "&latitude=" + -87.244843;
//        String longitude = "?longitude=" + -122.42595;
//
//        url += longitude;
//        url += latitude;

        url += "?zipcode=" + mLocation;

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

    public void setmLocation(String location) {
        mLocation = location;
        Log.d("Weather", "Updating location: " + location);
        //Pull update
        connectGet();
    }

}
