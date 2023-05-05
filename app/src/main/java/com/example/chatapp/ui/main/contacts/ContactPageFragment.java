package com.example.chatapp.ui.main.contacts;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.chatapp.R;
import com.example.chatapp.databinding.FragmentContactCardBinding;
import com.example.chatapp.databinding.FragmentContactPageBinding;


public class ContactPageFragment extends Fragment {


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ContactPageFragmentArgs args = ContactPageFragmentArgs.fromBundle(getArguments());

        FragmentContactPageBinding binding = FragmentContactPageBinding.bind(getView());

        binding.email.setText(args.getContact().getEmail());

        binding.fullname.setText(args.getContact().getName());

        binding.nickname.setText(args.getContact().getNick());




    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_contact_page, container, false);
    }
}