package com.example.chatapp.utils;

import android.util.Log;

import java.util.Date;

public class SimpleDate {
    public static String stringDateFromEpochString(String epochString) {
        Log.d("hi", new Date((long) (Double.parseDouble(epochString) * 1000)).toString());
        return new Date((long) (Double.parseDouble(epochString) * 1000)).toString();
    }
}
