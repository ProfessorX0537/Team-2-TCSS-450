package com.example.chatapp.ui.main.chat.chatroom.add;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.chatapp.R;
import com.example.chatapp.databinding.FragmentChatRoomAddUserBinding;
import com.example.chatapp.model.UserInfoViewModel;
import com.example.chatapp.ui.main.chat.chatroom.ChatRoomItemsViewModel;

public class ChatRoomAddUserFragment extends Fragment {
    private FragmentChatRoomAddUserBinding binding;

    private UserInfoViewModel userinfo;

    private ChatRoomAddUserItemViewModel mItemModel;
    private ChatRoomItemsViewModel mChatRoomItemsViewModel;
//    private int mChatId;

    private ChatRoomAddUserRequestsViewModel mRequestsModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //ViewModels
        userinfo = new ViewModelProvider(getActivity()).get(UserInfoViewModel.class);
        mChatRoomItemsViewModel = new ViewModelProvider(getActivity()).get(ChatRoomItemsViewModel.class);

        mItemModel = new ViewModelProvider(getActivity()).get(ChatRoomAddUserItemViewModel.class);
        mItemModel.userinfo = userinfo; //required
        mItemModel.getUsersInChat(mChatRoomItemsViewModel.mChatId, userinfo.getJwt());

        mRequestsModel = new ViewModelProvider(getActivity()).get(ChatRoomAddUserRequestsViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentChatRoomAddUserBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Observe Items
        mItemModel.addItemListObserver(getViewLifecycleOwner(), list -> {
            binding.recyclerView.setAdapter(new ChatRoomAddUserAdapter(list, this));
        });
        //Observe mRemoveFromChatResponse
        mRequestsModel.addRemoveFromChatResponseObserver(getViewLifecycleOwner(), json -> {
            mItemModel.getUsersInChat(mChatRoomItemsViewModel.mChatId, userinfo.getJwt()); //get users again after booting someone
        });

        //Hide Fragment Button (the area outside window)
        binding.buttonCloseWindow.setOnClickListener(button -> {
            //find the ChatRoom's FragmentContainerView that has this
            getActivity().findViewById(R.id.fragment_view_add_user).setVisibility(View.GONE);
        });

        //Add Contact Button
        binding.buttonDelete.setOnClickListener(button -> {
            Log.d("ChatRoomAddUserFragment", "buttonCreate / Add clicked");
        });


        //User Recycler View

        //Nuke Chat Button
        binding.buttonDelete.setOnClickListener(button -> {
            Log.d("ChatRoomAddUserFragment", "buttonDelete / Nuke clicked");
        });

        //Leave Chat Button
        binding.buttonLeave.setOnClickListener(button -> {
            showAlertConfirmToKickUser(userinfo.getUsername(), "Leave this chat room?", true); //TODO string
        });

        //Rename
        binding.editTextRename.setText(mChatRoomItemsViewModel.mChatRoomName);
    }

    public void showAlertConfirmToKickUser(String username, String message, boolean isNavigateUp) {
        // Alert confirmation
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(message);
        //Yes
        builder.setPositiveButton(R.string.alert_action_yes, (dialog, which) -> {
            mRequestsModel.requestRemoveFromChat(mChatRoomItemsViewModel.mChatId, username, userinfo.getJwt());
            if (isNavigateUp) Navigation.findNavController(getView()).navigateUp();
        });
        //No
        builder.setNegativeButton(R.string.button_chatlist_create_neg, (dialog, which) -> {
            dialog.cancel();
        });
        //Show
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}