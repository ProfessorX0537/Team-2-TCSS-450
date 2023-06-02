package com.example.chatapp.ui.main.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatapp.R;
import com.example.chatapp.databinding.FragmentHomeRequestsItemBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class HomeRequestsAdapter extends RecyclerView.Adapter<HomeRequestsAdapter.HomeRequestsViewHolder> {

    private final List<HomeRequestsItem> mRequestsItems;

    private HomeRequestsItemViewModel mHomeRequestsItemViewModel;

    private final AppCompatActivity mActivity;

    public HomeRequestsAdapter(List<HomeRequestsItem> mRequestsItems, HomeRequestsItemViewModel mHomeRequestsItemViewModel, AppCompatActivity mActivity) {
        this.mRequestsItems = mRequestsItems;
        this.mHomeRequestsItemViewModel = mHomeRequestsItemViewModel;
        this.mActivity = mActivity;
    }


    @NonNull
    @Override
    public HomeRequestsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new HomeRequestsViewHolder(LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.fragment_home_requests_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull HomeRequestsViewHolder holder, int position) {
        holder.mBinding.textSenderHome.setText(mRequestsItems.get(position).getSender());
        holder.mBinding.textHomeDatetime.setText(mRequestsItems.get(position).getTimeStamp());

        holder.mBinding.homeRequestCard.setOnClickListener(button -> {
            //mHomeMessagesItemViewModel.deleteAllMessagesFromChatRooms(mMessagesItems.get(position).getmChatId());
            BottomNavigationView temp = (mActivity.findViewById(R.id.nav_view));
            temp.setSelectedItemId(R.id.navigation_connections);
        });
    }

    @Override
    public int getItemCount() {
        return mRequestsItems.size();
    }

    @Override
    public long getItemId(int position) {
        return mRequestsItems.get(position).getSender().hashCode();
    }

    public static class HomeRequestsViewHolder extends RecyclerView.ViewHolder {

        public @NonNull FragmentHomeRequestsItemBinding mBinding;

        public HomeRequestsViewHolder(@NonNull View itemView) {
            super(itemView);
            mBinding = FragmentHomeRequestsItemBinding.bind(itemView);
        }
    }
}
