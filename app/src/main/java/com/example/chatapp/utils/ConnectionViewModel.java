package com.example.chatapp.utils;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.chatapp.R;

/**
 * An Application level ViewModel that connects to webservice to check of client is connected. <br>
 */
public class ConnectionViewModel extends AndroidViewModel {
    private MutableLiveData<Boolean> mIsConnected;
    private MutableLiveData<String> mJSONToken;
    //TODO Hold JSON token or other stuff here too

    public ConnectionViewModel(@NonNull Application application) {
        super(application);

        mIsConnected = new MutableLiveData<>();
        mIsConnected.setValue(false);

        mJSONToken = new MutableLiveData<>();

        connectTest();
    }

    public void addResponseObserver(@NonNull LifecycleOwner owner,
                                    @NonNull Observer<? super Boolean> observer) {
        mIsConnected.observe(owner, observer);
    }

    public void connectTest() {
        String url = getApplication().getString(R.string.url_webservices);

        Request request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                response -> {
                    Log.v("Connection", "Successfully connected.");
                    mIsConnected.setValue(true);
                },
                response -> {
                    Log.e("Connection", "No Connection.");
                    mIsConnected.setValue(false);
                }
        );
        request.setRetryPolicy(new DefaultRetryPolicy(
                10_000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(getApplication().getApplicationContext())
                .add(request);
    }

    public boolean getIsConnected() {
        return mIsConnected.getValue();
    }
    public String getJSONToken() {
        return mJSONToken.getValue();
    }

    public boolean testJSONToken() {
        return true; //TODO
    }
}
