package com.example.chatapp.ui.main.contacts;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.chatapp.R;
import com.example.chatapp.databinding.FragmentContactCardBinding;
import com.example.chatapp.databinding.FragmentLoginBinding;


public class ContactCardFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match


    private FragmentContactCardBinding binding;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ContactCardFragment() {
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
        binding = FragmentContactCardBinding.inflate(inflater);
        // Inflate the layout for this fragment
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.cardRoot.setOnClickListener(button -> {
            Navigation.findNavController(getView()).navigate(
                    ContactFragmentDirections.actionNavigationConnectionsToContactPageFragment()
            );
            Log.i("Button","Pressed");
        });

    }
}