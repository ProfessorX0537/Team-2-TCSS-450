package com.example.chatapp.ui.main.chat.chatroom.add;

import java.io.Serializable;

public class ChatRoomAddUserItem implements Serializable {
    private final String mUsername;

    private final String mEmail; //TODO use, or we can just leave idc
//    private int mMemberId;

    public ChatRoomAddUserItem(String username, String email) {
        mUsername = username;
        mEmail = email;
    }

    public String getmUsername() {
        return mUsername;
    }
}
