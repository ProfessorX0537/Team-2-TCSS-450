package com.example.chatapp.ui.main.contacts;

import java.io.Serializable;

public class ContactCard implements Serializable {

    private final String mName;

    private final char mFirstChar;

    public static class Builder{
        private final String mName;

        private final char mFirstChar;


        public Builder(String mName) {
            this.mName = mName;
            this.mFirstChar = mName.charAt(0);
        }

        public ContactCard build(){return new ContactCard(this);}
    }

    ContactCard(final Builder builder){
        this.mName = builder.mName;
        this.mFirstChar = builder.mFirstChar;
    }

    public String getName(){return mName;}

    public char getFirstChar(){return mFirstChar;}








}
