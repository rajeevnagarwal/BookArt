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
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
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

public class BookActivity extends MyBaseActivity {

    Button logout1;
    CameraPhoto cameraPhoto;
    GalleryPhoto galleryPhoto;
    ImageView camera,gallery,book_image;
    final int CAMERA_REQUEST=13323;
    final int GALLERY_REQUEST=22131;
    String selectedPhoto;
    EditText bookname,bookauthor,bookprice;
    String bname,bauthor,bprice;
    Button insert_book;
    Spinner book_sell,book_rent;
    String sell,rent;
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
        ArrayList<String> list = new ArrayList<String>();
        list.add("");
        list.add("yes");
        list.add("no");
        rent="";sell="";bname="";bauthor="";bprice="";
        setContentView(R.layout.activity_book);
        camera=(ImageView)findViewById(R.id.camera);
        gallery=(ImageView)findViewById(R.id.gallery);
        insert_book = (Button)findViewById(R.id.insert_book);
        book_image = (ImageView)findViewById(R.id.book_image);
        cameraPhoto = new CameraPhoto(getApplicationContext());
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,list);
        bookname = (EditText)findViewById(R.id.bookname);
        bookauthor = (EditText)findViewById(R.id.bookauthor);
        bookprice = (EditText)findViewById(R.id.bookprice);
        book_sell=(Spinner)findViewById(R.id.book_sell);
        book_rent=(Spinner)findViewById(R.id.book_rent);
        book_sell.setAdapter(dataAdapter);
        book_rent.setAdapter(dataAdapter);
        book_rent.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                rent = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        book_sell.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sell = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        galleryPhoto=new GalleryPhoto(getApplicationContext());
        System.out.println("Hello"+booklist);
        Intent i=new Intent();
        Bundle bundle=getIntent().getExtras();
        bund=bundle;
    }
    public void onLogout(View v)
    {
        Intent intent=new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);
    }
    public Boolean checkConnection()
    {
        ConnectivityManager connMgr = (ConnectivityManager)getSystemService(getApplicationContext().CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if(networkInfo!=null&&networkInfo.isConnected())
            return true;
        else
            return false;
    }

  public void onInsert(View v)
    {
        if(flag3==1)
        {
            String type="bookregister";
            bname = bookname.getText().toString();
            bauthor = bookauthor.getText().toString();
            bprice = bookprice.getText().toString();
            BackgroundWorker backgroundWorker = new BackgroundWorker(getApplicationContext());
            String user_name = (String) bund.getString("user_name");
            String isbn = (String)bund.getString("code");
            if(user_name!=null&&isbn!=null) {
                if (sell.equals("") || rent.toString().equals("") || bname.equals("") || bauthor.equals("") || bprice.equals("")) {
                    Toast.makeText(getApplicationContext(), "Please fill all the fields above", Toast.LENGTH_LONG).show();
                } else {
                    if(checkConnection()) {
                        backgroundWorker.execute(type, bname, bauthor, bprice, selectedPhoto, user_name, book_sell.toString(), book_rent.toString(), isbn);
                        Toast.makeText(getApplicationContext(), "done", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "Please connect to network first", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            else
            {
                Toast.makeText(getApplicationContext(),"Internal Error",Toast.LENGTH_SHORT).show();
            }
        }
        else if(flag3==0)
        {
            Toast.makeText(getApplicationContext(),"Please upload the photo",Toast.LENGTH_LONG).show();
        }
        /*else if(flag4==0)
        {
            Toast.makeText(getApplicationContext(),"Please fill all the mandatory fields",Toast.LENGTH_LONG).show();
        }*/
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


}
