package com.anshul.celusionapplication.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by anshultyagi on 07/06/17.
 */

public class AppUtils {
    public static String AppABaseUrl = "http://88.99.217.226:8000/api/";

    public static boolean hasNetworkConnection(Context con) {
        boolean hasConnectedWifi = false;
        boolean hasConnectedMobile = false;
        ConnectivityManager cm = (ConnectivityManager) con.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    hasConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    hasConnectedMobile = true;
        }
        return hasConnectedWifi || hasConnectedMobile;
    }
}
