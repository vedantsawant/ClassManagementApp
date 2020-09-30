package in.autodice.classmanagementsystem;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import in.autodice.classmanagementsystem.R;

import java.net.InetAddress;

public class TeacherFunctions extends AppCompatActivity {

    CardView lecturett,doubts,notification;
    String ADMINID,trname,sub,em;
    SharedPreferences SPclassid,SPtype,SPname,SPdata,SPemail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_functions);

        getSupportActionBar().setTitle("Class Manager");

        SPclassid= PreferenceManager.getDefaultSharedPreferences(this);
        SPtype=PreferenceManager.getDefaultSharedPreferences(this);
        SPname=PreferenceManager.getDefaultSharedPreferences(this);
        SPdata=PreferenceManager.getDefaultSharedPreferences(this);
        SPemail=PreferenceManager.getDefaultSharedPreferences(this);

        Bundle extras = getIntent().getExtras();
        ADMINID= extras.getString("classid");
        trname=extras.getString("myname");
        sub=extras.getString("mysubject");
        em=extras.getString("email");


        SPtype.edit().putString("mytype","teacher").commit();
        SPclassid.edit().putString("myclassid",ADMINID).commit();
        SPname.edit().putString("myname",trname).commit();
        SPdata.edit().putString("mydata",sub).commit();
        SPemail.edit().putString("myemail",em).commit();

        if (!(isInternetAvailable() || isNetworkConnected())){
            AlertDialog.Builder builder=new AlertDialog.Builder(TeacherFunctions.this);
            builder.setTitle("Alert").setMessage("No Internet Connection");
            AlertDialog alert=builder.create();
            alert.show();
        }

        lecturett=findViewById(R.id.lecturett);
        doubts=findViewById(R.id.attendance);
        notification=findViewById(R.id.notification);

        lecturett.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(),StudentLectures.class);
                intent.putExtra("classid",ADMINID);
                intent.putExtra("mybatch",trname);
                intent.putExtra("typelecture","teacher");
                intent.putExtra("email","none");
                startActivity(intent);

            }
        });

        doubts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(),DoubtList.class);
                intent.putExtra("classid",ADMINID);
                intent.putExtra("myname",trname);
                intent.putExtra("mysubject",sub);
                startActivity(intent);
            }
        });
        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(),StaffNotificationList.class);
                intent.putExtra("classid",ADMINID);
                intent.putExtra("typelist","teacher");
                intent.putExtra("data",trname);
                intent.putExtra("mysubject",sub);
                startActivity(intent);
            }
        });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.optmenu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getTitle().equals("Change Password")){
            Intent intent=new Intent(TeacherFunctions.this,ChangePassword.class);
            intent.putExtra("classid",ADMINID);
            intent.putExtra("email",em);
            intent.putExtra("typeofchange","teacher");
            startActivity(intent);

        }else if (item.getTitle().equals("Logout")){
            AlertDialog.Builder builder = new AlertDialog.Builder(TeacherFunctions.this);
            builder.setTitle("ALERT").setMessage("ARE YOU SURE DO YOU WANT TO LOGOUT");
            builder.setCancelable(false);
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    SPclassid.edit().putString("myclassid","none").commit();
                    SPtype.edit().putString("mytype","none").commit();
                    SPname.edit().putString("myname","none").commit();
                    SPdata.edit().putString("mydata","none").commit();
                    SPemail.edit().putString("myemail","none").commit();
                    Intent intent=new Intent(TeacherFunctions.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                    dialog.cancel();
                    String str="Successfully Logout";
                    Toast.makeText(getBaseContext(),str,Toast.LENGTH_SHORT).show();
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
        return super.onOptionsItemSelected(item);
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
