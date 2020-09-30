package in.autodice.classmanagementsystem;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.regex.Pattern;

public class StudentSignUp extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    EditText etname,etcontact,etaddress,etcontactparent,etemail,etpassword;
    Button btsubmit;
    DatePickerDialog datePickerDialog;
    int day,month,year;
    String mydate="",selectedBatch="",ADMINID,standard,validate;
    ArrayAdapter adapter,newadapter;
    Spinner sp1,spinner;
    ArrayList<String> batchlist,stdlist;
    String email,name,password,contact,parentcontact,address,ct="";
    HttpServicesHelper helper;
    TextView textView,btdate,checktxt;
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
        setContentView(R.layout.activity_student_sign_up);

        getSupportActionBar().setTitle("Student Sign Up");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);   //show back button

        Bundle extras = getIntent().getExtras();
        ADMINID= extras.getString("classid");

        checktxt=findViewById(R.id.checktext);
        etname=findViewById(R.id.etname);
        etcontact=findViewById(R.id.etcontact);
        etaddress=findViewById(R.id.etaddress);
        etcontactparent=findViewById(R.id.etcontactparent);
        etemail=findViewById(R.id.etemail);
        etpassword=findViewById(R.id.etpassword);
        textView=findViewById(R.id.tvspbatch);
        textView.setVisibility(View.INVISIBLE);
        spinner=findViewById(R.id.spinner);

        btsubmit=findViewById(R.id.btsubmit);
        btdate=findViewById(R.id.btAddDate);

        sp1=findViewById(R.id.spinnerBatchlist);

        Calendar c= Calendar.getInstance();

        day=c.get(Calendar.DAY_OF_MONTH);
        month=c.get(Calendar.MONTH);
        year=c.get(Calendar.YEAR);

        stdlist=new ArrayList<>();
        for(int i=1;i<=12;i++){
            stdlist.add(String.valueOf(i));
        }
        newadapter=new ArrayAdapter(StudentSignUp.this,android.R.layout.simple_list_item_1,stdlist);
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

        helper=new HttpServicesHelper(this);

        datePickerDialog=new DatePickerDialog(this,DatePickerDialog.THEME_DEVICE_DEFAULT_LIGHT,this,year,month,day);

        btdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show();
            }
        });

        helper.execute("getbatchlist","nothing",ADMINID);

        btsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectedBatch= (String) textView.getText();
                validate=(String) checktxt.getText();

                Pattern emailpattern= Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$");

                email = String.valueOf(etemail.getText()).trim();
                if(etname.getText().length()==0 || etaddress.getText().length()==0 || etcontact.getText().length()==0 || etcontactparent.getText().length()==0 || etemail.getText().length()==0 || etpassword.getText().length()==0){
                    Toast.makeText(getBaseContext(),"Enter Complete Data",Toast.LENGTH_SHORT).show();
                }
                else if(mydate.equals("")){
                    Toast.makeText(getBaseContext(),"Enter DOB",Toast.LENGTH_SHORT).show();
                }
                else if (selectedBatch.equals("")){
                    Toast.makeText(getBaseContext(),"Select Batch",Toast.LENGTH_SHORT).show();
                }
                else if(etpassword.getText().length()<6) {
                    Toast.makeText(getBaseContext(),"Password Length is Too Short",Toast.LENGTH_SHORT).show();
                }
                else if (!emailpattern.matcher(email).matches()){
                    Toast.makeText(getBaseContext(),"Enter Vaild Email ID",Toast.LENGTH_SHORT).show();
                }
                else if (etcontactparent.getText().length()<10 || etcontact.getText().length()<10){
                    Toast.makeText(getBaseContext(),"Enter Vaild Contact no.",Toast.LENGTH_SHORT).show();
                }
                else {
                    email = String.valueOf(etemail.getText()).trim();
                    name= String.valueOf(etname.getText()).trim();
                    password= String.valueOf(etpassword.getText()).trim();
                    address= String.valueOf(etaddress.getText()).trim();
                    contact= String.valueOf(etcontact.getText()).trim();
                    parentcontact= String.valueOf(etcontactparent.getText()).trim();


                    new SignupHttp().execute("validcheck","student",name,selectedBatch,ADMINID);
                /*    if (ct.equals("success")){
                        HttpServicesHelper helper1=new HttpServicesHelper(StudentSignUp.this);
                        helper1.execute("insertstudent",name,address,contact,parentcontact,email,selectedBatch,mydate,password,"ACTIVE",ADMINID,standard);
                    }else if (ct.equals("fail")){
                        Toast.makeText(StudentSignUp.this,"Student having same name already added in selected batch.",Toast.LENGTH_SHORT).show();
                    } */
                    etcontact.setText("");
                    etcontactparent.setText("");
                    etemail.setText("");
                    etpassword.setText("");
                    etaddress.setText("");
                    etname.setText("");

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

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        mydate= String.valueOf(dayOfMonth)+"/"+String.valueOf(month+1)+"/"+String.valueOf(year);
        btdate.setText(mydate);
    }

    public class SignupHttp extends AsyncTask<String,String,String> {

        @Override
        protected String doInBackground(String... strings) {

            String baseURL = "https://autodice.in/projects/classmanagement/";
            try {
                String checktype = strings[1];
                String name = strings[2];
                String itsdata = strings[3];
                String adm = strings[4];


                    url = new URL(baseURL + "checkstudent.php");

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
                if (s.equals("failure")) {
                    ct="fail";
                    AlertDialog.Builder builder=new AlertDialog.Builder(StudentSignUp.this);
                    builder.setTitle("Alert").setMessage("Student having same name already added in selected batch");
                    AlertDialog alert=builder.create();
                    alert.show();
                } else if (s.equals("success")){
                    ct="success";
                    HttpServicesHelper helper1=new HttpServicesHelper(StudentSignUp.this);
                    helper1.execute("insertstudent",name,address,contact,parentcontact,email,selectedBatch,mydate,password,"ACTIVE",ADMINID,standard);

                }


        }
    }
}
