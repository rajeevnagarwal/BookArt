package com.example.himaneeshchhabra.loginproject;

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

}
