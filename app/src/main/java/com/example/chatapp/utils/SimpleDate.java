package com.example.chatapp.utils;

import android.util.Log;

import java.util.Date;

public class SimpleDate {
    public static String stringDateFromEpochString(String epochString) {

        double epochTimeDecimal = Double.parseDouble(epochString);
        long epochTime = (long) epochTimeDecimal;
        long currentTime = System.currentTimeMillis()/1000;

        long timeDifference = currentTime - epochTime;

        long seconds = timeDifference % 60;
        long minutes = (timeDifference / 60) % 60;
        long hours = (timeDifference / (60 * 60)) % 24;
        long days = timeDifference / (60 * 60 * 24);

        String timeAgo = formatTimeAgo(days, hours % 24, minutes % 60, seconds % 60);

        return timeAgo;
        //return new Date((long) (Double.parseDouble(epochString) * 1000)).toString();
    }

    private static String formatTimeAgo(long days, long hours, long minutes, long seconds) {
        if (days > 0) {
            return days + " days ago";
        } else if (hours > 0) {
            return hours + " hours ago";
        } else if (minutes > 0) {
            return minutes + " minutes ago";
        } else {
            return "now";
        }
    }
}
