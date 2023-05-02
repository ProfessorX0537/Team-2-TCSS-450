package com.example.chatapp.ui.main.chat.chatroom;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatapp.R;
import com.example.chatapp.databinding.FragmentChatListItemBinding;
import com.example.chatapp.databinding.FragmentChatRoomBinding;
import com.example.chatapp.databinding.FragmentChatRoomBubbleReceiveBinding;
import com.example.chatapp.databinding.FragmentChatRoomBubbleSendBinding;

import java.util.ArrayList;

//https://droidbyme.medium.com/android-recyclerview-with-multiple-view-type-multiple-view-holder-af798458763b
public class ChatRoomAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static int VIEWTYPE_RECIEVE = 0;
    private static int VIEWTYPE_SEND = 1;
    public final ArrayList<ChatRoomItem> mChatRoomItems;

    public ChatRoomAdapter(ArrayList<ChatRoomItem> mChatRoomItems) {
        this.mChatRoomItems = mChatRoomItems;
    }

    @Override
    public int getItemViewType(int position) {
        if (mChatRoomItems.get(position).ismIsFromOther()) {
            return VIEWTYPE_RECIEVE;
        } else {
            return VIEWTYPE_SEND;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == VIEWTYPE_RECIEVE) { //note: switch didnt work
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_chat_room_bubble_receive, parent, false);
            return new ChatRoomViewHolderRec(view);
        } else { //VIEWTYPE_SEND
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_chat_room_bubble_send, parent, false);
            return new ChatRoomViewHolderSend(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ChatRoomItem curr = mChatRoomItems.get(position);
        if (getItemViewType(position) == VIEWTYPE_RECIEVE) {
            ChatRoomViewHolderRec holder1 = (ChatRoomViewHolderRec) holder;
            holder1.mBinding.textSender.setText(curr.getmSenderName());
            holder1.mBinding.textMessage.setText(curr.getmMessage());
            holder1.mBinding.textDate.setText(curr.getmDate());
        } else { //VIEWTYPE_SEND
            ChatRoomViewHolderSend holder1 = (ChatRoomViewHolderSend) holder;
            holder1.mBinding.textMessage.setText(curr.getmMessage());
            holder1.mBinding.textDate.setText(curr.getmDate());
        }
    }

    @Override
    public int getItemCount() {
        return mChatRoomItems.size();
    }

    private static class ChatRoomViewHolderRec extends RecyclerView.ViewHolder {
        public @NonNull FragmentChatRoomBubbleReceiveBinding mBinding;

        public ChatRoomViewHolderRec(@NonNull View itemView) {
            super(itemView);
            this.mBinding = FragmentChatRoomBubbleReceiveBinding.bind(itemView);
        }
    }

    private static class ChatRoomViewHolderSend extends RecyclerView.ViewHolder {
        public @NonNull FragmentChatRoomBubbleSendBinding mBinding;

        public ChatRoomViewHolderSend(@NonNull View itemView) {
            super(itemView);
            this.mBinding = FragmentChatRoomBubbleSendBinding.bind(itemView);
        }
    }
}
