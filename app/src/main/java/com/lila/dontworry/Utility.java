package com.lila.dontworry;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Gustav on 12.01.2018.
 */

public class Utility {
    public static final int TYPE_DISCONNECTED = 100;

    public static int getConnectionType(Context context) {
        int connectionType = TYPE_DISCONNECTED;

        int[] networkTypes = {ConnectivityManager.TYPE_MOBILE,
                ConnectivityManager.TYPE_WIFI};
        try {
            ConnectivityManager connectivityManager =
                    (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            if (activeNetworkInfo != null)
                if (activeNetworkInfo.getType() == ConnectivityManager.TYPE_MOBILE || activeNetworkInfo.getType() == ConnectivityManager.TYPE_WIFI)
                    connectionType = activeNetworkInfo.getType();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return connectionType;
    }
}
