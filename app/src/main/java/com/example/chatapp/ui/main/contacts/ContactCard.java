package com.example.chatapp.ui.main.contacts;

import java.io.Serializable;

public class ContactCard implements Serializable {

    // builds contact card using builder pattern
    private final String mName;

    private final String mNick;


    private final String mEmail;

    private final char mFirstChar;

    private boolean mAccepted;
    private final int mMemberID;
    private final boolean mOutgoing;
    private final boolean mIncoming;

    public static class Builder{
        private final String mName;

        private final char mFirstChar;
        private String mNick;

        private String mEmail;

        private boolean mAccepted;
        private boolean mOutgoing;
        private boolean mIncoming;


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


        public Builder addOutgoing(final boolean val){
            mOutgoing = val;
            return this;
        }

        public Builder addIncoming(final boolean val){
            mIncoming = val;
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
        this.mOutgoing = builder.mOutgoing;
        this.mIncoming = builder.mIncoming;
    }

    public String getName(){return mName;}
    public char getFirstChar(){return mFirstChar;}
    public String getNick(){return mNick;}
    public String getEmail(){return mEmail;}
    public boolean getAccepted(){return mAccepted;}

    public boolean getOutgoing(){return mOutgoing;}
    public boolean getIncoming(){return mIncoming;}

    public void setAccepted(boolean val){mAccepted = val;}

    public int getMemberID(){return mMemberID;}










}
