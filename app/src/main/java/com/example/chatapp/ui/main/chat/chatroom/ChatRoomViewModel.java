package com.example.chatapp.ui.main.chat.chatroom;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.chatapp.ui.main.chat.chatlist.ChatListItem;
import com.example.chatapp.ui.main.chat.chatlist.ChatListViewModel;

import java.util.ArrayList;

public class ChatRoomViewModel extends AndroidViewModel {
    ArrayList<ChatRoomItem> mItemList;

    public ChatRoomViewModel(@NonNull Application application) {
        super(application);
        mItemList = new ArrayList<>(10);
    }

    public void setupItemsList() { //TODO remove for webservice
        for (int i = 0; i < 3; i++) {
            ChatRoomItem curr = new ChatRoomItem(
                    "Joe Who",
                    true,
                    "This is a test text message from Joe" + i + ".",
                    "4:" + (20+i) +" PM"
            );
            mItemList.add(curr);
        }
        mItemList.add(new ChatRoomItem(
                "Dav",
                false,
                "Hi Joe, shut up and stop spamming before I perma block you. I'll also report you to the dev team and get you kicked off of the service.",
                "5:00"
        ));
        mItemList.add(new ChatRoomItem(
                "Joe Who",
                true,
                "\uD83D\uDE2D",
                "5:08"
        ));
    }
}
