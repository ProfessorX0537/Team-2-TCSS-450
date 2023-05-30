package com.example.chatapp.ui.main.home;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatapp.R;
import com.example.chatapp.databinding.FragmentHomeMessagesItemBinding;
import com.example.chatapp.utils.SimpleDate;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class HomeMessagesAdapter extends RecyclerView.Adapter<HomeMessagesAdapter.HomeMessagesViewHolder> {

    private final List<HomeMessagesItem> mMessagesItems;
    //make hashmap

    private HomeMessagesItemViewModel mHomeMessagesItemViewModel;

    private final AppCompatActivity mActivity;

    public HomeMessagesAdapter(List<HomeMessagesItem> mMessagesItems, HomeMessagesItemViewModel homeMessagesItemViewModel, AppCompatActivity mActivity) {
        this.mMessagesItems = mMessagesItems;
        this.mActivity = mActivity;
        this.mHomeMessagesItemViewModel = homeMessagesItemViewModel;
    }

    @NonNull
    @Override
    public HomeMessagesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new HomeMessagesViewHolder(LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.fragment_home_messages_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull HomeMessagesViewHolder holder, int position) {
        //holder.mBinding.textView4.setText("hi");
        holder.mBinding.textMessage.setText(mMessagesItems.get(position).getMessage());
        holder.mBinding.textSenderHome.setText(mMessagesItems.get(position).getSender());
        holder.mBinding.textHomeDatetime.setText(SimpleDate.stringDateFromEpochString(mMessagesItems.get(position).getTimeStamp()));

        holder.mBinding.homeMessageCard.setOnClickListener(button -> {
            mHomeMessagesItemViewModel.mChatRoomIdNavigate = mMessagesItems.get(position).getmChatId();
            mHomeMessagesItemViewModel.deleteAllMessagesFromChatRooms(mMessagesItems.get(position).getmChatId());
            BottomNavigationView temp = (mActivity.findViewById(R.id.nav_view));
            temp.setSelectedItemId(R.id.navigation_chat);
        });
    }

    @Override
    public int getItemCount() {
        return mMessagesItems.size();
    }

    @Override
    public long getItemId(int position) {
        return mMessagesItems.get(position).getMessageId();
    }

    public static class HomeMessagesViewHolder extends RecyclerView.ViewHolder {

        public @NonNull FragmentHomeMessagesItemBinding mBinding;

        public HomeMessagesViewHolder(@NonNull View itemView) {
            super(itemView);
            mBinding = FragmentHomeMessagesItemBinding.bind(itemView);
        }
    }
}
