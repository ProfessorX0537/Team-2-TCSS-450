package com.example.chatapp.ui.main.contacts;

import java.io.Serializable;

public class ContactCard implements Serializable {

    private final String mName;

    private final String mNick;


    private final String mEmail;

    private final char mFirstChar;

    public static class Builder{
        private final String mName;

        private final char mFirstChar;
        private String mNick;

        private String mEmail;





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
        public ContactCard build(){return new ContactCard(this);}
    }

    private ContactCard(final Builder builder){
        this.mEmail = builder.mEmail;
        this.mNick = builder.mNick;
        this.mName = builder.mName;
        this.mFirstChar = builder.mFirstChar;
    }

    public String getName(){return mName;}
    public char getFirstChar(){return mFirstChar;}
    public String getNick(){return mNick;}
    public String getEmail(){return mEmail;}








}
