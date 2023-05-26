package com.example.chatapp.services;

import android.app.ActivityManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import org.json.JSONException;

import me.pushy.sdk.Pushy;

import static android.app.ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND;
import static android.app.ActivityManager.RunningAppProcessInfo.IMPORTANCE_VISIBLE;

import com.example.chatapp.AuthActivity;
import com.example.chatapp.R;
import com.example.chatapp.ui.main.chat.chatroom.ChatRoomItem;

public class PushReceiver extends BroadcastReceiver {

    public static final String RECEIVED_NEW_MESSAGE = "new message from pushy";
    public static final String CHATLIST_INVITE = "new ChatListInvite from pushy";
    public static final String CHATLIST_KICK = "new ChatListKick from pushy";
    public static final String CHATLIST_RENAME = "new ChatListRename from pushy";

    private static final String CHANNEL_ID = "1";

    @Override
    public void onReceive(Context context, Intent intent) {

        //the following variables are used to store the information sent from Pushy
        //In the WS, you define what gets sent. You can change it there to suit your needs
        //Then here on the Android side, decide what to do with the message you got

        //for the lab, the WS is only sending chat messages so the type will always be msg
        //for your project, the WS needs to send different types of push messages.
        //So perform logic/routing based on the "type"
        //feel free to change the key or type of values.
        String typeOfMessage = intent.getStringExtra("type");
        if (typeOfMessage.equals("msg")) { //////////////////////////////////////////////////////////////////////////////////
            ChatRoomItem message = null;
            int chatId = -1;
            try{
                message = ChatRoomItem.createFromJsonString(intent.getStringExtra("message"));
                chatId = intent.getIntExtra("chatid", -1);
            } catch (JSONException e) {
                //Web service sent us something unexpected...I can't deal with this.
                throw new IllegalStateException("Error from Web Service. Contact Dev Support");
            }

            ActivityManager.RunningAppProcessInfo appProcessInfo = new ActivityManager.RunningAppProcessInfo();
            ActivityManager.getMyMemoryState(appProcessInfo);

            if (appProcessInfo.importance == IMPORTANCE_FOREGROUND || appProcessInfo.importance == IMPORTANCE_VISIBLE) {
                //app is in the foreground so send the message to the active Activities
                Log.d("PUSHY", "Message received in foreground: " + message);

                //create an Intent to broadcast a message to other parts of the app.
                Intent i = new Intent(RECEIVED_NEW_MESSAGE);
                i.putExtra("chatMessage", message);
                i.putExtra("chatId", chatId);
                i.putExtras(intent.getExtras());

                context.sendBroadcast(i);

            } else {
                //app is in the background so create and post a notification
                Log.d("PUSHY", "Message received in background: " + message.getMessage());

                Intent i = new Intent(context, AuthActivity.class);
                i.putExtras(intent.getExtras());
                i.putExtra("chatId", chatId);

                PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
                        i, PendingIntent.FLAG_UPDATE_CURRENT);

                //research more on notifications the how to display them
                //https://developer.android.com/guide/topics/ui/notifiers/notifications
                NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                        .setAutoCancel(true)
                        .setSmallIcon(R.drawable.ic_chat_black_24dp)
                        .setContentTitle("Message from: " + message.getSender())
                        .setContentText(message.getMessage())
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setContentIntent(pendingIntent);

                // Automatically configure a ChatMessageNotification Channel for devices running Android O+
                Pushy.setNotificationChannel(builder, context);

                // Get an instance of the NotificationManager service
                NotificationManager notificationManager =
                        (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);

                // Build the notification and display it
                notificationManager.notify(1, builder.build());
            }
        } else if (typeOfMessage.equals("ChatListInvite")) {  //////////////////////////////////////////////////////////////////////////////////
            //parse
            int chatId = intent.getIntExtra("chatId", -1);
            String username = intent.getStringExtra("username");
            String chatRoomName = intent.getStringExtra("chatRoomName");

            ActivityManager.RunningAppProcessInfo appProcessInfo = new ActivityManager.RunningAppProcessInfo();
            ActivityManager.getMyMemoryState(appProcessInfo);
            //if in foreground
            if (appProcessInfo.importance == IMPORTANCE_FOREGROUND || appProcessInfo.importance == IMPORTANCE_VISIBLE) {
                //app is in the foreground so send to MainActivity PushyService
                Log.d("PUSHY", "ChatListInvite received in foreground, chatId: " + chatId + " for username: " + username);
                //create an Intent to broadcast a message to other parts of the app.
                Intent i = new Intent(CHATLIST_INVITE);
                i.putExtra("chatId", chatId);
                i.putExtra("username", username);
                i.putExtra("chatRoomName", chatRoomName);
                //send
                context.sendBroadcast(i);
            } else {
                //else if off app, send notif
                //TODO if not EC
            }
        } else if (typeOfMessage.equals("ChatListKick")) {  //////////////////////////////////////////////////////////////////////////////////
            //parse
            int chatId = intent.getIntExtra("chatId", -1);
            String username = intent.getStringExtra("username");
            String chatRoomName = intent.getStringExtra("chatRoomName");

            ActivityManager.RunningAppProcessInfo appProcessInfo = new ActivityManager.RunningAppProcessInfo();
            ActivityManager.getMyMemoryState(appProcessInfo);
            //if in foreground
            if (appProcessInfo.importance == IMPORTANCE_FOREGROUND || appProcessInfo.importance == IMPORTANCE_VISIBLE) {
                //app is in the foreground so send to MainActivity PushyService
                Log.d("PUSHY", "ChatListKick received in foreground, chatId: " + chatId + " for username: " + username);
                //create an Intent to broadcast a message to other parts of the app.
                Intent i = new Intent(CHATLIST_KICK);
                i.putExtra("chatId", chatId);
                i.putExtra("username", username);
                i.putExtra("chatRoomName", chatRoomName);
                //send
                context.sendBroadcast(i);
            } else {
                //else if off app, send notif
                //TODO if not EC
            }
        } else if (typeOfMessage.equals("ChatListRename")) {  //////////////////////////////////////////////////////////////////////////////////
            //parse
            int chatId = intent.getIntExtra("chatId", -1);
            String chatRoomName = intent.getStringExtra("chatRoomName");

            ActivityManager.RunningAppProcessInfo appProcessInfo = new ActivityManager.RunningAppProcessInfo();
            ActivityManager.getMyMemoryState(appProcessInfo);
            //if in foreground
            if (appProcessInfo.importance == IMPORTANCE_FOREGROUND || appProcessInfo.importance == IMPORTANCE_VISIBLE) {
                //app is in the foreground so send to MainActivity PushyService
                Log.d("PUSHY", "ChatListRename received in foreground, chatId: " + chatId + " to: " + chatRoomName);

                //create an Intent to broadcast a message to other parts of the app.
                Intent i = new Intent(CHATLIST_RENAME);
                i.putExtra("chatId", chatId);
                i.putExtra("chatRoomName", chatRoomName);
                //send
                context.sendBroadcast(i);
            } else {
                //else if off app, send notif
                //TODO if not EC
            }
        }
    }
}
