package com.example.chatapp.ui.main.home;


import androidx.fragment.app.Fragment;

import java.io.Serializable;


public class HomeMessagesItem extends Fragment implements Serializable {

    private final int mMessageId;

    private final String mMessage;

    private final String mSender;

    private final String mTimeStamp;

    //save chat id
    private final int mChatId;



    public HomeMessagesItem(int messageId, String message, String sender, String timeStamp, int chatId) {
        mMessageId = messageId;
        mMessage = message;
        mSender = sender;
        mTimeStamp = timeStamp;
        mChatId = chatId;
    }

    public String getMessage() {
        return mMessage;
    }

    public String getSender() {
        return mSender;
    }

    public String getTimeStamp() {
        return mTimeStamp;
    }

    public int getMessageId() {
        return mMessageId;
    }

    public int getmChatId() {
        return mChatId;
    }
}