package com.example.chatapp.ui.auth.register;

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

/**
 * View model to store information necessary to complete the registration process.
 * Adapted from Professor Charles Bryan implementation.
 * @author Charles Bryan
 * @author Xavier Hines
 */
public class RegisterViewModel extends AndroidViewModel {

    /**
     * Private field variable that collect the response from
     * the webservice.
     */
    private MutableLiveData<JSONObject> mResponse;

    /**
     * RegisterViewModel constructor
     * @param application
     */
    public RegisterViewModel(@NonNull Application application) {
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
     * @param first User first name
     * @param last User last name
     * @param email User email
     * @param password User password
     */
    public void connectRegister(final String first,
                        final String last,
                        final String email,
                        final String password) {
        String url = "http://10.0.2.2:5000/auth";

        JSONObject body = new JSONObject();
        try {
            body.put("first", first);
            body.put("last", last);
            body.put("email", email);
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
