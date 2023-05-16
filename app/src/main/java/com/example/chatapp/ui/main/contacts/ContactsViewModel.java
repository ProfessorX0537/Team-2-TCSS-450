package com.example.chatapp.ui.main.contacts;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import java.util.ArrayList;
import java.util.List;

public class ContactsViewModel extends AndroidViewModel {

    private MutableLiveData<List<ContactCard>> mContacts;

    public ContactsViewModel(@NonNull Application application) {
        super(application);
        mContacts = new MutableLiveData<>();
        mContacts.setValue(new ArrayList<>());
    }

    public void addContactsObserver(@NonNull LifecycleOwner owner,
                                    @NonNull Observer<? super List<ContactCard>> observer) {
        mContacts.observe(owner, observer);
    }

    public void setContacts(List<ContactCard> contacts){ mContacts.setValue(contacts);}

    public List<ContactCard> getContacts(){return mContacts.getValue();}


}
