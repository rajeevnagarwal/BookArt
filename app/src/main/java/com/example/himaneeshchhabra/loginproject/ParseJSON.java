package com.example.himaneeshchhabra.loginproject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ParseJSON {
    public static Integer[] no_borrowed,no_sold,bid,market_price;
    public static String[] names,username,pass,gender,image,bname,author,imagebook;
    public static String[] addr,profession,contact,longitude,latitude,age;

    public static final String JSON_ARRAY = "result";
    public static final String KEY_BID="bid";
    public static final String KEY_MP="market_price";
    public static final String KEY_Username = "username";
    public static final String KEY_IMG="image";
    public static final String KEY_Pass="password";
    public static final String KEY_NAME = "name";
    public static final String KEY_longitude="longitude";
    public static final String KEY_latitude="latitude";
    public static final String KEY_ADDR="address";
    public static final String KEY_Gender="gender";
    public static final String KEY_AGE="age";
    public static final String KEY_Bname="bname", KEY_Auth="author",KEY_IMGB="imagebook",KEY_Borrow="no_borrowed",KEY_Sold="no_sold";
    public static final String KEY_PROF="profession";
    public static final String KEY_CONTACT="contact";


    private JSONArray users = null;

    private String json;

    public ParseJSON(String json){
        this.json = json;
    }

    public void parseJSON(){
        JSONObject jsonObject=null;
        try {
            jsonObject = new JSONObject(json);
            users = jsonObject.getJSONArray(JSON_ARRAY);
            //System.out.println("Hello"+users.toString());
            username = new String[users.length()];
            names = new String[users.length()];
            longitude=new String[users.length()];
            latitude=new String[users.length()];
            addr=new String[users.length()];
            profession=new String[users.length()];
            contact=new String[users.length()];
            age=new String[users.length()];
            gender=new String[users.length()];
            pass=new String[users.length()];
            image=new String[users.length()];
            bname=new String[users.length()];
            author=new String[users.length()];
            imagebook=new String[users.length()];
            no_borrowed=new Integer[users.length()];
            market_price=new Integer[users.length()];
            no_sold=new Integer[users.length()];
            bid=new Integer[users.length()];
            for(int i=0;i<users.length();i++){
                JSONObject jo = users.getJSONObject(i);
                bname[i]=jo.getString(KEY_Bname);
                author[i]=jo.getString(KEY_Auth);
                imagebook[i]=jo.getString(KEY_IMGB);
                no_borrowed[i]=jo.getInt(KEY_Borrow);
                no_sold[i]=jo.getInt(KEY_Sold);
                username[i] = jo.getString(KEY_Username);
                names[i] = jo.getString(KEY_NAME);
                longitude[i]=jo.getString(KEY_longitude);
                latitude[i]=jo.getString(KEY_latitude);
                addr[i]=jo.getString(KEY_ADDR);
                profession[i]=jo.getString(KEY_PROF);
                contact[i]=jo.getString(KEY_CONTACT);
                age[i]=jo.getString(KEY_AGE);
                gender[i]=jo.getString(KEY_Gender);
                pass[i]=jo.getString(KEY_Pass);
                image[i]=jo.getString(KEY_IMG);
                bid[i]=jo.getInt(KEY_BID);
                market_price[i]=jo.getInt(KEY_MP);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}