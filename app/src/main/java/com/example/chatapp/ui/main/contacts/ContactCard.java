package com.example.chatapp.ui.main.contacts;

import java.io.Serializable;

public class ContactCard implements Serializable {

    // builds contact card using builder pattern
    private final String mName;

    private final String mNick;


    private final String mEmail;

    private final char mFirstChar;

    private boolean mAccepted;
    public final int mMemberID;

    public static class Builder{
        private final String mName;

        private final char mFirstChar;
        private String mNick;

        private String mEmail;

        private boolean mAccepted;

        private int mMemberID;





        public Builder(String mName) {
            this.mName = mName;
            this.mFirstChar = mName.charAt(0);
        }
        public Builder addNick(final String val){
            mNick = val;
            return this;
        }

        public Builder addEmail(final String val){
            mEmail = val;
            return this;
        }

        public Builder addAccepted(final boolean val){
            mAccepted = val;
            return this;
        }

        public Builder addMemberID(final int val){
            mMemberID = val;
            return this;
        }



        public ContactCard build(){return new ContactCard(this);}
    }

    // build contact card
    private ContactCard(final Builder builder){
        this.mEmail = builder.mEmail;
        this.mNick = builder.mNick;
        this.mName = builder.mName;
        this.mFirstChar = builder.mFirstChar;
        this.mAccepted = builder.mAccepted;
        this.mMemberID = builder.mMemberID;
    }

    public String getName(){return mName;}
    public char getFirstChar(){return mFirstChar;}
    public String getNick(){return mNick;}
    public String getEmail(){return mEmail;}
    public boolean getAccepted(){return mAccepted;}

    public void setAccepted(boolean val){mAccepted = val;}

    public int getMemberID(){return mMemberID;}








}
