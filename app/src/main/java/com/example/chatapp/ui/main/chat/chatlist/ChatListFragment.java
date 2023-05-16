package com.example.chatapp.ui.main.chat.chatlist;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.chatapp.databinding.FragmentChatListBinding;
import com.example.chatapp.model.UserInfoViewModel;

public class ChatListFragment extends Fragment {
    private ChatListItemViewModel mModel;
    private FragmentChatListBinding mBinding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //UserInfoViewModel
        UserInfoViewModel userinfo = new ViewModelProvider(getActivity()).get(UserInfoViewModel.class);

        mModel = new ViewModelProvider(getActivity()).get(ChatListItemViewModel.class);
        mModel.getChatRooms(userinfo.getMemberID(), userinfo.getJwt());
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
        mModel.addItemListObserver(getViewLifecycleOwner(), list -> {
            if (mBinding.rootRecycler.getAdapter() == null) {
                mBinding.rootRecycler.setAdapter(new ChatListAdapter(mModel.mItemList.getValue()));
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
    }
}