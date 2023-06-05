package com.example.chatapp.ui.auth.register;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.chatapp.R;
import com.example.chatapp.databinding.FragmentRegisterBinding;
import com.example.chatapp.databinding.FragmentVerifyEmailBinding;
import com.example.chatapp.ui.auth.register.VerifyEmailFragmentDirections;

/**
 * When register succeed, send verification to email, then move user back to Login. <br>
 * - "understood" button
 */
public class VerifyEmailFragment extends Fragment {
    private FragmentVerifyEmailBinding binding;
    public VerifyEmailFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentVerifyEmailBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonVerifyEmail.setOnClickListener(
                button -> Navigation.findNavController(getView()).navigate(
                        VerifyEmailFragmentDirections.actionVerifyEmailFragmentToLogin())
        );
    }
}