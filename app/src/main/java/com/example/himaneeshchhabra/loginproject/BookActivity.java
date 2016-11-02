package com.example.himaneeshchhabra.loginproject;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.kosalgeek.android.photoutil.CameraPhoto;
import com.kosalgeek.android.photoutil.GalleryPhoto;
import com.kosalgeek.android.photoutil.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import static android.R.id.list;

public class BookActivity extends AppCompatActivity {

    Button btn;
    CameraPhoto cameraPhoto;
    GalleryPhoto galleryPhoto;
    ImageView camera,gallery,book_image;
    final int CAMERA_REQUEST=13323;
    final int GALLERY_REQUEST=22131;
    String selectedPhoto;
    String bname,bauthor,bprice;
    Button insert_book;
    AutoCompleteTextView view;
    EditText book_sell,book_rent;
    private  final String TAG=this.getClass().getName();
    int flag1=0;
    int flag2=0;
    int flag3=0;
    int flag4=0;
    int flag5=0;
    int flag6=0;
    Bundle bund;
    ArrayList<Auto_Suggest> booklist = new ArrayList<Auto_Suggest>();
    ArrayList<String> list = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);
        btn = (Button)findViewById(R.id.button4);
        camera=(ImageView)findViewById(R.id.camera);
        gallery=(ImageView)findViewById(R.id.gallery);
        insert_book = (Button)findViewById(R.id.insert_book);
        book_image = (ImageView)findViewById(R.id.book_image);
        view = (AutoCompleteTextView)findViewById(R.id.auto);
        cameraPhoto = new CameraPhoto(getApplicationContext());
        book_sell=(EditText)findViewById(R.id.book_sell);
        book_rent=(EditText)findViewById(R.id.book_rent);
        new AsyncFetch().execute();
        galleryPhoto=new GalleryPhoto(getApplicationContext());
        System.out.println("Hello"+booklist);
        Intent i=new Intent();
        Bundle bundle=getIntent().getExtras();
        bund=bundle;
    }
    public void onAdd(View v)
    {

        if(view.getText().toString().isEmpty()|| book_rent.getText().toString().isEmpty()|| book_sell.getText().toString().isEmpty()||list.contains(view.getText().toString()))
        {
            if(list.contains(view.getText().toString()))
            {
                if(book_rent.getText().toString().isEmpty()|| book_sell.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please all the fields", Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(getApplicationContext(), "Book already present", Toast.LENGTH_LONG).show();
                    String type = "registeruserbook";
                    String user_name = (String) bund.getString("user_name");
                    Toast.makeText(getApplicationContext(), user_name, Toast.LENGTH_LONG).show();
                    String pos = "";
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).equals(view.getText().toString())) {
                            pos = booklist.get(i).bid;
                            break;
                        }
                    }
                    new BackgroundWorker(getApplicationContext()).execute(type, user_name, "" + pos, book_sell.getText().toString(), book_rent.getText().toString());
                }
                //System.out.println("Himu:-"+user_name);
            }
            else {
                Toast.makeText(getApplicationContext(), "Please all the fields", Toast.LENGTH_LONG).show();
            }
        }
        else
        {
            Toast.makeText(getApplicationContext(),"Sorry this book is not in our databse. so u have to add it",Toast.LENGTH_LONG).show();
            LayoutInflater li = LayoutInflater.from(this);
            View promptview = li.inflate(R.layout.add_book, null);
            AlertDialog.Builder form = new AlertDialog.Builder(this);
            form.setTitle("Enter book details");
            form.setView(promptview);
            final EditText name = (EditText) promptview.findViewById(R.id.name);
            final EditText author = (EditText) promptview.findViewById(R.id.author);
            final EditText price = (EditText) promptview.findViewById(R.id.price);
            form.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which)
                {
                    name.setText(view.getText().toString());
                    if (name.getText().length() > 0 && price.getText().length() > 0 && author.getText().length() > 0)
                    {
                        bname = name.getText().toString();
                        bauthor = author.getText().toString();
                        bprice = price.getText().toString();
                        flag4 = 1;
                        if(flag3==1&&flag4==1)
                        {
                            String type="bookregister";
                            String user_name = (String) bund.getString("user_name");
                            BackgroundWorker backgroundWorker = new BackgroundWorker(getApplicationContext());
                            backgroundWorker.execute(type,bname,bauthor,bprice,selectedPhoto,user_name,book_sell.getText().toString(),book_rent.getText().toString());
                        }
                        else if(flag3==0)
                        {
                            Toast.makeText(getApplicationContext(),"Please upload the photo",Toast.LENGTH_LONG).show();
                        }
                        else if(flag4==0)
                        {
                            Toast.makeText(getApplicationContext(),"Please fill all the mandatory fields",Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "All the fields are required", Toast.LENGTH_SHORT).show();
                    }


                }
            });
            AlertDialog dialog = form.create();
            dialog.show();
        }



    }
    public void onInsert(View v)
    {
        if(flag3==1&&flag4==1)
        {
            String type="bookregister";
            BackgroundWorker backgroundWorker = new BackgroundWorker(getApplicationContext());
            String user_name = (String) bund.getString("user_name");
            if(book_sell.getText().toString().isEmpty()||book_rent.getText().toString().isEmpty())
            {
                Toast.makeText(getApplicationContext(),"Please fill whether book is avialble for sell and rent",Toast.LENGTH_LONG).show();
            }
            else {
                backgroundWorker.execute(type, bname, bauthor, bprice, selectedPhoto, user_name, book_sell.getText().toString(), book_rent.getText().toString());

            }
        }
        else if(flag3==0)
        {
            Toast.makeText(getApplicationContext(),"Please upload the photo",Toast.LENGTH_LONG).show();
        }
        else if(flag4==0)
        {
            Toast.makeText(getApplicationContext(),"Please fill all the mandatory fields",Toast.LENGTH_LONG).show();
        }
    }
    public void onUpload(View v)
    {
        try {
            if(flag1==1 || flag2==1)
            {
                flag3=1;
                Toast.makeText(getApplicationContext(), "Succesfully Uploaded", Toast.LENGTH_LONG).show();
            }
            else
            {
                Toast.makeText(getApplicationContext(),"Please choose book image from either camera or gallery", Toast.LENGTH_LONG).show();
            }
        }
        catch (Exception e)
        {
                    //e.printStackTrace();
            Toast.makeText(getApplicationContext(),"Something went wrong while encoding photos",Toast.LENGTH_LONG).show();
        }

    }

    public void onCamera(View v)
    {
        try {
            startActivityForResult(cameraPhoto.takePhotoIntent(),CAMERA_REQUEST);
            cameraPhoto.addToGallery();
            flag1=1;
        } catch (IOException e)
        {
            Toast.makeText(getApplicationContext(),"Something went wrong while taking photos",Toast.LENGTH_LONG).show();
        }
    }
    public void onGallery(View v)
    {
        startActivityForResult(galleryPhoto.openGalleryIntent(),GALLERY_REQUEST);
        flag2=1;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK)
        {
            if(requestCode==CAMERA_REQUEST)
            {
                String photoPath=cameraPhoto.getPhotoPath();
                // selectedPhoto=photoPath;
                try {
                    Bitmap bitmap= ImageLoader.init().from(photoPath).requestSize(100,100).getBitmap();
                    //book_image.setImageBitmap(bitmap);
                    try {
                        ExifInterface ei = new ExifInterface(photoPath);
                        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,ExifInterface.ORIENTATION_UNDEFINED);
                        switch(orientation)
                        {
                            case ExifInterface.ORIENTATION_ROTATE_90:
                                bitmap = rotateImage(bitmap,90);
                                break;
                            case ExifInterface.ORIENTATION_ROTATE_180:
                                bitmap = rotateImage(bitmap,180);
                                break;
                            case ExifInterface.ORIENTATION_ROTATE_270:
                                bitmap = rotateImage(bitmap,270);
                                break;
                            case ExifInterface.ORIENTATION_NORMAL:
                            default:
                                break;

                        }
                        book_image.setImageBitmap(bitmap);
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        ((BitmapDrawable)book_image.getDrawable()).getBitmap().compress(Bitmap.CompressFormat.PNG,100,stream);
                        byte[] b = stream.toByteArray();
                        selectedPhoto = Base64.encodeToString(b, Base64.DEFAULT);

                    }catch(IOException e)
                    { e.printStackTrace();

                    }
                } catch (FileNotFoundException e) {
                    //e.printStackTrace();
                    Toast.makeText(getApplicationContext(),"Something went wrong while taking photos",Toast.LENGTH_LONG).show();
                }
                Log.d(TAG,photoPath);
            }
            else if(requestCode==GALLERY_REQUEST)
            {
                Uri uri=data.getData();
                galleryPhoto.setPhotoUri(uri);
                String photoPath=galleryPhoto.getPath();
                //selectedPhoto=photoPath;
                try {
                    Bitmap bitmap= ImageLoader.init().from(photoPath).requestSize(100,100).getBitmap();
                    //book_image.setImageBitmap(bitmap);
                    try {
                        ExifInterface ei = new ExifInterface(photoPath);
                        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,ExifInterface.ORIENTATION_UNDEFINED);
                        switch(orientation)
                        {
                            case ExifInterface.ORIENTATION_ROTATE_90:
                                bitmap = rotateImage(bitmap,90);
                                break;
                            case ExifInterface.ORIENTATION_ROTATE_180:
                                bitmap = rotateImage(bitmap,180);
                                break;
                            case ExifInterface.ORIENTATION_ROTATE_270:
                                bitmap = rotateImage(bitmap,270);
                                break;
                            case ExifInterface.ORIENTATION_NORMAL:
                            default:
                                break;

                        }
                        book_image.setImageBitmap(bitmap);
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        ((BitmapDrawable)book_image.getDrawable()).getBitmap().compress(Bitmap.CompressFormat.PNG,100,stream);
                        byte[] b = stream.toByteArray();
                        selectedPhoto = Base64.encodeToString(b, Base64.DEFAULT);

                    }catch(IOException e)
                    { e.printStackTrace();

                    }
                } catch (FileNotFoundException e) {
                    //e.printStackTrace();
                    Toast.makeText(getApplicationContext(),"Something went wrong while choosing photos",Toast.LENGTH_LONG).show();
                }
                Log.d(TAG,photoPath);
            }
        }
    }
    public static Bitmap rotateImage(Bitmap source,float angle)
    {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source,0,0,source.getWidth(),source.getHeight(),matrix,true);
    }
    private class AsyncFetch extends AsyncTask<String, String, String> {

        ProgressDialog pdLoading = new ProgressDialog(BookActivity.this);
        HttpURLConnection conn;
        URL url = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //this method will be running on UI thread
            pdLoading.setMessage("\tLoading...");
            pdLoading.setCancelable(false);
            pdLoading.show();

        }

        @Override
        protected String doInBackground(String... params) {
            try {

                // Enter URL address where your php file resides or your JSON file address
                url = new URL("http://192.168.48.74"+"/test/fetch_book.php");

            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return e.toString();
            }
            try {

                // Setup HttpURLConnection class to send and receive data from php and mysql
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(500);
                conn.setConnectTimeout(500);
                conn.setRequestMethod("GET");

                // setDoOutput to true as we receive data
                conn.setDoOutput(true);
                conn.connect();

            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                return e1.toString();
            }

            try {

                int response_code = conn.getResponseCode();

                // Check if successful connection made
                if (response_code == HttpURLConnection.HTTP_OK) {

                    // Read data sent from server
                    InputStream input = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }

                    // Pass data to onPostExecute method
                    return (result.toString());

                } else {
                    return("Connection error");
                }

            } catch (IOException e) {
                e.printStackTrace();
                return e.toString();
            } finally {
                conn.disconnect();
            }


        }

        @Override
        protected void onPostExecute(String result) {

            //this method will be running on UI thread
            ArrayList<Auto_Suggest> names = new ArrayList<Auto_Suggest>();

            pdLoading.dismiss();


            if(result.equals("no rows")) {

                // Do some action if no data from database

            }else{

                try {

                    System.out.println(result);
                    JSONArray jArray = new JSONArray(result);

                    // Extract data from json and store into ArrayList
                    for (int i = 0; i < jArray.length(); i++) {
                        JSONObject json_data = jArray.getJSONObject(i);
                        names.add(new Auto_Suggest(json_data.getString("username"),json_data.getString("bid")));
                    }
                    System.out.println("hello");
                    System.out.println(names);
                    booklist = names;
                    System.out.println("Books:"+booklist);
                    for(int i=0;i<booklist.size();i++)
                    {
                        list.add(booklist.get(i).name);
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),R.layout.mytextview,list.toArray(new String[list.size()]));
                    view.setThreshold(1);
                    view.setAdapter(adapter);
                    view.setTextColor(Color.RED);

                } catch (JSONException e) {
                    // You to understand what actually error is and handle it appropriately
                    Toast.makeText(BookActivity.this, e.toString(), Toast.LENGTH_LONG).show();
                    Toast.makeText(BookActivity.this, result.toString(), Toast.LENGTH_LONG).show();
                }

            }

        }

    }

}
