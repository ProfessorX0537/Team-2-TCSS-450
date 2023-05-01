package com.example.chatapp.ui.main.chat.chatlist;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import java.util.ArrayList;

public class ChatListViewModel extends AndroidViewModel {

    ArrayList<ChatListItem> mItemList;

    public ChatListViewModel(@NonNull Application application) {
        super(application);
        mItemList = new ArrayList<>(10);
    }

    public void setupItemsList() { //TODO remove for webservice
        for (int i = 0; i < 10; i++) {
            ChatListItem curr = new ChatListItem(
                    "Chat Title " + i,
                    "This is the lastest message sent in the chat room bruv.",
                    "Apr " + (10 + i),
                    i
            );
            mItemList.add(curr);
        }
    }
}
