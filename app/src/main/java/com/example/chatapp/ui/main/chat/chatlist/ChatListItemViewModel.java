package com.example.chatapp.ui.main.chat.chatlist;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.chatapp.R;
import com.example.chatapp.io.RequestQueueSingleton;
import com.example.chatapp.model.UserInfoViewModel;
import com.example.chatapp.ui.main.chat.chatroom.ChatRoomItem;
import com.example.chatapp.utils.SimpleDate;

import org.json.JSONArray;
import org.json.JSONObject;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ChatListItemViewModel extends AndroidViewModel {

    MutableLiveData<ArrayList<ChatListItem>> mItemList;

    public ChatListItemViewModel(@NonNull Application application) {
        super(application);
        mItemList = new MutableLiveData<>();
    }

    public void addItemListObserver(@NonNull LifecycleOwner owner,
                                 @NonNull Observer<? super ArrayList> observer) {
        mItemList.observe(owner, observer);
    }

    public void getChatRooms(int memberID, String jwt) {
        String url = getApplication().getResources().getString(R.string.url_webservices) +
                "mychats/" + memberID;

        Request request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                this::handleSuccess,
                this::handleError) {

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                // add headers <key,value>
                headers.put("Authorization", jwt);
                return headers;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(
                10_000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //Instantiate the RequestQueue and add the request to the queue
        RequestQueueSingleton.getInstance(getApplication().getApplicationContext())
                .addToRequestQueue(request);
    }

    private void handleSuccess(final JSONObject response) {
        try {
            int rowCount = response.getInt("rowCount");
            JSONArray rows = response.getJSONArray("result");
            ArrayList temp = new ArrayList<>(rowCount);
            for (int i = 0; i < rowCount; i++) {
                JSONObject curr = rows.getJSONObject(i);
                Log.d("ChatListItemViewModel", "curr: " + curr.toString());
                if (!curr.isNull("message")) { //where latest message exists
                    Log.d("ChatListItemViewModel", "curr's latest is not empty ");
                    temp.add(new ChatListItem(
                            curr.getString("name"),
                            curr.getString("username") + ": " + curr.getString("message"),
                            SimpleDate.stringDateFromEpochString(curr.getString("timestampraw")),
                            0,
                            curr.getInt("countmembers"),
                            curr.getInt("chatid")
                    ));
                } else { //empty chatroom
                    Log.d("ChatListItemViewModel", "curr's latest is empty ");
                    temp.add(new ChatListItem(
                            curr.getString("name"),
                            "(Empty Chat)", //TODO String
                            "N/A", //TODO String
                            0,
                            curr.getInt("countmembers"),
                            curr.getInt("chatid")
                    ));
                }

            }
            mItemList.setValue(temp);
            Log.v("ChatListItemViewModel", "mItemList: " + mItemList.getValue().toString());
        } catch (Exception e) {
            Log.e("ChatListItemViewModel", "Couldn't handle success");
            e.printStackTrace();
        }
    }

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

//    public void setupItemsList() { //TODO remove for webservice
//        for (int i = 0; i < 10; i++) {
//            ChatListItem curr = new ChatListItem(
//                    "Chat Title " + i,
//                    "This is the lastest message sent in the chat room bruv.",
//                    "Apr " + (10 + i),
//                    i
//            );
//            mItemList.add(curr);
//        }
//    }
}
