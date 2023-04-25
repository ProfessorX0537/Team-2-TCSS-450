package com.example.chatapp.ui.auth.register;

import static com.example.chatapp.utils.PasswordValidator.*;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.chatapp.databinding.FragmentRegisterBinding;
import com.example.chatapp.utils.PasswordValidator;

/**
 * To register a new user. <br>
 * - username <br>
 * - email <br>
 * - password <br>
 * - password 2nd time to match <br>
 * - "register" button <br>
 * - "cancel" button <br>
 */
public class RegisterFragment extends Fragment {
    private FragmentRegisterBinding binding;

    private RegisterViewModel mRegisterModel;

    private PasswordValidator mNameValidator = checkPwdLength(1);

    private PasswordValidator mEmailValidator = checkPwdLength(2)
            .and(checkExcludeWhiteSpace()).and(checkPwdSpecialChar("@"));

    private PasswordValidator mPassWordValidator = checkClientPredicate(pwd -> pwd.equals(binding.editPassword2.getText().toString()))
            .and(checkPwdLength(9))
            .and(checkPwdSpecialChar())
            .and(checkExcludeWhiteSpace())
            .and(checkPwdDigit())
            .and(checkPwdLowerCase().or(checkPwdUpperCase()));



    public RegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRegisterModel = new ViewModelProvider(getActivity())
                .get(RegisterViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRegisterBinding.inflate(inflater);
        // Inflate the layout for this fragment
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonRegister.setOnClickListener(this::tryRegister);

        //TODO Buttons and stuff
    }

    private void tryRegister(final View button) {validateFirst();}

    private void validateFirst() {
        mNameValidator.processResult(
                mNameValidator.apply(binding.editFirst.getText().toString().trim()),
                this::validateLast,
                result -> binding.editFirst.setError("Please enter a first name."));
    }

    private void validateLast() {
        mNameValidator.processResult(
                mNameValidator.apply(binding.editLast.getText().toString().trim()),
                this::validateNickName,
                result -> binding.editLast.setError("Please enter a last name."));
    }

    private void validateNickName() {
        mNameValidator.processResult(
                mNameValidator.apply(binding.editNickname.getText().toString().trim()),
                this::validateEmail,
                result -> binding.editNickname.setError("Please enter a nickname."));
    }

    private void validateEmail() {
        mEmailValidator.processResult(
                mEmailValidator.apply(binding.editEmail.getText().toString().trim()),
                this::validatePasswordsMatch,
                result -> binding.editEmail.setError("Please enter a valid Email address."));
    }

    private void validatePasswordsMatch() {
        PasswordValidator matchValidator = checkClientPredicate(
                pwd -> pwd.equals(binding.editPassword2.getText().toString().trim()));

        mEmailValidator.processResult(
                matchValidator.apply(binding.editPassword1.getText().toString().trim()),
                this::validatePassword,
                result -> binding.editPassword1.setError("Passwords must match."));
    }

    private void validatePassword() {
        mPassWordValidator.processResult(
                mPassWordValidator.apply(binding.editPassword1.getText().toString().trim()),
                this::verifyAuthWithServer,
                result -> binding.editPassword1.setError("Please enter a Valid password."));
    }

    private void verifyAuthWithServer() {
        //TODO: add server registration calls here

        //temporarily
        Navigation.findNavController(getView()).navigate(
                RegisterFragmentDirections.actionRegisterFragmentToVerifyEmailFragment());
    }

    // TODO: navigate to email verification after registration complete

    // TODO: observer response method that whats for Async server call to respond
}