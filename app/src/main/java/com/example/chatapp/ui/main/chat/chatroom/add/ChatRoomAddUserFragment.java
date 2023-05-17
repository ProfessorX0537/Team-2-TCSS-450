package com.example.chatapp.ui.main.chat.chatroom.add;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
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

        //Observe Items (new user added / user kicked)
        mItemModel.addItemListObserver(getViewLifecycleOwner(), list -> {
            if (list == null) return;
            binding.recyclerView.setAdapter(new ChatRoomAddUserAdapter(list, this));
        });
        //Observe mRemoveFromChatResponse (kicking user)
        mRequestsModel.addRemoveFromChatResponseObserver(getViewLifecycleOwner(), json -> {
            if (json == null) return;
            mItemModel.getUsersInChat(mChatRoomItemsViewModel.mChatId, userinfo.getJwt()); //get users again after booting someone
            mRequestsModel.clearResponses();
        });
        //Observer mRemoveSelfFromChatResponse (leaving)
        mRequestsModel.addRemoveSelfFromChatResponseObserver(getViewLifecycleOwner(), json -> {
            if (json == null) return;
            if (json.has("message")) { //fail
                //warn user
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Failed leaving server."); //TODO string
                builder.setMessage("Please contact support. ¯\\_(ツ)_/¯"); //TODO string
                builder.setPositiveButton("Dismiss", (dialog, which) -> { //TODO string
                    dialog.cancel();
                });
                builder.show();
            } else { //success
                getActivity().onBackPressed(); //TODO Major bug, this backs Chat Navigations after executed unitl logout / program restart
            }
            mRequestsModel.clearResponses();
        });
        //Observe mAddToChatResponse (adding user)
        mRequestsModel.addAddToChatResponseObserver(getViewLifecycleOwner(), json -> {
            if (json == null) return;
            if (json.has("message")) { //fail
                //warn user
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Failed adding user."); //TODO string
                builder.setMessage("Make sure you spelt their username correctly."); //TODO string
                builder.setPositiveButton("Dismiss", (dialog, which) -> { //TODO string
                    dialog.cancel();
                });
                builder.show();
            } else { //success
                mItemModel.getUsersInChat(mChatRoomItemsViewModel.mChatId, userinfo.getJwt()); //get users again after adding someone
            }
            mRequestsModel.clearResponses();
        });
        //Observe mRenameChatResponse
        mRequestsModel.addRenameChatResponseObserver(getViewLifecycleOwner(), json -> {
            if (json == null) return;
            if (json.has("message")) { //fail
                //warn user
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Unable to rename chat."); //TODO string
                builder.setMessage("Please contact support. ¯\\_(ツ)_/¯"); //TODO string
                builder.setPositiveButton("Dismiss", (dialog, which) -> { //TODO string
                    dialog.cancel();
                });
                builder.show();
            } else { //success
                //show rename //TODO replace with Pushy
                binding.textRename.setText(mRequestsModel.renameTemp);
                androidx.appcompat.widget.Toolbar t = getActivity().findViewById(R.id.toolbar2);
                t.setTitle(mRequestsModel.renameTemp);
            }
            mRequestsModel.clearResponses();
        });

        //Hide Fragment Button (the area outside window)
        binding.buttonCloseWindow.setOnClickListener(button -> {
            //find the ChatRoom's FragmentContainerView that has this
            getActivity().findViewById(R.id.fragment_view_add_user).setVisibility(View.GONE);
        });

        //Add User to Chat Button
        binding.buttonAddUserToChat.setOnClickListener(button -> {
            // Alert confirmation
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("(WIP UI) Enter user to add:"); //TODO string
            //Edit Text
            final View customLayout = getLayoutInflater().inflate(R.layout.dialog_generic_edit_text, null);
            builder.setView(customLayout);
            EditText editText = customLayout.findViewById(R.id.edit_text_generic);
            editText.setHint("Username"); //TODO string
            //Yes
            builder.setPositiveButton("Add", (dialog, which) -> { //TODO string
                mRequestsModel.requestAddToChat(mChatRoomItemsViewModel.mChatId, editText.getText().toString().trim(), userinfo.getJwt());
            });
            //No
            builder.setNegativeButton("Cancel", (dialog, which) -> { //TODO string
                dialog.cancel();
            });
            //Show
            AlertDialog dialog = builder.create();
            dialog.show();
        });

        //Rename
        binding.textRename.setText(mChatRoomItemsViewModel.mChatRoomName);
        binding.buttonRename.setOnClickListener(button -> {
            // Alert confirmation
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Rename this chat room:"); //TODO string
            //Edit Text
            final View customLayout = getLayoutInflater().inflate(R.layout.dialog_generic_edit_text, null);
            builder.setView(customLayout);
            EditText editText = customLayout.findViewById(R.id.edit_text_generic);
            editText.setHint("Chat Room Name"); //TODO string
            //Yes
            builder.setPositiveButton("Rename", (dialog, which) -> { //TODO string
                String entry = editText.getText().toString().trim();
                mRequestsModel.requestRenameChat(mChatRoomItemsViewModel.mChatId, entry, userinfo.getJwt());
            });
            //No
            builder.setNegativeButton("Cancel", (dialog, which) -> { //TODO string
                dialog.cancel();
            });
            //Show
            AlertDialog dialog = builder.create();
            dialog.show();
        });

        //User Recycler View

//        //Nuke Chat Button
//        binding.buttonDelete.setOnClickListener(button -> {
//            Log.d("ChatRoomAddUserFragment", "buttonDelete / Nuke clicked");
//        });

        //Leave Chat Button
        binding.buttonLeave.setOnClickListener(button -> {
            showAlertLeaveChat();
        });
    }

    public void showAlertConfirmToKickUser(String username, String title, String message) {
        // Alert confirmation
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(title);
        builder.setMessage(message);
        //Yes
        builder.setPositiveButton(R.string.alert_action_yes, (dialog, which) -> {
            mRequestsModel.requestRemoveFromChat(mChatRoomItemsViewModel.mChatId, username, userinfo.getJwt());
        });
        //No
        builder.setNegativeButton(R.string.button_chatlist_create_neg, (dialog, which) -> {
            dialog.cancel();
        });
        //Show
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void showAlertLeaveChat() {
        // Alert confirmation
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Leave this chat room?"); //TODO string
        builder.setMessage("You will need to be added back by someone else."); //TODO string
        //Yes
        builder.setPositiveButton("Leave", (dialog, which) -> { //TODO string
            mRequestsModel.requestRemoveSelfFromChat(mChatRoomItemsViewModel.mChatId, userinfo);
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