package com.example.himaneeshchhabra.loginproject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.kosalgeek.android.photoutil.*;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class SignupActivity extends MyBaseActivity
{
    private  final String TAG=this.getClass().getName();
    ImageView imageView1, imageView2, imageView3,imageView4;
    EditText editText3,editText4,editText5,editText6,editText7,editText8,editText9,editText10,editText11;
    CameraPhoto cameraPhoto;
    GalleryPhoto galleryPhoto;
    final int CAMERA_REQUEST=13323;
    final int GALLERY_REQUEST=22131;
    String selectedPhoto;
    Button button3;
    GPSTracker gps;
    int flag1=0;
    int flag2=0;
    int flag3=0;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        imageView1=(ImageView)findViewById(R.id.imageView1);
        imageView2=(ImageView)findViewById(R.id.imageView2);
        imageView3=(ImageView)findViewById(R.id.imageView3);
        imageView4=(ImageView)findViewById(R.id.imageView4);
        editText3=(EditText)findViewById(R.id.editText3);
        editText4=(EditText)findViewById(R.id.editText4);
        editText5=(EditText)findViewById(R.id.editText5);
        editText6=(EditText)findViewById(R.id.editText6);
        editText7=(EditText)findViewById(R.id.editText7);
        editText8=(EditText)findViewById(R.id.editText8);
        editText9=(EditText)findViewById(R.id.editText9);
        editText10=(EditText)findViewById(R.id.editText10);
        editText11=(EditText)findViewById(R.id.editText11);
        button3=(Button)findViewById(R.id.button3);
        cameraPhoto = new CameraPhoto(getApplicationContext());
        galleryPhoto = new GalleryPhoto(getApplicationContext());
        imageView1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                try {
                    startActivityForResult(cameraPhoto.takePhotoIntent(),CAMERA_REQUEST);
                    cameraPhoto.addToGallery();
                    flag1=1;
                } catch (IOException e)
                {
                    Toast.makeText(getApplicationContext(),"Something went wrong while taking photos",Toast.LENGTH_LONG).show();
                }
            }
        });

        imageView2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                flag2=1;
                startActivityForResult(galleryPhoto.openGalleryIntent(),GALLERY_REQUEST);
            }
        });

        imageView3.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                try {
                   if(flag1==1 || flag2==1)
                   {
                       flag3=1;
                       Toast.makeText(getApplicationContext(), "Succesfully Uploaded", Toast.LENGTH_LONG).show();
                   }
                } catch (Exception e) {
                    //e.printStackTrace();
                    Toast.makeText(getApplicationContext(),"Something went wrong while encoding photos",Toast.LENGTH_LONG).show();
                }
            }
        });

        button3.setOnClickListener(new View.OnClickListener()
        {
            int temp1=0;
            int temp2=0;
            int temp3=0;
            int temp4=0;
            @Override
            public void onClick(View v)
            {
                double latitude=0;
                double longitude = 0;
                gps=new GPSTracker(SignupActivity.this);
                if(gps.canGetLocation())
                {
                    temp4=1;
                    latitude=gps.getLatitude();
                    longitude=gps.getLongitude();
                    //Toast.makeText(getApplicationContext(),"Your location coordinates are :-\nLat:-"+latitude+"\nLong:-"+longitude,Toast.LENGTH_LONG).show();
                }
                else
                {
                    gps.showSettingsAlert();
                }
                if(editText3.getText().toString().equals("")||editText4.getText().toString().equals("")||editText5.getText().toString().equals("")||editText6.getText().toString().equals("")||editText7.getText().toString().equals("")||editText8.getText().toString().equals("")||editText9.getText().toString().equals("")||editText10.getText().toString().equals("")||editText11.getText().toString().equals(""))
                {
                    Toast.makeText(getApplicationContext(),"Please fill all the fields",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    temp1=1;
                }
                if(flag3==0)
                {
                    Toast.makeText(getApplicationContext(),"Please upload your photo too",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    temp2=1;
                }
                if(temp1==1 && temp2==1)
                {
                   if(editText4.getText().toString().equals(editText5.getText().toString()))
                   {
                       temp3=1;

                   }
                    else
                   {
                       Toast.makeText(getApplicationContext(),"Both the passwords written above do not match",Toast.LENGTH_SHORT).show();
                   }
                }
                if(temp3==1)
                {
                    if(temp4==1)
                    {
                        String username = editText3.getText().toString();
                        String password = editText4.getText().toString();
                        String name = editText6.getText().toString();
                        String address= editText7.getText().toString();
                        String age = editText8.getText().toString();
                        String gender = editText9.getText().toString();
                        String profession=editText10.getText().toString();
                        String contact=editText11.getText().toString();
                        String image = selectedPhoto;
                        String type="register";
                        BackgroundWorker backgroundWorker = new BackgroundWorker(getApplicationContext());
                        if(checkConnection()) {
                            backgroundWorker.execute(type, username, password, name, address, age, gender, profession, image, contact, String.valueOf(longitude), String.valueOf(latitude));
                            ///get the latidude, longitude and image
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(),"connect to network first",Toast.LENGTH_SHORT).show();
                        }

                    }

                }
            }
        });
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
                    //imageView4.setImageBitmap(bitmap);
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
                        imageView4.setImageBitmap(bitmap);
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        ((BitmapDrawable)imageView4.getDrawable()).getBitmap().compress(Bitmap.CompressFormat.PNG,100,stream);
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
                    //imageView4.setImageBitmap(bitmap);
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
                        imageView4.setImageBitmap(bitmap);
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        ((BitmapDrawable)imageView4.getDrawable()).getBitmap().compress(Bitmap.CompressFormat.PNG,100,stream);
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
    public static Bitmap getResizedBitmap(Bitmap bm, float newHeight, float newWidth) {

        float width = bm.getWidth();

        float height = bm.getHeight();

        float scaleWidth = (newWidth) / width;

        float scaleHeight = (newHeight) / height;

        // create a matrix for the manipulation

        Matrix matrix = new Matrix();

        // resize the bit map

        matrix.postScale(scaleWidth, scaleHeight);

        // recreate the new Bitmap

        return Bitmap.createBitmap(bm, 0, 0, (int) width, (int) height, matrix, false);

    }

}
