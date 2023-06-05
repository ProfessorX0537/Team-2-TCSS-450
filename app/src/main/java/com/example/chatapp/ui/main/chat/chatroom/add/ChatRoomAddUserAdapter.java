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
    private final ChatRoomAddUserFragment mParentFragment;

    public ChatRoomAddUserAdapter(ArrayList<ChatRoomAddUserItem> userList, ChatRoomAddUserFragment parentFragment) {
        mUserList = userList;
        mParentFragment = parentFragment;
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
        String username = mUserList.get(position).getmUsername();

        //aka OnHolderViewCreated
        holder.mBinding.textUsername.setText(username);

        //remove button
        holder.mBinding.buttonKick.setOnClickListener(button -> {
            mParentFragment.showAlertConfirmToKickUser(username, "Kick " + username + " from the chat room?", "The user must be re-added."); //TODO String
        });
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
