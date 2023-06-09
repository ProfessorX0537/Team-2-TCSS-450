package com.example.chatapp.ui.main.contacts;

import android.app.Application;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;

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
import com.example.chatapp.databinding.FragmentContactsBinding;
import com.example.chatapp.io.RequestQueueSingleton;
import com.example.chatapp.ui.main.chat.chatroom.ChatRoomItem;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import me.pushy.sdk.lib.jackson.core.JsonParser;

public class ContactsViewModel extends AndroidViewModel {

    private MutableLiveData<List<ContactCard>> mContacts;
    public MutableLiveData<JSONObject> mAddedContactResponse;

    public ContactsViewModel(@NonNull Application application) {
        super(application);
        mContacts = new MutableLiveData<>();
        mContacts.setValue(new ArrayList<>());
        mAddedContactResponse = new MutableLiveData<>();
        mAddedContactResponse.setValue(null);
    }

    public void addContactsObserver(@NonNull LifecycleOwner owner,
                                    @NonNull Observer<? super List<ContactCard>> observer) {
        mContacts.observe(owner, observer);
    }


    // This method is called whenever the user accepts a pending contact request
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

    // This is the method that is called when the user clicks the "X" button on a contact card
    public void connectReject(final int memberid_a, final int memberid_b){
        String url = getApplication().getResources().getString(R.string.url_webservices) +
                "connections?MemberID_A=" + memberid_a+"&MemberID_B="+memberid_b;

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

    // This method is called whenever the user navigates to contacts fragment to reload the most recent contacts array
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

    // This method creates the Contact card objects from the JSON response made by connectGet
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
                        .addIncoming(message.getBoolean("incoming"))
                        .addOutgoing(message.getBoolean("outgoing"))
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

    // This is the method that is called when the user long clicks and deletes a contact card
    public void connectDelete(final int memberid_a, final int memberid_b){
        String url = getApplication().getResources().getString(R.string.url_webservices) +
                "connections?MemberID_A=" + memberid_a+"&MemberID_B="+memberid_b;

        Request request = new JsonObjectRequest(Request.Method.DELETE, url, null, null, this::handleError);

        request.setRetryPolicy(new DefaultRetryPolicy(
                10_000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //Instantiate the RequestQueue and add the request to the queue
        RequestQueueSingleton.getInstance(getApplication().getApplicationContext())
                .addToRequestQueue(request);

    }


    // This is the method that is called when the user adds a user through the FAB
    public void connectAdd(final int memberid_a, final String input, View view) {
        String url = getApplication().getResources().getString(R.string.url_webservices) +
                "connections?input=" + input+"&MemberID_A="+memberid_a;





        Request request = new JsonObjectRequest(Request.Method.POST, url, null, this::addContact, error ->{
            try {
                handleAddError(error, view);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });

        request.setRetryPolicy(new DefaultRetryPolicy(
                10_000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //Instantiate the RequestQueue and add the request to the queue
        RequestQueueSingleton.getInstance(getApplication().getApplicationContext())
                .addToRequestQueue(request);

    }

    // This method adds a contact to the contacts array at the end
    private void addContact(final JSONObject response){

        try {




            JSONObject contactJson = response.getJSONObject("contact");

            ContactCard contact = new ContactCard.Builder(contactJson.getString("firstname")+" "+contactJson.getString("lastname"))
                    .addNick(contactJson.getString("username"))
                    .addEmail(contactJson.getString("email"))
                    .addAccepted(false)
                    .addMemberID(contactJson.getInt("memberid"))
                    .addIncoming(false)
                    .addOutgoing(true)
                    .build();

            List<ContactCard> contacts = mContacts.getValue();
            contacts.add(0,contact);
            mContacts.setValue(contacts);
        }
        catch (JSONException e) {
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

    private void handleAddError(final VolleyError error, View view) throws JSONException {
        String data;
        if (Objects.isNull(error.networkResponse)) {
            Log.e("NETWORK ERROR", error.getMessage());
        } else {
            data = new String(error.networkResponse.data, Charset.defaultCharset());
            Log.e("CLIENT ERROR",
                    error.networkResponse.statusCode +
                            " " +
                            data);


            JSONObject jsonObject = new JSONObject(data);


            Snackbar snackbar = Snackbar.make(view, jsonObject.getString("message"), Snackbar.LENGTH_SHORT);
            View view1 = snackbar.getView();
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) view1.getLayoutParams();
            view1.setLayoutParams(params);
            snackbar.show();

        }


    }

    // This method is used to set the contacts array
    public void setContacts(List<ContactCard> contacts){ mContacts.setValue(contacts);}

    // This method removes a contact a specific index in the contacts array
    public void removeContact(int position){
        List<ContactCard> contacts = mContacts.getValue();
        contacts.remove(position);
        mContacts.setValue(contacts);
    }




    // This method returns the contacts array
    public List<ContactCard> getContacts(){return mContacts.getValue();}


}
