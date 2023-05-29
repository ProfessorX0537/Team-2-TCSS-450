package com.example.chatapp.ui.main.home;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import java.util.ArrayList;
import java.util.List;

public class HomeMessagesItemViewModel extends AndroidViewModel {

    public MutableLiveData<ArrayList<HomeMessagesItem>> mHomeMessageList;

    public int mChatRoomIdNavigate = -1;

    public HomeMessagesItemViewModel(@NonNull Application application) {
        super(application);
        mHomeMessageList = new MutableLiveData<>();
        mHomeMessageList.setValue(new ArrayList<>());
    }

    public void addHomeMessageObserver(@NonNull LifecycleOwner owner,
                                       @NonNull Observer<? super List<HomeMessagesItem>> observer) {
        mHomeMessageList.observe(owner, observer);
    }

    public void setHomeMessageList(MutableLiveData<ArrayList<HomeMessagesItem>> mHomeMessageList) {
        this.mHomeMessageList = mHomeMessageList;
    }

    public MutableLiveData<ArrayList<HomeMessagesItem>> getHomeMessageList() {
        return mHomeMessageList;
    }

    public void deleteAllMessagesFromChatRooms(int chatId) {
        ArrayList<HomeMessagesItem> temp = mHomeMessageList.getValue();
        temp.removeIf(e -> e.getmChatId() == chatId);
        mHomeMessageList.setValue(temp);
    }
}