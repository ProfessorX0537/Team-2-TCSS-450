package com.example.chatapp.ui.auth.login;

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
import com.example.chatapp.databinding.FragmentForgotPasswordResetBinding;
import com.example.chatapp.utils.PasswordValidator;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Fragment responsible for letting a user reset their password.
 * @author Xavier Hines
 */
public class ForgotPasswordResetFragment extends Fragment {
    /**
     * Private field variable binds this fragment to its gradle build resources
     */
    private FragmentForgotPasswordResetBinding mbinding;

    /**
     * Private field variable to access LoginViewModel
     */
    private LoginViewModel mLoginViewModel;

    /**
     * Private field variable to validate user Email
     */
    private PasswordValidator mEmailValidator = checkPwdLength(2)
            .and(checkExcludeWhiteSpace()).and(checkPwdSpecialChar("@"));

    /**
     * Private field variable to validate user passwords
     */
    private PasswordValidator mPassWordValidator = checkClientPredicate(pwd -> pwd.equals(mbinding.editPassword2.getText().toString()))
            .and(checkPwdLength(7))
            .and(checkPwdSpecialChar())
            .and(checkExcludeWhiteSpace())
            .and(checkPwdDigit())
            .and(checkPwdLowerCase().or(checkPwdUpperCase()));

    /**
     * Called on fragments creation. Connects ViewModel to this fragment
     * @param savedInstanceState If the fragment is being re-created from
     *                           a previous saved state, this is the state.
     */
    public void onCreate(@NonNull Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLoginViewModel = new ViewModelProvider(getActivity()).get(LoginViewModel.class);
    }

    /**
     * Called on view creation. Binds and inflates the fragment.
     * @param inflater           The LayoutInflater object that can be used to inflate
     *                           any views in the fragment,
     * @param container          If non-null, this is the parent view that the fragment's
     *                           UI should be attached to.  The fragment should not add the view itself,
     *                           but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     *                           from a previous saved state as given here.
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mbinding = FragmentForgotPasswordResetBinding.inflate(inflater);
        // Inflate the layout for this fragment
        return mbinding.getRoot();
    }

    /**
     * Called after view has been created. Sets on click listener for button and attaches
     * an observer to the fragment.
     * @param view               The View returned by {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     *                           from a previous saved state as given here.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //hide password edit texts until clicked email sent
        mbinding.editPassword1.setVisibility(View.GONE);
        mbinding.editPassword2.setVisibility(View.GONE);
        mbinding.buttonReset.setVisibility(View.GONE);

        mbinding.editPassword1.setOnTouchListener(this::passwordReqs);
        mbinding.buttonReset.setOnClickListener(this::resetPassword);
        mbinding.buttonEmail.setOnClickListener(this::sendResetEmail);
        mLoginViewModel.addResponseObserver(getViewLifecycleOwner(),
                response -> {
                    try {
                        observeResponse(response);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                });

        mLoginViewModel.mIsSentForgetEmail.observe(getViewLifecycleOwner(), bool -> {
            // sent email
            if (bool) {
                //show password edit texts
                mbinding.editPassword1.setVisibility(View.VISIBLE);
                mbinding.editPassword2.setVisibility(View.VISIBLE);
                mbinding.buttonReset.setVisibility(View.VISIBLE);

                //tell user to check email
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
                alertDialogBuilder.setTitle("Email sent");
                alertDialogBuilder.setNegativeButton("ok", new DialogInterface.OnClickListener(){

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });
    }

    private void sendResetEmail(View view) {
        mLoginViewModel.connectResetPasswordEmail(mbinding.editEmail.getText().toString());
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
     * Method that is entrance to validation
     * @param view
     */
    private void resetPassword(View view) {validateEmail();}

    /**
     * Checks that Email meets validation requirements
     */
    private void validateEmail() {
        mEmailValidator.processResult(
                mEmailValidator.apply(mbinding.editEmail.getText().toString().trim()),
                this::validatePasswordsMatch,
                result -> mbinding.editEmail.setError("Please enter a valid Email address."));
    }

    /**
     * Checks that both password fields match each other.
     */
    private void validatePasswordsMatch() {
        PasswordValidator matchValidator = checkClientPredicate(
                pwd -> pwd.equals(mbinding.editPassword2.getText().toString().trim()));

        mEmailValidator.processResult(
                matchValidator.apply(mbinding.editPassword1.getText().toString().trim()),
                this::validatePassword,
                result -> mbinding.editPassword1.setError("Passwords must match."));
    }

    /**
     * Checks that the password meets validation requirements
     */
    private void validatePassword() {
        mPassWordValidator.processResult(
                mPassWordValidator.apply(mbinding.editPassword1.getText().toString().trim()),
                this::verifyAuthWithServer,
                result -> mbinding.editPassword1.setError("Please enter a Valid password."));
    }

    /**
     * Passes user information to be formatted into a register request.
     */
    private void verifyAuthWithServer() {
        System.out.println("Calling connect");
        mLoginViewModel.connectResetPassword(
                mbinding.editEmail.getText().toString(),
                mbinding.editPassword1.getText().toString());
    }

    /**
     * method called to navigate from this fragment to the Login page
     */
    private void navigateToLoginPage() {
        Navigation.findNavController(getView())
                .navigate(ForgotPasswordResetFragmentDirections
                        .actionForgotPasswordResetFragmentToLogin());
    }

    /**
     * An observer on the HTTP Response from the web server. This observer should be
     * attached to LoginViewModel.
     *
     * @param response the Response from the server
     */
    private void observeResponse(final JSONObject response) throws JSONException {
        //System.out.println(response);
        if (response.length() > 0) {
            if (response.has("code")) {
                try {
                    mbinding.editPassword1.setError(
                            "Error Authenticating: " +
                                    response.getJSONObject("data").getString("message"));
                } catch (JSONException e) {
                    Log.e("JSON Parse Error", e.getMessage());
                }
            } else if (response.has("success")){
                //System.out.println("Received response that didn't 'fail'");
                    navigateToLoginPage();

            }
        } else {
            Log.d("JSON Response", "No Response");
        }
    }
}
