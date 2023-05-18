package com.example.chatapp.ui.main.contacts;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.chatapp.R;
import com.example.chatapp.io.RequestQueueSingleton;
import com.example.chatapp.ui.main.chat.chatroom.ChatRoomItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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

    public void connectAccept(final int memberid_a, final int memberid_b){
        String url = getApplication().getResources().getString(R.string.url_webservices) +
                "connections?MemberID_A=" + memberid_b+"&MemberID_B="+memberid_a;

        Request request = new JsonObjectRequest(Request.Method.PUT, url, null, null, this::handleError);


        request.setRetryPolicy(new DefaultRetryPolicy(
                10_000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //Instantiate the RequestQueue and add the request to the queue
        RequestQueueSingleton.getInstance(getApplication().getApplicationContext())
                .addToRequestQueue(request);

        //code here will run

    }
    public void connectReject(final int memberid_a, final String username){
        String url = getApplication().getResources().getString(R.string.url_webservices) +
                "connections?MemberID_A=" + memberid_a+"&username="+username;

        Request request = new JsonObjectRequest(Request.Method.DELETE, url, null, null, this::handleError);

        request.setRetryPolicy(new DefaultRetryPolicy(
                10_000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //Instantiate the RequestQueue and add the request to the queue
        RequestQueueSingleton.getInstance(getApplication().getApplicationContext())
                .addToRequestQueue(request);

        //code here will run

    }

    public void connectGet(final int memberid) {
        String url = getApplication().getResources().getString(R.string.url_webservices) +
                "connections?MemberID=" + memberid;

        Request request = new JsonObjectRequest(Request.Method.GET, url, null, this::handleGetResult, this::handleError);

        request.setRetryPolicy(new DefaultRetryPolicy(
                10_000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //Instantiate the RequestQueue and add the request to the queue
        RequestQueueSingleton.getInstance(getApplication().getApplicationContext())
                .addToRequestQueue(request);

        //code here will run
    }

    private void handleGetResult(final JSONObject response) {
        Log.i("JSON result", response.toString());
        List<ContactCard> list = new ArrayList<>();

        try {

            JSONArray messages = response.getJSONArray("rows");
            for(int i = 0; i < messages.length(); i++) {
                JSONObject message = messages.getJSONObject(i);
                ContactCard contact = new ContactCard.Builder(message.getString("firstname")+" "+message.getString("lastname"))
                        .addNick(message.getString("username"))
                        .addEmail(message.getString("email"))
                        .addAccepted(message.getBoolean("accepted"))
                        .addMemberID(message.getInt("memberid"))
                        .build();

                list.add(contact);


            }
            //inform observers of the change (setValue)
            setContacts(list);
        }catch (JSONException e) {
            Log.e("JSON PARSE ERROR", "Found in handle Success ContactViewModel");
            Log.e("JSON PARSE ERROR", "Error: " + e.getMessage());
        }
    }

    public void connectDelete(final int memberid_a, final String username) {
        String url = getApplication().getResources().getString(R.string.url_webservices) +
                "connections?MemberID_A=" + memberid_a+"&username="+username;

        Request request = new JsonObjectRequest(Request.Method.DELETE, url, null, null, this::handleError);

        request.setRetryPolicy(new DefaultRetryPolicy(
                10_000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //Instantiate the RequestQueue and add the request to the queue
        RequestQueueSingleton.getInstance(getApplication().getApplicationContext())
                .addToRequestQueue(request);

        //code here will run
    }


    public void connectAdd(final int memberid_a, final String input) {
        String url = getApplication().getResources().getString(R.string.url_webservices) +
                "connections?input=" + input+"&MemberID_A="+memberid_a;

        Request request = new JsonObjectRequest(Request.Method.POST, url, null, null, this::handleError);

        request.setRetryPolicy(new DefaultRetryPolicy(
                10_000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //Instantiate the RequestQueue and add the request to the queue
        RequestQueueSingleton.getInstance(getApplication().getApplicationContext())
                .addToRequestQueue(request);

        //code here will run
    }

    private void handleAddResult(final JSONObject response) {
        try {
            addContact(new ContactCard.Builder(response.getString("success")).build());
            Log.i("JSON result", response.toString());
        }catch (JSONException e) {
            Log.e("JSON PARSE ERROR", "Found in handle Success ContactViewModel");
            Log.e("JSON PARSE ERROR", "Error: " + e.getMessage());
        }
    }

//    private void handleDeleteResult(final JSONObject response) {
//        try {
//            Log.i("JSON result", response.toString());
//
//
//            //inform observers of the change (setValue)
//
//
//        }catch (JSONException e) {
//            Log.e("JSON PARSE ERROR", "Found in handle Success ContactViewModel");
//            Log.e("JSON PARSE ERROR", "Error: " + e.getMessage());
//        }
//    }


    private void handleError(final VolleyError error) {
        if (Objects.isNull(error.networkResponse)) {
            Log.e("NETWORK ERROR", error.getMessage());
        }
        else {
            String data = new String(error.networkResponse.data, Charset.defaultCharset());
            Log.e("CLIENT ERROR",
                    error.networkResponse.statusCode +
                            " " +
                            data);
        }
    }
    public void setContacts(List<ContactCard> contacts){ mContacts.setValue(contacts);}
    public void removeContact(int position){
        List<ContactCard> contacts = mContacts.getValue();
        contacts.remove(position);
        mContacts.setValue(contacts);
    }

    public void addContact(ContactCard contact){
        List<ContactCard> contacts = mContacts.getValue();
        contacts.add(contact);
        mContacts.setValue(contacts);
    }

    public List<ContactCard> getContacts(){return mContacts.getValue();}


}
