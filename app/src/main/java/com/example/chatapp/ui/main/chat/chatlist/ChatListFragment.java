package com.example.chatapp.ui.main.chat.chatlist;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.chatapp.R;
import com.example.chatapp.databinding.FragmentChatListBinding;
import com.example.chatapp.model.UserInfoViewModel;
import com.example.chatapp.ui.main.chat.chatlist.add.ChatListAddViewModel;

public class ChatListFragment extends Fragment {
    private ChatListItemViewModel mItemModel;
    private ChatListAddViewModel mAddModel;
    private FragmentChatListBinding mBinding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //ViewModels
        UserInfoViewModel userinfo = new ViewModelProvider(getActivity()).get(UserInfoViewModel.class);

        mItemModel = new ViewModelProvider(getActivity()).get(ChatListItemViewModel.class);
        mItemModel.getChatRooms(userinfo.getMemberID(), userinfo.getJwt());

        mAddModel = new ViewModelProvider(getActivity()).get(ChatListAddViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = FragmentChatListBinding.inflate(inflater);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //mModel.mItemList observe
        mItemModel.addItemListObserver(getViewLifecycleOwner(), list -> {
            if (mBinding.rootRecycler.getAdapter() == null) {
                mBinding.rootRecycler.setAdapter(new ChatListAdapter(mItemModel.mItemList.getValue()));
            } else {
                mBinding.rootRecycler.getAdapter().notifyDataSetChanged();
            }
        });


        //scrolling
        mBinding.rootRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                switch (newState) {
                    case RecyclerView.SCROLL_STATE_IDLE:
                        mBinding.floatingActionButton.setVisibility(View.VISIBLE);
//                        mBinding.floatingActionButton.animate().alpha(1f).setDuration(1).setListener(null);
                        break;
                    default:
                        mBinding.floatingActionButton.setVisibility(View.GONE);
//                        mBinding.floatingActionButton.animate().alpha(0f).setDuration(0).setListener(null);
                }
            }
        });

        //Add Floating Button
        UserInfoViewModel userinfo = new ViewModelProvider(getActivity()).get(UserInfoViewModel.class);
        mBinding.floatingActionButton.setOnClickListener(button -> {
            //https://www.geeksforgeeks.org/how-to-create-a-custom-alertdialog-in-android/
            // Create an alert builder
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(R.string.title_chatlist_create_new);

            // set the custom layout
            final View customLayout = getLayoutInflater().inflate(R.layout.fragment_chat_list_add_alert, null);
            builder.setView(customLayout);

            // add a button
            builder.setPositiveButton(R.string.button_chatlist_create_pos, (dialog, which) -> {
                // send data from the AlertDialog to the Activity
                EditText editText = customLayout.findViewById(R.id.edit_text_name);
                mAddModel.requestNewChatRoom(editText.getText().toString(), userinfo.getMemberID(), userinfo.getJwt());
                //TODO spinning wait
            });
            builder.setNegativeButton(R.string.button_chatlist_create_neg, (dialog, which) -> {
                dialog.cancel();
            });
            // create and show the alert dialog
            AlertDialog dialog = builder.create();
            dialog.show();
        });
        //Observe mAddModel mResponse & refresh recycler view
        mAddModel.addResponseObserver(
                getViewLifecycleOwner(),
                json -> {
                    mBinding.rootRecycler.getAdapter().notifyDataSetChanged();
                    //TODO stop spinning wait
                }
        );

        //TODO helper to show spinning wait
    }
}