package in.autodice.classmanagementsystem;

import android.app.AlertDialog;
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

public class PastStudent extends AppCompatActivity {
    Spinner sp1;
    String type,baseURL,ADMINID;
    ArrayAdapter adapter,listadapter;
    ArrayList<String> studentlist,yearlist,batchlist;
    ListView listView;
    String fetchedname,fetchedmobile,fetchedemail,fetchedpmobile,fetchedaddress;

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
        setContentView(R.layout.activity_past_student);

        getSupportActionBar().setTitle("Past Student Data");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);   //show back button

        Bundle extras = getIntent().getExtras();
        ADMINID= extras.getString("classid");

        sp1 = findViewById(R.id.spinner1);
        listView=findViewById(R.id.callList);

        yearlist=new ArrayList<>();
        for(int i=2019;i<=2040;i++){
            yearlist.add(String.valueOf(i));
        }
        adapter=new ArrayAdapter(PastStudent.this,android.R.layout.simple_list_item_1,yearlist);
        sp1.setAdapter(adapter);

        sp1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                listView.setVisibility(View.VISIBLE);
                String passyear=yearlist.get(position);
                new PastStudentListHttp().execute("getdata",passyear,ADMINID);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String batchname=batchlist.get(position);
                String name=studentlist.get(position);
                new PastStudentListHttp().execute("getsinglestudent",batchname,name,ADMINID);

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

    public class PastStudentListHttp extends AsyncTask<String,String,String> {

        @Override
        protected String doInBackground(String... strings) {

            type = strings[0];
            baseURL = "https://autodice.in/projects/classmanagement/";

            if (type.equals("getdata")) {
                String yeardata = strings[1];
                String adminiddata=strings[2];


                try {
                    url = new URL(baseURL + "fetchstudentviayear.php");
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setDoOutput(true);
                    connection.setDoInput(true);
                    connection.setRequestMethod("POST");

                    outputStream = connection.getOutputStream();
                    outputStreamWriter = new OutputStreamWriter(outputStream);
                    bufferedWriter = new BufferedWriter(outputStreamWriter);

                    String postdata = URLEncoder.encode("year", "UTF-8") + "=" + URLEncoder.encode(yeardata, "UTF-8");
                    postdata += "&" + URLEncoder.encode("adminid", "UTF-8") + "=" + URLEncoder.encode(adminiddata, "UTF-8");
                    postdata += "&" + URLEncoder.encode("status", "UTF-8") + "=" + URLEncoder.encode("INACTIVE", "UTF-8");
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
            }else if (type.equals("getsinglestudent")) {
                String batchdata = strings[1];
                String namedata = strings[2];
                String adminiddata=strings[3];

                try {
                    url = new URL(baseURL + "fetchsinglestudent.php");
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setDoOutput(true);
                    connection.setDoInput(true);
                    connection.setRequestMethod("POST");

                    outputStream = connection.getOutputStream();
                    outputStreamWriter = new OutputStreamWriter(outputStream);
                    bufferedWriter = new BufferedWriter(outputStreamWriter);

                    String postdata = URLEncoder.encode("batchname", "UTF-8") + "=" + URLEncoder.encode(batchdata, "UTF-8");
                    postdata += "&" + URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(namedata, "UTF-8");
                    postdata += "&" + URLEncoder.encode("adminid", "UTF-8") + "=" + URLEncoder.encode(adminiddata, "UTF-8");
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
                    batchlist=new ArrayList<>();
                    studentlist=new ArrayList<>();
                    JSONArray jsonArray = new JSONArray(s);
                    JSONObject jsonObject;
                    for(int i=0;i<jsonArray.length();i++) {
                        String fetchedstudent = "";
                        fetchedstudent = jsonArray.getString(i);
                        jsonObject = new JSONObject(fetchedstudent);
                        String name = jsonObject.getString("name");
                        studentlist.add(name);
                        String batch = jsonObject.getString("batch");
                        batchlist.add(batch);
                    }
                    if (studentlist.size()==0){
                        Toast.makeText(getBaseContext(),"No Students In Selected Batch",Toast.LENGTH_SHORT).show();
                        listView.setVisibility(View.INVISIBLE);
                    }else {
                        listadapter =new ArrayAdapter(PastStudent.this,android.R.layout.simple_list_item_1,studentlist);
                        listView.setAdapter(listadapter);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else if (type.equals("getsinglestudent")) {
                try {
                    JSONArray jsonArray = new JSONArray(s);
                    JSONObject jsonObject;
                    for(int i=0;i<jsonArray.length();i++) {
                        String fetchedstudent = "";
                        fetchedstudent = jsonArray.getString(i);
                        jsonObject = new JSONObject(fetchedstudent);

                        fetchedname = jsonObject.getString("name");
                        fetchedmobile = jsonObject.getString("mobile");
                        fetchedpmobile = jsonObject.getString("pmobile");
                        fetchedemail = jsonObject.getString("email");
                        fetchedaddress = jsonObject.getString("address");

                        Log.i("same",fetchedname+fetchedaddress+fetchedemail+fetchedpmobile+fetchedmobile);
                        AlertDialog.Builder builder=new AlertDialog.Builder(PastStudent.this);
                        builder.setTitle("DETAILS").setMessage("NAME : "+fetchedname+"\n"+"Contact : "+fetchedmobile+"\n"+"Parents No: "+fetchedpmobile+"\n"+"Email: "+fetchedemail+"\n"+"Address: "+fetchedaddress);
                        AlertDialog alert=builder.create();
                        alert.show();
                    }
                    if (studentlist.size()==0){
                        Toast.makeText(getBaseContext(),"No Students",Toast.LENGTH_SHORT).show();
                        listView.setVisibility(View.INVISIBLE);
                    }else {
                        listadapter =new ArrayAdapter(PastStudent.this,android.R.layout.simple_list_item_1,studentlist);
                        listView.setAdapter(listadapter);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }
    }

}
