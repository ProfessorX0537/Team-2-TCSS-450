package com.example.chatapp.utils;

import java.util.Date;

public class SimpleDate {
    public static String stringDateFromEpochString(String epochString) {
        return new Date((long) (Double.parseDouble(epochString) * 1000)).toString();
    }
}
