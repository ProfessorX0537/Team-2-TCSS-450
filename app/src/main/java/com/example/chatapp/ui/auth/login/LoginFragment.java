package com.example.chatapp.ui.auth.login;

import static com.example.chatapp.utils.PasswordValidator.checkClientPredicate;
import static com.example.chatapp.utils.PasswordValidator.checkExcludeWhiteSpace;
import static com.example.chatapp.utils.PasswordValidator.checkPwdDigit;
import static com.example.chatapp.utils.PasswordValidator.checkPwdLength;
import static com.example.chatapp.utils.PasswordValidator.checkPwdLowerCase;
import static com.example.chatapp.utils.PasswordValidator.checkPwdSpecialChar;
import static com.example.chatapp.utils.PasswordValidator.checkPwdUpperCase;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.chatapp.AuthActivity;
import com.example.chatapp.R;
import com.example.chatapp.databinding.FragmentLoginBinding;
import com.example.chatapp.model.PushyTokenViewModel;
import com.example.chatapp.model.UserInfoViewModel;
import com.example.chatapp.utils.ConnectionViewModel;
import com.example.chatapp.utils.PasswordValidator;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Default landing fragment. <br>
 * - logo <br>
 * - username <br>
 * - password <br>
 * - "login" button <br>
 * - "register" button <br>
 * Much of the code comes form Charles Bryan lab modified by Xavier Hines
 * to fit the needs of our app.
 *
 * @author Charles Byran
 * @author Xavier Hines
 * @author David Hunyah
 */
public class LoginFragment extends Fragment {

    /**
     * Private field variable binds this fragment to its gradle build resources
     */
    private FragmentLoginBinding binding;

    /**
     * Private field variable to access LoginViewModel
     */
    private LoginViewModel mLoginViewModel;

    /**
     * ViewModel to test connection to server
     */
    private ConnectionViewModel mConnectionViewModel;

    /**
     * PushyTokenViewModel to get token from Pushy servers
     */
    private PushyTokenViewModel mPushyTokenViewModel;

    /**
     * ViewModel that holds user's info
     */
    private UserInfoViewModel mUserViewModel;

    /**
     * Private field variable to validate user Email
     */
    private PasswordValidator mEmailValidator = checkPwdLength(2)
            .and(checkExcludeWhiteSpace()).and(checkPwdSpecialChar("@"));

    /**
     * Private field variable to validate user passwords
     */
    private PasswordValidator mPassWordValidator = checkClientPredicate(pwd -> pwd.equals(binding.textPassword.getText().toString()))
            .and(checkPwdLength(9))
            .and(checkPwdSpecialChar())
            .and(checkExcludeWhiteSpace())
            .and(checkPwdDigit())
            .and(checkPwdLowerCase().or(checkPwdUpperCase()));

    /**
     * Empty LoginFragment constructor
     */
    public LoginFragment() {
        // Required empty public constructor
    }

    /**
     * Called on fragments creation. Connects ViewModel to this fragment
     *
     * @param savedInstanceState If the fragment is being re-created from
     *                           a previous saved state, this is the state.
     */
    public void onCreate(@NonNull Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        //ViewModels
        mLoginViewModel = new ViewModelProvider(getActivity()).get(LoginViewModel.class);
        mConnectionViewModel = new ViewModelProvider(getActivity()).get(ConnectionViewModel.class);
        mPushyTokenViewModel = new ViewModelProvider(getActivity())
                .get(PushyTokenViewModel.class);
    }

    /**
     * Called on view creation. Binds and inflates the fragment.
     *
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

        binding = FragmentLoginBinding.inflate(inflater);
        // Inflate the layout for this fragment
        return binding.getRoot();
    }

    /**
     * Called after view has been created. Sets on click listener for button and attaches
     * an observer to the fragment.
     *
     * @param view               The View returned by {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     *                           from a previous saved state as given here.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mLoginViewModel.addResponseObserver(
                getViewLifecycleOwner(),
                this::observeSignInResponse
        );

        //WS connection observe. Make login/register buttons available
        mConnectionViewModel.addResponseObserver(
                getViewLifecycleOwner(),
                (result) -> {
                    ((AuthActivity) getActivity()).setConnectedWS(result);
                    observeConnectionResponse();
                }
        );
        //Pushy Token observe. Make login/register buttons available
        mPushyTokenViewModel.addTokenObserver(
                getViewLifecycleOwner(),
                (token) -> {
                    Log.i("Pushy Token", "token: " + token);
                    ((AuthActivity) getActivity()).setConnectedPushy(!token.isEmpty());
                    observeConnectionResponse();
                }
        );

        //Pushy token observer. Sends to webservice when done.
        mPushyTokenViewModel.addResponseObserver(
                getViewLifecycleOwner(),
                this::observePushyPutResponse);

        //comment this out to login w/o actually having to sign in.
        binding.buttonLogin.setVisibility(View.GONE);
        binding.buttonLogin.setOnClickListener(this::attemptLogin);
        binding.textForgotPassword.setOnClickListener(this::forgotPassword);

        LoginFragmentArgs args = LoginFragmentArgs.fromBundle(getArguments());
        binding.textEmail.setText(args.getEmail().equals("default") ? "" : args.getEmail()); //TODO
        binding.textPassword.setText(args.getPassword().equals("default") ? "" : args.getPassword()); //TODO

        //TODO Remove this placeholder account when shipping
        binding.textEmail.setText("test1@test.com");
        binding.textPassword.setText("Password123!");

        //Register Button
        binding.buttonRegister.setVisibility(View.GONE);
        binding.buttonRegister.setOnClickListener(button -> {
            Navigation.findNavController(getView()).navigate(
                    LoginFragmentDirections.actionLoginToRegisterFragment()
            );
        });
    }

    /**
     * Attempts to log the user into the application
     *
     * @param button
     */
    private void attemptLogin(final View button) {
        showLoginRegisterButtons(false, R.string.logging_in); //String is unused btw
        validateEmail();
    }

    /**
     * Checks that the password meets validation requirements
     */
    private void validateEmail() { //TODO remove No longer using email
        mEmailValidator.processResult(
                mEmailValidator.apply(binding.textEmail.getText().toString().trim()),
                this::validatePassword,
                result -> {
                    binding.textEmail.setError("Please enter a valid Email address.");
                    showLoginRegisterButtons(true, R.string.establishing_connection); //Reshow buttons
                });
    }

    /**
     * Checks that the password meets validation requirements
     */
    private void validatePassword() {
        mPassWordValidator.processResult(
                mPassWordValidator.apply(binding.textPassword.getText().toString().trim()),
                this::verifyAuthWithServer,
                result -> {
                    binding.textPassword.setError("Please enter a Valid password.");
                    showLoginRegisterButtons(true, R.string.establishing_connection); //Reshow buttons
                });
    }

    /**
     * Attempts to verify users credentials with the credentials in the data base.
     */
    private void verifyAuthWithServer() {
        mLoginViewModel.connectLogin(
                binding.textEmail.getText().toString(),
                binding.textPassword.getText().toString());
        //This is an Asynchronous call. No statements after should rely on the result of connect().
    }

    /**
     * If successfully verified with the server the user will be navigated
     * to the landing page and their email and JWT will be passed to
     * userinfo view-model for access inside of the application.
     *
     * @param email    User's email
     * @param jwt      User's JWT
     * @param memberID User's memberID
     * @param username User's username
     */
    private void navigateToHome(final String email, final String jwt, final int memberID, final String username) {
        //Save autologin shared prefs
        SharedPreferences prefs =
                getActivity().getSharedPreferences(
                        getString(R.string.keys_shared_prefs),
                        Context.MODE_PRIVATE);
        prefs.edit().putString(getString(R.string.keys_prefs_email), email).apply();
        prefs.edit().putString(getString(R.string.keys_prefs_jwt), jwt).apply();
        prefs.edit().putString(getString(R.string.keys_prefs_memberid), ""+memberID).apply();
        prefs.edit().putString(getString(R.string.keys_prefs_username), username).apply();

        //Ok, now navigate
        Navigation.findNavController(getView())
                .navigate(LoginFragmentDirections
                        .actionLoginToMainActivity(email, jwt, memberID, username));
    }

    /**
     * Will resend the verification message during the login process if user isn't
     * verified in our DB
     * @param view
     */
    private void resendVerificationLogin(View view) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext())
                .setPositiveButton(R.string.alert_action_login_negative, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mLoginViewModel.connectResendVerification(binding.textEmail.getText().toString());
                    }
                })
                .setNegativeButton(R.string.alert_action_login_positive, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        alertDialogBuilder.setMessage(R.string.alert_message_login_unverified);
        alertDialogBuilder.setTitle(R.string.alert_title_login_unverified_email);
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }



    /**
     * called when user forgets password and wishes to reset it.
     * @param view
     */
    private void forgotPassword(View view) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
        alertDialogBuilder.setMessage(R.string.alert_login_forgot_password_message);
        alertDialogBuilder.setTitle("Forgot Password?");
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_generic_edit_text, null);
        alertDialogBuilder.setView(dialogView);
        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                EditText edit = (EditText) dialogView.findViewById(R.id.edit_text_generic);
                if(edit == null){
                    Log.v("Add","edit text is null");
                    return;
                }
                String text=edit.getText().toString();
                mLoginViewModel.connectResetPasswordEmail(text);
                //TODO navigate to forgot password fragment and send email http request
                Navigation.findNavController(getView())
                        .navigate(LoginFragmentDirections.actionLoginToForgotPasswordFragment());
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    /**
     * An observer on the HTTP Response from the web server. This observer should be
     * attached to LoginViewModel.
     *
     * @param response the Response from the server
     */
    private void observeSignInResponse(final JSONObject response) {
        if (response.length() > 0) {
            if (response.has("code")) {
                try {
                    binding.textEmail.setError(
                            "Error Authenticating: " +
                                    response.getJSONObject("data").getString("message"));
                    if (response.getJSONObject("data").getString("message").equals("Account needs verification.")) {
                        resendVerificationLogin(this.getView());
                    }
                } catch (JSONException e) {
                    Log.e("JSON Parse Error", e.getMessage());
                }
                showLoginRegisterButtons(true, R.string.establishing_connection); //Reshow buttons
            } else {
                try {

                    //make UserInfo ViewModel here & sendPushyToken to move to Home
                    mUserViewModel = new ViewModelProvider(getActivity(),
                            new UserInfoViewModel.UserInfoViewModelFactory(
                                    binding.textEmail.getText().toString().trim(),
                                    response.getString("token"),
                                    response.getInt("memberid"),
                                    response.getString("username")
                            )).get(UserInfoViewModel.class);
                    sendPushyToken();

                    //Old move to home on sucess
//                    navigateToHome(
//                            binding.textEmail.getText().toString(),
//                            response.getString("token")
//                    );

                } catch (JSONException e) {
                    Log.e("JSON Parse Error", e.getMessage());
                    showLoginRegisterButtons(true, R.string.establishing_connection); //Reshow buttons
                }
            }
        } else {
            Log.d("JSON Response", "No Response");
            showLoginRegisterButtons(true, R.string.establishing_connection); //Reshow buttons
        }
    }

    private void observeConnectionResponse() {
        //check if WS and Pushy is online
        if (((AuthActivity) getActivity()).isConnectedWS() && ((AuthActivity) getActivity()).isConnectedPushy()) {
            showLoginRegisterButtons(true, R.string.establishing_connection); //Reshow buttons. String is unused btw
        } else {
            showLoginRegisterButtons(false, R.string.establishing_connection);
        }
    }

    private void showLoginRegisterButtons(boolean show, int message) {
        if (show) {
            binding.buttonRegister.setVisibility(View.VISIBLE);
            binding.buttonLogin.setVisibility(View.VISIBLE);
            binding.textNotConnected.setVisibility(View.GONE);
        } else {
            binding.buttonRegister.setVisibility(View.GONE);
            binding.buttonLogin.setVisibility(View.GONE);
            binding.textNotConnected.setVisibility(View.VISIBLE);
            binding.textNotConnected.setText(message);
        }
    }

    /**
     * Helper to abstract the request to send the pushy token to the web service
     */
    private void sendPushyToken() {
        mPushyTokenViewModel.sendTokenToWebservice(mUserViewModel.getJwt());
    }

    /**
     * An observer on the HTTP Response from the web server. This observer should be
     * attached to PushyTokenViewModel.
     *
     * @param response the Response from the server
     */
    private void observePushyPutResponse(final JSONObject response) {
        if (response.length() > 0) {
            if (response.has("code")) {
                //this error cannot be fixed by the user changing credentials...
                binding.textEmail.setError(
                        "Error Authenticating on Push Token. Please contact support");
                showLoginRegisterButtons(true, R.string.establishing_connection); //Reshow buttons
            } else {
                navigateToHome(
                        binding.textEmail.getText().toString(),
                        mUserViewModel.getJwt(),
                        mUserViewModel.getMemberID(),
                        mUserViewModel.getUsername()
                );
                //close AuthActivity
                getActivity().finish();
            }
        }
    }

    /**
     * OnStart() <br>
     * - Finds shared prefs and autologin if credentials exists.
     */
    @Override
    public void onStart() {
        super.onStart();
        SharedPreferences prefs =
                getActivity().getSharedPreferences(
                        getString(R.string.keys_shared_prefs),
                        Context.MODE_PRIVATE);
        if (prefs.contains(getString(R.string.keys_prefs_email))
                && prefs.contains(getString(R.string.keys_prefs_jwt))
                && prefs.contains(getString(R.string.keys_prefs_memberid))
                && prefs.contains(getString(R.string.keys_prefs_username))
        ) {
            String email = prefs.getString(getString(R.string.keys_prefs_email), "");
            String token = prefs.getString(getString(R.string.keys_prefs_jwt), "");
            String memberid = prefs.getString(getString(R.string.keys_prefs_memberid), "");
            String username = prefs.getString(getString(R.string.keys_prefs_username), "");

            navigateToHome(email, token, Integer.parseInt(memberid), username);
        }
    }
}