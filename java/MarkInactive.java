package in.autodice.classmanagementsystem;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
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
import java.util.Calendar;

public class MarkInactive extends AppCompatActivity {

    Spinner sp1;
    String batchname="",type,baseURL,ADMINID;
    ArrayAdapter adapter;
    ArrayAdapter listview_adapter;
    ArrayList<String> batchlist,studentlist;
    ListView listView;

    URL url;
    HttpURLConnection connection;
    OutputStream outputStream;
    OutputStreamWriter outputStreamWriter;
    BufferedWriter bufferedWriter;
    String passkey="ydcYkdJhhArwijdoPslK";
    InputStream inputStream;
    InputStreamReader inputStreamReader;
    BufferedReader bufferedReader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mark_inactive);

        getSupportActionBar().setTitle("Mark Inactive");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);   //show back button

        Bundle extras = getIntent().getExtras();
        ADMINID= extras.getString("classid");

        sp1 = findViewById(R.id.spinner1);
        listView=findViewById(R.id.callList);


        batchlist=new ArrayList<>();
        new StudentListHttp().execute("getbatchlist",ADMINID);

        sp1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                listView.setVisibility(View.VISIBLE);
                batchname = batchlist.get(position);
                studentlist=new ArrayList<>();
                new StudentListHttp().execute("getdata",batchname,ADMINID);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final String name=studentlist.get(position);
                AlertDialog.Builder builder = new AlertDialog.Builder(MarkInactive.this);
                builder.setTitle("ALERT").setMessage("ARE YOU SURE DO YOU WANT TO MARK INACTIVE?");
                builder.setCancelable(false);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Calendar c=Calendar.getInstance();
                        int year=c.get(Calendar.YEAR);
                        new StudentListHttp().execute("updatestatus",batchname,name,ADMINID,String.valueOf(year));
                        studentlist=new ArrayList<>();
                        new StudentListHttp().execute("getdata",batchname,ADMINID);
                        dialog.cancel();
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
        });

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

    public class StudentListHttp extends AsyncTask<String,String,String> {

        @Override
        protected String doInBackground(String... strings) {

            type = strings[0];
            baseURL = "https://autodice.in/projects/classmanagement/";

            if (type.equals("getdata")) {
                String batchnamedata = strings[1];
                String adminiddata=strings[2];


                try {
                    url = new URL(baseURL + "fetchstudentviabatch.php");
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setDoOutput(true);
                    connection.setDoInput(true);
                    connection.setRequestMethod("POST");

                    outputStream = connection.getOutputStream();
                    outputStreamWriter = new OutputStreamWriter(outputStream);
                    bufferedWriter = new BufferedWriter(outputStreamWriter);

                    String postdata = URLEncoder.encode("batchname", "UTF-8") + "=" + URLEncoder.encode(batchnamedata, "UTF-8");
                    postdata += "&" + URLEncoder.encode("adminid", "UTF-8") + "=" + URLEncoder.encode(adminiddata, "UTF-8");
                    postdata += "&" + URLEncoder.encode("status", "UTF-8") + "=" + URLEncoder.encode("ACTIVE", "UTF-8");
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
            }else if (type.equals("getbatchlist")) {
                try {
                    String adminiddata=strings[1];
                    Log.i("catchmsg","msg :"+baseURL);
                    URL url = new URL(baseURL +"fetchbatchlist.php");

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

                    Log.i("checkmsg", "after getting jason: "+result);

                    return result;
                } catch (MalformedURLException e) {
                    Log.i("catchmsg","catch1 :"+e);
                    e.printStackTrace();
                } catch (IOException e) {
                    Log.i("catchmsg","catch2 :"+e);
                    e.printStackTrace();
                }
            }else if (type.equals("updatestatus")) {
                String batchnamedata = strings[1];
                String studentname=strings[2];
                String adminiddata=strings[3];
                String passing=strings[4];


                try {
                    url = new URL(baseURL + "updatestatus.php");
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setDoOutput(true);
                    connection.setDoInput(true);
                    connection.setRequestMethod("POST");

                    outputStream = connection.getOutputStream();
                    outputStreamWriter = new OutputStreamWriter(outputStream);
                    bufferedWriter = new BufferedWriter(outputStreamWriter);

                    String postdata = URLEncoder.encode("batchname", "UTF-8") + "=" + URLEncoder.encode(batchnamedata, "UTF-8");
                    postdata += "&" + URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(studentname, "UTF-8");
                    postdata += "&" + URLEncoder.encode("adminid", "UTF-8") + "=" + URLEncoder.encode(adminiddata, "UTF-8");
                    postdata += "&" + URLEncoder.encode("status", "UTF-8") + "=" + URLEncoder.encode("INACTIVE", "UTF-8");
                    postdata += "&" + URLEncoder.encode("year", "UTF-8") + "=" + URLEncoder.encode(passing, "UTF-8");
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

            if (type.equals("getdata")) {
                try {
                    JSONArray jsonArray = new JSONArray(s);
                    JSONObject jsonObject;
                    for(int i=0;i<jsonArray.length();i++) {
                        String fetchedstudent = "";
                        fetchedstudent = jsonArray.getString(i);
                        jsonObject = new JSONObject(fetchedstudent);
                        String fetchedname = jsonObject.getString("name");
                        studentlist.add(fetchedname);
                    }
                    if (studentlist.size()==0){
                        Toast.makeText(getBaseContext(),"No Students In Selected Batch",Toast.LENGTH_SHORT).show();
                        listView.setVisibility(View.INVISIBLE);
                    }else {
                        listview_adapter =new ArrayAdapter(MarkInactive.this,android.R.layout.simple_list_item_1,studentlist);
                        listView.setAdapter(listview_adapter);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }else if (type.equals("updatestatus")) {
                String st = "";
                if (s.equals("success")) {
                    st = "Marked As Inactive";
                } else {
                    st = "Some error Occured";
                }
                Toast.makeText(MarkInactive.this, st, Toast.LENGTH_LONG).show();
            }
            else if (type.equals("getbatchlist")) {
                try {
                    JSONArray jsonArray = new JSONArray(s);
                    JSONObject jsonObject;

                    String fetchedbatch = "";
                    for (int i = 0; i < jsonArray.length(); i++) {
                        fetchedbatch = jsonArray.getString(i);
                        jsonObject = new JSONObject(fetchedbatch);
                        batchlist.add(jsonObject.getString("name"));

                    }
                    adapter=new ArrayAdapter(MarkInactive.this,android.R.layout.simple_list_item_1,batchlist);
                    sp1.setAdapter(adapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}

