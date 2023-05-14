package com.example.chatapp.ui.main.chat.chatroom;

import androidx.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * AKA ChatMessage by Charles Bryan
 */
public class ChatRoomItem  implements Serializable {

    private final int mMessageId;
    private final String mMessage;
    private final String mSender;
    private final String mTimeStamp; //TODO parse to mTimeDisplay or some

    public ChatRoomItem(int messageId, String message, String sender, String timeStamp) {
        mMessageId = messageId;
        mMessage = message;
        mSender = sender;
        mTimeStamp = timeStamp;
    }

    /**
     * Static factory method to turn a properly formatted JSON String into a
     * ChatMessage object.
     * @param cmAsJson the String to be parsed into a ChatMessage Object.
     * @return a ChatMessage Object with the details contained in the JSON String.
     * @throws JSONException when cmAsString cannot be parsed into a ChatMessage.
     */
    public static ChatRoomItem createFromJsonString(final String cmAsJson) throws JSONException {
        final JSONObject msg = new JSONObject(cmAsJson);
        return new ChatRoomItem(msg.getInt("messageid"),
                msg.getString("message"),
                msg.getString("username"),
                msg.getString("timestamp"));
    }

    public String getMessage() {
        return mMessage;
    }

    public String getSender() {
        return mSender;
    }

    public String getTimeStamp() {
        return mTimeStamp;
    }

    public int getMessageId() {
        return mMessageId;
    }

    /**
     * Provides equality solely based on MessageId.
     * @param other the other object to check for equality
     * @return true if other message ID matches this message ID, false otherwise
     */
    @Override
    public boolean equals(@Nullable Object other) {
        boolean result = false;
        if (other instanceof ChatRoomItem) {
            result = mMessageId == ((ChatRoomItem) other).mMessageId;
        }
        return result;
    }
}
