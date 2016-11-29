package com.example.himaneeshchhabra.loginproject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.himaneeshchhabra.loginproject.R;

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

import static android.R.attr.onClick;


/**
 * Created by HARSHIT on 15-Oct-16.
 */
public class ItemAdapter extends ArrayAdapter<Book> {

    private ArrayList<Book> books;
    private Context context;
    private String current_user;
    private String receive_user;
    private EditText amt;
    Dialog payDialog;

    public ItemAdapter(Context context, int resource, ArrayList<Book> objects,String user,String ruser) {
        super(context, resource, objects);
        this.books=objects;
        this.context=context;
        this.current_user =user;
        System.out.println(ruser);
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
                Buy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        amt = new EditText(context);
                        if(current_user.equals(receive_user)){
                            Toast.makeText(context,"You can't buy from yourself!!",Toast.LENGTH_LONG).show();
                        }
                        else {
                            new AlertDialog.Builder(context)
                                    .setTitle("Payment")
                                    .setMessage("Enter the amount")
                                    .setView(amt)
                                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            System.out.println(amt.getText().toString());
                                            // continue with delete
                                            new Transaction().execute(amt.getText().toString());
                                        }
                                    })
                                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            // do nothing
                                        }
                                    })
                                    .setIcon(R.mipmap.ic_launcher)
                                    .show();
                        }
                        /*Dialog payDialog=new Dialog(getContext());
                        payDialog.setContentView(R.layout.payment);
                        payDialog.setCancelable(true);
                        payDialog.setOnDismissListener();
                        Button pay=(Button)payDialog.findViewById(R.id.Pay);
                        pay.setOnClickListener(this);
                        final EditText amt=(EditText)payDialog.findViewById(R.id.Amount);
                        /*pay.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(amt.getText().equals("")){
                                    Toast.makeText(getContext(),"Enter the Amount to be paid",Toast.LENGTH_LONG);
                                }
                                else{
                                    /*current_user.deductBalance(Integer.parseInt(amt.getText().toString()));
                                    receive_user.addBalance(Integer.parseInt(amt.getText().toString()));
                                    System.out.println(current_user.getBalance());
                                    System.out.println(receive_user.getBalance());
                                    new Transaction().execute(amt.getText().toString());
                                }
                            }
                        });*/
                       // payDialog.show();
                    }
                });
                if(b.getAvailable_for_borrow()==0){
                    Buy.setEnabled(false);
                }
            }
            if(Rent!=null){
                Rent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if(current_user.equals(receive_user)){
                            Toast.makeText(context,"You can't rent from yourself!!",Toast.LENGTH_LONG).show();
                        }
                        else {
                            amt = new EditText(context);
                            new AlertDialog.Builder(context)
                                    .setTitle("Payment")
                                    .setMessage("Enter the amount")
                                    .setView(amt)
                                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            System.out.println(amt.getText().toString());
                                            new Transaction().execute(amt.getText().toString());
                                            // continue with delete
                                        }
                                    })
                                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            // do nothing
                                        }
                                    })
                                    .setIcon(R.mipmap.ic_launcher)
                                    .show();
                        }
                        /*payDialog=new Dialog(getContext());
                        payDialog.setContentView(R.layout.payment);
                        payDialog.setCancelable(true);
                        payDialog.show();
                        Button pay=(Button)payDialog.findViewById(R.id.Pay);
                        pay.setOnClickListener(this);
                        amt=(EditText)payDialog.findViewById(R.id.Amount);

                        /*pay.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(amt.getText().equals("")){
                                    Toast.makeText(getContext(),"Enter the Amount to be paid", Toast.LENGTH_LONG);
                                }
                                else{

                                    /*if(current_user.isPayable(Integer.parseInt(amt.getText().toString()))) {
                                        current_user.deductBalance(Integer.parseInt(amt.getText().toString()));
                                        receive_user.addBalance(Integer.parseInt(amt.getText().toString()));
                                    }
                                    System.out.println(amt.getText());
                                    new Transaction().execute(amt.getText().toString());
                                }
                            }
                        });*/
                    }
                });
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
                        System.out.println("BookScanActivity "+current_user);
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



    private class Transaction extends AsyncTask<String,String,String>{
            ProgressDialog pdLoading = new ProgressDialog(context);
    HttpURLConnection conn;
    java.net.URL URL = null;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        //this method will be running on UI thread


    }

    protected String doInBackground(String... params) {
        String result = "";
        String current = current_user;
        String receiver = receive_user;
        String amt = params[0];
        System.out.println(current);
        System.out.println(receive_user);
        System.out.println(receiver);
        System.out.println(amt);
        String url = Link.link + "/test/transaction.php";
        try {
            URL = new URL(url);
            HttpURLConnection httpURLConnection = (HttpURLConnection) URL.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            String post_data = URLEncoder.encode("amt", "UTF-8") + "=" + URLEncoder.encode(amt, "UTF-8")+"&"+URLEncoder.encode("current_user", "UTF-8") + "=" + URLEncoder.encode(current, "UTF-8")+ "&"+URLEncoder.encode("receive_user", "UTF-8") + "=" + URLEncoder.encode(receiver, "UTF-8");
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
        System.out.println(result);


        }

    }
}





