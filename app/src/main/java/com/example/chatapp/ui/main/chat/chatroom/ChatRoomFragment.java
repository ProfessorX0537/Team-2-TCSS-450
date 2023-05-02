package com.example.chatapp.ui.main.chat.chatroom;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.chatapp.databinding.FragmentChatRoomBinding;

public class ChatRoomFragment extends Fragment {
    private ChatRoomViewModel mModel;
    private FragmentChatRoomBinding mBinding;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mModel = new ViewModelProvider(getActivity()).get(ChatRoomViewModel.class);
        mModel.setupItemsList();
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

        //recycler //TODO listen for ArrayList change
        mBinding.recyclerBubbles.setAdapter(new ChatRoomAdapter(mModel.mItemList));

        //Send Button
        mBinding.actionSend.setOnClickListener(button -> {
            mBinding.textMessageInput.setText("");
            //TODO Actually send text
        });
    }
}