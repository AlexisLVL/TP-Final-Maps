package com.lavieille.lavieille_guilland.utils;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;

import java.net.InetAddress;

public class CheckConnection {
    public static boolean isNetworkConnected(Activity activity) {
        ConnectivityManager cm = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    public static boolean isInternetWorking() {
        try {
            InetAddress result = InetAddress.getByName("google.com");
            return !result.toString().equals("");
        } catch (Exception ignored) {}
        return false;
    }

    public static boolean isInternetAvailable(Activity activity) {
        if (isNetworkConnected(activity)) {
            return isInternetWorking();
        }
        return false;
    }
}
