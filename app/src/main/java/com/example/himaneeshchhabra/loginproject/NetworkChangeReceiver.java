package com.example.himaneeshchhabra.loginproject;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.widget.TextView;

/**
 * Created by Rajeev Nagarwal on 11/27/2016.
 */

public class NetworkChangeReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(final Context context, final Intent intent) {
        final ConnectivityManager connMgr = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        final android.net.NetworkInfo wifi = connMgr
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        final android.net.NetworkInfo mobile = connMgr
                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if (wifi.isConnected() || mobile.isConnected()) {
            // i need to update activity ?????
            Activity currentActivity = ((Fire)context.getApplicationContext()).getmCurrentActivity();
            System.out.println("helooasldkjfpasdg");
            Intent i = new Intent(context,currentActivity.getClass());
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        }
    }
}
