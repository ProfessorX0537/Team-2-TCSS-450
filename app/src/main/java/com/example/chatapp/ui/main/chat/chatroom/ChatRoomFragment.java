package com.example.chatapp.ui.main.chat.chatroom;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.chatapp.R;
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
        LinearLayoutManager lnearLayoutManager = new LinearLayoutManager(getContext());
        lnearLayoutManager.setStackFromEnd(true);
        mBinding.recyclerBubbles.setLayoutManager(lnearLayoutManager);
        mBinding.recyclerBubbles.setAdapter(new ChatRoomAdapter(mModel.mItemList));
//        mBinding.recyclerBubbles.scrollToPosition(mModel.mItemList.size() - 1); //scroll to end

        //Send Button
        mBinding.actionSend.setOnClickListener(button -> {
            mBinding.textMessageInput.setText("");
            //TODO Actually send text
        });
    }

    //Hides bottom menu bar
    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity)getActivity()).findViewById(R.id.nav_view).setVisibility(View.GONE);
    }
    @Override
    public void onStop() {
        super.onStop();
        ((AppCompatActivity)getActivity()).findViewById(R.id.nav_view).setVisibility(View.VISIBLE);
    }

}