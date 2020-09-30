package in.autodice.classmanagementsystem;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import in.autodice.classmanagementsystem.R;

import java.net.InetAddress;


public class StudentFunctions extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle toggle;
    String ADMINID,batch,stname,mail;
    SharedPreferences SPclassid,SPtype,SPname,SPdata,SPemail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_functions);

        getSupportActionBar().setTitle("Class Manager");

        if (!(isInternetAvailable() || isNetworkConnected())){
            AlertDialog.Builder builder=new AlertDialog.Builder(StudentFunctions.this);
            builder.setTitle("Alert").setMessage("No Internet Connection");
            AlertDialog alert=builder.create();
            alert.show();
        }

        Bundle extras = getIntent().getExtras();
        ADMINID= extras.getString("classid");
        batch=extras.getString("mybatch");
        stname=extras.getString("myname");
        mail=extras.getString("email");


        SPclassid= PreferenceManager.getDefaultSharedPreferences(this);
        SPtype=PreferenceManager.getDefaultSharedPreferences(this);
        SPname=PreferenceManager.getDefaultSharedPreferences(this);
        SPdata=PreferenceManager.getDefaultSharedPreferences(this);
        SPemail=PreferenceManager.getDefaultSharedPreferences(this);

        SPtype.edit().putString("mytype","student").commit();
        SPclassid.edit().putString("myclassid",ADMINID).commit();
        SPname.edit().putString("myname",stname).commit();
        SPdata.edit().putString("mydata",batch).commit();
        SPemail.edit().putString("myemail",mail).commit();

        drawerLayout=findViewById(R.id.drawerlayout);
        navigationView=findViewById(R.id.navigationview);

        toggle=new ActionBarDrawerToggle(this, drawerLayout, R.string.start, R.string.end);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = (TextView) headerView.findViewById(R.id.headertv);
        navUsername.setText("Welcome "+stname);

        navigationView.setNavigationItemSelectedListener(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(toggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        String str="";
        if(menuItem.getItemId()==R.id.home){
            Intent intent=new Intent(getBaseContext(),StudentFunctions.class);
            intent.putExtra("classid",ADMINID);
            intent.putExtra("myname",stname);
            intent.putExtra("mybatch",batch);
            intent.putExtra("email",mail);
            startActivity(intent);
            drawerLayout.closeDrawers();
            finish();
        }
        else if(menuItem.getItemId()==R.id.noti){
            Intent intent=new Intent(getBaseContext(),StaffNotificationList.class);
            intent.putExtra("classid",ADMINID);
            intent.putExtra("typelist","student");
            intent.putExtra("data",batch);
            intent.putExtra("mysubject","nothing");
            startActivity(intent);
            drawerLayout.closeDrawers();
        }
        else if(menuItem.getItemId()==R.id.changepass){
            Intent intent=new Intent(getBaseContext(),ChangePassword.class);
            intent.putExtra("classid",ADMINID);
            intent.putExtra("email",mail);
            intent.putExtra("typeofchange","student");
            startActivity(intent);
            drawerLayout.closeDrawers();
        }
        else if(menuItem.getItemId()==R.id.doubts){
            Intent intent=new Intent(getBaseContext(),StudentDoubt.class);
            intent.putExtra("classid",ADMINID);
            intent.putExtra("stname",stname);
            startActivity(intent);
            drawerLayout.closeDrawers();
        }
        else if(menuItem.getItemId()==R.id.logout){
            AlertDialog.Builder builder = new AlertDialog.Builder(StudentFunctions.this);
            builder.setTitle("ALERT").setMessage("ARE YOU SURE DO YOU WANT TO LOGOUT");
            builder.setCancelable(false);
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    SPclassid.edit().putString("myclassid","none").commit();
                    SPtype.edit().putString("mytype","none").commit();
                    SPname.edit().putString("myname","none").commit();
                    SPdata.edit().putString("mydata","none").commit();
                    SPdata.edit().putString("myemail","none").commit();

                    Intent intent=new Intent(StudentFunctions.this,MainActivity.class);
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
            drawerLayout.closeDrawers();
        }
        return true;
    }

    public void mylectures(View view) {
        Intent intent=new Intent(getBaseContext(),StudentLectures.class);
        intent.putExtra("classid",ADMINID);
        intent.putExtra("mybatch",batch);
        intent.putExtra("typelecture","student");
        intent.putExtra("email",mail);
        Log.i("checkmsg",ADMINID+" : "+batch);
        startActivity(intent);
    }

    public void mytest(View view) {
        Intent intent=new Intent(getBaseContext(),StudentTest.class);
        intent.putExtra("classid",ADMINID);
        intent.putExtra("mybatch",batch);
        Log.i("checkmsg",ADMINID+" : "+batch);
        intent.putExtra("email",mail);
        startActivity(intent);
        //Toast.makeText(getBaseContext(),"test",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
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
