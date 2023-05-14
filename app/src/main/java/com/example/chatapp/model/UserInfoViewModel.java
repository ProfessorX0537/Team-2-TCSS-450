package com.example.chatapp.model;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

/**
 * ViewModel that exist over the whole app
 */
public class UserInfoViewModel extends ViewModel {

    /**
     * The user's email
     */
    private final String mEmail;

    /**
     * The user's JWT
     */
    private final String mJwt;
    /**
     * The user's MemberID
     */
    private final int mMemberID;
    /**
     * The user's Username
     */
    private final String mUsername;

    /**
     * UserInfoViewModel Constructor that takes the user email and JWT after
     * successful login attempt.
     * @param email Users email
     * @param jwt Users JWT
     */
    private UserInfoViewModel(String email, String jwt, int memberID, String username) {
        mEmail = email;
        mJwt = jwt;
        mMemberID = memberID;
        mUsername = username;
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
    public String getJwt() {
        return mJwt;
    }

    /**
     * Get the current user's MemberID.
     * @return mMemberID
     */
    public int getMemberID() {
        return mMemberID;
    }
    /**
     * Get the current user's Username.
     * @return mUsername
     */
    public String getUsername() {
        return mUsername;
    }


    /**
     * Inner class that will create a new UserViewModel
     */
    public static class UserInfoViewModelFactory implements ViewModelProvider.Factory {

        private final String email;
        private final String jwt;
        private final int memberID;
        private final String username;

        public UserInfoViewModelFactory(String email, String jwt, int memberID, String username) {
            this.email = email;
            this.jwt = jwt;
            this.memberID = memberID;
            this.username = username;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if (modelClass == UserInfoViewModel.class) {
                return (T) new UserInfoViewModel(email, jwt, memberID, username);
            }
            throw new IllegalArgumentException(
                    "Argument must be: " + UserInfoViewModel.class);
        }
    }
}
