package com.example.chatapp.ui.main.home;

import androidx.fragment.app.Fragment;

public class HomeRequestsItem extends Fragment {


    private final String mSender;

    private final String mTimeStamp;


    public HomeRequestsItem(String sender, String timeStamp) {
        this.mSender = sender;
        this.mTimeStamp = timeStamp;
    }

    public String getSender() {
        return mSender;
    }

    public String getTimeStamp() {
        return mTimeStamp;
    }
}