package com.example.chatapp.ui.main.home;

import androidx.fragment.app.Fragment;

public class HomeRequestsItem extends Fragment {


    private final String mSender;



    public HomeRequestsItem(String sender) {
        this.mSender = sender;
    }

    public String getSender() {
        return mSender;
    }

}