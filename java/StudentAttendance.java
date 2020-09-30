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
import android.widget.Button;
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

public class StudentAttendance extends AppCompatActivity {

    ListView mylist;
    ArrayList<SingleStudentAttendance> arrayList;
    SingleStdAttendanceAdapter stdAttendanceAdapter;
    Spinner sp1, sp2;
    Button btgetatt;
    String batchname="", studentname="",ADMINID,type,baseURL;
    ArrayAdapter adapter, adapter2;
    ArrayList<String>  studentlist, batchlist,idlist;
    float presentcount=0,totalc=0;
    String passkey="ydcYkdJhhArwijdoPslK";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_attendance);
        mylist=findViewById(R.id.listview);
        btgetatt=findViewById(R.id.btgetatt);

        getSupportActionBar().setTitle("Attendance");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);   //show back button

        Bundle extras = getIntent().getExtras();
        ADMINID= extras.getString("classid");

        sp1 = findViewById(R.id.spinner1);
        sp2 = findViewById(R.id.spinner2);

        batchlist=new ArrayList<>();
        new StudentAttendanceHttp().execute("getbatchlist",ADMINID);

        Log.i("checkmsg","inside");

        sp2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                btgetatt.setEnabled(true);
                mylist.setVisibility(View.INVISIBLE);
                studentname=studentlist.get(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        sp1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mylist.setVisibility(View.INVISIBLE);
                batchname = batchlist.get(position);
                studentlist=new ArrayList<>();
                new StudentAttendanceHttp().execute("getstudentlist",batchname,ADMINID);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btgetatt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new StudentAttendanceHttp().execute("getdata",batchname,studentname,ADMINID);

                float att=(presentcount/totalc)*100;
                Log.i("checkmsg",String.valueOf(att)+":"+String.valueOf(presentcount)+":"+String.valueOf(totalc));
                Toast.makeText(StudentAttendance.this,"Attendance: "+String.valueOf(att)+"%",Toast.LENGTH_SHORT).show();

            }
        });

        mylist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedid = idlist.get(position);
                final int mainID = Integer.valueOf(selectedid);

                AlertDialog.Builder builder = new AlertDialog.Builder(StudentAttendance.this);
                builder.setTitle("ALERT").setMessage("ARE YOU SURE DO YOU WANT TO DELETE?");
                builder.setCancelable(false);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new HttpServicesHelper(StudentAttendance.this).execute("delete","att", String.valueOf(mainID));
                        dialog.cancel();

                        new StudentAttendanceHttp().execute("getdata",batchname,studentname,ADMINID);
                        float att=(presentcount/totalc)*100;
                        //Toast.makeText(StudentAttendance.this,"Attendance: "+String.valueOf(att)+"%",Toast.LENGTH_SHORT).show();
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

    public class StudentAttendanceHttp extends AsyncTask<String,String,String> {

        @Override
        protected String doInBackground(String... strings) {

            type = strings[0];
            baseURL = "https://autodice.in/projects/classmanagement/";

            if (type.equals("getdata")) {
                String batchnamedata = strings[1];
                String studentdataname = strings[2];
                String adminiddata=strings[3];

                presentcount=0;
                totalc=0;

                URL url;
                HttpURLConnection connection;
                OutputStream outputStream;
                OutputStreamWriter outputStreamWriter;
                BufferedWriter bufferedWriter;

                InputStream inputStream;
                InputStreamReader inputStreamReader;
                BufferedReader bufferedReader;


                try {
                    url = new URL(baseURL + "fetchsingleattendance.php");
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setDoOutput(true);
                    connection.setDoInput(true);
                    connection.setRequestMethod("POST");

                    outputStream = connection.getOutputStream();
                    outputStreamWriter = new OutputStreamWriter(outputStream);
                    bufferedWriter = new BufferedWriter(outputStreamWriter);

                    String postdata = URLEncoder.encode("batchname", "UTF-8") + "=" + URLEncoder.encode(batchnamedata, "UTF-8");
                    postdata += "&" + URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(studentdataname, "UTF-8");
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
            } else if (type.equals("getstudentlist")) {
                try {
                    String batchnamedata=strings[1];
                    String adminiddata=strings[2];

                    Log.i("catchmsg","msg :"+baseURL);
                    URL url = new URL(baseURL +"fetchstudentfromtable.php");

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

            if (type.equals("getdata")) {
                try {

                    totalc=0;
                    presentcount=0;
                    arrayList=new ArrayList<SingleStudentAttendance>();
                    JSONArray jsonArray = new JSONArray(s);
                    JSONObject jsonObject;
                    idlist=new ArrayList<>();
                    for(int i=0;i<jsonArray.length();i++) {
                        String fetchedstudent = "";
                        fetchedstudent = jsonArray.getString(i);
                        jsonObject = new JSONObject(fetchedstudent);
                        String fetchedsubject = jsonObject.getString("subject");
                        String fetcheddate = jsonObject.getString("date");
                        String fetchedattendance = jsonObject.getString("attendance");
                        String fetchedid = jsonObject.getString("id");

                        idlist.add(fetchedid);

                        totalc++;
                        if (fetchedattendance.equals("Present")){
                            presentcount++;
                        }
                        arrayList.add(new SingleStudentAttendance(fetcheddate,fetchedsubject,fetchedattendance));
                    }
                    if (arrayList.size()==0){
                        Toast.makeText(getBaseContext(),"No Attendance for Selected Student",Toast.LENGTH_SHORT).show();
                        btgetatt.setEnabled(false);
                    }else {
                        mylist.setVisibility(View.VISIBLE);
                        stdAttendanceAdapter=new SingleStdAttendanceAdapter(StudentAttendance.this,arrayList);
                        mylist.setAdapter(stdAttendanceAdapter);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }else if (type.equals("getbatchlist")) {
                try {
                    JSONArray jsonArray = new JSONArray(s);
                    JSONObject jsonObject;


                    String fetchedbatch = "";
                    for (int i = 0; i < jsonArray.length(); i++) {
                        fetchedbatch = jsonArray.getString(i);
                        jsonObject = new JSONObject(fetchedbatch);
                        batchlist.add(jsonObject.getString("name"));
                    }
                    Log.i("nothing", String.valueOf(batchlist));
                    adapter=new ArrayAdapter(StudentAttendance.this,android.R.layout.simple_list_item_1,batchlist);
                    sp1.setAdapter(adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else if (type.equals("getstudentlist")) {
                try {
                    JSONArray jsonArray = new JSONArray(s);
                    JSONObject jsonObject;

                    studentlist=new ArrayList<>();
                    String fetchedstudent = "";
                    for (int i = 0; i < jsonArray.length(); i++) {
                        fetchedstudent = jsonArray.getString(i);
                        jsonObject = new JSONObject(fetchedstudent);
                        studentlist.add(jsonObject.getString("name"));
                    }
                    adapter2=new ArrayAdapter(StudentAttendance.this,android.R.layout.simple_list_item_1,studentlist);
                    sp2.setAdapter(adapter2);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
