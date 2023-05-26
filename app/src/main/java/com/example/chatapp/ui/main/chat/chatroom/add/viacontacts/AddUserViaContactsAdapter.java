package com.example.chatapp.ui.main.chat.chatroom.add.viacontacts;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatapp.R;
import com.example.chatapp.databinding.FragmentAddUserViaContactsItemBinding;
import com.example.chatapp.ui.main.chat.chatroom.add.ChatRoomAddUserFragment;
import com.example.chatapp.ui.main.chat.chatroom.add.ChatRoomAddUserItem;
import com.example.chatapp.ui.main.contacts.ContactCard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AddUserViaContactsAdapter extends RecyclerView.Adapter<AddUserViaContactsAdapter.AddUserViaContactsViewHolder> {

//    public final List<ContactCard> mItemsContacts;

//    public final List<ChatRoomAddUserItem> mItemsAlreadyInRoom;
    public final ArrayList<ContactCard> mItems;
    private final AddUserViaContactsFragment mParentFragment;

    public AddUserViaContactsAdapter(List<ContactCard> mItemsContacts, List<ChatRoomAddUserItem> mItemsAlreadyInRoom, AddUserViaContactsFragment parentFragment) {
        this.mParentFragment = parentFragment;

        HashMap<String, Object> temp = new HashMap<>();
        for (int i = 0; i < mItemsAlreadyInRoom.size(); i++) {
            ChatRoomAddUserItem curr = mItemsAlreadyInRoom.get(i);
            temp.put(curr.getmUsername(), null);
        }

        mItems = new ArrayList<>();
        for (int i = 0; i < mItemsContacts.size(); i++) {
            ContactCard curr = mItemsContacts.get(i);
            if (!temp.containsKey(curr.getNick())) {
                mItems.add(curr);
            }
        }
        Log.d("AddUserViaContactsAdapter", "mItemsContacts.size(): " + mItemsContacts.size());
        Log.d("AddUserViaContactsAdapter", "mItemsAlreadyInRoom.size(): " + mItemsAlreadyInRoom.size());
        Log.d("AddUserViaContactsAdapter", "mItems.size(): " + mItems.size());
    }

    @NonNull
    @Override
    public AddUserViaContactsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AddUserViaContactsViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_add_user_via_contacts_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AddUserViaContactsViewHolder holder, int position) {
        String username = mItems.get(position).getNick();

        holder.binding.textUsername.setText(username);
        holder.binding.textAdded.setVisibility(View.INVISIBLE);

        holder.binding.buttonAddUser.setEnabled(true);
        holder.binding.buttonAddUser.setOnClickListener(button -> {
            mParentFragment.mChatRoomAddUserRequestsViewModel.requestAddToChat(mParentFragment.mChatRoomItemsViewModel.mChatId, username, mParentFragment.userinfo.getJwt());
            holder.binding.buttonAddUser.setEnabled(false);
            holder.binding.buttonAddUser.setVisibility(View.INVISIBLE);
            holder.binding.textAdded.setVisibility(View.VISIBLE);
        });
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public static class AddUserViaContactsViewHolder extends RecyclerView.ViewHolder {

        public @NonNull FragmentAddUserViaContactsItemBinding binding;

        public AddUserViaContactsViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = FragmentAddUserViaContactsItemBinding.bind(itemView);
        }
    }
}
