package com.example.himaneeshchhabra.loginproject;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.ByteArrayOutputStream;
import java.util.List;

/**
 * Created by Rajeev Nagarwal on 10/15/2016.
 */

public class BooksAdapter extends RecyclerView.Adapter<BooksAdapter.MyViewHolder> {
    private Context mContext;
    private List<Books> albumList;
    private String user;

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView title;
        public ImageView thumbnail;
        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
        }
    }


    public BooksAdapter(Context mContext, List<Books> albumList,String user) {
        this.mContext = mContext;
        this.albumList = albumList;
        this.user = user;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.book_card, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        final Books album = albumList.get(position);
        if(holder.title!=null&&holder.thumbnail!=null&&albumList.get(position)!=null) {
            holder.title.setText(album.getName());
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            ((BitmapDrawable) album.getImage().getDrawable()).getBitmap().compress(Bitmap.CompressFormat.PNG, 100, stream);
            Glide.with(mContext).load(stream.toByteArray()).into(holder.thumbnail);

        }
        if(position==getItemCount()-1)
        {
            holder.title.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
            holder.thumbnail.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
        }
        holder.thumbnail.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v)
            {
                Intent i = new Intent(mContext,SearchActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.putExtra("Search",album.getId());
                i.putExtra("What","Book");
                i.putExtra("current_user",user);
                mContext.startActivity(i);
            }

        });

    }
    @Override
    public int getItemCount() {
        return albumList.size();
    }
    @Override
    public int getItemViewType(int position) {

        return position;
    }
}

