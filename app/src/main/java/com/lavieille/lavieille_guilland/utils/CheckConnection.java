package com.lavieille.lavieille_guilland.utils;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;

import java.net.InetAddress;

public class CheckConnection {
    /**
     * Checks if a network is connected
     * @param activity The activity that ask for the check
     * @return True if a network is connected, false otherwise
     */
    public static boolean isNetworkConnected(Activity activity) {
        ConnectivityManager cm = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isAvailable();
    }
}
