package com.example.chatapp.ui.main.chat.chatroom;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.chatapp.R;
import com.example.chatapp.databinding.FragmentChatRoomBinding;
import com.example.chatapp.model.UserInfoViewModel;

public class ChatRoomFragment extends Fragment {
    private ChatRoomItemsViewModel mItemsModel;
    private FragmentChatRoomBinding mBinding;
    private UserInfoViewModel mUserInfoModel;
    private ChatRoomSendViewModel mSendModel;
//    private int HARD_CODED_CHAT_ID = 1; //TODO REMOVE
    private int mChatID;
    private boolean isSending = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //ChatID
        mChatID = ChatRoomFragmentArgs.fromBundle(getArguments()).getChatid();

        //ViewModels
        ViewModelProvider provider = new ViewModelProvider(getActivity());

        mUserInfoModel = provider.get(UserInfoViewModel.class);

        mItemsModel = provider.get(ChatRoomItemsViewModel.class);
        mItemsModel.getFirstMessages(mChatID, mUserInfoModel.getJwt()); //TODO CHANGE CHAT ID

        mSendModel = provider.get(ChatRoomSendViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = FragmentChatRoomBinding.inflate(inflater);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ////////ActionBar/////////
        mBinding.toolbar2.inflateMenu(R.menu.toolbar_chatroom);
        //Title
//        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(ChatRoomFragmentArgs.fromBundle(getArguments()).getChatName());
        mBinding.toolbar2.setTitle(ChatRoomFragmentArgs.fromBundle(getArguments()).getChatName());
        //Contacts menu button
        mBinding.toolbar2.getMenu().getItem(0).setOnMenuItemClickListener(button -> {
            Log.d("ChatRoomFragment", "Add users Button Clicked"); //TODO
            return true;
        });
        //Back Navigation
        mBinding.toolbar2.setNavigationIcon(R.drawable.baseline_arrow_back_24);
        mBinding.toolbar2.setNavigationOnClickListener(button -> {
            getActivity().onBackPressed();
        });

        //Swipe refresh Container
        mBinding.swipeContainer.setRefreshing(true); //SetRefreshing shows the internal Swiper view progress bar. Show this until messages load
        //When the user scrolls to the top of the RV, the swiper list will "refresh"
        //The user is out of messages, go out to the service and get more
        mBinding.swipeContainer.setOnRefreshListener(() -> {
            if (!mItemsModel.getNextMessages(mChatID, mUserInfoModel.getJwt())) {
                mBinding.swipeContainer.setRefreshing(false);
            }
        });

        //recycler //TODO listen for ArrayList change
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setStackFromEnd(true);
        mBinding.recyclerBubbles.setLayoutManager(linearLayoutManager);
        mBinding.recyclerBubbles.setAdapter(new ChatRoomAdapter(mItemsModel.getMessageListByChatId(mChatID), mUserInfoModel.getUsername()));
//        mBinding.recyclerBubbles.getAdapter().setStateRestorationPolicy(RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY);

        //Show scroll to bottom button when not at bottom
        mBinding.actionScrollToBottom.setVisibility(View.GONE); //hide initially
        mBinding.actionScrollToBottom.setOnClickListener(button -> {
            mBinding.actionScrollToBottom.setVisibility(View.GONE); //hide self
            mBinding.recyclerBubbles.smoothScrollToPosition(mBinding.recyclerBubbles.getAdapter().getItemCount() - 1); //scroll to end
            mBinding.actionScrollToBottom.setText(R.string.action_scroll_to_bottom); //reset new message alert
        });
        mBinding.recyclerBubbles.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (recyclerView.canScrollVertically(1)) { //if can scroll down
                    mBinding.actionScrollToBottom.setVisibility(View.VISIBLE);
                } else {
                    mBinding.actionScrollToBottom.setVisibility(View.GONE);
                    mBinding.actionScrollToBottom.setText(R.string.action_scroll_to_bottom); //reset new message alert
                }
            }
        });

        //Change in items/message listener
        mItemsModel.addMessageObserver(mChatID, getViewLifecycleOwner(),
                list -> {
                    //TODO Save scroll position
//                    Parcelable recyclerViewState = mBinding.recyclerBubbles.getLayoutManager().onSaveInstanceState();
                    mBinding.recyclerBubbles.getAdapter().notifyDataSetChanged(); //tell recycler to update
                    mBinding.swipeContainer.setRefreshing(false);
//                    mBinding.recyclerBubbles.getLayoutManager().onRestoreInstanceState(recyclerViewState);

                    //Scroll to bottom if change was from self sending
                    if (isSending) {
                        mBinding.recyclerBubbles.smoothScrollToPosition(mBinding.recyclerBubbles.getAdapter().getItemCount() - 1); //scroll to end
                        isSending = false;
                    }

                    //If at the bottom already, scroll to bottom on new received messages
                    if (mBinding.recyclerBubbles.getAdapter().getItemCount() != 0) {
                        if (!mBinding.recyclerBubbles.canScrollVertically(1)) {
                            mBinding.recyclerBubbles.smoothScrollToPosition(mBinding.recyclerBubbles.getAdapter().getItemCount() - 1); //scroll to end
                        } else {
                            //else show scroll to bottom button
                            mBinding.actionScrollToBottom.setVisibility(View.VISIBLE);
                            mBinding.actionScrollToBottom.setText(R.string.action_scroll_to_bottom_new); //alert of new message
                        }
                    }
                });

        //Send Button
        //Send button was clicked. Send the message via the SendViewModel
        mBinding.actionSend.setOnClickListener(button -> {
            mSendModel.sendMessage(mChatID,
                    mUserInfoModel.getJwt(),
                    mBinding.textMessageInput.getText().toString());
        });
        //when we get the response back from the server, clear the edittext
        mSendModel.addResponseObserver(getViewLifecycleOwner(), response -> {
            if (response != null) {
                mBinding.textMessageInput.setText("");
                isSending = true;
            }
        });
    }

    //Hides bottom menu bar
    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setShowHideAnimationEnabled(false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        ((AppCompatActivity) getActivity()).findViewById(R.id.nav_view).setVisibility(View.GONE);
    }

    @Override
    public void onStop() {
        super.onStop();
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
        ((AppCompatActivity) getActivity()).findViewById(R.id.nav_view).setVisibility(View.VISIBLE);
    }
}