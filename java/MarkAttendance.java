package in.autodice.classmanagementsystem;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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

public class MarkAttendance extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    TextView btdate;
    String status;
    Button  btmark,btreset;
    Spinner sp1, sp2;
    ListView stud;
    DatePickerDialog datePickerDialog;
    String batchname="", subjectname="", studentname="", mydate="",type,baseURL,ADMINID;
    ArrayAdapter adapter, adapter2, adapter3;
    int day, month, year;
    ArrayList<String> subjectlist, studentlist, batchlist,combolist,statuslist,namelist;
    ArrayList<SingleMarkAttendance> wholeatt;
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
        setContentView(R.layout.activity_mark_attendance);

        getSupportActionBar().setTitle("Mark Attendance");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);   //show back button

        Bundle extras = getIntent().getExtras();
        ADMINID= extras.getString("classid");

        btdate = findViewById(R.id.btAddDate);
        btmark = findViewById(R.id.btaddAttendance);
        btreset=findViewById(R.id.btresetAttendance);

        Calendar c = Calendar.getInstance();

        day = c.get(Calendar.DAY_OF_MONTH);
        month = c.get(Calendar.MONTH);
        year = c.get(Calendar.YEAR);

        sp1 = findViewById(R.id.spinnerBatchlist);
        sp2 = findViewById(R.id.spinnerSubjectFrombatch);
        stud = findViewById(R.id.spinnerStudentFromBatch);

        batchlist=new ArrayList<>();
        subjectlist=new ArrayList<>();
        namelist=new ArrayList<>();
        new MarkAttendanceHttp().execute("getbatchlist",ADMINID);
        wholeatt=new ArrayList<SingleMarkAttendance>();
        statuslist=new ArrayList<>();

        stud.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                final String selectedname = studentlist.get(position);
                if(namelist.contains(selectedname)){
                    AlertDialog.Builder builder=new AlertDialog.Builder(MarkAttendance.this);
                    builder.setTitle("DETAILS").setMessage("Attendance already marked to change, Press reset button");
                    AlertDialog alert=builder.create();
                    alert.show();

                }else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MarkAttendance.this);
                    builder.setTitle("ALERT").setMessage("MARK AS PRESENT?");
                    builder.setCancelable(false);
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            namelist.add(selectedname);
                            statuslist.set(position, " - Present");
                            wholeatt.add(new SingleMarkAttendance(mydate, subjectname, "Present", batchname, ADMINID, selectedname));
                            printcombolist();

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

            }
        });
        stud.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                final String selectedname = studentlist.get(position);
                if (namelist.contains(selectedname)){
                    AlertDialog.Builder builder=new AlertDialog.Builder(MarkAttendance.this);
                    builder.setTitle("DETAILS").setMessage("Attendance already marked to change,Press reset button");
                    AlertDialog alert=builder.create();
                    alert.show();

                }else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MarkAttendance.this);
                    builder.setTitle("ALERT").setMessage("MARK AS ABSENT?");
                    builder.setCancelable(false);
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            namelist.add(selectedname);
                            statuslist.set(position, " -Absent");
                            wholeatt.add(new SingleMarkAttendance(mydate, subjectname,"Absent",batchname, ADMINID,selectedname));
                            printcombolist();

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

        sp2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                subjectname=subjectlist.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        sp1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                batchname = batchlist.get(position);
                studentlist=new ArrayList<>();
                subjectlist=new ArrayList<>();
                new MarkAttendanceHttp().execute("getsubjectlist",batchname,ADMINID);
                new MarkAttendanceHttp().execute("getstudentlist",batchname,ADMINID);
                wholeatt=new ArrayList<>();
                statuslist=new ArrayList<>();
                namelist=new ArrayList<>();
                for(int i=0;i<studentlist.size();i++){
                    statuslist.add(" ");
                }
                printcombolist();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        datePickerDialog = new DatePickerDialog(this, DatePickerDialog.THEME_DEVICE_DEFAULT_LIGHT, this, year, month, day);

        btdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show();
            }
        });

        btmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (batchname.equals("") || subjectname.equals("")){
                    Toast.makeText(getBaseContext(),"Select Data",Toast.LENGTH_SHORT).show();
                }
                else if (mydate.equals("")){
                    Toast.makeText(getBaseContext(),"Add date",Toast.LENGTH_SHORT).show();
                }else if (wholeatt.size()!=studentlist.size()){
                    Toast.makeText(getBaseContext(),"Mark Complete Attendance",Toast.LENGTH_SHORT).show();
                }
                else {
                    for(int i=0;i<wholeatt.size();i++) {
                        SingleMarkAttendance markAttendance=wholeatt.get(i);
                        String bn=markAttendance.bname;
                        String sn=markAttendance.sname;
                        String stname=markAttendance.stname;
                        String dat=markAttendance.date;
                        String ad=markAttendance.ADid;
                        String stat=markAttendance.status;
                        new MarkAttendanceHttp().execute("markatt", bn, sn, stname, dat, stat, ad);
                    }
                    Toast.makeText(MarkAttendance.this, "Attendance Added Successfully", Toast.LENGTH_LONG).show();
                    wholeatt=new ArrayList<>();
                    statuslist=new ArrayList<>();
                    namelist=new ArrayList<>();
                    for(int i=0;i<studentlist.size();i++){
                        statuslist.add(" ");
                    }
                    printcombolist();

                }
            }
        });

        btreset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wholeatt=new ArrayList<>();
                statuslist=new ArrayList<>();
                namelist=new ArrayList<>();
                for(int i=0;i<studentlist.size();i++){
                    statuslist.add(" ");
                }
                printcombolist();
            }
        });

    }


    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        mydate= String.valueOf(dayOfMonth)+"/"+String.valueOf(month+1)+"/"+String.valueOf(year);
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

    public class MarkAttendanceHttp extends AsyncTask<String,String,String> {

        @Override
        protected String doInBackground(String... strings) {

            type = strings[0];
            baseURL = "https://autodice.in/projects/classmanagement/";

            if (type.equals("markatt")) {
                String batchnamedata = strings[1];
                String subjectdataname = strings[2];
                String studentdataname = strings[3];
                String datedata=strings[4];
                String attendancedata = strings[5];
                String adminiddata=strings[6];

                try {
                    url = new URL(baseURL + "insertattendance.php");
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setDoOutput(true);
                    connection.setDoInput(true);
                    connection.setRequestMethod("POST");

                    outputStream = connection.getOutputStream();
                    outputStreamWriter = new OutputStreamWriter(outputStream);
                    bufferedWriter = new BufferedWriter(outputStreamWriter);

                    String postdata = URLEncoder.encode("batchname", "UTF-8") + "=" + URLEncoder.encode(batchnamedata, "UTF-8");
                    postdata += "&" + URLEncoder.encode("subject", "UTF-8") + "=" + URLEncoder.encode(subjectdataname, "UTF-8");
                    postdata += "&" + URLEncoder.encode("studentname", "UTF-8") + "=" + URLEncoder.encode(studentdataname, "UTF-8");
                    postdata += "&" + URLEncoder.encode("date", "UTF-8") + "=" + URLEncoder.encode(datedata, "UTF-8");
                    postdata += "&" + URLEncoder.encode("attendance", "UTF-8") + "=" + URLEncoder.encode(attendancedata, "UTF-8");
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
            } else if (type.equals("getstudentlist")) {
                try {
                    String batchnamedata=strings[1];
                    String adminiddata=strings[2];

                    Log.i("catchmsg","msg :"+baseURL);
                    URL url = new URL(baseURL +"fetchstudentfromtable.php");

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
            }else if (type.equals("getsubjectlist")) {
                try {
                    String batchnamedata=strings[1];
                    String adminid=strings[2];

                    Log.i("catchmsg","msg :"+baseURL);
                    URL url = new URL(baseURL +"fetchsubjectfromtable.php");

                    connection = (HttpURLConnection) url.openConnection();
                    connection.setDoOutput(true);
                    connection.setDoInput(true);
                    connection.setRequestMethod("POST");

                    outputStream = connection.getOutputStream();
                    outputStreamWriter = new OutputStreamWriter(outputStream);
                    bufferedWriter = new BufferedWriter(outputStreamWriter);

                    String postdata = URLEncoder.encode("batchname", "UTF-8") + "=" + URLEncoder.encode(batchnamedata, "UTF-8");
                    postdata += "&" + URLEncoder.encode("adminid", "UTF-8") + "=" + URLEncoder.encode(adminid, "UTF-8");
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

            if (type.equals("markatt")) {
                String st = "";
                if (s.equals("success")) {

                } else {
                    st = "Some error Occured";
                    Toast.makeText(MarkAttendance.this, st, Toast.LENGTH_LONG).show();
                }

            } else if (type.equals("getbatchlist")) {
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
                    adapter=new ArrayAdapter(MarkAttendance.this,android.R.layout.simple_list_item_1,batchlist);
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
                    for(int i=0;i<studentlist.size();i++){
                        statuslist.add(" ");
                    }
                    printcombolist();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else if (type.equals("getsubjectlist")) {
                try {
                    JSONArray jsonArray = new JSONArray(s);
                    JSONObject jsonObject;

                    subjectlist = new ArrayList();

                    String fetchedbatch = "";
                    for (int i = 0; i < jsonArray.length(); i++) {
                        fetchedbatch= jsonArray.getString(i);
                        jsonObject = new JSONObject(fetchedbatch);
                        subjectlist.add(jsonObject.getString("subject"));
                    }
                    adapter3=new ArrayAdapter(MarkAttendance.this,android.R.layout.simple_list_item_1,subjectlist);
                    sp2.setAdapter(adapter3);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void printcombolist() {
        combolist=new ArrayList<>();
        for (int i = 0; i < studentlist.size(); i++) {
            String stt=studentlist.get(i);
            String ss=statuslist.get(i);
            combolist.add(stt+" "+ss);
        }
        adapter2=new ArrayAdapter(MarkAttendance.this,android.R.layout.simple_list_item_1,combolist);
        stud.setAdapter(adapter2);
    }
}
