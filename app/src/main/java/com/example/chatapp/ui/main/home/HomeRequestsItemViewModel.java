package com.example.chatapp.ui.main.home;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class HomeRequestsItemViewModel extends AndroidViewModel {

    public MutableLiveData<ArrayList<HomeRequestsItem>> mHomeRequestList;
    public HomeRequestsItemViewModel(@NonNull Application application) {
        super(application);
        mHomeRequestList = new MutableLiveData<>();
        mHomeRequestList.setValue(new ArrayList<>());
    }

    public void addHomeRequestObserver(@NonNull LifecycleOwner owner,
                                       @NonNull Observer<? super List<HomeRequestsItem>> observer) {
        mHomeRequestList.observe(owner, observer);
    }

    public void setHomeRequestList(MutableLiveData<ArrayList<HomeRequestsItem>> homeRequestList) {
        this.mHomeRequestList = homeRequestList;
    }

    public MutableLiveData<ArrayList<HomeRequestsItem>> getHomeRequestList() {
        return mHomeRequestList;
    }
}