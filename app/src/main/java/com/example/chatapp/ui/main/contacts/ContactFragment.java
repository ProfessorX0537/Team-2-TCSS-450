package com.example.chatapp.ui.main.contacts;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.chatapp.R;
import com.example.chatapp.databinding.FragmentContactsBinding;

public class ContactFragment extends Fragment {


    private ContactsViewModel mModel;
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


        FragmentContactsBinding binding = FragmentContactsBinding.bind(getView());


        binding.listRoot.setAdapter(new ContactRecycleViewAdapter(ContactGenerator.getCardList()));

/*        mModel.addContactsObserver(getViewLifecycleOwner(), contactsList -> {
            if (!contactsList.isEmpty()) {
                binding.listRoot.setAdapter(
                        new ContactRecycleViewAdapter(contactsList)
                );
            }
        });*/
    }
}