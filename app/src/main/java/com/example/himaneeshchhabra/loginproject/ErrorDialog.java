package com.example.himaneeshchhabra.loginproject;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;

/**
 * Created by Rajeev Nagarwal on 11/28/2016.
 */

public class ErrorDialog {
    public static void showDialog(Context context)
    {
        Dialog builder = new Dialog(context);
        builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
        builder.getWindow().setBackgroundDrawable(new ColorDrawable((Color.TRANSPARENT)));
        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {

            }
        });
        ImageView imageView = new ImageView(context);
        imageView.setImageDrawable(context.getResources().getDrawable(R.mipmap.error));
        builder.addContentView(imageView,new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        builder.show();

    }

}
