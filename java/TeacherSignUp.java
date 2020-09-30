package in.autodice.classmanagementsystem;

import android.app.AlertDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import in.autodice.classmanagementsystem.R;

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
import java.util.regex.Pattern;

public class TeacherSignUp extends AppCompatActivity {

    EditText etname,etcontact,etemail,etpassword;
    Button btsubmit;
    String email,name,password,contact,ADMINID,ct="";
    ArrayAdapter adapter;
    Spinner sp1;
    String selectedSubject="",validate;
    HttpServicesHelper helper;
    TextView textView,checktext;
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
        setContentView(R.layout.activity_teacher_sign_up);

        getSupportActionBar().setTitle("Teacher Sign Up");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);   //show back button

        Bundle extras = getIntent().getExtras();
        ADMINID= extras.getString("classid");

        etname=findViewById(R.id.etname);
        etcontact=findViewById(R.id.etcontact);
        etemail=findViewById(R.id.etemail);
        etpassword=findViewById(R.id.etpassword);
        textView=findViewById(R.id.tvspsubject);
        textView.setVisibility(View.INVISIBLE);
        checktext=findViewById(R.id.checktext);
        checktext.setVisibility(View.INVISIBLE);

        btsubmit=findViewById(R.id.btsubmit);

        sp1=findViewById(R.id.spinnerSubjectlist);

        helper=new HttpServicesHelper(this);

        helper.execute("getsubjectlist",ADMINID);

        btsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectedSubject= (String) textView.getText();
                validate=(String) checktext.getText();

                Pattern emailpattern= Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$");

                email = String.valueOf(etemail.getText()).trim();
                if(etname.getText().length()==0 || etcontact.getText().length()==0 || etemail.getText().length()==0 || etpassword.getText().length()==0){
                    Toast.makeText(getBaseContext(),"Enter Complete Data",Toast.LENGTH_SHORT).show();
                }
                else if(etpassword.getText().length()<6) {
                    Toast.makeText(getBaseContext(),"Password Length is Too Short",Toast.LENGTH_SHORT).show();
                }
                else if (!emailpattern.matcher(email).matches()){
                    Toast.makeText(getBaseContext(),"Enter Vaild Email ID",Toast.LENGTH_SHORT).show();
                }else if(selectedSubject.equals("")) {
                    Toast.makeText(getBaseContext(),"Select Subject",Toast.LENGTH_SHORT).show();
                } else {
                    email = String.valueOf(etemail.getText()).trim();
                    name= String.valueOf(etname.getText()).trim();
                    password= String.valueOf(etpassword.getText()).trim();
                    contact= String.valueOf(etcontact.getText()).trim();

                    etname.setText("");
                    etpassword.setText("");
                    etemail.setText("");
                    etcontact.setText("");

                    Log.i("checkmsg", "Button clicked");
                    new TSignupHttp().execute("validcheck","teacher",name,selectedSubject,ADMINID);

                 /*   if (ct.equals("success")){
                        HttpServicesHelper helper1=new HttpServicesHelper(getBaseContext());
                        helper1.execute("insertteacher",name,contact,email,selectedSubject,password,ADMINID);
                        Log.i("Checkmsg", "inside success");

                    }else if (ct.equals("fail")){
                        Log.i("Checkmsg", "inside failure");
                        AlertDialog.Builder builder=new AlertDialog.Builder(TeacherSignUp.this);
                        builder.setTitle("Alert").setMessage("Teacher having same name already added for selected subject.");
                        AlertDialog alert=builder.create();
                        alert.show();
                    }*/
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

    public class TSignupHttp extends AsyncTask<String,String,String> {

        @Override
        protected String doInBackground(String... strings) {

            String baseURL = "https://autodice.in/projects/classmanagement/";
            try {
                String checktype = strings[1];
                String name = strings[2];
                String itsdata = strings[3];
                String adm = strings[4];

                Log.i("Checkmsg", "msg1");
                url = new URL(baseURL + "checkteacher.php");

                connection = (HttpURLConnection) url.openConnection();
                connection.setDoOutput(true);
                connection.setDoInput(true);
                connection.setRequestMethod("POST");

                outputStream = connection.getOutputStream();
                outputStreamWriter = new OutputStreamWriter(outputStream);
                bufferedWriter = new BufferedWriter(outputStreamWriter);

                String postdata = URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(name, "UTF-8");
                postdata += "&" + URLEncoder.encode("itsdata", "UTF-8") + "=" + URLEncoder.encode(itsdata, "UTF-8");
                postdata += "&" + URLEncoder.encode("adminid", "UTF-8") + "=" + URLEncoder.encode(adm, "UTF-8");
                postdata += "&" + URLEncoder.encode("passkey", "UTF-8") + "=" + URLEncoder.encode(passkey, "UTF-8");

                Log.i("Checkmsg", postdata);
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

                Log.i("Checkmsg", result);

                return result;

            } catch (MalformedURLException e) {
                Log.i("ecx", String.valueOf(e));
                e.printStackTrace();
            } catch (IOException e) {
                Log.i("ecx1", String.valueOf(e));
                e.printStackTrace();
            }


            return null;
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            String st = "";
            Log.i("Checkmsg", s);
            if (s.equals("failure")) {
                ct="fail";
                AlertDialog.Builder builder=new AlertDialog.Builder(TeacherSignUp.this);
                builder.setTitle("Alert").setMessage("Teacher having same name already added for selected subject.");
                AlertDialog alert=builder.create();
                alert.show();
            } else if (s.equals("success")){
                ct="success";
                Log.i("checkmsg:valid", s);
                HttpServicesHelper helper1=new HttpServicesHelper(TeacherSignUp.this);
                helper1.execute("insertteacher",name,contact,email,selectedSubject,password,ADMINID);

            }
            Log.i("Checkmsg", ct);


        }
    }
}
