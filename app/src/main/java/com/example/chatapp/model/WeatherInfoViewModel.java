package com.example.chatapp.model;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.chatapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.Charset;

import com.example.chatapp.databinding.FragmentWeatherBinding;
import com.example.chatapp.ui.main.weather.Weather10DayCardItem;
import com.example.chatapp.ui.main.weather.Weather24HourCardItem;
import com.example.chatapp.ui.main.weather.WeatherFragment;
import com.example.chatapp.ui.main.weather.WeatherLocationsCardItem;
import com.example.chatapp.utils.Storage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class WeatherInfoViewModel extends AndroidViewModel {

    public ArrayList<Weather24HourCardItem> mToday;
    public ArrayList<Weather10DayCardItem> mDays;
    public ArrayList<WeatherLocationsCardItem> mPastLocations;
    public static final String PASTLOCATION_FILE = "WeatherPastLocations";

    public String[] mMonthName;

    private MutableLiveData<JSONObject> mWeatherResponse;
    private MutableLiveData<JSONObject> mLocationResponse;

    public String mTime;

    public HashMap<String, String> mLocation;
    //Used for reverting to previous location is to be updated location is invalid
    //private HashMap<String, String> mLocationBackup;

    public boolean mInvalidLocationFormatting;
    public boolean mInvalidLocationRequest;

    public interface VolleyCallback {
        void onSuccess(String result);
        void onError(VolleyError error);
    }

    public WeatherInfoViewModel(@NonNull Application application) {
        super(application);
        mWeatherResponse = new MutableLiveData<>();
        mWeatherResponse.setValue(new JSONObject());
        mLocationResponse = new MutableLiveData<>();
        mLocationResponse.setValue(new JSONObject());
        mTime = ("");
        mToday = new ArrayList<>(24);
        mDays = new ArrayList<>(10);
        mPastLocations = new ArrayList<>(10);
        mInvalidLocationFormatting = false;
        mInvalidLocationRequest = false;

        mMonthName = new String[]{"Jan", "Feb", "Mar", "April",
                                    "May", "June", "July", "Aug",
                                    "Sept", "Oct", "Nov", "Dec"};


        //Default location (Tacoma)
        HashMap<String, String> location = new HashMap<>();
        location.put("Latitude", "47.2529105");
        location.put("Longitude", "-122.4417426");

        mLocation = location;
        addLocationToWeatherLocations(mLocation);
        mPastLocations.add(new WeatherLocationsCardItem("Tacoma", "WA", "98402", "U.S."));
        //mLocationBackup = mLocation;

    }



    public void addWeatherResponseObserver(@NonNull LifecycleOwner owner,
                                    @NonNull Observer<? super JSONObject> observer) {
        mWeatherResponse.observe(owner, observer);
    }

    public void addLocationResponseObserver(@NonNull LifecycleOwner owner,
                                           @NonNull Observer<? super JSONObject> observer) {
        mLocationResponse.observe(owner, observer);
    }

    private void handleWeatherResult(final JSONObject result) {

        //mInvalidLocationRequest = false;
        //mLocationBackup = mLocation;
        mWeatherResponse.setValue(result);

    }

    private void handleError(final VolleyError error) {

        mInvalidLocationRequest = true;

        if (Objects.isNull(error.networkResponse)) {
            try {
                mWeatherResponse.setValue(new JSONObject("{" +
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
                mWeatherResponse.setValue(new JSONObject("{" +
                        "code:" + error.networkResponse.statusCode +
                        ", data:\"" + data +
                        "\"}"));
            } catch (JSONException e) {
                Log.e("JSON PARSE", "JSON Parse Error in handleError");
            }
        }

        //Set location to backup due to error
        //mLocation = mLocationBackup;
        //connectGet();
    }

    private void addLocationToWeatherLocations(HashMap<String, String> location) {

        //Use location coords to get city info from api
        String url = getApplication().getString(R.string.url_webservices) + "weather/regioninfo";

        String latitude = location.get("Latitude");
        String longitude = location.get("Longitude");

        url += "?latitude=" + latitude;
        url += "&longitude=" + longitude;

        Request request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                mLocationResponse::setValue,
                this::handleError);

        request.setRetryPolicy(new DefaultRetryPolicy(
                10_000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //Instantiate the RequestQueue and add the request to the queue
        Volley.newRequestQueue(getApplication().getApplicationContext())
                .add(request);

    }



    public void connectGet() {
        String url = getApplication().getString(R.string.url_webservices) + "weather";

        String latitude = mLocation.get("Latitude");
        String longitude = mLocation.get("Longitude");

        Log.d("Weather_Diag", "latitude: " + latitude);
        Log.d("Weather_Diag", "longitude: " + longitude);

        url += "?latitude=" + latitude;
        url += "&longitude=" + longitude;

        Request request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                this::handleWeatherResult,
                this::handleError);

        request.setRetryPolicy(new DefaultRetryPolicy(
                10_000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //Instantiate the RequestQueue and add the request to the queue
        Volley.newRequestQueue(getApplication().getApplicationContext())
                .add(request);
    }

    public void setLocation(final String locationInput) {

        //Remove whitespace from input
        String reformattedInput = locationInput.trim();

        //Is user sending zipcode or lat/long, check with regex
        String patternLatLong = "^[-+]?([1-8]?\\d(\\.\\d+)?|90(\\.0+)?),\\s*[-+]?(180(\\.0+)?|((1[0-7]\\d)|([1-9]?\\d))(\\.\\d+)?)$";
        String patternZipcode = "^\\d{5}(?:[-\\s]\\d{4})?$";


        if (checkRegex(reformattedInput, patternLatLong)) { //Check for lat / long input

            mInvalidLocationFormatting = false;
            Log.d("WEATHER_LOCATION", "Entered valid longitude & latitude formatting");
            HashMap<String, String> location = new HashMap<>();

            //Separate Longitude and latitude from input
            String latitude = reformattedInput.substring(0,reformattedInput.indexOf(','));
            String longitude = reformattedInput.substring(reformattedInput.indexOf(',') + 1);
            Log.d("WEATHER_LOCATION", "Getting latitude and longitude from user input, lat: \'"
            + latitude + "\' long: \'" + longitude + "\'");

            location.put("Longitude", longitude);
            location.put("Latitude", latitude);

            addLocationToWeatherLocations(location);
            mLocation = location;

        } else if (checkRegex(reformattedInput, patternZipcode)) { //Check if input is zipcode
            Log.d("WEATHER_LOCATION", "Entered valid zipcode formatting");


            getLatLngByZipcode(reformattedInput, new VolleyCallback() {
                @Override
                public void onSuccess(String result) {
                    //Call setLocation again now with long/lat format
                    setLocation(result);
                }

                @Override
                public void onError(VolleyError error) {
                    // Handle the error
                    //mInvalidLocationRequest = true;
                    setInvalidLocationRequest();
                    Log.d("LOCATION_TEST", "Test Request Error " + mInvalidLocationRequest);
                    Log.e("WEATHER_LOCATION", "Error: Not valid zipcode");
                    error.printStackTrace();
                }

            });


        } else {
            Log.e("WEATHER_LOCATION", "Not valid location formatting (Specify zipcode or lat/long)");
            //Set error on text box
            mInvalidLocationFormatting = true;

        }

        //Pull update
        connectGet();
    }

    private void setInvalidLocationRequest() {
        mInvalidLocationRequest = true;
        System.out.println(mInvalidLocationRequest);
    }

    private boolean checkRegex(final String input, final String pattern) {
        // Create a Pattern object for LatLong
        Pattern r = Pattern.compile(pattern);

        // Now create matcher object for LatLong.
        Matcher m = r.matcher(input);

        if (m.find()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Converts zipcode input into latitude and longitude user input (e.g. "47.2529105,-122.4417426")
     * @param zipcode zipcode to
     * @param callback Volley callback for returning value
     */
    private void getLatLngByZipcode(String zipcode, final VolleyCallback callback) {

        //Request for converting zipcode
        String url = getApplication().getString(R.string.url_webservices) + "weather/convertZipcode";
        url += "?zipcode=" + zipcode;

        Request request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                response -> {
                    // Process the response data
                    try {
                        String location = response.getString("Latitude") + "," +
                                response.getString("Longitude");
                        callback.onSuccess(location);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        callback.onError(new VolleyError(e.getMessage()));
                    }
                },
                error -> callback.onError(error));

        request.setRetryPolicy(new DefaultRetryPolicy(
                10_000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        //Instantiate the RequestQueue and add the request to the queue
        Volley.newRequestQueue(getApplication().getApplicationContext())
                .add(request);


    }

    public void save(Context ctx) {
        Storage.saveSerializable(PASTLOCATION_FILE, mPastLocations, ctx);
    }

    public void tryLoad(Context ctx) {
        ArrayList<WeatherLocationsCardItem> list = (ArrayList<WeatherLocationsCardItem>) Storage.loadSerializable(PASTLOCATION_FILE, ctx);
        if (list != null) {
            mPastLocations = list;

            WeatherLocationsCardItem curr = list.get(0);
            setLocation(curr.getZipCode());
        }
    }

}
