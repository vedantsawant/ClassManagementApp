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

public class PromoteStudent extends AppCompatActivity {
    Spinner spinner;
    ArrayAdapter listadapter,newadapter;
    ArrayList<String> studentlist,stdlist,idlist;
    String standard,type,baseURL,ADMINID;
    ListView listView;
    String passkey="ydcYkdJhhArwijdoPslK";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promote_student);

        getSupportActionBar().setTitle("Promote Student");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);   //show back button

        Bundle extras = getIntent().getExtras();
        ADMINID= extras.getString("classid");

        listView=findViewById(R.id.callList);
        spinner=findViewById(R.id.spinner);

        idlist=new ArrayList<>();
        stdlist=new ArrayList<>();
        for(int i=1;i<=12;i++){
            stdlist.add(String.valueOf(i));
        }
        newadapter=new ArrayAdapter(PromoteStudent.this,android.R.layout.simple_list_item_1,stdlist);
        spinner.setAdapter(newadapter);

        studentlist=new ArrayList<>();
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                listView.setVisibility(View.VISIBLE);
                standard=stdlist.get(position);
                idlist=new ArrayList<>();
                studentlist=new ArrayList<>();
                new PromoteStudenttHttp().execute("getstudentlist",standard,ADMINID);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final String listid=idlist.get(position);
                int newstd=Integer.valueOf(standard);
                newstd++;
                AlertDialog.Builder builder = new AlertDialog.Builder(PromoteStudent.this);
                builder.setTitle("ALERT").setMessage("ARE YOU SURE DO YOU WANT TO PROMOTE FROM "+standard+" TO "+String.valueOf(newstd)+"?");
                builder.setCancelable(false);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new PromoteStudenttHttp().execute("promote",listid,standard);

                        idlist=new ArrayList<>();
                        studentlist=new ArrayList<>();
                        new PromoteStudenttHttp().execute("getstudentlist",standard,ADMINID);
                        studentlist=new ArrayList<>();
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

    public class PromoteStudenttHttp extends AsyncTask<String,String,String> {

        @Override
        protected String doInBackground(String... strings) {

            type = strings[0];
            baseURL = "https://autodice.in/projects/classmanagement/";

            if (type.equals("getstudentlist")) {
                String std = strings[1];
                String adminiddata=strings[2];

                URL url;
                HttpURLConnection connection;
                OutputStream outputStream;
                OutputStreamWriter outputStreamWriter;
                BufferedWriter bufferedWriter;

                InputStream inputStream;
                InputStreamReader inputStreamReader;
                BufferedReader bufferedReader;


                try {
                    url = new URL(baseURL + "fetchstudentviastandard.php");
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setDoOutput(true);
                    connection.setDoInput(true);
                    connection.setRequestMethod("POST");

                    outputStream = connection.getOutputStream();
                    outputStreamWriter = new OutputStreamWriter(outputStream);
                    bufferedWriter = new BufferedWriter(outputStreamWriter);

                    String postdata = URLEncoder.encode("standard", "UTF-8") + "=" + URLEncoder.encode(std, "UTF-8");
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
            }else if (type.equals("promote")) {
                try {
                    String iddata=strings[1];
                    String standarddata=strings[2];

                    int s= Integer.valueOf(standarddata);
                    s=s+1;
                    standarddata=String.valueOf(s);

                    Log.i("catchmsg","msg :"+baseURL);
                    URL url = new URL(baseURL +"promote.php");

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

                    String postdata = URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(iddata, "UTF-8");
                    postdata += "&" + URLEncoder.encode("standard", "UTF-8") + "=" + URLEncoder.encode(standarddata, "UTF-8");
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

            if (type.equals("getstudentlist")) {
                try {
                    studentlist=new ArrayList<>();
                    JSONArray jsonArray = new JSONArray(s);
                    JSONObject jsonObject;
                    for(int i=0;i<jsonArray.length();i++) {
                        String fetchedstudent = "";
                        fetchedstudent = jsonArray.getString(i);
                        jsonObject = new JSONObject(fetchedstudent);
                        String fetchedname = jsonObject.getString("name");
                        String iddata = jsonObject.getString("id");
                        idlist.add(iddata);
                        studentlist.add(fetchedname);
                    }
                    if (studentlist.size()==0){
                        Toast.makeText(getBaseContext(),"No Students In Selected Standard",Toast.LENGTH_SHORT).show();
                        listView.setVisibility(View.INVISIBLE);
                    }else {
                        newadapter = new ArrayAdapter(PromoteStudent.this,android.R.layout.simple_list_item_1,studentlist);
                        listView.setAdapter(newadapter);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }else if (type.equals("promote")) {
                if (type.equals("updatestudent")) {
                    String st = "";
                    if (s.equals("success")) {
                        st = "Student Promoted!!..";
                    } else {
                        st = "Some error Occured";
                    }
                    Toast.makeText(PromoteStudent.this, st, Toast.LENGTH_LONG).show();
                }
            }
        }
    }

}
