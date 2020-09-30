package in.autodice.classmanagementsystem;

import android.app.DatePickerDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.Spinner;
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
import java.util.Calendar;

public class GetBatchAttendance extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{

    Button btgetatt;
    TextView btdate;
    Spinner sp1, sp2;
    DatePickerDialog datePickerDialog;
    String batchname="", subjectname="", mydate="",type,baseURL,ADMINID;
    ArrayAdapter adapter, adapter2;
    int day, month, year;
    ListView mylistview;
    TextView tv1,tv2;
    String passkey="ydcYkdJhhArwijdoPslK";

    ArrayList<String> subjectlist,batchlist;
    ArrayList<SingleBatchAtt> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_batch_attendance);

        Bundle extras = getIntent().getExtras();
        ADMINID= extras.getString("classid");

        getSupportActionBar().setTitle("Batch Attendance");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);   //show back button

        Calendar c = Calendar.getInstance();

        day = c.get(Calendar.DAY_OF_MONTH);
        month = c.get(Calendar.MONTH);
        year = c.get(Calendar.YEAR);

        tv1=findViewById(R.id.tvspbatch);
        tv2=findViewById(R.id.tvspsubject);

        tv1.setVisibility(View.INVISIBLE);
        tv2.setVisibility(View.INVISIBLE);

        sp1 = findViewById(R.id.spinner1);
        sp2 = findViewById(R.id.spinner2);
        btdate = findViewById(R.id.btAddDate);
        btgetatt=findViewById(R.id.btgetatt);
        mylistview=findViewById(R.id.batchattList);
        final HttpServicesHelper helper=new HttpServicesHelper(this);

        datePickerDialog = new DatePickerDialog(this, DatePickerDialog.THEME_DEVICE_DEFAULT_LIGHT, this, year, month, day);

        btdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show();
            }
        });

        HttpServicesHelper helper1=new HttpServicesHelper(this);
        helper1.execute("getbatchlist","callsubject",ADMINID);


        btgetatt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HttpServicesHelper helper=new HttpServicesHelper(v.getContext());
                batchname= String.valueOf(tv1.getText());
                subjectname= String.valueOf(tv2.getText());

                if (batchname.equals("") || subjectname.equals("")){
                    Toast.makeText(GetBatchAttendance.this,"Select Data",Toast.LENGTH_SHORT).show();
                }else if (mydate.equals("")){
                    Toast.makeText(GetBatchAttendance.this,"Select Date",Toast.LENGTH_SHORT).show();
                }else {
                    helper.execute("getbatchattendance", batchname, subjectname, mydate, ADMINID);
                }

            }
        });


    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        mydate = String.valueOf(dayOfMonth) + "/" + String.valueOf(month + 1) + "/" + String.valueOf(year);
        btdate.setText(mydate);
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

    public class BatchAttendanceHttp extends AsyncTask<String,String,String> {

        @Override
        protected String doInBackground(String... strings) {

            type = strings[0];
            baseURL = "https://autodice.in/projects/classmanagement/";
            if (type.equals("getbatchlist")) {
                try {
                    String adminiddata=strings[1];
                    Log.i("catchmsg","msg :"+baseURL);
                    URL url = new URL(baseURL +"fetchbatchlist.php");

                    HttpURLConnection connection;
                    OutputStream outputStream;
                    OutputStreamWriter outputStreamWriter;
                    BufferedWriter bufferedWriter;

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

                    InputStream inputStream = connection.getInputStream();
                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);


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
            } else if (type.equals("getsubjectlist")) {
                try {
                    String batchnamedata=strings[1];
                    String adminiddata=strings[2];

                    Log.i("catchmsg","msg :"+baseURL);
                    URL url = new URL(baseURL +"fetchsubjectfromtable.php");

                    HttpURLConnection connection;
                    OutputStream outputStream;
                    OutputStreamWriter outputStreamWriter;
                    BufferedWriter bufferedWriter;

                    connection = (HttpURLConnection) url.openConnection();
                    connection.setDoOutput(true);
                    connection.setDoInput(true);
                    connection.setRequestMethod("POST");

                    outputStream = connection.getOutputStream();
                    outputStreamWriter = new OutputStreamWriter(outputStream);
                    bufferedWriter = new BufferedWriter(outputStreamWriter);

                    String postdata = URLEncoder.encode("batchname", "UTF-8") + "=" + URLEncoder.encode(batchnamedata, "UTF-8");
                    postdata += "&" + URLEncoder.encode("adminid", "UTF-8") + "=" + URLEncoder.encode(adminiddata, "UTF-8");
                    postdata += "&" + URLEncoder.encode("passkey", "UTF-8") + "=" + URLEncoder.encode(passkey, "UTF-8");

                    bufferedWriter.write(postdata);
                    bufferedWriter.flush();

                    bufferedWriter.close();
                    outputStreamWriter.close();
                    outputStream.close();

                    InputStream inputStream = connection.getInputStream();
                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);


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

            if (type.equals("getbatchlist")) {
                try {
                    JSONArray jsonArray = new JSONArray(s);
                    JSONObject jsonObject;

                    String fetchedbatch = "";
                    for (int i = 0; i < jsonArray.length(); i++) {
                        fetchedbatch = jsonArray.getString(i);
                        jsonObject = new JSONObject(fetchedbatch);
                        batchlist.add(jsonObject.getString("batchname"));
                    }
                    Log.i("nothing", String.valueOf(batchlist));
                    adapter=new ArrayAdapter(GetBatchAttendance.this,android.R.layout.simple_list_item_1,batchlist);
                    sp1.setAdapter(adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else if (type.equals("getsubjectlist")) {
                try {
                    JSONArray jsonArray = new JSONArray(s);
                    JSONObject jsonObject;

                    String fetchedsubject = "";
                    for (int i = 0; i < jsonArray.length(); i++) {
                        fetchedsubject = jsonArray.getString(i);
                        jsonObject = new JSONObject(fetchedsubject);
                        subjectlist.add(jsonObject.getString("subject"));
                    }
                    adapter2=new ArrayAdapter(GetBatchAttendance.this,android.R.layout.simple_list_item_1,subjectlist);
                    sp2.setAdapter(adapter2);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
