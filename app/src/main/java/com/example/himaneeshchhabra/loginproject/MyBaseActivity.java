package com.example.himaneeshchhabra.loginproject;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

/**
 * Created by Rajeev Nagarwal on 11/27/2016.
 */

public class MyBaseActivity extends AppCompatActivity {
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
    protected void alert()
    {
        Dialog builder = new Dialog(this);
        builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
        builder.getWindow().setBackgroundDrawable(new ColorDrawable((Color.TRANSPARENT)));
        builder.setContentView(R.layout.custom_dialog);

        builder.show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent i=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(i);
            }
        }, 5000);
    }



}
