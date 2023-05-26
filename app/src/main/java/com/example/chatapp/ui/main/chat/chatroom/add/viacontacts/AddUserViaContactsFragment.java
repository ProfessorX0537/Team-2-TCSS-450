package com.example.chatapp.ui.main.chat.chatroom.add.viacontacts;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.chatapp.R;
import com.example.chatapp.databinding.FragmentAddUserViaContactsBinding;
import com.example.chatapp.model.UserInfoViewModel;
import com.example.chatapp.ui.main.chat.chatroom.ChatRoomItemsViewModel;
import com.example.chatapp.ui.main.chat.chatroom.add.ChatRoomAddUserItem;
import com.example.chatapp.ui.main.chat.chatroom.add.ChatRoomAddUserItemViewModel;
import com.example.chatapp.ui.main.chat.chatroom.add.ChatRoomAddUserRequestsViewModel;
import com.example.chatapp.ui.main.contacts.ContactCard;
import com.example.chatapp.ui.main.contacts.ContactsViewModel;

import java.util.List;

public class AddUserViaContactsFragment extends Fragment {
    private FragmentAddUserViaContactsBinding binding;
    public ContactsViewModel mContactsViewModel;
    public ChatRoomAddUserItemViewModel mChatRoomAddUserItemViewModel;
    public ChatRoomAddUserRequestsViewModel mChatRoomAddUserRequestsViewModel;
    public ChatRoomItemsViewModel mChatRoomItemsViewModel;
    public UserInfoViewModel userinfo;

    private List<ContactCard> mContactCards;
    private List<ChatRoomAddUserItem> mChatRoomAddUserItems;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContactsViewModel = new ViewModelProvider(getActivity()).get(ContactsViewModel.class);
        mChatRoomAddUserItemViewModel = new ViewModelProvider(getActivity()).get(ChatRoomAddUserItemViewModel.class);
        mChatRoomAddUserRequestsViewModel = new ViewModelProvider(getActivity()).get(ChatRoomAddUserRequestsViewModel.class);
        mChatRoomItemsViewModel = new ViewModelProvider(getActivity()).get(ChatRoomItemsViewModel.class);
        userinfo = new ViewModelProvider(getActivity()).get(UserInfoViewModel.class);

        mContactCards = null;
        mChatRoomAddUserItems = null;

        mContactsViewModel.connectGet(userinfo.getMemberID());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAddUserViaContactsBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //recycler
        mContactsViewModel.addContactsObserver(getViewLifecycleOwner(), list -> {
            mContactCards = list;
            trySetAdapter();
        });
        mChatRoomAddUserItemViewModel.addItemListObserver(getViewLifecycleOwner(), list -> {
            mChatRoomAddUserItems = list;
            trySetAdapter();
        });

        //exit
        binding.buttonExit.setOnClickListener(button -> {
            getActivity().findViewById(R.id.fragment_container_add_user_via_contacts).setVisibility(View.GONE);
            getActivity().findViewById(R.id.fragment_view_add_user).setVisibility(View.VISIBLE);
        });
    }

    private void trySetAdapter() {
        if (mContactCards != null && mChatRoomAddUserItems != null) {
            binding.recyclerView.setAdapter(new AddUserViaContactsAdapter(mContactCards, mChatRoomAddUserItems, this));
        }
    }
}