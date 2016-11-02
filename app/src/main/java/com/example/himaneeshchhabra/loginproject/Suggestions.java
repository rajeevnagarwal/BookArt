package com.example.himaneeshchhabra.loginproject;
import android.widget.ImageView;

/**
 * Created by Rajeev Nagarwal on 10/12/2016.
 */

public class Suggestions
{
    public String suggestion;
    public String bid;
    public ImageView view;
    public String type;
    public Suggestions(String suggestion,ImageView view,String type)
    {
        this.suggestion = suggestion;
        this.view = view;
        this.type = type;
        this.bid = "";
    }
    public Suggestions(String bid,String suggestion,ImageView view,String type)
    {
        this.suggestion = suggestion;
        this.bid = bid;
        this.view = view;
        this.type = type;
    }


}

