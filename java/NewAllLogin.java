package in.autodice.classmanagementsystem;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import in.autodice.classmanagementsystem.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;

public class NewAllLogin extends AppCompatActivity {

    EditText email1,password1,classid1;
    String email,password,classid,tokenid;
    Button submit;
    String logindir,typelogin,mainemail,adminidforcommonlogin;
    ProgressDialog progressDialog;
    HttpServicesHelper tokenhelper1,tokenhelper2;
    URL url;
    HttpURLConnection connection;
    OutputStream outputStream;
    OutputStreamWriter outputStreamWriter;
    BufferedWriter bufferedWriter;
    InputStream inputStream;
    InputStreamReader inputStreamReader;
    BufferedReader bufferedReader;
    String passkey="ydcYkdJhhArwijdoPslK";
    TextView title;
    String baseURL = "https://autodice.in/projects/classmanagement/";
//    LoginHttp loginHttp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_all_login);

        title=findViewById(R.id.login_title);
        submit=findViewById(R.id.submit);
        email1=findViewById(R.id.email);
        password1=findViewById(R.id.password);
        classid1=findViewById(R.id.etclassid);

        tokenhelper1=new HttpServicesHelper(this);
        tokenhelper2=new HttpServicesHelper(this);


        getSupportActionBar().hide();

        //progressDialog=new ProgressDialog(NewAllLogin.this);

        Bundle extras = getIntent().getExtras();
        logindir= extras.getString("logintype");

        if (logindir.equals("studentlogin")){
            title.setText("Student Login");
        }else if (logindir.equals("teacherlogin")){
            title.setText("Teacher Login");
        }else if (logindir.equals("stafflogin")){
            title.setText("Staff Login");
        }

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.i("MYToken", "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        tokenid = task.getResult().getToken();

                        // Log and toast
                        Log.i("MYToken", tokenid);
                        // Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                email= String.valueOf(email1.getText());
                password= String.valueOf(password1.getText());
                classid= String.valueOf(classid1.getText());
                if (email.length()==0 || password.length()==0 || classid.length()==0){
                    Toast.makeText(getBaseContext(),"Enter Empty Fields",Toast.LENGTH_SHORT).show();
                }
                else {

                    if(isInternetAvailable() || isNetworkConnected()) {

                       if (logindir.equals("studentlogin")) {

                           new HttpServicesHelper(NewAllLogin.this).execute("commonlogin", logindir, email, classid, password);
                           new HttpServicesHelper(NewAllLogin.this).execute("inserttoken", "student", email, tokenid);


                       } else if (logindir.equals("teacherlogin")) {

                           new HttpServicesHelper(NewAllLogin.this).execute("commonlogin", logindir, email, classid, password);
                           new HttpServicesHelper(NewAllLogin.this).execute("inserttoken", "teacher", email, tokenid);

                        }else if (logindir.equals("stafflogin")) {
                           new HttpServicesHelper(NewAllLogin.this).execute("commonlogin", logindir, email, classid, password);
                       }



                    }else {
                        Toast.makeText(getBaseContext(),"No Internet Connection",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();
    }
    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    public boolean isInternetAvailable() {
        try {
            InetAddress ipAddr = InetAddress.getByName("google.com");
            //You can replace it with your name
            return !ipAddr.equals("");

        } catch (Exception e) {
            return false;
        }
    }

    }
