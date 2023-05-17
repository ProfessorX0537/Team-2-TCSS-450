package com.example.chatapp.ui.main.chat.chatroom.add;

import android.app.AlertDialog;
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

import org.json.JSONObject;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ChatRoomAddUserRequestsViewModel extends AndroidViewModel {
    private MutableLiveData<JSONObject> mRemoveFromChatResponse;
    private MutableLiveData<JSONObject> mRemoveSelfFromChatResponse;
    private MutableLiveData<JSONObject> mAddToChatResponse;

    public ChatRoomAddUserRequestsViewModel(@NonNull Application application) {
        super(application);
        mRemoveFromChatResponse = new MutableLiveData<>();
        mRemoveSelfFromChatResponse = new MutableLiveData<>();
        mAddToChatResponse = new MutableLiveData<>();
    }

    ////////OBSERVERS////////
    public void addRemoveFromChatResponseObserver(@NonNull LifecycleOwner owner, @NonNull Observer<? super JSONObject> observer) {
        mRemoveFromChatResponse.observe(owner, observer);
    }
    public void addRemoveSelfFromChatResponseObserver(@NonNull LifecycleOwner owner, @NonNull Observer<? super JSONObject> observer) {
        mRemoveSelfFromChatResponse.observe(owner, observer);
    }
    public void addAddToChatResponseObserver(@NonNull LifecycleOwner owner, @NonNull Observer<? super JSONObject> observer) {
        mAddToChatResponse.observe(owner, observer);
    }

    ////////REQUESTS (async)/////////
    public void requestRemoveFromChat(int chatId, String username, String jwt) {
        String url = getApplication().getResources().getString(R.string.url_webservices) +
                "chats/" + chatId + '/' + username;

        Request request = new JsonObjectRequest(
                Request.Method.DELETE,
                url,
                null,
                response -> mRemoveFromChatResponse.setValue(response),
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
    public void requestRemoveSelfFromChat(int chatId, UserInfoViewModel userinfo) {
        String url = getApplication().getResources().getString(R.string.url_webservices) +
                "chats/" + chatId + '/' + userinfo.getUsername();

        Request request = new JsonObjectRequest(
                Request.Method.DELETE,
                url,
                null,
                response -> mRemoveSelfFromChatResponse.setValue(response),
                this::handleError) {

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                // add headers <key,value>
                headers.put("Authorization", userinfo.getJwt());
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


    public void requestAddToChat(int chatId, String username, String jwt) {
        String url = getApplication().getResources().getString(R.string.url_webservices) +
                "chats/" + chatId + '/' + username;

        Request request = new JsonObjectRequest(
                Request.Method.PUT,
                url,
                null,
                response -> mAddToChatResponse.setValue(response),
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

    private void handleError(final VolleyError error) {
        if (Objects.isNull(error.networkResponse)) {
            Log.e("NETWORK ERROR", error.getMessage());
        } else {
            String data = new String(error.networkResponse.data, Charset.defaultCharset());
            Log.e("CLIENT ERROR",
                    error.networkResponse.statusCode +
                            " " +
                            data);
            try {
                mAddToChatResponse.setValue(new JSONObject(data));
            } catch (Exception e) {
                Log.e("ChatRoomAddUserRequestsViewModel", "handleError: couldn't make JSONObject out of data.");
            }

        }
    }

    public void clearResponses() {
        mRemoveSelfFromChatResponse.setValue(null);
        mAddToChatResponse.setValue(null);
        mRemoveFromChatResponse.setValue(null);
    }
}
