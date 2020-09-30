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

public class EditStudentData extends AppCompatActivity {

    EditText etname,etcontact,etaddress,etcontactparent,etemail;
    String email,name,contact,parentcontact,address,status,type,baseURL,ADMINID,mainid,standard;
    Button btsubmit;
    Spinner sp1,spinner;
    String batchname="";
    ArrayAdapter adapter,newadapter;
    ArrayList<String> batchlist,stdlist;
    int con=0;
    String passkey="ydcYkdJhhArwijdoPslK";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_student_data);

        getSupportActionBar().setTitle("Edit");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);   //show back button

        Bundle extras = getIntent().getExtras();
        mainid= extras.getString("studentid");
        ADMINID=extras.getString("classid");

        Log.i("print",mainid);
        sp1 = findViewById(R.id.spinner1);
        spinner=findViewById(R.id.spinner);


        stdlist=new ArrayList<>();
        for(int i=1;i<=12;i++){
            stdlist.add(String.valueOf(i));
        }
        newadapter=new ArrayAdapter(EditStudentData.this,android.R.layout.simple_list_item_1,stdlist);
        spinner.setAdapter(newadapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                standard=stdlist.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        sp1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                batchname = batchlist.get(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        etname=findViewById(R.id.etname);
        etcontact=findViewById(R.id.etcontact);
        etaddress=findViewById(R.id.etaddress);
        etcontactparent=findViewById(R.id.etcontactparent);
        etemail=findViewById(R.id.etemail);

        new EditstudentHttp().execute("getdatatoedit");

        btsubmit=findViewById(R.id.btsubmit);

        btsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Pattern emailpattern = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$");

                email = String.valueOf(etemail.getText()).trim();
                if (etname.getText().length() == 0 || etaddress.getText().length() == 0 || etcontact.getText().length() == 0 || etcontactparent.getText().length() == 0 || etemail.getText().length() == 0) {
                    Toast.makeText(getBaseContext(), "Enter Complete Data", Toast.LENGTH_SHORT).show();
                } else if (!emailpattern.matcher(email).matches()) {
                    Toast.makeText(getBaseContext(), "Enter Vaild Email ID", Toast.LENGTH_SHORT).show();
                } else if (etcontactparent.getText().length() < 10 || etcontact.getText().length() < 10) {
                    Toast.makeText(getBaseContext(), "Enter Vaild Contact no.", Toast.LENGTH_SHORT).show();
                }else if (batchname.equals("")){
                    Toast.makeText(getBaseContext(), "Select Batch", Toast.LENGTH_SHORT).show();
                }
                else {

                    email = String.valueOf(etemail.getText()).trim();
                    name = String.valueOf(etname.getText()).trim();
                    address = String.valueOf(etaddress.getText()).trim();
                    contact = String.valueOf(etcontact.getText()).trim();
                    parentcontact = String.valueOf(etcontactparent.getText()).trim();

                    new EditstudentHttp().execute("updatestudent",name,address,contact,parentcontact,email,batchname,standard);

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

    public class EditstudentHttp extends AsyncTask<String,String,String> {

        @Override
        protected String doInBackground(String... strings) {

            type = strings[0];
            baseURL = "https://autodice.in/projects/classmanagement/";

            if (type.equals("updatestudent")) {
                String namedata = strings[1];
                String addressdata = strings[2];
                String mobiledata = strings[3];
                String pmobiledata = strings[4];
                String emaildata = strings[5];
                String batchdata = strings[6];
                String stddata = strings[7];

                URL url;
                HttpURLConnection connection;
                OutputStream outputStream;
                OutputStreamWriter outputStreamWriter;
                BufferedWriter bufferedWriter;

                InputStream inputStream;
                InputStreamReader inputStreamReader;
                BufferedReader bufferedReader;


                try {
                    url = new URL(baseURL + "updatestudent.php");
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setDoOutput(true);
                    connection.setDoInput(true);
                    connection.setRequestMethod("POST");

                    outputStream = connection.getOutputStream();
                    outputStreamWriter = new OutputStreamWriter(outputStream);
                    bufferedWriter = new BufferedWriter(outputStreamWriter);

                    String postdata = URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(mainid, "UTF-8");
                    postdata += "&" + URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(namedata, "UTF-8");
                    postdata += "&" + URLEncoder.encode("mobile", "UTF-8") + "=" + URLEncoder.encode(mobiledata, "UTF-8");
                    postdata += "&" + URLEncoder.encode("address", "UTF-8") + "=" + URLEncoder.encode(addressdata, "UTF-8");
                    postdata += "&" + URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(emaildata, "UTF-8");
                    postdata += "&" + URLEncoder.encode("pmobile", "UTF-8") + "=" + URLEncoder.encode(pmobiledata, "UTF-8");
                    postdata += "&" + URLEncoder.encode("batch", "UTF-8") + "=" + URLEncoder.encode(batchdata, "UTF-8");
                    postdata += "&" + URLEncoder.encode("standard", "UTF-8") + "=" + URLEncoder.encode(stddata, "UTF-8");
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

                    Log.i("msg","update result:"+result);

                    return result;


                } catch (MalformedURLException e) {
                    Log.i("msg", "Catch1");
                    e.printStackTrace();
                } catch (IOException e) {
                    Log.i("msg", e.getMessage());
                    e.printStackTrace();
                }
            }else if (type.equals("getdatatoedit")) {

                URL url;
                HttpURLConnection connection;
                OutputStream outputStream;
                OutputStreamWriter outputStreamWriter;
                BufferedWriter bufferedWriter;

                InputStream inputStream;
                InputStreamReader inputStreamReader;
                BufferedReader bufferedReader;


                try {
                    url = new URL(baseURL + "fetchstudentviaid.php");
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setDoOutput(true);
                    connection.setDoInput(true);
                    connection.setRequestMethod("POST");
                    con++;

                    outputStream = connection.getOutputStream();
                    outputStreamWriter = new OutputStreamWriter(outputStream);
                    bufferedWriter = new BufferedWriter(outputStreamWriter);

                    String postdata = URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(mainid, "UTF-8");
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

                    Log.i("msg","result single:"+result);
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
                    String adminid=strings[1];
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

                    String postdata = URLEncoder.encode("adminid", "UTF-8") + "=" + URLEncoder.encode(adminid, "UTF-8");
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

            if (type.equals("updatestudent")) {
                String st = "";
                Log.i("msg","update result:"+s);
                if (s.equals("success")) {
                    st = "Data Updated Successfully";
                } else {
                    st = "Some error Occured";
                }
                Toast.makeText(EditStudentData.this, st, Toast.LENGTH_LONG).show();
                finish();
            }if (type.equals("getdatatoedit")) {
                try {
                    JSONArray jsonArray = new JSONArray(s);
                    JSONObject jsonObject;
                    for(int i=0;i<jsonArray.length();i++) {
                        String fetchedstudent = "";
                        fetchedstudent = jsonArray.getString(i);
                        jsonObject = new JSONObject(fetchedstudent);
                        String fetchedname = jsonObject.getString("name");
                        String fechedcontact = jsonObject.getString("mobile");
                        String fetchedaddress = jsonObject.getString("address");
                        String fetchedpcontact = jsonObject.getString("pmobile");
                        String fetchedemail = jsonObject.getString("email");

                        etemail.setText(fetchedemail);
                        etaddress.setText(fetchedaddress);
                        etname.setText(fetchedname);
                        etcontact.setText(fechedcontact);
                        etcontactparent.setText(fetchedpcontact);

                        batchlist=new ArrayList<>();
                        new EditstudentHttp().execute("getbatchlist",ADMINID);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

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
                        adapter=new ArrayAdapter(EditStudentData.this,android.R.layout.simple_list_item_1,batchlist);
                        sp1.setAdapter(adapter);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
