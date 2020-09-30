package in.autodice.classmanagementsystem;

import in.autodice.classmanagementsystem.R;
import com.github.clans.fab.FloatingActionMenu;

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
import android.widget.Toast;

import java.net.InetAddress;

public class StaffFunctions extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    FloatingActionMenu fabMenu;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle toggle;
    NavigationView navigationView;
    String ADMINID,mail;
    SharedPreferences SPclassid,SPtype,SPname,SPdata,SPemail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_functions);

        SPclassid= PreferenceManager.getDefaultSharedPreferences(this);
        SPtype=PreferenceManager.getDefaultSharedPreferences(this);
        SPname=PreferenceManager.getDefaultSharedPreferences(this);
        SPdata=PreferenceManager.getDefaultSharedPreferences(this);
        SPemail=PreferenceManager.getDefaultSharedPreferences(this);

        fabMenu=findViewById(R.id.fab_menu);

        drawerLayout=findViewById(R.id.drawer);
        navigationView=findViewById(R.id.navigation);
        toggle=new ActionBarDrawerToggle(this,drawerLayout,R.string.start,R.string.end);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        if (!(isInternetAvailable() || isNetworkConnected())){
            AlertDialog.Builder builder=new AlertDialog.Builder(StaffFunctions.this);
            builder.setTitle("Alert").setMessage("No Internet Connection");
            AlertDialog alert=builder.create();
            alert.show();
        }

        Bundle extras = getIntent().getExtras();
        ADMINID= extras.getString("classid");
        mail= extras.getString("email");

        SPtype.edit().putString("mytype","staff").commit();
        SPclassid.edit().putString("myclassid",ADMINID).commit();
        SPname.edit().putString("myname","none").commit();
        SPdata.edit().putString("mydata","none").commit();
        SPemail.edit().putString("myemail",mail).commit();

        String AD=SPdata.getString("myclassid","none");
        Log.i("MYERROR_stSP",AD);

        //imp
        navigationView.setNavigationItemSelectedListener(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(toggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showMessage(String st) {
        Toast.makeText(this,st,Toast.LENGTH_SHORT).show();
    }

    public void mymethod1(View view) {
        fabMenu.close(true);
        Intent intent=new Intent(StaffFunctions.this,StudentSignUp.class);
        intent.putExtra("classid",ADMINID);
        startActivity(intent);
    }

    public void mymethod2(View view) {
        fabMenu.close(true);
        Intent intent=new Intent(StaffFunctions.this,TeacherSignUp.class);
        intent.putExtra("classid",ADMINID);
        startActivity(intent);
    }

    public void mymethod3(View view) {
        fabMenu.close(true);
        Intent intent=new Intent(StaffFunctions.this,AddBatches.class);
        intent.putExtra("classid",ADMINID);
        startActivity(intent);
    }


    public void card1Function(View view) {
        Intent intent=new Intent(StaffFunctions.this,ScheduleLecture.class);
        intent.putExtra("classid",ADMINID);
        startActivity(intent);
    }

    public void card2Function(View view) {
        Intent intent=new Intent(StaffFunctions.this,MarkAttendance.class);
        intent.putExtra("classid",ADMINID);
        startActivity(intent);
    }

    public void card3Function(View view) {
        Intent intent=new Intent(StaffFunctions.this,CreateNotification.class);
        intent.putExtra("classid",ADMINID);
        startActivity(intent);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        if (menuItem.getItemId()==R.id.opt_student){
            Intent intent=new Intent(getBaseContext(),StudentLectures.class);
            intent.putExtra("classid",ADMINID);
            intent.putExtra("mybatch","nothing");
            intent.putExtra("typelecture","staff");
            intent.putExtra("email","none");
            drawerLayout.closeDrawers();
            startActivity(intent);
        }
        else if (menuItem.getItemId()==R.id.opt_teacher){
            Intent intent=new Intent(getBaseContext(),StudentTest.class);
            intent.putExtra("classid",ADMINID);
            intent.putExtra("mybatch","nothing");
            intent.putExtra("email","none");
            drawerLayout.closeDrawers();
            startActivity(intent);
        }
        else if (menuItem.getItemId()==R.id.opt_notification){
            Intent intent=new Intent(StaffFunctions.this,StaffNotificationList.class);
            intent.putExtra("classid",ADMINID);
            intent.putExtra("typelist","staff");
            intent.putExtra("data","nothing");
            intent.putExtra("mysubject","nothing");
            drawerLayout.closeDrawers();
            startActivity(intent);
        }
        else if(menuItem.getItemId()==R.id.opt_past){
            Intent intent=new Intent(StaffFunctions.this,PastStudent.class);
            intent.putExtra("classid",ADMINID);
            drawerLayout.closeDrawers();
            startActivity(intent);
        }
        else if (menuItem.getItemId()==R.id.opt_batch){
            Intent intent=new Intent(StaffFunctions.this,BatchList.class);
            intent.putExtra("classid",ADMINID);
            intent.putExtra("type","batch");
            drawerLayout.closeDrawers();
            startActivity(intent);
        }
        else if (menuItem.getItemId()==R.id.opt_logout){
            AlertDialog.Builder builder = new AlertDialog.Builder(StaffFunctions.this);
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

                    Intent intent=new Intent(StaffFunctions.this,MainActivity.class);
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
        else if(menuItem.getItemId()==R.id.opt_call){
            Intent intent=new Intent(StaffFunctions.this,StudentCallList.class);
            intent.putExtra("classid",ADMINID);
            drawerLayout.closeDrawers();
            startActivity(intent);
        }
        else if(menuItem.getItemId()==R.id.opt_getattendance){
            Intent intent=new Intent(StaffFunctions.this,StudentAttendance.class);
            intent.putExtra("classid",ADMINID);
            drawerLayout.closeDrawers();
            startActivity(intent);
        }
        else if(menuItem.getItemId()==R.id.opt_getbatchattendance){
            Intent intent=new Intent(StaffFunctions.this,GetBatchAttendance.class);
            intent.putExtra("classid",ADMINID);
            drawerLayout.closeDrawers();
            startActivity(intent);
        }else if(menuItem.getItemId()==R.id.opt_markinactive){
            Intent intent=new Intent(StaffFunctions.this,MarkInactive.class);
            intent.putExtra("classid",ADMINID);
            drawerLayout.closeDrawers();
            startActivity(intent);
        }else if(menuItem.getItemId()==R.id.opt_promote){
            Intent intent=new Intent(StaffFunctions.this,PromoteStudent.class);
            intent.putExtra("classid",ADMINID);
            drawerLayout.closeDrawers();
            startActivity(intent);
        }else if(menuItem.getItemId()==R.id.opt_changepassword){
            Intent intent=new Intent(StaffFunctions.this,ChangePassword.class);
            intent.putExtra("classid",ADMINID);
            intent.putExtra("email",mail);
            intent.putExtra("typeofchange","staff");
            drawerLayout.closeDrawers();
            startActivity(intent);
        }

        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        fabMenu.close(true);
    }

    public void card4function(View view) {
        Intent intent=new Intent(StaffFunctions.this,ScheduleTest.class);
        intent.putExtra("classid",ADMINID);
        startActivity(intent);
    }

    public void card5Function(View view) {
        Intent intent=new Intent(StaffFunctions.this,GetTeacherData.class);
        intent.putExtra("classid",ADMINID);
        drawerLayout.closeDrawers();
        startActivity(intent);
    }

    public void card6function(View view) {
        Intent intent=new Intent(StaffFunctions.this,GetStudentData.class);
        intent.putExtra("classid",ADMINID);
        drawerLayout.closeDrawers();
        startActivity(intent);
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
