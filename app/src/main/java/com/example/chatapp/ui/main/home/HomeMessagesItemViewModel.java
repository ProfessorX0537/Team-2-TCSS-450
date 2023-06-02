package com.example.chatapp.ui.main.home;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.example.chatapp.utils.Storage;

import java.util.ArrayList;
import java.util.List;

public class HomeMessagesItemViewModel extends AndroidViewModel {
    private static final String HOMEMESSAGE_FILE = "HomeMessagesItem";

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

    public void save(Context ctx) {
        Storage.saveSerializable(HOMEMESSAGE_FILE, mHomeMessageList.getValue(), ctx);
    }

    public void tryLoad(Context ctx) {
        try {
            ArrayList<HomeMessagesItem> list = (ArrayList<HomeMessagesItem>) Storage.loadSerializable(HOMEMESSAGE_FILE, ctx);
            if (list != null) {
                mHomeMessageList.setValue(list);
            }
        } catch (Exception e) {
            Log.e("HomeMessagesItemViewModel", "tryLoad: FAILED");
        }
    }
}