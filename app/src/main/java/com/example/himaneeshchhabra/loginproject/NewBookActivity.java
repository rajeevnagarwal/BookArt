package com.example.himaneeshchhabra.loginproject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

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
import java.util.ArrayList;
import java.util.List;

public class NewBookActivity extends MyBaseActivity {

    private String current_user;
    private String code;
    private Spinner spinner_rent,spinner_sell;
    private Button button_book;
    private String sell,rent;
    private String book_name;
    private String bid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_book);
        initialize();
        spinner_rent.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                rent = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
    });
        spinner_sell.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sell = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



    }
    private void initialize()
    {
        ArrayList<String> list = new ArrayList<String>();
        list.add("");
        list.add("yes");
        list.add("no");
        book_name = getIntent().getStringExtra("book_name");
        bid = getIntent().getStringExtra("bid");
        Toast.makeText(getApplication(),"Book name is "+book_name,Toast.LENGTH_LONG).show();
        current_user = getIntent().getStringExtra("current_user");
        code = getIntent().getStringExtra("code");
        spinner_sell = (Spinner)findViewById(R.id.spinner_sell);
        spinner_rent = (Spinner)findViewById(R.id.spinner_rent);
        button_book = (Button)findViewById(R.id.button_book);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,list);
        spinner_sell.setAdapter(dataAdapter);
        spinner_rent.setAdapter(dataAdapter);
        sell="";
        rent="";

    }
    public void onClick(View v)
    {
        if(rent.length()>0&&sell.length()>0&&book_name!=null&&bid!=null)
        {
            if(checkConnection()) {

                new addbook().execute(current_user, bid, sell, rent);
            }
            else
            {
                Toast.makeText(getApplicationContext(),"Connect to network first",Toast.LENGTH_SHORT).show();
            }
        }
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
    private class addbook extends AsyncTask<String,String,String> {
        ProgressDialog pdLoading = new ProgressDialog(NewBookActivity.this);
        HttpURLConnection conn;
        java.net.URL URL = null;

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
            String user = params[0];
            String id = params[1];
            String s = params[2];
            String r= params[3];
            String url = Link.link + "/test/addbook.php";
            try {
                URL = new URL(url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) URL.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("user", "UTF-8") + "=" + URLEncoder.encode(user, "UTF-8") +
                        "&"+URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(id, "UTF-8")+"&"
                        +URLEncoder.encode("sell", "UTF-8") + "=" + URLEncoder.encode(s, "UTF-8")+"&"+
                        URLEncoder.encode("rent", "UTF-8") + "=" + URLEncoder.encode(r, "UTF-8");
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
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return result;

        }
        @Override
        protected void onPostExecute(String result) {
            pdLoading.dismiss();
            if(result.equals("0"))
            {
                Toast.makeText(getApplicationContext(),"You already have this book",Toast.LENGTH_LONG).show();
            }
            else if(result.equals("error"))
            {
                Toast.makeText(getApplicationContext(),"Internal Error",Toast.LENGTH_LONG).show();
            }
            else if(result.equals("done"))
            {
                Toast.makeText(getApplicationContext(),"Done",Toast.LENGTH_LONG).show();
            }
            Intent i = new Intent(getApplicationContext(),HomeActivity.class);
            i.putExtra("user_name",current_user);
            System.out.println("HomeActivity "+current_user);
            startActivity(i);
        }
    }


}
