package com.example.chatapp.ui.auth.login;

import android.app.Application;
import android.util.Base64;
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
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * ViewModel that will hold information relevant to the Login process
 * throughout its life time.
 * @author Xavier Hines
 */
public class LoginViewModel extends AndroidViewModel {

    /**
     * Private field variable that collect the response from
     * the webservice.
     */
    private MutableLiveData<JSONObject> mResponse;

    /**
     * When email is sent for repass, this becomes true. (for UI hidding on forget pass)
     */
    public MutableLiveData<Boolean> mIsSentForgetEmail;

    /**
     * LoginViewModel constructor
     * @param application
     */
    public LoginViewModel(@NonNull Application application) {
        super(application);
        mResponse = new MutableLiveData<>();
        mResponse.setValue(new JSONObject());

        mIsSentForgetEmail = new MutableLiveData<>();
        mIsSentForgetEmail.setValue(false);
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
     * Will take users entered email and password making a GET request to the webservice
     * which will verify whether or not the users credentials exist in the web service.
     * @param email Users email
     * @param password Users password
     */
    public void connectLogin(final String email, final String password) {
        String url = getApplication().getString(R.string.url_webservices) + "auth";

        Request request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null, //no body for this get request
                mResponse::setValue,
                this::handleError) { // This allows me to override existing non-final methods

            // getHeaders override to set headers of the new JsonObjectRequest
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                // add headers <key,value>
                String credentials = email + ":" + password;
                String auth = "Basic "
                        + Base64.encodeToString(credentials.getBytes(),
                        Base64.NO_WRAP);
                headers.put("Authorization", auth);
                return headers;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(
                10_000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //Instantiate the RequestQueue and add the request to the queue
        // This may need changed to the requestSingletonQueue in lab 3 Auth
        Volley.newRequestQueue(getApplication().getApplicationContext())
                .add(request);
    }

    /**
     * http call that will resend verification email.
     * @param email the users email
     */
    public void connectResendVerification(final String email) {
        String url = getApplication().getString(R.string.url_webservices) + "verify/send?email=" + email;

        Request request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null, //no body for this get request
                mResponse::setValue,
                this::handleError);

        request.setRetryPolicy(new DefaultRetryPolicy(
                10_000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //Instantiate the RequestQueue and add the request to the queue
        // This may need changed to the requestSingletonQueue in lab 3 Auth
        Volley.newRequestQueue(getApplication().getApplicationContext())
                .add(request);
    }

    /**
     * http call that will check that an account exists under the given email
     * and then send email that will allow user to proceed with password reset.
     * @param email the email the user made account with.
     */
    public void connectResetPasswordEmail(final String email) {
        String url = getApplication().getString(R.string.url_webservices) + "repass/request?email=" + email;

        Request request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null, //no body for this get request
                response -> {
                    mResponse.setValue(response);
                    mIsSentForgetEmail.setValue(true);
                },
                this::handleError);

        request.setRetryPolicy(new DefaultRetryPolicy(
                10_000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //Instantiate the RequestQueue and add the request to the queue
        // This may need changed to the requestSingletonQueue in lab 3 Auth
        Volley.newRequestQueue(getApplication().getApplicationContext())
                .add(request);
    }

    /**
     * http call that will change the users password for our application
     */
    public void connectResetPassword(final String email, final String newPassword) {
        //TODO
        String url = getApplication().getString(R.string.url_webservices) +"repass";

        JSONObject body = new JSONObject();
        try {
            body.put("email", email);
            body.put("newPassword", newPassword);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Request request = new JsonObjectRequest(
                Request.Method.POST,
                url,
                body, //no body for this get request
                mResponse::setValue,
                this::handleError);

        request.setRetryPolicy(new DefaultRetryPolicy(
                10_000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //Instantiate the RequestQueue and add the request to the queue
        // This may need changed to the requestSingletonQueue in lab 3 Auth
        Volley.newRequestQueue(getApplication().getApplicationContext())
                .add(request);
    }
}
