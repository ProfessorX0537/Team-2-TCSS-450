package com.example.chatapp.ui.main;

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

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.Charset;
import java.util.Objects;

public class WeatherApiViewModel extends AndroidViewModel {

    private MutableLiveData<JSONObject> mResponse;

    private String mDate;

    public WeatherApiViewModel(@NonNull Application application) {
        super(application);
        mResponse = new MutableLiveData<>();
        mResponse.setValue(new JSONObject());
        mDate = ("");
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
        try {
            //JSONArray temp = result.getJSONArray("daily");

            daily = result.getJSONObject("daily");
            mDate = daily.getJSONArray("time").getString(0);
//            Log.d("hi", hi.getJSONArray("time").toString());
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
        String url = "https://api.open-meteo.com/v1/forecast?latitude=47.38&longitude=-122.23&hourly=temperature_2m,weathercode&daily=weathercode&temperature_unit=fahrenheit&windspeed_unit=mph&forecast_days=1&timezone=America%2FLos_Angeles";

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
}
