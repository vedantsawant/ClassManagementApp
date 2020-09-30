package in.autodice.classmanagementsystem;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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

public class GetTeacherData extends AppCompatActivity {

    Button btgetdata,btsave;
    Spinner sp2,sp3;
    ArrayAdapter adapter,adapter2;
    String subjectname="";
    String teachername="",newName,newContact,newEmail,baseURL,type,fetchedid,ADMINID;

    EditText et1,et2,et3;
    TextView name1,contact1,email1;
    Boolean ib1value=false,ib2value=false,ib3value=false;
    ImageButton ib1,ib2,ib3;
    String passkey="ydcYkdJhhArwijdoPslK";
    ArrayList<String> subjectlist,teacherlist;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_teacher_data);

        getSupportActionBar().setTitle("Teacher Data");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);   //show back button

        sp2=findViewById(R.id.spinner2);
        sp3=findViewById(R.id.spinner3);

        name1=findViewById(R.id.name);
        contact1=findViewById(R.id.contact);
        email1=findViewById(R.id.email);

        btsave=findViewById(R.id.btsave);
        btgetdata=findViewById(R.id.btgetdata);

        ib1=findViewById(R.id.imagebutton1);
        ib2=findViewById(R.id.imagebutton2);
        ib3=findViewById(R.id.imagebutton3);

        ib1.setEnabled(false);
        ib2.setEnabled(false);
        ib3.setEnabled(false);

        et1=findViewById(R.id.etname);
        et2=findViewById(R.id.etemail);
        et3=findViewById(R.id.etcontact);

        et1.setVisibility(View.INVISIBLE);
        et2.setVisibility(View.INVISIBLE);
        et3.setVisibility(View.INVISIBLE);

        Bundle extras = getIntent().getExtras();
        ADMINID= extras.getString("classid");

        btsave.setEnabled(false);

        subjectlist=new ArrayList<>();
        new GetTeacherHttp().execute("getsubjectlist",ADMINID);

        sp3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                teachername=teacherlist.get(position);
                et1.setVisibility(View.INVISIBLE);
                et2.setVisibility(View.INVISIBLE);
                et3.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        sp2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                subjectname=subjectlist.get(position);
                teacherlist=new ArrayList<>();
                new GetTeacherHttp().execute("getteacherlist",subjectname,ADMINID);
                et1.setVisibility(View.INVISIBLE);
                et2.setVisibility(View.INVISIBLE);
                et3.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btgetdata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (subjectname.equals("") || teachername.equals("")){
                    Toast.makeText(getBaseContext(),"Select Data",Toast.LENGTH_SHORT).show();
                }
                else {
                    btsave.setEnabled(true);
                    new GetTeacherHttp().execute("getdata",subjectname,teachername,ADMINID);
                    ib1.setEnabled(true);
                    ib2.setEnabled(true);
                    ib3.setEnabled(true);
                }
            }
        });

        ib1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name1.setVisibility(View.GONE);
                et1.setVisibility(View.VISIBLE);
                ib1value=true;

            }
        });
        ib2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email1.setVisibility(View.GONE);
                et2.setVisibility(View.VISIBLE);
                ib2value=true;

            }
        });
        ib3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contact1.setVisibility(View.GONE);
                et3.setVisibility(View.VISIBLE);
                ib3value=true;
            }
        });
        btsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et1.getText().length()==0 && ib1value==true){
                    Toast.makeText(GetTeacherData.this,"Enter Name",Toast.LENGTH_SHORT).show();
                    newName=String.valueOf(et1.getText());
                }
                else if (et2.getText().length()==0 && ib2value==true){
                    Toast.makeText(GetTeacherData.this,"Enter Email",Toast.LENGTH_SHORT).show();
                    newEmail=String.valueOf(et2.getText());
                }else if (et3.getText().length()==0 && ib3value==true){
                    Toast.makeText(GetTeacherData.this,"Enter Contact",Toast.LENGTH_SHORT).show();
                    newContact=String.valueOf(et3.getText());
                }
                else {
                    newName= String.valueOf(et1.getText());
                    newEmail= String.valueOf(et2.getText());
                    newContact= String.valueOf(et3.getText());
                    new GetTeacherHttp().execute("updateteacher",fetchedid,newName,newEmail,newContact);
                }
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

    public class GetTeacherHttp extends AsyncTask<String,String,String> {

        @Override
        protected String doInBackground(String... strings) {

            type = strings[0];
            baseURL = "https://autodice.in/projects/classmanagement/";

            if (type.equals("updateteacher")) {

                String iddata = strings[1];
                String namedata = strings[2];
                String emaildata = strings[3];
                String contactdata = strings[4];

                URL url;
                HttpURLConnection connection;
                OutputStream outputStream;
                OutputStreamWriter outputStreamWriter;
                BufferedWriter bufferedWriter;

                InputStream inputStream;
                InputStreamReader inputStreamReader;
                BufferedReader bufferedReader;


                try {
                    url = new URL(baseURL + "updateteacher.php");
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setDoOutput(true);
                    connection.setDoInput(true);
                    connection.setRequestMethod("POST");

                    outputStream = connection.getOutputStream();
                    outputStreamWriter = new OutputStreamWriter(outputStream);
                    bufferedWriter = new BufferedWriter(outputStreamWriter);

                    String postdata = URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(iddata, "UTF-8");
                    postdata += "&" + URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(namedata, "UTF-8");
                    postdata += "&" + URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(emaildata, "UTF-8");
                    postdata += "&" + URLEncoder.encode("mobile", "UTF-8") + "=" + URLEncoder.encode(contactdata, "UTF-8");
                    postdata += "&" + URLEncoder.encode("passkey", "UTF-8") + "=" + URLEncoder.encode(passkey, "UTF-8");

                    Log.i("updateloop",postdata);
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
            }else if (type.equals("getdata")) {
                try {
                    String subjectname=strings[1];
                    String teachername=strings[2];
                    String adminiddata=strings[3];

                    Log.i("catchmsg","msg :"+baseURL);
                    URL url = new URL(baseURL +"fetchteacherfromtable.php");

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

                    String postdata = URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(teachername, "UTF-8");
                    postdata += "&" + URLEncoder.encode("subject", "UTF-8") + "=" + URLEncoder.encode(subjectname, "UTF-8");
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
            } else if (type.equals("getsubjectlist")) {
                try {
                    String adminiddata=strings[1];

                    Log.i("catchmsg","msg :"+baseURL);
                    URL url = new URL(baseURL +"fetchsubjectlist.php");

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
            }else if (type.equals("getteacherlist")) {
                try {
                    String subjectnamedata=strings[1];
                    String adminiddata=strings[2];

                    Log.i("catchmsg","msg :"+baseURL);
                    URL url = new URL(baseURL +"fetchteacherlist.php");

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

                    String postdata = URLEncoder.encode("subject", "UTF-8") + "=" + URLEncoder.encode(subjectnamedata, "UTF-8");
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
            Log.i("same",type);
            if (type.equals("getdata")) {
                try {
                    JSONArray jsonArray = new JSONArray(s);
                    JSONObject jsonObject;
                    for(int i=0;i<jsonArray.length();i++) {
                        String fetchedstudent = "";
                        fetchedstudent = jsonArray.getString(i);
                        jsonObject = new JSONObject(fetchedstudent);
                        String fetchedname = jsonObject.getString("name");
                        String fetchedcontact = jsonObject.getString("mobile");
                        String fetchedemail = jsonObject.getString("email");
                        fetchedid = jsonObject.getString("id");

                        newContact=fetchedcontact;
                        newEmail=fetchedemail;
                        newName=fetchedname;

                        email1.setText(fetchedemail);
                        name1.setText(fetchedname);
                        contact1.setText(fetchedcontact);

                        et1.setText(fetchedname);
                        et2.setText(fetchedemail);
                        et3.setText(fetchedcontact);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else if (type.equals("getsubjectlist")) {
                try {
                    Log.i("same",type);
                    JSONArray jsonArray = new JSONArray(s);
                    JSONObject jsonObject;

                    String fetchedsubject = "";
                    for (int i = 0; i < jsonArray.length(); i++) {
                        fetchedsubject = jsonArray.getString(i);
                        jsonObject = new JSONObject(fetchedsubject);
                        subjectlist.add(jsonObject.getString("subject"));
                    }
                    Log.i("same", String.valueOf(subjectlist));
                    adapter=new ArrayAdapter(GetTeacherData.this,android.R.layout.simple_list_item_1,subjectlist);
                    sp2.setAdapter(adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else if (type.equals("getteacherlist")) {
                try {
                    JSONArray jsonArray = new JSONArray(s);
                    JSONObject jsonObject;

                    String fetchedteacher = "";
                    for (int i = 0; i < jsonArray.length(); i++) {
                        fetchedteacher = jsonArray.getString(i);
                        jsonObject = new JSONObject(fetchedteacher);
                        teacherlist.add(jsonObject.getString("name"));
                    }
                    Log.i("same", String.valueOf(teacherlist));
                    adapter2=new ArrayAdapter(GetTeacherData.this,android.R.layout.simple_list_item_1,teacherlist);
                    sp3.setAdapter(adapter2);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else if (type.equals("updateteacher")) {
                String st = "";
                if (s.equals("success")) {
                    st = "Data Added Successfully";
                } else {
                    st = "Some error Occured";
                }
                Toast.makeText(GetTeacherData.this, st, Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }


}
