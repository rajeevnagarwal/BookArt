package com.example.himaneeshchhabra.loginproject;

import android.widget.ImageView;

/**
 * Created by Rajeev Nagarwal on 10/15/2016.
 */

public class Books {
    private String name;
    private ImageView image;
    private String bid;
    private String author;
    public Books(String name,ImageView image,String bid,String author)
    {
        this.name = name;
        this.image = image;
        this.bid = bid;
        this.author = author;
    }
    public String getName()
    {
        return this.name;
    }
    public ImageView getImage()
    {
        return this.image;
    }
    public String getId()
    {
        return this.bid;
    }
    public String getAuthor()
    {
        return this.author;
    }







}
