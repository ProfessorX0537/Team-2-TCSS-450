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
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.chatapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.Charset;

import com.example.chatapp.ui.main.weather.Weather10DayCardItem;
import com.example.chatapp.ui.main.weather.Weather24HourCardItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class WeatherInfoViewModel extends AndroidViewModel {

    public ArrayList<Weather24HourCardItem> mToday;
    public ArrayList<Weather10DayCardItem> mDays;

    public String[] mMonthName;

    private MutableLiveData<JSONObject> mResponse;

    public String mTime;

    private HashMap<String, String> mLocation;
    //Used for reverting to previous location is to be updated location is invalid
    private HashMap<String, String> mLocationBackup;


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

        //Declare mLocation with this call
        getLatLngByZipcode("98335", new VolleyCallback() {
            @Override
            public void onSuccess(HashMap<String, String> result) {
                // Process the result
                processData(result);

            }

            @Override
            public void onError(VolleyError error) {
                // Handle the error
                error.printStackTrace();
            }

        });

        mLocationBackup = mLocation;

    }

    public interface VolleyCallback {
        void onSuccess(HashMap<String, String> result);
        void onError(VolleyError error);
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

    public void setLocation(final String locationInput) {

        //Remove whitespace from input
        String reformattedInput = locationInput.trim();

        //Is user sending zipcode or lat/long, check with regex
        String patternLatLong = "^[-+]?([1-8]?\\d(\\.\\d+)?|90(\\.0+)?),\\s*[-+]?(180(\\.0+)?|((1[0-7]\\d)|([1-9]?\\d))(\\.\\d+)?)$";
        String patternZipcode = "^\\d{5}(?:[-\\s]\\d{4})?$";


        if (checkRegex(reformattedInput, patternLatLong)) { //Check for lat / long input

            Log.d("WEATHER_LOCATION", "Entered valid longitude & latitude formatting");
            HashMap<String, String> location = new HashMap<>();

            //Separate Longitude and latitude from input
            String latitude = reformattedInput.substring(0,reformattedInput.indexOf(','));
            String longitude = reformattedInput.substring(reformattedInput.indexOf(',') + 1);
            Log.d("WEATHER_LOCATION", "Getting latitude and longitude from user input, lat: \'"
            + latitude + "\' long: \'" + longitude + "\'");

            location.put("Longitude", longitude);
            location.put("Latitude", latitude);

            mLocation = location;

        } else if (checkRegex(reformattedInput, patternZipcode)) { //Check if input is zipcode
            Log.d("WEATHER_LOCATION", "Entered valid zipcode formatting");

            getLatLngByZipcode("98335", new VolleyCallback() {
                @Override
                public void onSuccess(HashMap<String, String> result) {
                    // Process the result
                    processData(result);

                }

                @Override
                public void onError(VolleyError error) {
                    // Handle the error
                    error.printStackTrace();
                }

            });


        } else {
            Log.e("WEATHER_LOCATION", "Not valid location formatting (Specify zipcode or lat/long)");
        }

        //Pull update
        connectGet();
    }

    public boolean checkRegex(final String input, final String pattern) {
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
     * Converts a zipcode into latitude and longitude HashMap
     * @param zipcode
     */
    public HashMap<String, String> getLatLngByZipcode(String zipcode, final VolleyCallback callback) {

        AtomicReference<HashMap<String, String>> location = new AtomicReference<>(new HashMap<>());

        //Request for converting zipcode
        String url = getApplication().getString(R.string.url_webservices) + "weather/convertZipcode";
        url += "?zipcode=" + zipcode;

        Request request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Process the response data
                        try {
                            HashMap<String, String> location = handleZipcodeResult(response);
                            callback.onSuccess(location);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            callback.onError(new VolleyError(e.getMessage()));
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        callback.onError(error);
                    }
                });

        request.setRetryPolicy(new DefaultRetryPolicy(
                10_000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //Instantiate the RequestQueue and add the request to the queue
        Volley.newRequestQueue(getApplication().getApplicationContext())
                .add(request);

        return location.get();
    }

    private HashMap<String, String> handleZipcodeResult(JSONObject result) throws JSONException {
        HashMap<String, String> location = new HashMap<>();

        if (result.has("Latitude") && result.has("Longitude")) {
            location.put("Longitude", result.getString("Longitude"));
            location.put("Latitude", result.getString("Latitude"));
//            Log.d("location check1", location.toString());
        }

        return location;

    }

    public void processData(HashMap<String, String> data) {
        mLocation = data;
        System.out.println("Response Data: " + data.toString());
    }




}
