package com.example.himaneeshchhabra.loginproject;

import android.app.Activity;
import android.app.Application;

import com.firebase.client.Firebase;

/**
 * Created by Rajeev Nagarwal on 11/17/2016.
 */

public class Fire extends Application {
    public void onCreate()
    {
        super.onCreate();
        Firebase.setAndroidContext(this);
    }
    private static Activity mCurrentActivity = null;
    public Activity getmCurrentActivity()
    {
        return mCurrentActivity;
    }
    public void setmCurrentActivity(Activity currentActivity)
    {
        this.mCurrentActivity = currentActivity;
    }


}

