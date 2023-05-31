package com.example.chatapp.ui.auth.login;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.chatapp.databinding.FragmentForgotPasswordBinding;
import com.example.chatapp.databinding.FragmentForgotPasswordResetBinding;

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
     * Empty ForgotPasswordFragment constructor
     */
    public ForgotPasswordResetFragment() {

    }

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
        mbinding.buttonReset.setOnClickListener(this::resetPassword);
    }

    private void resetPassword(View view) {
        Navigation.findNavController(getView())
                .navigate(ForgotPasswordResetFragmentDirections
                        .actionForgotPasswordResetFragmentToLogin());
    }
}
