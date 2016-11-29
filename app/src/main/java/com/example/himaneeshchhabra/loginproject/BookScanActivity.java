package com.example.himaneeshchhabra.loginproject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

import static android.R.attr.password;

public class BookScanActivity extends MyBaseActivity {

    private Button mButton_bar;
    private static final String ACTION_SCAN = "com.google.zxing.client.android.SCAN";
    private String code;
    private String user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_scan);
        initialize();
        System.out.println(user);
    }
    public void initialize()
    {

        code = "";
        user = getIntent().getStringExtra("current_user");
        mButton_bar = (Button)findViewById(R.id.button_bar);
    }
    public void onClick(View v)
    {
        IntentIntegrator scanIntegrator = new IntentIntegrator(this);
        scanIntegrator.initiateScan();


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
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanningResult != null) {
            String scanContent = scanningResult.getContents();
            String scanFormat = scanningResult.getFormatName();
            System.out.println(scanContent);
            System.out.println(scanFormat);
            Toast.makeText(this,"Book scanned",Toast.LENGTH_SHORT).show();
            code = scanContent;
            if(checkConnection()) {
                new fetchid().execute(scanContent);
            }else
            {
                Toast.makeText(getApplicationContext(),"Connect to network first",Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "No scan data received!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }
    private class fetchid extends AsyncTask<String,String,String> {
        ProgressDialog pdLoading = new ProgressDialog(BookScanActivity.this);
        HttpURLConnection conn;
        URL URL = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //this method will be running on UI thread
            pdLoading.setMessage("\tLoading...");
            pdLoading.setCancelable(false);
            pdLoading.show();

        }

        protected String doInBackground(String... params) {
            String result = "";
            String isbn = params[0];
            String url = Link.link + "/test/fetchisbn.php";
            try {
                URL = new URL(url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) URL.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("isbn", "UTF-8") + "=" + URLEncoder.encode(isbn, "UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                result = "";
                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    result = result + line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return result;
            } catch (MalformedURLException e) {
               // e.printStackTrace();
            } catch (IOException e) {
               // e.printStackTrace();
            }

            return result;

        }
        @Override
        protected void onPostExecute(String result) {
            pdLoading.dismiss();
            if(result.equals("no"))
            {
                    Intent i = new Intent(getApplicationContext(),BookActivity.class);
                    i.putExtra("user_name",user);
                    i.putExtra("code",code);
                System.out.println("BookActivity "+user);
                    startActivity(i);

            }
            else
            {
                Intent i = new Intent(getApplicationContext(),NewBookActivity.class);;
                try {

                    JSONArray obj = new JSONArray(result);
                    System.out.println(result);
                    i.putExtra("book_name",obj.getJSONObject(0).getString("bookname"));
                    i.putExtra("bid",obj.getJSONObject(0).getString("bid"));
                    i.putExtra("current_user",user);
                    i.putExtra("code",code);
                    System.out.println("NewBookActivity "+user);
                }
                catch (JSONException e)
                {
                    //e.printStackTrace();

                }
                startActivity(i);


            }

        }
    }






}
