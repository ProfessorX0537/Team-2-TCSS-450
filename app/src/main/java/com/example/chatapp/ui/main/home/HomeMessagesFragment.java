package com.example.chatapp.ui.main.home;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.chatapp.databinding.FragmentHomeMessagesBinding;

public class HomeMessagesFragment extends Fragment {

    private FragmentHomeMessagesBinding mBinding;

    private HomeMessagesItemViewModel mHomeMessagesItemViewModel;

    private int adapterTwice = 0;

    public HomeMessagesFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_home_messages, container, false);
        mBinding = FragmentHomeMessagesBinding.inflate(inflater);
        mHomeMessagesItemViewModel = new ViewModelProvider(getActivity()).get(HomeMessagesItemViewModel.class);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mHomeMessagesItemViewModel.addHomeMessageObserver(getViewLifecycleOwner(), list -> {
            if (list.size() == 0){
                mBinding.textNoMessages.setVisibility(View.VISIBLE);
            } else {
                mBinding.textNoMessages.setVisibility(View.GONE);
            }


            if (mBinding.rootRecycler.getAdapter() == null) {
                mBinding.rootRecycler.setAdapter(new HomeMessagesAdapter(
                        mHomeMessagesItemViewModel.getHomeMessageList().getValue(),
                        mHomeMessagesItemViewModel,
                        (AppCompatActivity) getActivity()));
                mBinding.rootRecycler.getAdapter().notifyDataSetChanged();
            } else {
                mBinding.rootRecycler.getAdapter().notifyDataSetChanged();
            }

            if (adapterTwice < 2) { //jank TODO idk
                mBinding.rootRecycler.setAdapter(new HomeMessagesAdapter(
                        mHomeMessagesItemViewModel.getHomeMessageList().getValue(),
                        mHomeMessagesItemViewModel,
                        (AppCompatActivity) getActivity()));
                mBinding.rootRecycler.getAdapter().notifyDataSetChanged();
                adapterTwice++;
            }
        });
        mBinding.textNoMessages.setVisibility(View.GONE);



        //test to see notif

//        List<HomeMessagesItem> items = new ArrayList<>();
//        items.add(new HomeMessagesItem(0, "hi", "philip", "11", 0));
//        items.add(new HomeMessagesItem(0, "hi", "philip", "11", 0));
//        items.add(new HomeMessagesItem(0, "hi", "philip", "11", 0));
//        items.add(new HomeMessagesItem(0, "hi", "philip", "11", 0));
//        items.add(new HomeMessagesItem(0, "hi", "philip", "11", 0));
//
//        mBinding.rootRecycler.setAdapter(new HomeMessagesAdapter(
//                items,
//                mHomeMessagesItemViewModel,
//                (AppCompatActivity) getActivity()));
////////////////////////////////////////////////////////////////
    }
}