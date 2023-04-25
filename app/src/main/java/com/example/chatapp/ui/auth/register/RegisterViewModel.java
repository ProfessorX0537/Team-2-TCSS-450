package com.example.chatapp.ui.auth.register;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import org.json.JSONObject;

/**
 * View model to store information necessary to complete the registration process.
 * @author Xavier Hines
 */
public class RegisterViewModel extends AndroidViewModel {

    //This may need to change depending on our server
    private MutableLiveData<JSONObject> mResponse;

    public RegisterViewModel(@NonNull Application application) {
        super(application);
    }


}
