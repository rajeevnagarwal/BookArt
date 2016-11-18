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

import com.example.himaneeshchhabra.loginproject.R;

import java.util.ArrayList;


/**
 * Created by HARSHIT on 15-Oct-16.
 */
public class ItemAdapter extends ArrayAdapter<Book> {

    private ArrayList<Book> books;
    private Context context;
    private String current_user;
    private String receive_user;

    public ItemAdapter(Context context, int resource, ArrayList<Book> objects,String user,String ruser) {
        super(context, resource, objects);
        this.books=objects;
        this.context=context;
        this.current_user =user;
        this.receive_user = ruser;
    }

    public View getView(int position, View convertView, ViewGroup parent){
        View v=convertView;
        if(v==null){
            LayoutInflater inflater=(LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v=inflater.inflate(R.layout.list_item,null);
        }
        final Book b=books.get(position);
        if(b!=null){
            TextView name=(TextView)v.findViewById(R.id.BookName);
            ImageView imageView=(ImageView)v.findViewById(R.id.img);
            Button Buy=(Button)v.findViewById(R.id.Buy);
            Button Rent=(Button)v.findViewById(R.id.Rent);
            LinearLayout book=(LinearLayout)v.findViewById(R.id.Book);
            if(name!=null){
                name.setText(b.getName());
            }
            if(Buy!=null){
                if(b.getAvailable_for_borrow()==0){
                    Buy.setEnabled(false);
                }
            }
            if(Rent!=null){
                if(b.getAvailable_for_renting()==0){
                    Rent.setEnabled(false);
                }
            }
            if(book!=null){
                book.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(context,SearchActivity.class);
                        intent.putExtra("Search", b.getBid());
                        intent.putExtra("What","Book");
                        intent.putExtra("current_user",current_user);
                        intent.putExtra("receive_user",receive_user);
                        context.startActivity(intent);
                    }
                });
                byte[] decodedString= Base64.decode(b.getImg(),Base64.DEFAULT);
                Bitmap decodeByte= BitmapFactory.decodeByteArray(decodedString,0,decodedString.length);
                imageView.setImageBitmap(decodeByte);

            }


        }
        return v;
    }



}
