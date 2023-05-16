package com.example.chatapp.ui.main.contacts;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.chatapp.R;
import com.example.chatapp.databinding.FragmentContactsBinding;

public class ContactFragment extends Fragment {

    ContactsViewModel mModel;




//    private ContactsViewModel mModel;
    public ContactFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mModel = new ViewModelProvider(getActivity()).get(ContactsViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contacts, container, false);
//        if (view instanceof RecyclerView) {
//            ((RecyclerView) view).setAdapter(
//                    new ContactRecycleViewAdapter(ContactGenerator.getCardList()));
//        }
        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        //hides floating button on scroll
        FragmentContactsBinding mBinding = FragmentContactsBinding.bind(getView());
        mBinding.addContactFab.setOnClickListener(this::requestConnection);
        //scrolling
        mBinding.listRoot.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                switch (newState) {
                    case RecyclerView.SCROLL_STATE_IDLE:
                        mBinding.addContactFab.setVisibility(View.VISIBLE);
                        break;
                    default:
                        mBinding.addContactFab.setVisibility(View.GONE);
                }
            }
        });

        // checks if user is scrolling up or down and hides search bar accordingly
        mBinding.listRoot.addOnScrollListener(new RecyclerView.OnScrollListener() {
            private int lastFirstVisibleItem;
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {

                int firstVisibleItem = ((LinearLayoutManager)recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
                Log.i("Scroll Y", String.valueOf(firstVisibleItem));

                if(lastFirstVisibleItem<firstVisibleItem){

                    mBinding.searchView.setVisibility(View.GONE);
                    Log.d("RecyclerView scrolled: ", "up!");

                } else if (lastFirstVisibleItem>firstVisibleItem){
                        mBinding.searchView.setVisibility(View.VISIBLE);

                        Log.d("RecyclerView scrolled: ", "down!");
                }


                lastFirstVisibleItem = firstVisibleItem;
            }


        });

        mBinding.listRoot.setAdapter(new ContactRecycleViewAdapter(ContactGenerator.getCardList()));

        mModel.addContactsObserver(getViewLifecycleOwner(), contactsList -> {
            if (!contactsList.isEmpty()) {
                mBinding.listRoot.setAdapter(
                        new ContactRecycleViewAdapter(contactsList)
                );
            }
        });
    }

    private void requestConnection(View view) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        alertDialogBuilder.setView(inflater.inflate(R.layout.dialog_addconnection, null))
                .setPositiveButton(R.string.action_connections_Add, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //TODO: add connection request stuff here.
                    }
                })
                .setNegativeButton(R.string.action_connections_cancel, new DialogInterface.OnClickListener(){

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}