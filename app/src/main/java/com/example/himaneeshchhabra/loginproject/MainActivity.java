package com.example.himaneeshchhabra.loginproject;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity
{
    Button button1,button2;
    EditText editText1,editText2;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button1=(Button)findViewById(R.id.button1);
        button2=(Button)findViewById(R.id.button2);
        editText1=(EditText)findViewById(R.id.editText1);
        editText2=(EditText)findViewById(R.id.editText2);
        button1.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {

                if(editText1.getText().toString().isEmpty() || editText2.getText().toString().isEmpty())
                {
                    Toast.makeText(getApplicationContext(), "Please fill both the username and password fields", Toast.LENGTH_LONG).show();
                }
                else
                {
                    BackgroundWorker backgroundWorker=new BackgroundWorker(getApplicationContext());
                    String type="login";
                    backgroundWorker.execute(type,editText1.getText().toString(),editText2.getText().toString());

                }
            }
        });

        button2.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                Intent i=new Intent(getApplicationContext(),SignupActivity.class);
                startActivity(i);
            }
        });
    }
}