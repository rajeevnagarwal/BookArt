package com.example.himaneeshchhabra.loginproject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.provider.BaseColumns;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
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


public class HomeActivity extends MyBaseActivity {

    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    private SimpleCursorAdapter myAdapter;
    private Suggestions[] strArrData;
    private RecyclerView recyclerView;
    private BooksAdapter adapter;
    private List<Books> albumList;
    String user;


    SearchView searchView = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handleIntent(getIntent());
        setContentView(R.layout.activity_home);
        user = getIntent().getStringExtra("user_name");
        if(user==null)
        {
            user = MainActivity.pref.getString("current_user","");
        }
        System.out.println("Hello "+user);
        final String[] from = new String[] {"username","image"};
        final int[] to = new int[] {R.id.text,R.id.image};
        /* simple cursor adapter for suggestion views*/
        myAdapter = new SimpleCursorAdapter(HomeActivity.this, R.layout.suggestion, null, from, to, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        myAdapter.setViewBinder(new SimpleCursorAdapter.ViewBinder(){
            public boolean setViewValue(View view, Cursor cursor, int columnIndex){
                if(view.getId() == R.id.image){

                    ByteArrayInputStream inputStream = new ByteArrayInputStream(cursor.getBlob(2));
                    Bitmap decodeByte = BitmapFactory.decodeStream(inputStream);
                    ((ImageView)view).setImageBitmap(decodeByte);
                    return true;
                }
                return false;
            }
        });
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        albumList = new ArrayList<>();
        adapter = new BooksAdapter(this, albumList,user);
        /* Setting layout properties of recyclerview*/

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        prepareBooks();
        if(checkConnection()) { //check network connection before fetching data
            new AsyncFetch().execute();
        }
        else
        {
            Toast.makeText(getApplicationContext(),"Connect to Network first",Toast.LENGTH_SHORT).show();
        }

    }
    public void onBackPressed()// disabling back pressed functioning
    {}
    public Boolean checkConnection()//Network connection manager
    {
        ConnectivityManager connMgr = (ConnectivityManager)getSystemService(getApplicationContext().CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if(networkInfo!=null&&networkInfo.isConnected())
            return true;
        else
            return false;
    }

    private void prepareBooks()
    {
        if(checkConnection()) {
            new fetch_popular().execute(); // Fetching database of books
        }
        else
        {
            Toast.makeText(getApplicationContext(),"Connect to network first",Toast.LENGTH_SHORT).show();
        }
    }
    private void handleIntent(Intent intent) // handling search queries
    {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            Bundle bundle = intent.getBundleExtra(SearchManager.APP_DATA);
            String type = bundle.getString("type");
            user = bundle.getString("current_user");
            System.out.println(type);
            Intent in = new Intent(this,SearchActivity.class);
            if(type.equals("User")) {
                System.out.println("hello user");
                in.putExtra("Search",query);
                in.putExtra("What",type);
            }
            else if(type.equals("Book"))
            {
                System.out.println("hello book");
                String bid = bundle.getString("bid");
                in.putExtra("Search",bid);
                in.putExtra("What",type);
            }
            in.putExtra("current_user",user);
            System.out.println("kasjd "+user);
            System.out.println("SearchActivity "+user);
            startActivity(in);
        }
    }

    protected void onNewIntent(Intent intent)
    {
        super.onNewIntent(intent);
        handleIntent(intent);
    }

    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.search);
        SearchManager searchManager = (SearchManager) HomeActivity.this.getSystemService(Context.SEARCH_SERVICE);
        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
        }
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(HomeActivity.this.getComponentName()));
            searchView.setIconified(false);
            searchView.setSuggestionsAdapter(myAdapter);
            // Getting selected (clicked) item suggestion
            searchView.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
                @Override
                public boolean onSuggestionClick(int position) {

                    // Add clicked text to search box
                    CursorAdapter ca = searchView.getSuggestionsAdapter();
                    Cursor cursor = ca.getCursor();
                    cursor.moveToPosition(position);
                    searchView.setQuery(cursor.getString(cursor.getColumnIndex("username")),false);
                    Bundle bundle = new Bundle();
                    System.out.println("TYPE:"+strArrData[cursor.getInt(0)].type);
                    System.out.println(position);
                    if(strArrData[cursor.getInt(0)].type.equals("User"))
                        bundle.putString("type", strArrData[cursor.getInt(0)].type);
                    else if(strArrData[cursor.getInt(0)].type.equals("Book"))
                    {
                        System.out.println("BOOK");
                        bundle.putString("type",strArrData[cursor.getInt(0)].type);
                        bundle.putString("bid",strArrData[cursor.getInt(0)].bid);
                    }
                    System.out.println("fffff "+user);
                    bundle.putString("current_user",user);
                    searchView.setAppSearchData(bundle);




                    return true;
                }

                @Override
                public boolean onSuggestionSelect(int position) {
                    return true;
                }
            });
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String s) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String s) {

                    // Filter data
                    if(!s.equals("")) {

                        final MatrixCursor mc = new MatrixCursor(new String[]{BaseColumns._ID, "username","image"});
                        try {
                            for (int i = 0; i < strArrData.length; i++) {
                                if (strArrData[i].suggestion.toLowerCase().startsWith(s.toLowerCase()))
                                    mc.addRow(new Object[]{i, strArrData[i].suggestion, tobyte(strArrData[i].view)});
                            }
                            if(mc.getCount()==0)
                            {
                                ImageView view =  new ImageView(getApplicationContext());
                                view.setImageDrawable(getResources().getDrawable(R.mipmap.ic_la));
                                mc.addRow(new Object[]{0,"No Suggestions",tobyte(view)});
                            }
                        }
                        catch(NullPointerException e) {
                            ImageView view =  new ImageView(getApplicationContext());
                            view.setImageDrawable(getResources().getDrawable(R.mipmap.ic_la));
                            mc.addRow(new Object[]{0,"No Suggestions",tobyte(view)});
                        }

                        myAdapter.changeCursor(mc);
                    }
                    return false;
                }
            });
        }

        return true;

    }
    private byte[] tobyte(ImageView view) // converting imageview to byte
    {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        ((BitmapDrawable)view.getDrawable()).getBitmap().compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch(item.getItemId())
        {
            case R.id.add_book:
                Intent intent1=new Intent(getApplicationContext(),BookScanActivity.class); //for adding books
                intent1.putExtra("current_user",user);
                System.out.println("BookScanActivity "+user);
                startActivity(intent1);
                break;
            case R.id.logout:
                Intent intent2 = new Intent(getApplicationContext(),MainActivity.class); //for logout
                System.out.println("MainActivity "+user);
                startActivity(intent2);
                break;
            case R.id.profile:
                final EditText amt = new EditText(this); //for modifying balance
                new AlertDialog.Builder(this)
                        .setTitle("Modify your balance")
                        .setMessage("Enter the new amount")
                        .setView(amt)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                System.out.println(amt.getText().toString());
                                // continue with delete
                                if(checkConnection()) {
                                    new Modify().execute(amt.getText().toString());
                                }

                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                        .setIcon(R.mipmap.ic_launcher)
                        .show();
            default:
                return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }
    private class AsyncFetch extends AsyncTask<String, String, String> {

        ProgressDialog pdLoading = new ProgressDialog(HomeActivity.this);
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
                url = new URL(Link.link+"/test/fetch.php");

            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return e.toString();
            }
            try {

                // Setup HttpURLConnection class to send and receive data from php and mysql
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
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
            ArrayList<Suggestions> dataList = new ArrayList<Suggestions>();
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
                        ImageView image = new ImageView(getApplicationContext());
                        byte[] decodedString = Base64.decode(json_data.getString("image"), Base64.DEFAULT);
                        Bitmap decodeByte = BitmapFactory.decodeByteArray(decodedString,0,decodedString.length);
                        image.setImageBitmap(decodeByte);
                        if(json_data.getString("type").equals("User"))
                            dataList.add(new Suggestions(json_data.getString("username"),image,json_data.getString("type")));
                        else if(json_data.getString("type").equals("Book"))
                        {
                            System.out.println("type book");
                            System.out.println(json_data.getString("bid"));
                            System.out.println(json_data.getString("username"));
                            dataList.add(new Suggestions(json_data.getString("bid"),json_data.getString("username"),image,json_data.getString("type")));
                        }
                    }

                    strArrData = dataList.toArray(new Suggestions[dataList.size()]);


                } catch (JSONException e) {
                    // You to understand what actually error is and handle it appropriately
                    //Toast.makeText(HomeActivity.this, e.toString(), Toast.LENGTH_LONG).show();
                    //Toast.makeText(HomeActivity.this, result.toString(), Toast.LENGTH_LONG).show();
                }

            }

        }

    }
    private class Modify extends AsyncTask<String,String,String>{
        HttpURLConnection conn;
        java.net.URL URL = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //this method will be running on UI thread


        }

        protected String doInBackground(String... params) {
            String result = "";
            String current = user;
            String amt = params[0];
            System.out.println(current);
            System.out.println(amt);
            String url = Link.link + "/test/modify.php";
            try {
                URL = new URL(url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) URL.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("amt", "UTF-8") + "=" + URLEncoder.encode(amt, "UTF-8")+"&"+URLEncoder.encode("current_user", "UTF-8") + "=" + URLEncoder.encode(current, "UTF-8");
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
            if(result.equals("done"))
                Toast.makeText(getApplicationContext(),"done",Toast.LENGTH_LONG).show();
            else
                Toast.makeText(getApplicationContext(),"error",Toast.LENGTH_LONG).show();
            System.out.println(result);


        }

    }
    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view);
            int column = position % spanCount;

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount;
                outRect.right = (column + 1) * spacing / spanCount;

                if (position < spanCount) {
                    outRect.top = spacing;
                }
                outRect.bottom = spacing;
            } else {
                outRect.left = column * spacing / spanCount;
                outRect.right = spacing - (column + 1) * spacing / spanCount;
                if (position >= spanCount) {
                    outRect.top = spacing;
                }
            }
        }

    }
    private class fetch_popular extends AsyncTask<String, String, String> {

        ProgressDialog pdLoading = new ProgressDialog(HomeActivity.this);
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
                url = new URL(Link.link+"/test/fetch_popular.php");

            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return e.toString();
            }
            try {

                // Setup HttpURLConnection class to send and receive data from php and mysql
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
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

            pdLoading.dismiss();


            if(result.equals("no rows")) {

                // Do some action if no data from database

            }else{

                try {

                    JSONArray jArray = new JSONArray(result);

                    // Extract data from json and store into ArrayList
                    for (int i = 0; i < jArray.length(); i++) {
                        JSONObject json_data = jArray.getJSONObject(i);
                        ImageView image = new ImageView(getApplicationContext());
                        byte[] decodedString = Base64.decode(json_data.getString("image"), Base64.DEFAULT);
                        Bitmap decodeByte = BitmapFactory.decodeByteArray(decodedString,0,decodedString.length);
                        image.setImageBitmap(decodeByte);
                        albumList.add(new Books(json_data.getString("username"), image,json_data.getString("bid"),json_data.getString("author")));

                    }

                } catch (JSONException e) {
                    // You to understand what actually error is and handle it appropriately
                    //Toast.makeText(HomeActivity.this, e.toString(), Toast.LENGTH_LONG).show();
                    //Toast.makeText(HomeActivity.this, result.toString(), Toast.LENGTH_LONG).show();
                }

            }

        }

    }





}
