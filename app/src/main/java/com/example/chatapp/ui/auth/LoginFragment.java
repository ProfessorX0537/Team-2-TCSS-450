package com.example.chatapp.ui.auth;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.chatapp.R;
import com.example.chatapp.databinding.FragmentLoginBinding;

/**
 * Default landing fragment. <br>
 * - logo <br>
 * - username <br>
 * - password <br>
 * - "login" button <br>
 * - "register" button <br>
 */
public class LoginFragment extends Fragment {

    private FragmentLoginBinding binding;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater);
        // Inflate the layout for this fragment
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Login Button
        binding.buttonLogin.setOnClickListener(button -> {
            if (checkPassword()) {
                Navigation.findNavController(getView()).navigate(
                        LoginFragmentDirections.actionLoginToMainActivity()
                );
            }
        }); //TODO check correct credential and say error

        //Register Button
        binding.buttonRegister.setOnClickListener(button -> {
            Navigation.findNavController(getView()).navigate(
                    LoginFragmentDirections.actionLoginToRegisterFragment()
            );
        });
    }

    private boolean checkPassword() {
        return true; //TODO check correct credential and say error
    }
}