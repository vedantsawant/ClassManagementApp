package in.autodice.classmanagementsystem;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import in.autodice.classmanagementsystem.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

public class MainActivity extends AppCompatActivity {

    CardView cvadmin;
    CardView cvadminsignup;
    CardView cvstudent;
    CardView cvteacher;
    CardView cvstaff;
    SharedPreferences SPclassid,SPtype,SPname,SPdata,SPemail;
    String clsid,nm,typ,dat,em;
    TextView textView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView=findViewById(R.id.toolbar_title);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(MainActivity.this,"Test",Toast.LENGTH_SHORT).show();
                Intent i=new Intent(MainActivity.this,Guide.class);
                startActivity(i);
            }
        });

        SPclassid= PreferenceManager.getDefaultSharedPreferences(this);
        clsid=SPclassid.getString("myclassid","none");
        SPtype=PreferenceManager.getDefaultSharedPreferences(this);
        typ=SPtype.getString("mytype","none");
        SPname=PreferenceManager.getDefaultSharedPreferences(this);
        nm=SPname.getString("myname","none");
        SPdata=PreferenceManager.getDefaultSharedPreferences(this);
        dat=SPdata.getString("mydata","none");
        SPemail=PreferenceManager.getDefaultSharedPreferences(this);
        em=SPdata.getString("myemail","none");

        getSupportActionBar().hide();
        //Log.i("MYToken", FirebaseInstanceId.getInstance().toString());
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.i("MYToken", "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();

                        // Log and toast
                        Log.i("MYToken", token);
                       // Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });

        if (typ.equals("student")){
            Intent intent=new Intent(MainActivity.this,StudentFunctions.class);
            intent.putExtra("myname",nm);
            intent.putExtra("mybatch",dat);
            intent.putExtra("classid",clsid);
            intent.putExtra("email",em);
            startActivity(intent);
            finish();
        }else if (typ.equals("staff")){
            Intent intent=new Intent(MainActivity.this,StaffFunctions.class);
            intent.putExtra("classid",clsid);
            intent.putExtra("email",em);
            Log.i("MYERRORSPmain",clsid);
            startActivity(intent);
            finish();
        }else if (typ.equals("admin")) {
            Intent intent = new Intent(MainActivity.this, AdminFunctions.class);
            intent.putExtra("classid", clsid);
            intent.putExtra("email",em);
            Log.i("MYERRORmain",clsid);
            startActivity(intent);
            finish();
        }else if (typ.equals("teacher")) {
            Intent intent = new Intent(MainActivity.this, TeacherFunctions.class);
            intent.putExtra("classid", clsid);
            intent.putExtra("myname",nm);
            intent.putExtra("mysubject",dat);
            intent.putExtra("email",em);
            startActivity(intent);
            finish();
        }


        cvadminsignup=findViewById(R.id.signup);
        cvadmin=findViewById(R.id.cvadmin);
        cvstaff=findViewById(R.id.cvstaff);
        cvstudent=findViewById(R.id.cvstudent);
        cvteacher=findViewById(R.id.cvteacher);


        cvadminsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,AdminSignUp.class);
                startActivity(intent);
                finish();
            }
        });

        cvadmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(MainActivity.this,NewCommonLogin.class);
                startActivity(i);
                finish();

            }
        });
        cvteacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(MainActivity.this,NewAllLogin.class);
                i.putExtra("logintype","teacherlogin");
                startActivity(i);
                finish();
            }
        });
        cvstudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(MainActivity.this,NewAllLogin.class);
                i.putExtra("logintype","studentlogin");
                startActivity(i);
                finish();
            }
        });
        cvstaff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(MainActivity.this,NewAllLogin.class);
                i.putExtra("logintype","stafflogin");
                startActivity(i);
                finish();
            }
        });

    }

}
