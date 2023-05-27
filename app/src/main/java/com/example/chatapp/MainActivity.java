package com.example.chatapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.chatapp.databinding.ActivityMainBinding;
import com.example.chatapp.model.NewMessageCountViewModel;
import com.example.chatapp.model.PushyTokenViewModel;
import com.example.chatapp.model.UserInfoViewModel;
import com.example.chatapp.services.PushReceiver;
import com.example.chatapp.ui.main.chat.chatlist.ChatListItemViewModel;
import com.example.chatapp.ui.main.chat.chatroom.ChatRoomItem;
import com.example.chatapp.ui.main.chat.chatroom.ChatRoomItemsViewModel;
import com.example.chatapp.ui.main.chat.chatroom.add.ChatRoomAddUserItemViewModel;
import com.example.chatapp.ui.main.home.HomeMessagesItem;
import com.example.chatapp.ui.main.home.HomeMessagesItemViewModel;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private AppBarConfiguration mAppBarConfiguration;
    private NewMessageCountViewModel mNewMessageModel;

    private ActivityMainBinding binding;

    private MainPushMessageReceiver mPushMessageReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

//        setContentView(R.layout.activity_main);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Objects.requireNonNull(getSupportActionBar()).
                setBackgroundDrawable(new ColorDrawable(Color.parseColor("#673AB7")));

        // Accepts intent from AuthActivity and gets users email and JWT
        // storing them in the UserInfoViewModel for Webservice calls that require JWT auth
        MainActivityArgs args = MainActivityArgs.fromBundle(getIntent().getExtras());
        new ViewModelProvider(this,
                new UserInfoViewModel.UserInfoViewModelFactory(args.getEmail(), args.getJwt(), args.getMemberid(), args.getUsername())
        ).get(UserInfoViewModel.class);
        Log.i("UserInfo", args.toString());

        //Bottom Nav
        BottomNavigationView navView = findViewById(R.id.nav_view);
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_weather, R.id.navigation_connections, R.id.navigation_chat)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        //ActionBar
        getSupportActionBar().setShowHideAnimationEnabled(false); //Hide goofy slide in/anim

        //NewMessageModel
        mNewMessageModel = new ViewModelProvider(this).get(NewMessageCountViewModel.class);
//        //When the user navigates to the chats page, reset the new message count.
//        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
//            if (destination.getId() == R.id.navigation_chat) {
//                //This will need some extra logic for your project as it should have
//                //multiple chat rooms.
//                //TODO dont remove badge on switch
//                mNewMessageModel.reset();
//            }
//        });
        //Observe MessageCount and create Badge
        mNewMessageModel.addNewTotalMessageCountObserver(this, count -> { //TODO TEST THIS!!!
            BadgeDrawable badge = binding.navView.getOrCreateBadge(R.id.navigation_chat);
            badge.setMaxCharacterCount(2);
            if (count > 0) {
                //new messages! update and show the notification badge.
                badge.setNumber(count);
                badge.setVisible(true);
            } else {
                //user did some action to clear the new messages, remove the badge
                badge.clearNumber();
                badge.setVisible(false);
            }
        });



    }

    /**
     * Creates the option menu
     *
     * @param menu The options menu in which you place your items.
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        Fragment home = getSupportFragmentManager().findFragmentByTag("HomeFragment");
//        if (home != null && home.isVisible()) {
//            //getMenuInflater().inflate(R.menu.toolbar, menu);
//            Log.d("Hi", "Hi");
//        } else {
//            Log.d("Hi2", "Hi2");
//        });
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }

    /**
     * When Log out button is clicked removes previous stack data
     * and moves user back to log in activity
     *
     * @param item The menu item that was selected.
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_logout) {
            signOut();
        } else if (id == R.id.action_settings) {
            Log.d("MainActivity", "Settings menu item clicked!"); //TODO
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Delete shared prefs and tell WS to delete pushy, then quit and return to login fragment.
     */
    private void signOut() {
        SharedPreferences prefs =
                getSharedPreferences(
                        getString(R.string.keys_shared_prefs),
                        Context.MODE_PRIVATE);
        prefs.edit().remove(getString(R.string.keys_prefs_email)).apply();
        prefs.edit().remove(getString(R.string.keys_prefs_jwt)).apply();
        prefs.edit().remove(getString(R.string.keys_prefs_memberid)).apply();
        prefs.edit().remove(getString(R.string.keys_prefs_username)).apply();


        //Delete token from web server
        PushyTokenViewModel model = new ViewModelProvider(this)
                .get(PushyTokenViewModel.class);

        //when we hear back from the web service quit //TODO this spam switch activity the user twice, once on add, once when request returns.
        model.addResponseObserver(this, result -> {
//            finishAndRemoveTask(); //quits app completely Lab6
            Intent intent = new Intent(getApplicationContext(), AuthActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });

        //send request to WS
        model.deleteTokenFromWebservice(
                new ViewModelProvider(this)
                        .get(UserInfoViewModel.class)
                        .getJwt()
        );
   }

    //Action bar nav back/up
    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration) || super.onSupportNavigateUp();
    }

    /**
     * A BroadcastReceiver that listens for messages sent from PushReceiver
     */
    private class MainPushMessageReceiver extends BroadcastReceiver {
        private final UserInfoViewModel mUserInfo =
                new ViewModelProvider(MainActivity.this)
                        .get(UserInfoViewModel.class);
        private final ChatRoomItemsViewModel mChatRoomItemsViewModel =
                new ViewModelProvider(MainActivity.this)
                        .get(ChatRoomItemsViewModel.class);
        private final ChatListItemViewModel mChatListItemViewModel =
                new ViewModelProvider(MainActivity.this)
                        .get(ChatListItemViewModel.class);

        private final ChatRoomAddUserItemViewModel mChatRoomAddUserItemViewModel =
                new ViewModelProvider(MainActivity.this)
                        .get(ChatRoomAddUserItemViewModel.class);


        private final HomeMessagesItemViewModel mHomeMessagesItemViewModel =
                new ViewModelProvider(MainActivity.this)
                        .get(HomeMessagesItemViewModel.class);


        //On receive new message
        @Override
        public void onReceive(Context context, Intent intent) {
            NavController nc =
                    Navigation.findNavController(
                            MainActivity.this, R.id.nav_host_fragment);
            NavDestination nd = nc.getCurrentDestination(); //Get the current fragment

            if (intent.getAction().equals(PushReceiver.RECEIVED_NEW_MESSAGE)) {
                if (intent.hasExtra("chatMessage")) {
                    ChatRoomItem cm = (ChatRoomItem) intent.getSerializableExtra("chatMessage");
                    //If the user is not on the chat room (aka not the sender) screen, update NewMessageCountView Model
                    if (nd.getId() != R.id.chatRoomFragment && mChatRoomItemsViewModel.mChatId != intent.getIntExtra("chatId", -1)) {
                        mNewMessageModel.incrementFromChatId(intent.getIntExtra("chatId", -1));
                    }
                    //Inform the view model holding chatroom messages of the new message.
                    mChatRoomItemsViewModel.addMessage(intent.getIntExtra("chatId", -1), cm);

                    ////////////////////////////////////FOR HOMEPAGE///////////////////////////////

                    ArrayList<HomeMessagesItem> temp = mHomeMessagesItemViewModel.mHomeMessageList.getValue();
                    temp.add(new HomeMessagesItem(cm.getMessageId(), cm.getMessage(), cm.getSender(), cm.getTimeStamp(), intent.getIntExtra("chatId", -1)));
                    mHomeMessagesItemViewModel.getmHomeMessageList().setValue(temp);


                }
            } else if (intent.getAction().equals(PushReceiver.CHATLIST_INVITE)) {
                if (intent.getStringExtra("username").equals(mUserInfo.getUsername())) { //if I got added
                    if (nd.getId() == R.id.navigation_chat) { //if in ChatList fragment
                        //refresh list
                        mChatListItemViewModel.getChatRooms(mUserInfo.getMemberID(), mUserInfo.getJwt());
                    }
                    //toast
                    Toast toast = Toast.makeText(getApplicationContext(), "You got added chat room: " + intent.getStringExtra("chatRoomName"), Toast.LENGTH_LONG);//TODO
                    toast.show();
                } else { //someone else was added
                    if (nd.getId() == R.id.navigation_chat) { //in ChatList
                        //refresh list
                        mChatListItemViewModel.getChatRooms(mUserInfo.getMemberID(), mUserInfo.getJwt());
                    } else { //in the specific chat room
                        if (nd.getId() == R.id.chatRoomFragment && intent.getIntExtra("chatId", -1) == mChatRoomItemsViewModel.mChatId) {
                            mChatRoomAddUserItemViewModel.getUsersInChat(intent.getIntExtra("chatId", -1), mUserInfo.getJwt());
                        }
                    }
                }

            } else if (intent.getAction().equals(PushReceiver.CHATLIST_KICK)) {
                if (intent.getStringExtra("username").equals(mUserInfo.getUsername())) { //if I was kicked
                    if (nd.getId() == R.id.chatRoomFragment && intent.getIntExtra("chatId", -1) == mChatRoomItemsViewModel.mChatId) { //if in ChatRoom fragment
                        //warn user
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle(R.string.alert_chatroom_you_got_kicked);
                        builder.setPositiveButton(R.string.alert_chatroom_you_got_kicked_pos, (dialog, which) -> {
                            onBackPressed();
                        });
                        builder.show();
                    } else {
                        if (nd.getId() == R.id.navigation_chat) {
                            //refresh list
                            mChatListItemViewModel.getChatRooms(mUserInfo.getMemberID(), mUserInfo.getJwt());
                        }
                        //toast
                        Toast toast = Toast.makeText(getApplicationContext(), "You were kicked you from chat room: " + intent.getStringExtra("chatRoomName"), Toast.LENGTH_LONG);//TODO
                        toast.show();
                    }
                } else { //someone else was kicked
                    if (nd.getId() == R.id.navigation_chat) { //in ChatList
                        //refresh list //TODO find chat room via id and decrement instead
                        mChatListItemViewModel.getChatRooms(mUserInfo.getMemberID(), mUserInfo.getJwt());
                    } else { //in the specific chat room
                        if (nd.getId() == R.id.chatRoomFragment && intent.getIntExtra("chatId", -1) == mChatRoomItemsViewModel.mChatId) {
                            mChatRoomAddUserItemViewModel.getUsersInChat(intent.getIntExtra("chatId", -1), mUserInfo.getJwt());
                        }
                    }
                }
            } else if (intent.getAction().equals(PushReceiver.CHATLIST_RENAME)) {
                if (nd.getId() == R.id.chatRoomFragment  && intent.getIntExtra("chatId", -1) == mChatRoomItemsViewModel.mChatId) { //if in chat room
                    mChatRoomItemsViewModel.mChatRoomName.setValue(intent.getStringExtra("chatRoomName"));
                } else if (nd.getId() == R.id.navigation_chat) {
                    //refresh list //TODO find in list and add instead
                    mChatListItemViewModel.getChatRooms(mUserInfo.getMemberID(), mUserInfo.getJwt());
                }
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        //Start receiver
        if (mPushMessageReceiver == null) {
            mPushMessageReceiver = new MainPushMessageReceiver();
        }
        registerReceiver(mPushMessageReceiver, new IntentFilter(PushReceiver.RECEIVED_NEW_MESSAGE));
        registerReceiver(mPushMessageReceiver, new IntentFilter(PushReceiver.CHATLIST_INVITE));
        registerReceiver(mPushMessageReceiver, new IntentFilter(PushReceiver.CHATLIST_KICK));
        registerReceiver(mPushMessageReceiver, new IntentFilter(PushReceiver.CHATLIST_RENAME));
    }

    @Override
    public void onPause() {
        super.onPause();
        //Stop receiver
        if (mPushMessageReceiver != null) {
            unregisterReceiver(mPushMessageReceiver);
        }
    }
}