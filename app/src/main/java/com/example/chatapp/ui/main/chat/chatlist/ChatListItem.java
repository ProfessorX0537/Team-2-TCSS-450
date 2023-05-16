package com.example.chatapp.ui.main.chat.chatlist;

import java.io.Serializable;

public class ChatListItem implements Serializable  {
    private final String mRoomName;
    private final String mLatestMessage;
    private final String mLatestDate;
    private final int mNotifCount;
    private final int mRoomID;

    public ChatListItem(String mRoomName, String mLatestMessage, String mLatestDate, int mNotifCount, int mRoomID) {
        //TODO parse data & format
        this.mRoomName = mRoomName;
        this.mLatestMessage = mLatestMessage;
        this.mLatestDate = mLatestDate;
        this.mNotifCount = mNotifCount;
        this.mRoomID = mRoomID;
    }

    public String getmRoomName() {
        return mRoomName;
    }

    public String getmLatestMessage() {
        return mLatestMessage;
    }

    public String getmLatestDate() {
        return mLatestDate;
    }

    public int getmNotifCount() {
        return mNotifCount;
    }
}
