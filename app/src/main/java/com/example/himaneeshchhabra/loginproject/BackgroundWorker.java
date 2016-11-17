package com.example.himaneeshchhabra.loginproject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by Himaneesh Chhabra on 10/14/2016.
 */

public class BackgroundWorker extends AsyncTask<String,Void,String>
{
    private Context context;
    AlertDialog.Builder alertDialog;
    String my_user_name;
    BackgroundWorker (Context context)
    {
        this.context=context;
    }

    @Override
    protected String doInBackground(String... params)
    {
        String type=params[0];
        String login_url=Link.link+"/test/login.php";
        String register_url=Link.link+"/test/register.php";
        String bookregister_url=Link.link+"/test/bookregister.php";
        String user_book_url=Link.link+"/test/register_user_book.php";
        if(type.equals("login"))
        {
            try {
                String user_name=params[1];
                my_user_name=user_name;
                String password=params[2];
                URL url = new URL(login_url);
                HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream=httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String post_data= URLEncoder.encode("user_name","UTF-8")+"="+URLEncoder.encode(user_name,"UTF-8")+"&"+URLEncoder.encode("password","UTF-8")+"="+URLEncoder.encode(password,"UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream=httpURLConnection.getInputStream();
                BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                String result="";
                String line="";
                while((line=bufferedReader.readLine())!=null)
                {
                    result=result+line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if(type.equals("register"))
        {
            try {
                String username=params[1];
                my_user_name=username;
                String password=params[2];
                String name=params[3];
                String address=params[4];
                String age=params[5];
                String gender=params[6];
                String profession=params[7];
                String image=params[8];
                String contact=params[9];
                String longitude_pos=params[10];
                String latitude_pos=params[11];
                URL url = new URL(register_url);
                HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream=httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String post_data= URLEncoder.encode("username","UTF-8")+"="+URLEncoder.encode(username,"UTF-8")+"&"+URLEncoder.encode("password","UTF-8")+"="+URLEncoder.encode(password,"UTF-8")+"&"+URLEncoder.encode("name","UTF-8")+"="+URLEncoder.encode(name,"UTF-8")+"&"+URLEncoder.encode("address","UTF-8")+"="+URLEncoder.encode(address,"UTF-8")+"&"+URLEncoder.encode("age","UTF-8")+"="+URLEncoder.encode(age,"UTF-8")+"&"+URLEncoder.encode("gender","UTF-8")+"="+URLEncoder.encode(gender,"UTF-8")+"&"+URLEncoder.encode("profession","UTF-8")+"="+URLEncoder.encode(profession,"UTF-8")+"&"+URLEncoder.encode("image","UTF-8")+"="+URLEncoder.encode(image,"UTF-8")+"&"+URLEncoder.encode("contact","UTF-8")+"="+URLEncoder.encode(contact,"UTF-8")+"&"+URLEncoder.encode("longitude_pos","UTF-8")+"="+URLEncoder.encode(longitude_pos,"UTF-8")+"&"+URLEncoder.encode("latitude_pos","UTF-8")+"="+URLEncoder.encode(latitude_pos,"UTF-8");
                Log.d("Rajeev",params[7]);
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream=httpURLConnection.getInputStream();
                BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                String result="";
                String line="";
                while((line=bufferedReader.readLine())!=null)
                {
                    result=result+line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if(type.equals("bookregister"))
        {
            try {
                String bookname=params[1];
                String authorname=params[2];
                String marketprice=params[3];
                String image=params[4];
                String username = params[5];
                String available_for_selling=params[6];
                String available_for_renting=params[7];
                URL url = new URL(bookregister_url);
                HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream=httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String post_data= URLEncoder.encode("bookname","UTF-8")+"="+URLEncoder.encode(bookname,"UTF-8")+"&"+URLEncoder.encode("authorname","UTF-8")+"="+URLEncoder.encode(authorname,"UTF-8")+"&"+URLEncoder.encode("marketprice","UTF-8")+"="+URLEncoder.encode(marketprice,"UTF-8")+"&"+URLEncoder.encode("image","UTF-8")+"="+URLEncoder.encode(image,"UTF-8")+"&"+URLEncoder.encode("username","UTF-8")+"="+URLEncoder.encode(username,"UTF-8")+"&"+URLEncoder.encode("available_for_selling","UTF-8")+"="+URLEncoder.encode(available_for_selling,"UTF-8")+"&"+URLEncoder.encode("available_for_renting","UTF-8")+"="+URLEncoder.encode(available_for_renting,"UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream=httpURLConnection.getInputStream();
                BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                String result="";
                String line="";
                while((line=bufferedReader.readLine())!=null)
                {
                    result=result+line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        else if(type.equals("registeruserbook"))
        {
            try {
                String username = params[1];
                String bid = params[2];
                System.out.println("bid"+bid);
                String availabe_for_selling = params[3];
                String available_for_renting = params[4];
                URL url = new URL(user_book_url);
                HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream=httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String post_data= URLEncoder.encode("username","UTF-8")+"="+URLEncoder.encode(username,"UTF-8")+"&"+URLEncoder.encode("bid","UTF-8")+"="+URLEncoder.encode(bid,"UTF-8")+"&"+URLEncoder.encode("available_for_selling","UTF-8")+"="+URLEncoder.encode(availabe_for_selling,"UTF-8")+"&"+URLEncoder.encode("available_for_renting","UTF-8")+"="+URLEncoder.encode(available_for_renting,"UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream=httpURLConnection.getInputStream();
                BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                String result="";
                String line="";
                while((line=bufferedReader.readLine())!=null)
                {
                    result=result+line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return result;
            }
            catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return null;
    }

    @Override
    protected void onPreExecute()
    {
        //alertDialog=new AlertDialog.Builder(context);
       //alertDialog.setTitle("Login Status");
    }

    @Override
    protected void onPostExecute(String result) {
        //alertDialog.setMessage(result);
        //alertDialog.show();
        Toast.makeText(context,result,Toast.LENGTH_LONG).show();
        if(result.equals("Inserted into database sucessfully"))
        {
            Intent i = new Intent(context,BookActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.putExtra("user_name",my_user_name);
            context.startActivity(i);
        }
        if(result.equals("Congrats!!!!!!!!Login is sucessful"))
        {
            Intent i= new Intent(context,HomeActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.putExtra("user_name",my_user_name);
            context.startActivity(i);
        }
        if(result.equals("Inserted into user_book table database sucessfully"))
        {
            Intent i= new Intent(context,HomeActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            //i.putExtra("user_name",my_user_name);
            context.startActivity(i);
        }

        Log.d("OnPostExecute",result);
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }
}
