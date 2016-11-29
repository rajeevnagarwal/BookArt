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
            if(currentActivity.getClass().getSimpleName().equals("MainActivity"))
            {
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);

            }
            else if(currentActivity.getClass().getSimpleName().equals("SignupActivity"))
            {
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            }
            else if(currentActivity.getClass().getSimpleName().equals("BookActivity"))
            {
                String user_name = currentActivity.getIntent().getStringExtra("user_name");
                String isbn = currentActivity.getIntent().getStringExtra("code");
                i.putExtra("user_name",user_name);
                i.putExtra("code",isbn);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);

            }
            else if(currentActivity.getClass().getSimpleName().equals("ChatActivity"))
            {
                String current = currentActivity.getIntent().getStringExtra("receive_user");
                String receive = currentActivity.getIntent().getStringExtra("current_user");
                i.putExtra("receive_user",receive);
                i.putExtra("current_user",current);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            }
            else if(currentActivity.getClass().getSimpleName().equals("ResultActivity"))
            {
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);

            }
            else if(currentActivity.getClass().getSimpleName().equals("SearchActivity"))
            {
                String what = currentActivity.getIntent().getStringExtra("What");
                String search = currentActivity.getIntent().getStringExtra("Search");
                String current_user = currentActivity.getIntent().getStringExtra("current_user");
                i.putExtra("What",what);
                i.putExtra("Search",search);
                i.putExtra("current_user",current_user);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            }
            else if(currentActivity.getClass().getSimpleName().equals("BookScanActivity"))
            {
                String current_user = currentActivity.getIntent().getStringExtra("current_user");
                i.putExtra("current_user",current_user);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);


            }
            else if(currentActivity.getClass().getSimpleName().equals("NewBookActivity"))
            {
                String book_name = currentActivity.getIntent().getStringExtra("book_name");
                String bid = currentActivity.getIntent().getStringExtra("bid");
                String code = currentActivity.getIntent().getStringExtra("code");
                String current_user = currentActivity.getIntent().getStringExtra("current_user");
                i.putExtra("book_name",book_name);
                i.putExtra("bid",bid);
                i.putExtra("code",code);
                i.putExtra("current_user",current_user);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);

            }
        }
        else
        {
            Activity currentActivity = ((Fire)context.getApplicationContext()).getmCurrentActivity();
            if(!currentActivity.getClass().getSimpleName().equals("SearchActivity")) {
                ((MyBaseActivity) currentActivity).alert();

            }
            else
            {
                ((MyListActivity) currentActivity).alert();


            }
        }
    }
}
