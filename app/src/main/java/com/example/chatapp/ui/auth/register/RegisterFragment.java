package com.example.chatapp.ui.auth.register;

import static com.example.chatapp.utils.PasswordValidator.*;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.chatapp.R;
import com.example.chatapp.databinding.FragmentRegisterBinding;
import com.example.chatapp.utils.PasswordValidator;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Fragment that will allow a new user to register with our service so that they can
 * login to our application.
 * @author Xavier Hines
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

        binding.editPassword1.setOnTouchListener(this::passwordReqs);
        binding.buttonRegister.setOnClickListener(this::tryRegister);
        mRegisterModel.addResponseObserver(getViewLifecycleOwner(),
                this::observeResponse);
    }

    /**
     * Method called when the first password editText is touched. This informs
     * the user of the minimum requirements of a password.
     * @param view The View returned by {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}.
     * @param motionEvent The event registered by the system
     * @return boolean that dictates whether or not to suppress keyboard.
     */
    private boolean passwordReqs(View view, MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
//            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
//            alertDialogBuilder.setMessage(R.string.snackbar_register_password_requirements);
//            alertDialogBuilder.setTitle("Password Requirements");
//            alertDialogBuilder.setNegativeButton("ok", new DialogInterface.OnClickListener(){
//
//                @Override
//                public void onClick(DialogInterface dialogInterface, int i) {
//                }
//            });
//            AlertDialog alertDialog = alertDialogBuilder.create();
//            alertDialog.show();
            Snackbar snackbar = Snackbar.make(view, R.string.snackbar_register_password_requirements, Snackbar.LENGTH_LONG);
            View view1 = snackbar.getView();
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams)view1.getLayoutParams();
            params.gravity = Gravity.TOP;
            view1.setLayoutParams(params);
            snackbar.show();
        }
        return false;
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