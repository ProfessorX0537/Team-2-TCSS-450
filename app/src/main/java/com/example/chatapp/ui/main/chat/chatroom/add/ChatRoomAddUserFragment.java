package com.example.chatapp.ui.main.chat.chatroom.add;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.chatapp.R;
import com.example.chatapp.databinding.FragmentChatRoomAddUserBinding;
import com.example.chatapp.model.UserInfoViewModel;
import com.example.chatapp.ui.main.chat.chatroom.ChatRoomFragmentArgs;
import com.example.chatapp.ui.main.chat.chatroom.ChatRoomItem;
import com.example.chatapp.ui.main.chat.chatroom.ChatRoomItemsViewModel;

public class ChatRoomAddUserFragment extends Fragment {
    private FragmentChatRoomAddUserBinding binding;

    private UserInfoViewModel userinfo;

    private ChatRoomAddUserItemViewModel mItemModel;
//    private int mChatId;
    //AddViewModel
    //RenameViewModel

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //ViewModels
        userinfo = new ViewModelProvider(getActivity()).get(UserInfoViewModel.class);

        mItemModel = new ViewModelProvider(getActivity()).get(ChatRoomAddUserItemViewModel.class);
        mItemModel.userinfo = userinfo; //required

        mItemModel.getUsersInChat(new ViewModelProvider(getActivity()).get(ChatRoomItemsViewModel.class).mChatId, userinfo.getJwt()); //TODO change hard coded
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
            binding.recyclerView.setAdapter(new ChatRoomAddUserAdapter(list));
        });

        //Hide Fragment Button (the area outside window)
        binding.buttonCloseWindow.setOnClickListener(button -> {
            //find the ChatRoom's FragmentContainerView that has this
            getActivity().findViewById(R.id.fragment_view_add_user).setVisibility(View.GONE);
        });

        //Add Contact Button
        binding.buttonCreate.setOnClickListener(button -> {
            Log.d("ChatRoomAddUserFragment", "buttonCreate / Add clicked");
        });


        //User Recycler View

        //Nuke Chat Button
        binding.buttonDelete.setOnClickListener(button -> {
            Log.d("ChatRoomAddUserFragment", "buttonDelete / Nuke clicked");
        });

        //Leave Chat Button
        binding.buttonLeave.setOnClickListener(button -> {
            Log.d("ChatRoomAddUserFragment", "buttonLeave clicked");
        });
    }
}