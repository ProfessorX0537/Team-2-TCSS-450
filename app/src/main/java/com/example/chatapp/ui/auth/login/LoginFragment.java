package com.example.chatapp.ui.auth.login;

import static com.example.chatapp.utils.PasswordValidator.checkClientPredicate;
import static com.example.chatapp.utils.PasswordValidator.checkExcludeWhiteSpace;
import static com.example.chatapp.utils.PasswordValidator.checkPwdDigit;
import static com.example.chatapp.utils.PasswordValidator.checkPwdLength;
import static com.example.chatapp.utils.PasswordValidator.checkPwdLowerCase;
import static com.example.chatapp.utils.PasswordValidator.checkPwdSpecialChar;
import static com.example.chatapp.utils.PasswordValidator.checkPwdUpperCase;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.chatapp.R;
import com.example.chatapp.databinding.FragmentLoginBinding;
import com.example.chatapp.ui.auth.login.LoginFragmentDirections;
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
 * @author Charles Byran
 * @author Xavier Hines
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
    private ConnectionViewModel mConnectionViewModel;

    /**
     * Private field variable to validate user Email
     */
    private PasswordValidator mEmailValidator = checkPwdLength(2)
            .and(checkExcludeWhiteSpace()).and(checkPwdSpecialChar("@"));

    /**
     * Private field variable to validate user passwords
     */
    private PasswordValidator mPassWordValidator = checkClientPredicate(pwd -> pwd.equals(binding.textPassword.getText().toString()))
            .and(checkPwdLength(7))
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
     * @param savedInstanceState If the fragment is being re-created from
     * a previous saved state, this is the state.
     */
    public void onCreate(@NonNull Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        mLoginViewModel = new ViewModelProvider(getActivity()).get(LoginViewModel.class);

        mConnectionViewModel = new ViewModelProvider(getActivity()).get(ConnectionViewModel.class);
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

        binding = FragmentLoginBinding.inflate(inflater);
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

        mLoginViewModel.addResponseObserver(
                getViewLifecycleOwner(),
                this::observeResponse
        );

        mConnectionViewModel.addResponseObserver(
                getViewLifecycleOwner(),
                this::observeConnectionResponse
        );

        ///////////////////////////////////////////////////////////////////////////////////////
        // Uncomment this to have login button send you to landing page w/o sign in.
        // ////////////////////////////////////////////////////////////////////////////////////
        //NOTICE: this doesn't have a JWT or email so request in app that require it will fail.
//        binding.buttonLogin.setOnClickListener(button -> {Navigation.findNavController(getView())
//                .navigate(LoginFragmentDirections.actionLoginToMainActivity("",""));});
        //comment this out to login w/o actually having to sign in.
        binding.buttonLogin.setVisibility(View.GONE);
        binding.buttonLogin.setOnClickListener(this::attemptLogin);

        LoginFragmentArgs args = LoginFragmentArgs.fromBundle(getArguments());
        binding.textEmail.setText(args.getEmail().equals("default") ? "" : args.getEmail()); //TODO
        binding.textPassword.setText(args.getPassword().equals("default") ? "" : args.getPassword()); //TODO

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
     * @param button
     */
    private void attemptLogin(final View button) {
        validateEmail();
    }

    /**
     * Checks that the password meets validation requirements
     */
    private void validateEmail() { //TODO remove No longer using email
        mEmailValidator.processResult(
                mEmailValidator.apply(binding.textEmail.getText().toString().trim()),
                this::validatePassword,
                result -> binding.textEmail.setError("Please enter a valid Email address."));
    }

    /**
     * Checks that the password meets validation requirements
     */
    private void validatePassword() {
        mPassWordValidator.processResult(
                mPassWordValidator.apply(binding.textPassword.getText().toString().trim()),
                this::verifyAuthWithServer,
                result -> binding.textPassword.setError("Please enter a Valid password."));
    }

    /**
     * Attempts to verify users credentials with the credentials in the data base.
     */
    private void verifyAuthWithServer() {
        mLoginViewModel.connectLogin(
                binding.textEmail.getText().toString(),
                binding.textPassword.getText().toString());
        //This is an Asynchronous call. No statements after should rely on the
        //result of connect().
    }

    /**
     * If successfully verified with the server the user will be navigated
     * to the landing page and their email and JWT will be passed to
     * userinfo view-model for access inside of the application.
     * @param email Users email
     * @param jwt Users JWT
     */
    private void navigateToHome(final String email, final String jwt) {
        Navigation.findNavController(getView())
                .navigate(LoginFragmentDirections
                        .actionLoginToMainActivity(email, jwt));
    }

    /**
     * An observer on the HTTP Response from the web server. This observer should be
     * attached to LoginViewModel.
     *
     * @param response the Response from the server
     */
    private void observeResponse(final JSONObject response) {
        if (response.length() > 0) {
            if (response.has("code")) {
                try {
                    binding.textEmail.setError(
                            "Error Authenticating: " +
                                    response.getJSONObject("data").getString("message"));
                } catch (JSONException e) {
                    Log.e("JSON Parse Error", e.getMessage());
                }
            } else {
                try {
                    navigateToHome(
                            binding.textEmail.getText().toString(),
                            response.getString("token")
                    );
                } catch (JSONException e) {
                    Log.e("JSON Parse Error", e.getMessage());
                }
            }
        } else {
            Log.d("JSON Response", "No Response");
        }
    }

    private void observeConnectionResponse(Boolean connected) {
        if (connected) {
            binding.buttonRegister.setVisibility(View.VISIBLE);
            binding.buttonLogin.setVisibility(View.VISIBLE);
            binding.textNotConnected.setVisibility(View.GONE);
        } else {
            binding.buttonRegister.setVisibility(View.GONE);
            binding.buttonLogin.setVisibility(View.GONE);
            binding.textNotConnected.setVisibility(View.VISIBLE);
            binding.textNotConnected.setText(R.string.not_connected);
        }
    }
}