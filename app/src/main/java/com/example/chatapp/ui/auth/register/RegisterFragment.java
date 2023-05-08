package com.example.chatapp.ui.auth.register;

import static com.example.chatapp.utils.PasswordValidator.*;

import android.os.Bundle;
import android.util.Log;
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

import org.json.JSONException;
import org.json.JSONObject;

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
    /**
     * Private field variable binds this fragment to its gradle build resources
     */
    private FragmentRegisterBinding binding;

    /**
     * Private field variable to access RegisterViewModel
     */
    private RegisterViewModel mRegisterModel;

    /**
     * Private field variable to validate user's names
     */
    private PasswordValidator mNameValidator = checkPwdLength(1);

    /**
     * Private field variable to validate user Email
     */
    private PasswordValidator mEmailValidator = checkPwdLength(2)
            .and(checkExcludeWhiteSpace()).and(checkPwdSpecialChar("@"));

    /**
     * Private field variable to validate user passwords
     */
    private PasswordValidator mPassWordValidator = checkClientPredicate(pwd -> pwd.equals(binding.editPassword2.getText().toString()))
            .and(checkPwdLength(7))
            .and(checkPwdSpecialChar())
            .and(checkExcludeWhiteSpace())
            .and(checkPwdDigit())
            .and(checkPwdLowerCase().or(checkPwdUpperCase()));

    /**
     * Empty RegisterFragment constructor
     */
    public RegisterFragment() {
        // Required empty public constructor
    }

    /**
     * Called on fragments creation. Connects ViewModel to this fragment
     * @param savedInstanceState If the fragment is being re-created from
     * a previous saved state, this is the state.
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRegisterModel = new ViewModelProvider(getActivity())
                .get(RegisterViewModel.class);
    }

    /**
     * Called on view creation. Binds and inflates the fragment.
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRegisterBinding.inflate(inflater);
        // Inflate the layout for this fragment
        return binding.getRoot();
    }

    /**
     * Called after view has been created. Sets on click listener for button and attaches
     * an observer to the fragment.
     * @param view The View returned by {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonRegister.setOnClickListener(this::tryRegister);
        mRegisterModel.addResponseObserver(getViewLifecycleOwner(),
                this::observeResponse);
    }

    /**
     * Attempts to register the user.
     * @param button
     */
    private void tryRegister(final View button) {validateFirst();}

    /**
     * Checks that First name meets validation requirements
     */
    private void validateFirst() {
        mNameValidator.processResult(
                mNameValidator.apply(binding.editFirst.getText().toString().trim()),
                this::validateLast,
                result -> binding.editFirst.setError("Please enter a first name."));
    }

    /**
     * Checks that Last name meets validation requirements.
     */
    private void validateLast() {
        mNameValidator.processResult(
                mNameValidator.apply(binding.editLast.getText().toString().trim()),
                this::validateNickName,
                result -> binding.editLast.setError("Please enter a last name."));
    }

    /**
     * Checks that NickName meets validation requirements
     */
    private void validateNickName() {
        mNameValidator.processResult(
                mNameValidator.apply(binding.editUsername.getText().toString().trim()),
                this::validateEmail,
                result -> binding.editUsername.setError("Please enter a username."));
    }

    /**
     * Checks that Email meets validation requirements
     */
    private void validateEmail() {
        mEmailValidator.processResult(
                mEmailValidator.apply(binding.editEmail.getText().toString().trim()),
                this::validatePasswordsMatch,
                result -> binding.editEmail.setError("Please enter a valid Email address."));
    }

    /**
     * Checks that both password fields match each other.
     */
    private void validatePasswordsMatch() {
        PasswordValidator matchValidator = checkClientPredicate(
                pwd -> pwd.equals(binding.editPassword2.getText().toString().trim()));

        mEmailValidator.processResult(
                matchValidator.apply(binding.editPassword1.getText().toString().trim()),
                this::validatePassword,
                result -> binding.editPassword1.setError("Passwords must match."));
    }

    /**
     * Checks that the password meets validation requirements
     */
    private void validatePassword() {
        mPassWordValidator.processResult(
                mPassWordValidator.apply(binding.editPassword1.getText().toString().trim()),
                this::verifyAuthWithServer,
                result -> binding.editPassword1.setError("Please enter a Valid password."));
    }

    /**
     * Passes user information to be formatted into a register request.
     */
    private void verifyAuthWithServer() {
        System.out.println("Calling connect");
        mRegisterModel.connectRegister(
                binding.editFirst.getText().toString(),
                binding.editLast.getText().toString(),
                binding.editEmail.getText().toString(),
                binding.editUsername.getText().toString(),
                binding.editPassword1.getText().toString());
        //This is an Asynchronous call. No statements after should rely on the
        //result of connect().
    }

    /**
     * After request comes back from webservice will navigate to the verify email fragment.
     */
    private void navigateToVerifyEmail() {
//        RegisterFragmentDirections.ActionRegisterFragmentToLogin directions =
//            RegisterFragmentDirections.actionRegisterFragmentToLogin();
//
//
//        directions.setEmail(binding.editEmail.getText().toString());
//        directions.setPassword(binding.editPassword1.getText().toString());

        Navigation.findNavController(getView()).navigate(
                RegisterFragmentDirections.actionRegisterFragmentToVerifyEmailFragment());
    }

    /**
     * An observer on the HTTP Response from the web server. This observer should be
     * attached to RegisterViewModel.
     *
     * @param response the Response from the server
     */
    private void observeResponse(final JSONObject response) {
        //System.out.println(response);
        if (response.length() > 0) {
            if (response.has("code")) {
                try {
                    binding.editEmail.setError(
                            "Error Authenticating: " +
                                    response.getJSONObject("data").getString("message"));
                } catch (JSONException e) {
                    Log.e("JSON Parse Error", e.getMessage());
                }
            } else {
                //System.out.println("Received response that didn't 'fail'");
                navigateToVerifyEmail();
            }
        } else {
            Log.d("JSON Response", "No Response");
        }
    }
}