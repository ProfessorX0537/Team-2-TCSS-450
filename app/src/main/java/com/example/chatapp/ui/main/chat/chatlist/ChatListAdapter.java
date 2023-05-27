package com.example.chatapp.ui.main.chat.chatlist;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatapp.R;
import com.example.chatapp.databinding.FragmentChatListItemBinding;
import com.example.chatapp.model.NewMessageCountViewModel;
import com.example.chatapp.ui.main.chat.chatroom.ChatRoomItemsViewModel;

import java.util.ArrayList;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ChatListViewHolder> {
    public ArrayList<ChatListItem> mChatListItems;
    private ViewModelStoreOwner mActivity;
    private View mParentView;

    private int mHomeChatIdAutoDirect;

    public ChatListAdapter(ArrayList<ChatListItem> mChatListItems, ViewModelStoreOwner activity, int HomeChatIdAutoDirect, View parentView) {
        this.mChatListItems = mChatListItems;
        mActivity = activity;
        mParentView = parentView;
        setHasStableIds(true);
        mHomeChatIdAutoDirect = HomeChatIdAutoDirect;
        mParentView = parentView;
    }

    @NonNull
    @Override
    public ChatListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ChatListViewHolder(LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.fragment_chat_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ChatListViewHolder holder, int position) {
        //autonav
        if (mHomeChatIdAutoDirect == mChatListItems.get(position).mRoomID) {
            navigateToRoom(holder, position);
        }
        //navigation
        holder.mBinding.actionOpenChatRoom.setOnClickListener(button -> {
            navigateToRoom(holder, position);
        });

        holder.mBinding.textRoomName.setText(mChatListItems.get(position).getmRoomName());
        holder.mBinding.textLatestmessage.setText(mChatListItems.get(position).getmLatestMessage());
        holder.mBinding.textDatetime.setText(mChatListItems.get(position).getmLatestDate());
        holder.mBinding.textUserCount.setText(mChatListItems.get(position).getmUserCount()+"");

        //notif
        int notifCount = mChatListItems.get(position).getmNotifCount();
        if (notifCount > 0) {
            holder.mBinding.imgNotifdot.setVisibility(View.VISIBLE);
            holder.mBinding.textNotifnumber.setVisibility(View.VISIBLE);
            holder.mBinding.textNotifnumber.setText(notifCount + "");
        } else {
            holder.mBinding.imgNotifdot.setVisibility(View.INVISIBLE);
            holder.mBinding.textNotifnumber.setVisibility(View.INVISIBLE);
        }
    }

    private void navigateToRoom(ChatListViewHolder holder, int position) {
        ChatRoomItemsViewModel tempChatRoomItemsViewModel = new ViewModelProvider(mActivity).get(ChatRoomItemsViewModel.class);
        //ViewModel as args to inner FragmentViewContainer
        tempChatRoomItemsViewModel.mChatId = mChatListItems.get(position).getmRoomID();
        tempChatRoomItemsViewModel.mChatRoomName.setValue(mChatListItems.get(position).getmRoomName());

        //NewMessage update
        NewMessageCountViewModel tempNewMessageCountViewModel = new ViewModelProvider(mActivity).get(NewMessageCountViewModel.class);
        tempNewMessageCountViewModel.decrementFromChatId(tempChatRoomItemsViewModel.mChatId);

        Navigation.findNavController(mParentView).navigate(
                com.example.chatapp.ui.main.chat.chatlist.ChatListFragmentDirections.actionNavigationChatToChatRoomFragment(
                        mChatListItems.get(position).getmRoomID(),
                        mChatListItems.get(position).getmRoomName()
                )
        );
    }

    @Override
    public int getItemCount() {
        return mChatListItems.size();
    }

    public static class ChatListViewHolder extends RecyclerView.ViewHolder {
//        public final View mView;
        public @NonNull FragmentChatListItemBinding mBinding;
//        private ChatListItem mChatListItem;

        public ChatListViewHolder(@NonNull View itemView) {
            super(itemView);
//            mView = itemView;
            mBinding = FragmentChatListItemBinding.bind(itemView);
        }
    }

    @Override
    public long getItemId(int position) {
        return mChatListItems.get(position).mRoomID;
    }
}
