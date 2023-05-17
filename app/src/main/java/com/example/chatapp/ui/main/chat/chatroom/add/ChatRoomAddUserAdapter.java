package com.example.chatapp.ui.main.chat.chatroom.add;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatapp.R;
import com.example.chatapp.databinding.FragmentChatRoomAddUserItemBinding;

import java.util.ArrayList;

public class ChatRoomAddUserAdapter extends RecyclerView.Adapter<ChatRoomAddUserAdapter.ChatRoomAddUserViewHolder> {
    public final ArrayList<ChatRoomAddUserItem> mUserList;

    public ChatRoomAddUserAdapter(ArrayList<ChatRoomAddUserItem> userList) {
        mUserList = userList;
    }

    @NonNull
    @Override
    public ChatRoomAddUserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ChatRoomAddUserViewHolder(LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.fragment_chat_room_add_user_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ChatRoomAddUserViewHolder holder, int position) {
        //aka OnHolderViewCreated
        holder.mBinding.textUsername.setText(mUserList.get(position).getmUsername());
    }

    @Override
    public int getItemCount() {
        return mUserList.size();
    }

    public static class ChatRoomAddUserViewHolder extends RecyclerView.ViewHolder {

        public @NonNull FragmentChatRoomAddUserItemBinding mBinding;

        public ChatRoomAddUserViewHolder(@NonNull View itemView) {
            super(itemView);
            mBinding = FragmentChatRoomAddUserItemBinding.bind(itemView);
        }
    }
}
