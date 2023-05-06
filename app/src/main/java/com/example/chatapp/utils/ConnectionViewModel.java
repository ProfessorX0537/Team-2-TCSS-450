package com.example.chatapp.utils;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.chatapp.R;

/**
 * An Application level ViewModel that connects to webservice to check of client is connected. <br>
 */
public class ConnectionViewModel extends AndroidViewModel {
    private MutableLiveData<Boolean> mIsConnected;
    //TODO Hold JSON token or other stuff here too

    public ConnectionViewModel(@NonNull Application application) {
        super(application);
        mIsConnected.setValue(false);
        connectTest();
    }

    public void addResponseObserver(@NonNull LifecycleOwner owner,
                                    @NonNull Observer<? super Boolean> observer) {
        mIsConnected.observe(owner, observer);
    }

    public void connectTest() {
        String url = R.string.url_webservices + "auth";

        Request request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                response -> mIsConnected.setValue(true),
                response -> mIsConnected.setValue(false)
        );
    }

    public boolean isConnected() {
        return mIsConnected.getValue();
    }
}
