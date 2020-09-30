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
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import in.autodice.classmanagementsystem.R;

import java.net.InetAddress;

public class AdminFunctions extends AppCompatActivity {

    CardView cvaddstaff,cvgetstudent,cvgetteacher;
    TextView textView;
    String mainid,mail;
    SharedPreferences SPclassid,SPtype,SPname,SPdata,SPemail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_functions);

        getSupportActionBar().setTitle("Class Manager");

        SPclassid= PreferenceManager.getDefaultSharedPreferences(this);
        SPtype=PreferenceManager.getDefaultSharedPreferences(this);
        SPname=PreferenceManager.getDefaultSharedPreferences(this);
        SPdata=PreferenceManager.getDefaultSharedPreferences(this);
        SPemail=PreferenceManager.getDefaultSharedPreferences(this);

        cvaddstaff=findViewById(R.id.cvaddstaff);
        cvgetstudent=findViewById(R.id.cvgetstudent);
        cvgetteacher=findViewById(R.id.cvgetteacher);
        textView=findViewById(R.id.tvid);

        Bundle extras = getIntent().getExtras();
        mainid= extras.getString("classid");
        mail=extras.getString("email");

        Log.i("MYERROR",mainid);

        if (!(isInternetAvailable() || isNetworkConnected())){
            AlertDialog.Builder builder=new AlertDialog.Builder(AdminFunctions.this);
            builder.setTitle("Alert").setMessage("No Internet Connection");
            AlertDialog alert=builder.create();
            alert.show();
        }

        SPtype.edit().putString("mytype","admin").commit();
        SPclassid.edit().putString("myclassid",mainid).commit();
        SPname.edit().putString("myname","none").commit();
        SPdata.edit().putString("mydata","none").commit();
        SPemail.edit().putString("myemail",mail).commit();

        String ADMINID=SPdata.getString("myclassid","none");
        Log.i("MYERRORSP",ADMINID);

        textView.setText("CLASS ID : "+mainid);
        cvaddstaff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1=new Intent(AdminFunctions.this,StaffSignUp.class);
                intent1.putExtra("classid",mainid);
                Log.i("MYERRORadfn",mainid);
                startActivity(intent1);
            }
        });


        cvgetteacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//Toast.makeText(StaffDashboard.this,"Data Fetched",Toast.LENGTH_LONG).show();
                Intent intent2=new Intent(AdminFunctions.this,GetTeacherData.class);
                intent2.putExtra("classid",mainid);
                startActivity(intent2);
            }
        });





        cvgetstudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(StaffDashboard.this,"Data Fetched",Toast.LENGTH_LONG).show();
                Intent intent3=new Intent(AdminFunctions.this,GetStudentData.class);
                intent3.putExtra("classid",mainid);
                startActivity(intent3);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.adminmenu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getTitle().equals("Change Password")){
            Log.i("NewError",mail);
            Intent intent=new Intent(AdminFunctions.this,ChangePassword.class);
            intent.putExtra("classid",mainid);
            intent.putExtra("email",mail);
            intent.putExtra("typeofchange","admin");
            startActivity(intent);

        }else if (item.getTitle().equals("Mark Staff as Inactive")){
            Intent intent=new Intent(AdminFunctions.this,BatchList.class);
            intent.putExtra("classid",mainid);
            intent.putExtra("type","staff");
            startActivity(intent);

        }
        else if (item.getTitle().equals("Logout")){
            AlertDialog.Builder builder = new AlertDialog.Builder(AdminFunctions.this);
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
                    Intent intent=new Intent(AdminFunctions.this,MainActivity.class);
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

