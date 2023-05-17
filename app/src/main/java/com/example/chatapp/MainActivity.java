package com.example.chatapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.chatapp.databinding.ActivityMainBinding;
import com.example.chatapp.model.NewMessageCountViewModel;
import com.example.chatapp.model.UserInfoViewModel;
import com.example.chatapp.services.PushReceiver;
import com.example.chatapp.ui.main.chat.chatroom.ChatRoomItem;
import com.example.chatapp.ui.main.chat.chatroom.ChatRoomItemsViewModel;
import com.example.chatapp.ui.main.contacts.ContactsViewModel;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private AppBarConfiguration mAppBarConfiguration;
    private NewMessageCountViewModel mNewMessageModel;
    private ContactsViewModel mContactViewModel;

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
        //When the user navigates to the chats page, reset the new message count.
        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            if (destination.getId() == R.id.navigation_chat) {
                //This will need some extra logic for your project as it should have
                //multiple chat rooms.
                mNewMessageModel.reset();
            }
        });
        //Observe MessageCount and create Badge
        mNewMessageModel.addMessageCountObserver(this, count -> { //TODO TEST THIS!!!
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
        if (item.getItemId() == R.id.action_logout) {
            //TODO remove user data/token from webservice from logging out
            Intent intent = new Intent(getApplicationContext(), AuthActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
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
        private ChatRoomItemsViewModel mModel =
                new ViewModelProvider(MainActivity.this)
                        .get(ChatRoomItemsViewModel.class);

        //On receive new message
        @Override
        public void onReceive(Context context, Intent intent) {
            NavController nc =
                    Navigation.findNavController(
                            MainActivity.this, R.id.nav_host_fragment);
            NavDestination nd = nc.getCurrentDestination(); //Get the current fragment
            if (intent.hasExtra("chatMessage")) {
                ChatRoomItem cm = (ChatRoomItem) intent.getSerializableExtra("chatMessage");
                //If the user is not on the chat screen, update the
                // NewMessageCountView Model
                if (nd.getId() != R.id.navigation_chat) {
                    mNewMessageModel.increment();
                }
                //Inform the view model holding chatroom messages of the new
                //message.
                mModel.addMessage(intent.getIntExtra("chatid", -1), cm);
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
        IntentFilter iFilter = new IntentFilter(PushReceiver.RECEIVED_NEW_MESSAGE);
        registerReceiver(mPushMessageReceiver, iFilter);
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