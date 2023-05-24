package com.example.chatapp.ui.main.chat.chatlist;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStore;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatapp.R;
import com.example.chatapp.databinding.FragmentChatListItemBinding;
import com.example.chatapp.ui.auth.login.LoginFragmentDirections;
import com.example.chatapp.ui.main.chat.chatroom.ChatRoomItemsViewModel;

import java.util.ArrayList;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ChatListViewHolder> {
    public final ArrayList<ChatListItem> mChatListItems;
    private ViewModelStoreOwner mActivity;

    public ChatListAdapter(ArrayList<ChatListItem> mChatListItems, ViewModelStoreOwner activity) {
        this.mChatListItems = mChatListItems;
        mActivity = activity;
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

        //navgiations
        holder.mBinding.actionOpenChatRoom.setOnClickListener(button -> {
            ChatRoomItemsViewModel vm = new ViewModelProvider(mActivity).get(ChatRoomItemsViewModel.class);
            //ViewModel as args to inner FragmentViewContainer
            vm.mChatId = mChatListItems.get(position).getmRoomID();
            vm.mChatRoomName = mChatListItems.get(position).getmRoomName();

            Navigation.findNavController(holder.itemView).navigate(
                    com.example.chatapp.ui.main.chat.chatlist.ChatListFragmentDirections.actionNavigationChatToChatRoomFragment(
                            mChatListItems.get(position).getmRoomID(),
                            mChatListItems.get(position).getmRoomName()
                    )
            );
        });
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
}
