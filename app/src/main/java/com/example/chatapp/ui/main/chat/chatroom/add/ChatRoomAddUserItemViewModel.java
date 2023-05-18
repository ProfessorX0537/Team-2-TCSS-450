package com.example.chatapp.ui.main.chat.chatroom.add;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.chatapp.R;
import com.example.chatapp.io.RequestQueueSingleton;
import com.example.chatapp.model.UserInfoViewModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ChatRoomAddUserItemViewModel extends AndroidViewModel {

    MutableLiveData<ArrayList<ChatRoomAddUserItem>> mItemList;
    public UserInfoViewModel userinfo; //REQUIRED TODO factory

    public ChatRoomAddUserItemViewModel(@NonNull Application application) {
        super(application);
        mItemList = new MutableLiveData<>();
    }

    public void addItemListObserver(@NonNull LifecycleOwner owner,
                                    @NonNull Observer<? super ArrayList> observer) {
        mItemList.observe(owner, observer);
    }

    //async
    public void getUsersInChat(int chatId, String jwt) {
        String url = getApplication().getResources().getString(R.string.url_webservices) +
                "chats/" + chatId;

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
            JSONArray rows = response.getJSONArray("rows");
            ArrayList temp = new ArrayList<>(rowCount);
            for (int i = 0; i < rowCount; i++) {
                JSONObject curr = rows.getJSONObject(i);
                //skip if same as user
                if (userinfo.getUsername().equals(curr.getString("username"))) continue;
                //otherwise add
                temp.add(new ChatRoomAddUserItem(
                        curr.getString("username"),
                        curr.getString("email")
                ));
            }
            mItemList.setValue(temp);
            Log.v("ChatRoomAddUserViewModel", "mItemList: " + mItemList.getValue().toString());
        } catch (Exception e) {
            Log.e("ChatRoomAddUserViewModel", "Couldn't handle success:\n" + e.getMessage());
        }
    }

    private void handleError(final VolleyError error) {
        try {
            if (Objects.isNull(error.networkResponse)) {
                Log.e("NETWORK ERROR", error.getMessage());
            } else {
                String data = new String(error.networkResponse.data, Charset.defaultCharset());
                Log.e("CLIENT ERROR",
                        error.networkResponse.statusCode +
                                " " +
                                data);
            }
        } catch (Exception e) {
            //TODO wth
            Log.e("ERROR ERROR", e.getMessage());
        }
    }
}
