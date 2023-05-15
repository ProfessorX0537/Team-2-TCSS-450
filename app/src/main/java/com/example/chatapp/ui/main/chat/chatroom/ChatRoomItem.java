package com.example.chatapp.ui.main.chat.chatroom;

import java.io.Serializable;

public class ChatRoomItem  implements Serializable {
    private final String mSenderName;
    private final boolean mIsFromOther; //TODO find who sent it automatically from name
    private final String mMessage;
    private final String mDate; //TODO Date object

    public ChatRoomItem(String mSenderName, boolean mIsFromOther, String mMessage, String mDate) {
        this.mSenderName = mSenderName;
        this.mIsFromOther = mIsFromOther;
        this.mMessage = mMessage;
        this.mDate = mDate;
    }

    public String getmSenderName() {
        return mSenderName;
    }

    public boolean ismIsFromOther() {
        return mIsFromOther;
    }

    public String getmMessage() {
        return mMessage;
    }

    public String getmDate() {
        return mDate; //TODO parse Date to String for view
    }
}
