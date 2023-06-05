package com.example.chatapp.ui.main.changepass;

import static com.example.chatapp.utils.PasswordValidator.checkClientPredicate;
import static com.example.chatapp.utils.PasswordValidator.checkExcludeWhiteSpace;
import static com.example.chatapp.utils.PasswordValidator.checkPwdDigit;
import static com.example.chatapp.utils.PasswordValidator.checkPwdLength;
import static com.example.chatapp.utils.PasswordValidator.checkPwdLowerCase;
import static com.example.chatapp.utils.PasswordValidator.checkPwdSpecialChar;
import static com.example.chatapp.utils.PasswordValidator.checkPwdUpperCase;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.chatapp.R;
import com.example.chatapp.databinding.FragmentChangepassBinding;
import com.example.chatapp.databinding.FragmentRegisterBinding;
import com.example.chatapp.model.UserInfoViewModel;
import com.example.chatapp.ui.auth.register.RegisterFragmentDirections;
import com.example.chatapp.ui.auth.register.RegisterViewModel;
import com.example.chatapp.ui.main.home.HomeFragment;
import com.example.chatapp.utils.PasswordValidator;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Fragment that will allow a user to change their password
 * @author Xavier Hines
 */
public class ChangePassFragment extends Fragment {
    /**
     * Private field variable binds this fragment to its gradle build resources
     */
    private FragmentChangepassBinding binding;

    /**
     * Private field variable to access RegisterViewModel
     */
    private ChangePassViewModel mChangePassModel;
    private UserInfoViewModel mUserInfoViewModel;

    /**
     * Private field variable to validate user's names
     */
    private PasswordValidator mNameValidator = checkPwdLength(1);

    /**
     * Private field variable to validate user Email
     */
    private PasswordValidator mEmailValidator = checkPwdLength(2)
            .and(checkExcludeWhiteSpace()).and(checkPwdSpecialChar("@+"));

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
     * Empty ChangePassFragment constructor
     */
    public ChangePassFragment() {}

    /**
     * Called on fragments creation. Connects ViewModel to this fragment
     * @param savedInstanceState If the fragment is being re-created from
     * a previous saved state, this is the state.
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mChangePassModel = new ViewModelProvider(getActivity())
                .get(ChangePassViewModel.class);
        mUserInfoViewModel = new ViewModelProvider(getActivity())
                .get(UserInfoViewModel.class);
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
        binding = FragmentChangepassBinding.inflate(inflater);
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
        binding.buttonRegister.setOnClickListener(this::tryChangePassword);
        mChangePassModel.addResponseObserver(getViewLifecycleOwner(),
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
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
            alertDialogBuilder.setMessage(R.string.snackbar_register_password_requirements);
            alertDialogBuilder.setTitle("Password Requirements");
            alertDialogBuilder.setNegativeButton("ok", new DialogInterface.OnClickListener(){

                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }
        return false;
    }

    /**
     * Attempts to register the user.
     * @param button
     */
    private void tryChangePassword(final View button) {validatePasswordsMatch();}

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
        //TODO change WS endpoint to take old pass and validate it.
        mChangePassModel.connectChangePassword(
                binding.editOldPassword.getText().toString(),
                binding.editPassword1.getText().toString(),
                mUserInfoViewModel
        );
    }

    /**
     * After request comes back from webservice will navigate to the verify email fragment.
     */
    private void navigateToLandingPage() {
        Navigation.findNavController(getView()).navigate(
                ChangePassFragmentDirections.actionNavigationChangepassToNavigationHome());
    }

    /**
     * An observer on the HTTP Response from the web server. This observer should be
     * attached to ChangePassViewModel.
     *
     * @param response the Response from the server
     */
    private void observeResponse(final JSONObject response) {
        //System.out.println(response);
        if (response.length() > 0) {
            if (response.has("code")) {
                try {
                    binding.editOldPassword.setError(
                            "Error Authenticating: " +
                                    response.getJSONObject("data").getString("message"));
                } catch (JSONException e) {
                    Log.e("JSON Parse Error", e.getMessage());
                }
            } else {
                navigateToLandingPage();
            }
        } else {
            Log.d("JSON Response", "No Response");
        }
    }
}
