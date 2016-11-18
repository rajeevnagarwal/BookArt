package com.example.himaneeshchhabra.loginproject;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.himaneeshchhabra.loginproject.*;

import java.util.ArrayList;

import android.app.Activity;


/**
 * Created by HARSHIT on 15-Oct-16.
 */
public class BookAdapter extends ArrayAdapter<User> {

    private ArrayList<User> users;
    private Context context;
    private String current_user;
    private String receive_user;

    public BookAdapter(Context context, int resource, ArrayList<User> objects,String user,String ruser) {
        super(context, resource, objects);
        this.users=objects;
        this.context=context;
        this.current_user = user;
        this.receive_user = ruser;
    }

    public View getView(int position, View convertView, ViewGroup parent){
        View v=convertView;
        if(v==null){
            LayoutInflater inflater=(LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v=inflater.inflate(R.layout.book_item,null);
        }
        final User b=users.get(position);
        if(b!=null){
            TextView name=(TextView)v.findViewById(R.id.Name);
            ImageView imageView=(ImageView)v.findViewById(R.id.img);
            TextView age =(TextView)v.findViewById(R.id.Age);
            TextView contact=(TextView)v.findViewById(R.id.Contact);
            LinearLayout user=(LinearLayout)v.findViewById(R.id.User);
            if(name!=null){
                name.setText("Name: "+b.getName());
            }
            if(age!=null){
                age.setText("Age: "+b.getAge());
            }
            if(contact!=null){
                contact.setText("Contact: "+b.getContact());
            }
            if(user!=null){
                user.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(context,SearchActivity.class);
                        intent.putExtra("Search", b.getUsername());
                        intent.putExtra("What","User");
                        intent.putExtra("current_user",current_user);
                        intent.putExtra("receive_user",receive_user);
                        context.startActivity(intent);
                    }
                });

            }
            byte[] decodedString= Base64.decode(b.getIMG(),Base64.DEFAULT);
            Bitmap decodeByte= BitmapFactory.decodeByteArray(decodedString,0,decodedString.length);
            imageView.setImageBitmap(decodeByte);

        }
        return v;
    }



}
