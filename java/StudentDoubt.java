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
import java.util.regex.Pattern;

public class StudentDoubt extends AppCompatActivity {
    Spinner sp2,sp3;
    Button submit;
    EditText email,doubt;
    String ADMINID;
    ArrayList<String> subjectlist,teacherlist;
    String teachername,subjectname,type,baseURL,stname;
    ArrayAdapter adapter,adapter2;
    String passkey="ydcYkdJhhArwijdoPslK";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_doubt);

        getSupportActionBar().setTitle("Ask A Doubt");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);   //show back button

        Bundle extras = getIntent().getExtras();
        ADMINID= extras.getString("classid");
        stname=extras.getString("stname");

        sp2=findViewById(R.id.spinner2);
        sp3=findViewById(R.id.spinner3);

        submit=findViewById(R.id.submit);
        email=findViewById(R.id.email);
        doubt=findViewById(R.id.doubt);

        subjectlist=new ArrayList<>();
        new InsertDoubtHttp().execute("getsubjectlist",ADMINID);

        sp3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                teachername=teacherlist.get(position);
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
                new InsertDoubtHttp().execute("getteacherlist",subjectname,ADMINID);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String e=String.valueOf(email.getText());
                String d=String.valueOf(doubt.getText());

                Pattern emailpattern= Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$");

                if(e.length()==0||d.length()==0){
                    Toast.makeText(getBaseContext(),"Fill in all the fields",Toast.LENGTH_SHORT).show();
                }else if (subjectname.equals("") || teachername.equals("")){
                    Toast.makeText(getBaseContext(),"Select Data",Toast.LENGTH_SHORT).show();
                }
                else if(e.equals("")){
                    Toast.makeText(getBaseContext(),"Fill in the email field",Toast.LENGTH_SHORT).show();
                }
                else if(d.equals("")){
                    Toast.makeText(getBaseContext(),"Fill in the doubt field",Toast.LENGTH_SHORT).show();
                }
                else if (!emailpattern.matcher(e).matches()){
                    Toast.makeText(getBaseContext(),"Enter Vaild Email ID",Toast.LENGTH_SHORT).show();
                }
                else {

                    new InsertDoubtHttp().execute("insertdoubt",stname,e,subjectname,teachername,d,ADMINID);
                    email.setText("");
                    doubt.setText("");
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

    public class InsertDoubtHttp extends AsyncTask<String,String,String> {

        @Override
        protected String doInBackground(String... strings) {

            type = strings[0];
            baseURL = "https://autodice.in/projects/classmanagement/";

            if (type.equals("insertdoubt")) {

                String namedata = strings[1];
                String emaildata = strings[2];
                String subjectdata = strings[3];
                String teacherdata = strings[4];
                String doubt = strings[5];
                String adminid=strings[6];

                URL url;
                HttpURLConnection connection;
                OutputStream outputStream;
                OutputStreamWriter outputStreamWriter;
                BufferedWriter bufferedWriter;

                InputStream inputStream;
                InputStreamReader inputStreamReader;
                BufferedReader bufferedReader;


                try {
                    url = new URL(baseURL + "insertdoubt.php");
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setDoOutput(true);
                    connection.setDoInput(true);
                    connection.setRequestMethod("POST");

                    outputStream = connection.getOutputStream();
                    outputStreamWriter = new OutputStreamWriter(outputStream);
                    bufferedWriter = new BufferedWriter(outputStreamWriter);

                    String postdata = URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(namedata, "UTF-8");
                    postdata += "&" + URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(emaildata, "UTF-8");
                    postdata += "&" + URLEncoder.encode("subject", "UTF-8") + "=" + URLEncoder.encode(subjectdata, "UTF-8");
                    postdata += "&" + URLEncoder.encode("teacher", "UTF-8") + "=" + URLEncoder.encode(teacherdata, "UTF-8");
                    postdata += "&" + URLEncoder.encode("doubt", "UTF-8") + "=" + URLEncoder.encode(doubt, "UTF-8");
                    postdata += "&" + URLEncoder.encode("adminid", "UTF-8") + "=" + URLEncoder.encode(adminid, "UTF-8");
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
            if (type.equals("getsubjectlist")) {
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
                    adapter=new ArrayAdapter(StudentDoubt.this,android.R.layout.simple_list_item_1,subjectlist);
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
                    adapter2=new ArrayAdapter(StudentDoubt.this,android.R.layout.simple_list_item_1,teacherlist);
                    sp3.setAdapter(adapter2);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else if (type.equals("insertdoubt")) {
                String st = "";
                if (s.equals("success")) {
                    st = "Doubt Submitted Successfully";
                } else {
                    st = "Some error Occured";
                }
                Toast.makeText(StudentDoubt.this, st, Toast.LENGTH_LONG).show();
            }
        }
    }


}
