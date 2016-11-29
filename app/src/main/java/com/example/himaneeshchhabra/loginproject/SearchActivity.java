package com.example.himaneeshchhabra.loginproject;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.RequestQueue;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;

public class SearchActivity extends MyListActivity {

    ArrayList<Book> listItems = new ArrayList<Book>(); //List of books with the corresponding user
    ArrayList<User> bookItems = new ArrayList<User>(); //List of users having a particular book
    ProgressDialog pDialog;
    public static int[] no_borrowed, no_sold, bid, market_price;
    public static String[] names, username, pass, gender, image, bname, author, imagebook;
    public static String[] addr, profession, contact, longitude, latitude, age;
    private ItemAdapter adapter;
    public int size;
    private String current_user; //corresponding to the logged in user

    private ImageView imageView; //this image will be displayed corresponding to the searched user or book
    private Button msg;

    private BookAdapter bookAdapter;
    private int index = -1;

    public static String JSON_URL = "";


    private TextView viewDetails, labels;

    private String What; //Indicates what kinds of object needs to be searched
    private String search; //Indicates the Id of corresponding to the objected searched
    private Integer id; //Id corresponding to the objected searched
    private String name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Intent intent = getIntent();
        imageView = (ImageView) findViewById(R.id.img);
        What = intent.getStringExtra("What");
        search = intent.getStringExtra("Search");
        current_user = intent.getStringExtra("current_user");
        System.out.println("hello user");
        System.out.println(current_user);
        msg = (Button)findViewById(R.id.smsg);
        //pDialog = new ProgressDialog(getApplicationContext());

       {
            if (What.equals("User")) {
                JSON_URL = Link.link+"/test/getUsers.php";

            } else {
                //System.out.println(Bookjson);
                msg.setVisibility(View.GONE);
                //showJSONBook(Bookjson);
                JSON_URL = Link.link+"/test/getUsers.php";
                id = Integer.parseInt(search);

            }


            sendRequest();
        }
        viewDetails = (TextView) findViewById(R.id.ViewDetails);
        labels = (TextView) findViewById(R.id.labels);
    }
    public void onSend(View v)
    {
        System.out.println("asdhfoowiuefhasldgj");
        if(name.equals(current_user)){
            Toast.makeText(SearchActivity.this,"You can't chat with yourself !!",Toast.LENGTH_LONG).show();
        }else {
            Intent i = new Intent(SearchActivity.this, ChatActivity.class);
            System.out.println(current_user);
            System.out.println(name);
            i.putExtra("current_user", current_user);
            i.putExtra("receive_user", name);
            System.out.println("ChatActivity "+current_user);
            startActivity(i);
        }
    }
    public Boolean checkConnection() //checking whether the internet access is available
    {
        ConnectivityManager connMgr = (ConnectivityManager)getSystemService(getApplicationContext().CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if(networkInfo!=null&&networkInfo.isConnected())
            return true;
        else
            return false;
    }




    private synchronized void sendRequest() { //called to feth the data from the database using volley
        if(checkConnection()) {
            pDialog = new ProgressDialog(SearchActivity.this);
            pDialog.setMessage("Loading");
            pDialog.setCancelable(false);
            pDialog.show();
            StringRequest stringRequest = new StringRequest(JSON_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            System.out.println("Inside onResponse " + What + " " + JSON_URL + " " + index);

                            if (What.equals("User")) {
                                System.out.println("User satisfies");
                                showJSON(response); //Calling the function to parse the resuling JSON result corresponding to the searching query

                            } else if (What.equals("Book")) {
                                System.out.println("Book satisfies");
                                showJSONBook(response); //Calling the function to parse the resuling JSON result corresponding to the searching query
                            }

                        }
                    },
                    new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            System.out.println("Inside onResponse Err");
                            Toast.makeText(SearchActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });

            RequestQueue requestQueue = Volley.newRequestQueue(this); //the request for the data is queued in this list
            requestQueue.add(stringRequest);
            System.out.println("Outside onResponse " + What + " " + JSON_URL + " " + index);
        }
        else
        {
            Toast.makeText(getApplicationContext(),"Connect to network first",Toast.LENGTH_SHORT).show();
        }
    }

    private void showJSON(String json) {
        pDialog.dismiss();;
        System.out.println("Hello");
        System.out.println("Inside showJSON" + json);
        ParseJSON pj = new ParseJSON(json);
        pj.parseJSON();
        size=pj.names.length;
        username = new String[size];
        names = new String[size];
        longitude=new String[size];
        latitude=new String[size];
        addr=new String[size];
        profession=new String[size];
        contact=new String[size];
        age=new String[size];
        gender=new String[size];
        pass=new String[size];
        image=new String[size];
        bname=new String[size];
        author=new String[size];
        imagebook=new String[size];
        no_borrowed= new int[size];
        market_price= new int[size];
        no_sold= new int[size];
        bid= new int[size];
        int j=0;
        int i = 0;
        boolean flag = false;
        for (i = 0; i < pj.names.length; i++) {
            //System.out.println(pj.username[i]);
            username[i]=pj.username[i];
            pass[i]=pj.pass[i];
            names[i]=pj.names[i];
            addr[i]=pj.addr[i];
            age[i]=pj.age[i];
            gender[i]=pj.gender[i];
            profession[i]=pj.profession[i];
            contact[i]=pj.contact[i];
            longitude[i]=pj.longitude[i];
            latitude[i]=pj.latitude[i];
            bid[i]=pj.bid[i];
            bname[i]=pj.bname[i];
            author[i]=pj.author[i];
            market_price[i]=pj.market_price[i];
            no_borrowed[i]=pj.no_borrowed[i];
            no_sold[i]=pj.no_sold[i];
            image[i]=pj.image[i];
            imagebook[i]=pj.imagebook[i];
            if (pj.username[i].equals(search)) {
                if(!flag){
                    j=i;
                }
                flag = true;
                Book b=new Book(pj.bname[i],pj.no_borrowed[i],pj.no_sold[i],pj.imagebook[i],pj.bid[i]+"");
                //listItems.add(pj.bname[i]+" "+pj.available_for_renting[i]+" "+pj.available_for_buying[i]);
                listItems.add(b);
                //imageView.setImageResource(R.drawable.user);

            }
        }
        if (flag) {
            labels.setText("Username: \nName: \nAge: \nContact: \nProfession: \nGender: \nAddress:");
            viewDetails.setText(pj.username[j]+ "\n" + pj.names[j] + "\n" + pj.age[j] + "\n" + pj.contact[j] + "\n" + pj.profession[j] + "\n" + pj.gender[j]+"\n"+ pj.addr[j]);
            byte[] decodedString= Base64.decode(pj.image[j],Base64.DEFAULT);
            Bitmap decodeByte= BitmapFactory.decodeByteArray(decodedString,0,decodedString.length);
            imageView.setImageBitmap(decodeByte);
            name = username[j];
            adapter=new ItemAdapter(SearchActivity.this,R.layout.list_item,listItems,current_user,name);

            setListAdapter(adapter);


        }
    }

    private void showJSONBook(String json) {


        pDialog.dismiss();

        System.out.println("Inside showJSONBook" + json);
        //Bookjson=json;
        ParseJSON pj = new ParseJSON(json);
        pj.parseJSON();
        int j=0;
        int i = 0;
        boolean flag = false;
        size=pj.names.length;
        username = new String[size];
        names = new String[size];
        longitude=new String[size];
        latitude=new String[size];
        addr=new String[size];
        profession=new String[size];
        contact=new String[size];
        age=new String[size];
        gender=new String[size];
        pass=new String[size];
        image=new String[size];
        bname=new String[size];
        author=new String[size];
        imagebook=new String[size];
        no_borrowed= new int[size];
        market_price= new int[size];
        no_sold= new int[size];
        bid= new int[size];
        for (i = 0; i < pj.addr.length; i++) {
            username[i]=pj.username[i];
            pass[i]=pj.pass[i];
            names[i]=pj.names[i];
            addr[i]=pj.addr[i];
            age[i]=pj.age[i];
            gender[i]=pj.gender[i];
            profession[i]=pj.profession[i];
            contact[i]=pj.contact[i];
            longitude[i]=pj.longitude[i];
            latitude[i]=pj.latitude[i];
            bid[i]=pj.bid[i];
            bname[i]=pj.bname[i];
            author[i]=pj.author[i];
            market_price[i]=pj.market_price[i];
            no_borrowed[i]=pj.no_borrowed[i];
            no_sold[i]=pj.no_sold[i];
            image[i]=pj.image[i];
            imagebook[i]=pj.imagebook[i];
            if (pj.bid[i]==id) {

                if(!flag){
                    j=i;
                }
                User u=new User(pj.names[i],pj.contact[i],pj.age[i],pj.image[i],pj.username[i]);
                bookItems.add(u);
                //listItems.add(pj.bname[i]+" "+pj.available_for_renting[i]+" "+pj.available_for_buying[i]);


                flag=true;

                //break;
            }

        }
        if(flag){
            labels.setText("Name: \nMarket Price: \nTimes Sold: \nTimes Borrowed:");
            viewDetails.setText(pj.bname[j] + "\n" + pj.market_price[j] + "\n" + pj.no_sold[j] + "\n" + pj.no_borrowed[j]);
            byte[] decodedString= Base64.decode(pj.imagebook[j],Base64.DEFAULT);
            Bitmap decodeByte= BitmapFactory.decodeByteArray(decodedString,0,decodedString.length);
            imageView.setImageBitmap(decodeByte);
            bookAdapter=new BookAdapter(SearchActivity.this,R.layout.book_item,bookItems,current_user,name);
            setListAdapter(bookAdapter);
        }
    }

}