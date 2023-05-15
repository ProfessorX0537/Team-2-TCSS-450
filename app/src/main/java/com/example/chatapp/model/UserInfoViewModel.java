package com.example.chatapp.model;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

/**
 * ViewModel that exist over the whole app
 */
public class UserInfoViewModel extends ViewModel {

    /**
     * The users email
     */
    private final String mEmail;

    /**
     * The users JWT
     */
    private final String mJwt;

    /**
     * UserInfoViewModel Constructor that takes the user email and JWT after
     * successful login attempt.
     * @param email Users email
     * @param jwt Users JWT
     */
    private UserInfoViewModel(String email, String jwt) {
        mEmail = email;
        mJwt = jwt;
    }

    /**
     * Gets the email the user logged in with.
     * @return user email
     */
    public String getEmail() {
        return mEmail;
    }

    /**
     * Gets the JWT generated from when the user logged in
     * @return user JWT
     */
    public String getmJwt() {
        return mJwt;
    }

    /**
     * Inner class that will create a new UserViewModel
     */
    public static class UserInfoViewModelFactory implements ViewModelProvider.Factory {

        private final String email;
        private final String jwt;

        public UserInfoViewModelFactory(String email, String jwt) {
            this.email = email;
            this.jwt = jwt;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if (modelClass == UserInfoViewModel.class) {
                return (T) new UserInfoViewModel(email, jwt);
            }
            throw new IllegalArgumentException(
                    "Argument must be: " + UserInfoViewModel.class);
        }
    }
}
