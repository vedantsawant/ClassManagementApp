package in.autodice.classmanagementsystem;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import in.autodice.classmanagementsystem.R;

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
import java.util.ArrayList;

public class BatchList extends AppCompatActivity {

    ArrayList<String> batchlist,idlist;
    ArrayAdapter<String> adapter;
    ListView listView;
    SwipeRefreshLayout refreshLayout;
    String ADMINID,ty,type;
    TextView note;
    URL url;
    HttpURLConnection connection;
    OutputStream outputStream;
    OutputStreamWriter outputStreamWriter;
    BufferedWriter bufferedWriter;
    InputStream inputStream;
    InputStreamReader inputStreamReader;
    BufferedReader bufferedReader;
    String passkey="ydcYkdJhhArwijdoPslK";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_batch_list);

        Bundle extras = getIntent().getExtras();
        ADMINID= extras.getString("classid");
        ty= extras.getString("type");

        note=findViewById(R.id.note);
        listView=findViewById(R.id.listview_batch);
        refreshLayout=findViewById(R.id.batchswipe);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);   //show back button

        if (ty.equals("batch")){
            note.setText("Long press on Batchname to remove from list");
            getSupportActionBar().setTitle("Batches");
        }else if (ty.equals("staff")){
            note.setText("Long press on Staffname to block its login");
            getSupportActionBar().setTitle("Staff List");
        }

        batchlist=new ArrayList<>();
        idlist=new ArrayList<>();
        new BatchListHttp().execute("show",ADMINID);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                batchlist=new ArrayList<>();
                idlist=new ArrayList<>();
                new BatchListHttp().execute("show",ADMINID);
                Toast.makeText(getBaseContext(),"Refreshing", Toast.LENGTH_SHORT).show();
                refreshLayout.setRefreshing(false);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                final String idname=idlist.get(position);
                Log.i("checkmsg", "inside long click");
                if (ty.equals("batch")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(BatchList.this);
                    builder.setTitle("ALERT").setMessage("Are YOU SURE DO YOU DELETE?");
                    builder.setCancelable(false);
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            new BatchListHttp().execute("delete","batch",idname);
                            new BatchListHttp().execute("show",ADMINID);
                        }
                    });
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    AlertDialog alert=builder.create();
                    alert.show();
                }else if (ty.equals("staff")){
                    Log.i("checkmsg", "inside long click staff");
                    AlertDialog.Builder builder = new AlertDialog.Builder(BatchList.this);
                    builder.setTitle("ALERT").setMessage("Are YOU SURE DO YOU DELETE?");
                    builder.setCancelable(false);
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            new BatchListHttp().execute("delete","staff",idname);
                            new BatchListHttp().execute("show",ADMINID);
                        }
                    });
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    AlertDialog alert=builder.create();
                    alert.show();
                }
                return true;
            }
        });

    }

    public class BatchListHttp extends AsyncTask<String,String,String> {

        @Override
        protected String doInBackground(String... strings) {

           type=strings[0];
           String baseURL = "https://autodice.in/projects/classmanagement/";
           if (type.equals("show")) {
               try {

                   String adminiddata = strings[1];
                   Log.i("catchmsg", "msg :" + baseURL);
                   if (ty.equals("batch")) {
                       url = new URL(baseURL + "fetchbatchlist.php");
                   } else if (ty.equals("staff")) {
                       url = new URL(baseURL + "fetchstafflist.php");
                   }

                   connection = (HttpURLConnection) url.openConnection();
                   connection.setDoOutput(true);
                   connection.setDoInput(true);
                   connection.setRequestMethod("POST");

                   outputStream = connection.getOutputStream();
                   outputStreamWriter = new OutputStreamWriter(outputStream);
                   bufferedWriter = new BufferedWriter(outputStreamWriter);

                   String postdata = URLEncoder.encode("adminid", "UTF-8") + "=" + URLEncoder.encode(adminiddata, "UTF-8");
                   postdata += "&" + URLEncoder.encode("passkey", "UTF-8") + "=" + URLEncoder.encode(passkey, "UTF-8");


                   bufferedWriter.write(postdata);
                   bufferedWriter.flush();

                   bufferedWriter.close();
                   outputStreamWriter.close();
                   outputStream.close();

                   inputStream = connection.getInputStream();
                   inputStreamReader = new InputStreamReader(inputStream);
                   bufferedReader = new BufferedReader(inputStreamReader);


                   String line = "";
                   String result = "";

                   while ((line = bufferedReader.readLine()) != null) {
                       Log.i("Msg", "In while");
                       result += line;
                   }
                   bufferedReader.close();
                   inputStream.close();
                   inputStreamReader.close();
                   connection.disconnect();

                   Log.i("checkmsg", "after getting jason: " + result);

                   return result;
               } catch (MalformedURLException e) {
                   Log.i("catchmsg", "catch1 :" + e);
                   e.printStackTrace();
               } catch (IOException e) {
                   Log.i("catchmsg", "catch2 :" + e);
                   e.printStackTrace();
               }

           }else if (type.equals("delete")) {
               String myid = strings[2];
               String typedelete = strings[1];

               try {
                   if (typedelete.equals("batch")){
                       url = new URL(baseURL + "deletebatch.php");
                   }else if (typedelete.equals("staff")){
                       url = new URL(baseURL + "deletestaff.php");
                   }
                   connection = (HttpURLConnection) url.openConnection();
                   connection.setDoOutput(true);
                   connection.setDoInput(true);
                   connection.setRequestMethod("POST");

                   outputStream = connection.getOutputStream();
                   outputStreamWriter = new OutputStreamWriter(outputStream);
                   bufferedWriter = new BufferedWriter(outputStreamWriter);

                   String postdata = URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(myid, "UTF-8");
                   postdata += "&" + URLEncoder.encode("passkey", "UTF-8") + "=" + URLEncoder.encode(passkey, "UTF-8");

                   bufferedWriter.write(postdata);
                   bufferedWriter.flush();

                   bufferedWriter.close();
                   outputStreamWriter.close();
                   outputStream.close();

                   inputStream = connection.getInputStream();
                   inputStreamReader = new InputStreamReader(inputStream);
                   bufferedReader = new BufferedReader(inputStreamReader);
                   String line = "";
                   String result = "";

                   while ((line = bufferedReader.readLine()) != null) {
                       result += line;
                   }
                   bufferedReader.close();
                   inputStreamReader.close();
                   inputStream.close();
                   connection.disconnect();

                   Log.i("same","data:"+result);
                   return result;


               } catch (MalformedURLException e) {
                   Log.i("msg", "Catch1");
                   e.printStackTrace();
               } catch (IOException e) {
                   Log.i("msg", e.getMessage());
                   e.printStackTrace();
               }
           }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (type.equals("show")) {
                try {
                    batchlist=new ArrayList<>();
                    idlist=new ArrayList<>();
                    JSONArray jsonArray = new JSONArray(s);
                    JSONObject jsonObject;


                    String fetchedbatch = "";
                    for (int i = 0; i < jsonArray.length(); i++) {
                        fetchedbatch = jsonArray.getString(i);
                        jsonObject = new JSONObject(fetchedbatch);
                        String name = jsonObject.getString("name");
                        String ids = jsonObject.getString("id");
                        batchlist.add(name);
                        idlist.add(ids);

                    }
                    adapter = new ArrayAdapter(BatchList.this, android.R.layout.simple_list_item_1, batchlist);
                    listView.setAdapter(adapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else if (type.equals("delete")) {
                String st = "";
                if (s.equals("success")) {
                    st = "Deleted !..";
                } else {
                    st = "Some error Occured";
                }
                Toast.makeText(BatchList.this, st, Toast.LENGTH_LONG).show();
            }

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
