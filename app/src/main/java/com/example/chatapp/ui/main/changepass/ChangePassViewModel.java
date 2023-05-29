package com.example.chatapp.ui.main.changepass;

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

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.Charset;
import java.util.Objects;

/**
 * View model that stores information and http connection calls necessary
 * to allow a user to change their password.
 * @author Xavier Hines
 */
public class ChangePassViewModel extends AndroidViewModel {
    /**
     * Private field variable that collect the response from
     * the webservice.
     */
    private MutableLiveData<JSONObject> mResponse;

    /**
     * RegisterViewModel constructor
     * @param application
     */
    public ChangePassViewModel(@NonNull Application application) {
        super(application);
        mResponse = new MutableLiveData<>();
        mResponse.setValue(new JSONObject());
    }

    /**
     * Method that attaches a response observer to a given lifecycleowner
     * @param owner The lifecycleowner that needs observer
     * @param observer The observer
     */
    public void addResponseObserver(@NonNull LifecycleOwner owner,
                                    @NonNull Observer<? super JSONObject> observer) {
        mResponse.observe(owner, observer);
    }

    /**
     * Will handleErrors returned by the webservice
     * @param error The error returned by webservice
     */
    private void handleError(final VolleyError error) {
        //System.out.println("Entered error handler");
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
                JSONObject response = new JSONObject();
                response.put("code", error.networkResponse.statusCode);
                response.put("data", new JSONObject(data));
                mResponse.setValue(response);
            } catch (JSONException e) {
                Log.e("JSON PARSE", "JSON Parse Error in handleError");
            }
        }
    }

    /**
     * Will make POST request to the webservice attempting to register
     * a user with the provided information.
     * @param oldPassword Users old password
     * @param password User password
     */
    public void connectChangePassword(final String oldPassword,
                                final String password) {
        //TODO change to right endpoint
        String url = getApplication().getString(R.string.url_webservices) + "auth";
        JSONObject body = new JSONObject();
        try {
            body.put("oldPassword", oldPassword);
            body.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //System.out.println("the JSON being sent: "+body);

        Request request = new JsonObjectRequest(
                Request.Method.POST,
                url,
                body,
                mResponse::setValue,
                this::handleError);
        System.out.println("sent request");

        request.setRetryPolicy(new DefaultRetryPolicy(
                10_000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //Instantiate the RequestQueue and add the request to the queue
        Volley.newRequestQueue(getApplication().getApplicationContext())
                .add(request);
    }
}
