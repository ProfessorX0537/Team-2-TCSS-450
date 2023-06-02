package com.example.chatapp.model;

import static com.example.chatapp.utils.Storage.loadSerializable;
import static com.example.chatapp.utils.Storage.saveSerializable;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import java.util.HashMap;
import java.util.Map;

public class NewMessageCountViewModel extends ViewModel {
    private static final String NEWMESSAGE_FILE = "NewMessageCountViewModelFiles";
    public MutableLiveData<Integer> mNewTotalMessageCount;

    /**
     * chatId, NewMessageCount
     */
    public MutableLiveData<HashMap<Integer, Integer>> mNewHashMapMessageCount; //TODO save this to storage

    public NewMessageCountViewModel() {
        mNewTotalMessageCount = new MutableLiveData<>();
        mNewTotalMessageCount.setValue(0);

        mNewHashMapMessageCount = new MutableLiveData<>();
        mNewHashMapMessageCount.setValue(new HashMap<>());
    }

    public void addNewTotalMessageCountObserver(@NonNull LifecycleOwner owner,
                                                @NonNull Observer<? super Integer> observer) {
        mNewTotalMessageCount.observe(owner, observer);
    }

    public void addNewHashMapMessageCountObserver(@NonNull LifecycleOwner owner,
                                                  @NonNull Observer<? super HashMap<Integer, Integer>> observer) {
        mNewHashMapMessageCount.observe(owner, observer);
    }

    public void incrementFromChatId(int chatId) {
        mNewTotalMessageCount.setValue(mNewTotalMessageCount.getValue() + 1);

        HashMap<Integer, Integer> map = mNewHashMapMessageCount.getValue();
        assert map != null;
        if (map.containsKey(chatId)) {
            map.replace(chatId, map.get(chatId) + 1);
        } else {
            map.put(chatId, 1);
        }

        //stupid observers
        mNewHashMapMessageCount.setValue(map);
    }

//    public void reset() {
//        mNewTotalMessageCount.setValue(0);
//    }

    public void decrementFromChatId(int chatId) {
        HashMap<Integer, Integer> map = mNewHashMapMessageCount.getValue();
        assert map != null;

        if (map.containsKey(chatId)) {
            mNewTotalMessageCount.setValue(mNewTotalMessageCount.getValue() - map.get(chatId));
            Log.i("NewMessageCountViewModel", "decremented " + map.get(chatId));
            map.remove(chatId);
        } else {
            Log.i("NewMessageCountViewModel", "decremented nothing");
        }

        //stupid observers
        mNewHashMapMessageCount.setValue(map);
    }

    public void save(Context ctx) {
        saveSerializable(NEWMESSAGE_FILE, mNewHashMapMessageCount.getValue(), ctx);
    }

    public void tryLoad(Context ctx) {
        try {
            HashMap<Integer, Integer> hashMap = (HashMap<Integer, Integer>) loadSerializable(NEWMESSAGE_FILE, ctx);
            if (hashMap != null) {
                mNewHashMapMessageCount.setValue(hashMap);

                int count = 0;
                for (Map.Entry<Integer, Integer> set : mNewHashMapMessageCount.getValue().entrySet()) {
                    count += set.getValue();
                }
                mNewTotalMessageCount.setValue(count);
            }
        } catch (Exception e) {
            Log.d("NewMessageCountViewModel", "Failed Loading null");
        }
    }
}
