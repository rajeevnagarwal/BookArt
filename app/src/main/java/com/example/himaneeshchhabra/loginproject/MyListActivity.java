package com.example.himaneeshchhabra.loginproject;

import android.app.Activity;
import android.app.ListActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Rajeev Nagarwal on 11/27/2016.
 */

public class MyListActivity extends ListActivity {
    protected Fire mMyApp;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMyApp = (Fire)this.getApplicationContext();
    }
    protected void onResume() {
        super.onResume();
        mMyApp.setmCurrentActivity(this);
    }
    protected void onPause() {
        clearReferences();
        super.onPause();
    }
    protected void onDestroy() {
        clearReferences();
        super.onDestroy();
    }

    private void clearReferences(){
        Activity currActivity = mMyApp.getmCurrentActivity();
        if (this.equals(currActivity))
            mMyApp.setmCurrentActivity(null);
    }

}

