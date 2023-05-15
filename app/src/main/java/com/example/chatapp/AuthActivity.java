package com.example.chatapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import com.example.chatapp.model.PushyTokenViewModel;

import me.pushy.sdk.Pushy;

public class AuthActivity extends AppCompatActivity {
    private boolean isConnectedWS = false;
    private boolean isConnectedPushy = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_auth);

        //Pushy
        Pushy.listen(this); //If it is not already running, start the Pushy listening service
        new ViewModelProvider(this).get(PushyTokenViewModel.class).retrieveToken(); //Creates PushyTokenViewModel
    }

    public boolean isConnectedWS() {
        return isConnectedWS;
    }

    public void setConnectedWS(boolean connectedWS) {
        isConnectedWS = connectedWS;
    }

    public boolean isConnectedPushy() {
        return isConnectedPushy;
    }

    public void setConnectedPushy(boolean connectedPushy) {
        isConnectedPushy = connectedPushy;
    }
}