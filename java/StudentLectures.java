package in.autodice.classmanagementsystem;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import in.autodice.classmanagementsystem.R;

public class StudentLectures extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    ListView mylist;
    SwipeRefreshLayout swipeRefreshLayout;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle toggle;
    String tt,ADMINID,batchname,mail;
    SharedPreferences SPclassid,SPtype,SPname,SPdata,SPemail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_lectures);

        SPclassid= PreferenceManager.getDefaultSharedPreferences(this);
        SPtype=PreferenceManager.getDefaultSharedPreferences(this);
        SPname=PreferenceManager.getDefaultSharedPreferences(this);
        SPdata=PreferenceManager.getDefaultSharedPreferences(this);
        SPemail=PreferenceManager.getDefaultSharedPreferences(this);

        getSupportActionBar().setTitle("Lecture Schedule");


        Bundle extras = getIntent().getExtras();
        ADMINID= extras.getString("classid");
        batchname= extras.getString("mybatch");
        tt= extras.getString("typelecture");
        mail= extras.getString("email");

        if (tt.equals("student")){
            drawerLayout=findViewById(R.id.lectdrawer);
            navigationView=findViewById(R.id.lectnavigationview);

            toggle=new ActionBarDrawerToggle(this, drawerLayout, R.string.start, R.string.end);
            drawerLayout.addDrawerListener(toggle);
            toggle.syncState();

            navigationView.setNavigationItemSelectedListener(this);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        mylist=findViewById(R.id.mylectlist);
        swipeRefreshLayout=findViewById(R.id.mylectswipe);


        //arrayList.add(new lecturedetails("Maths","Bernoulli","7/10/19","9.00pm to 12.00pm"));
        //arrayList.add(new lecturedetails("English","Sushil","8/10/18","7.00am to 9.00am"));
        //arrayList.add(new lecturedetails("Science","Mahesh","10/10/19","8.30pm to 10.00pm"));

        //prepareListView();

        HttpServicesHelper helper=new HttpServicesHelper(StudentLectures.this);
        helper.execute("getlecturelist",tt,ADMINID,batchname);


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new HttpServicesHelper(StudentLectures.this).execute("getlecturelist",tt,ADMINID,batchname);
                Toast.makeText(getBaseContext(),"Refreshing", Toast.LENGTH_SHORT).show();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
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
            Intent intent=new Intent(StudentLectures.this,StudentFunctions.class);
            intent.putExtra("classid",ADMINID);
            intent.putExtra("myname","");
            intent.putExtra("mybatch",batchname);
            intent.putExtra("email",mail);
            startActivity(intent);
            drawerLayout.closeDrawers();
            finish();
        }
        else if(menuItem.getItemId()==R.id.noti){
            Intent intent=new Intent(getBaseContext(),StaffNotificationList.class);
            intent.putExtra("typelist","student");
            intent.putExtra("data",batchname);
            intent.putExtra("mysubject","nothing");
            intent.putExtra("classid",ADMINID);
            startActivity(intent);
            drawerLayout.closeDrawers();
            finish();
        }
        else if(menuItem.getItemId()==R.id.changepass){
            Intent intent=new Intent(getBaseContext(),ChangePassword.class);
            intent.putExtra("classid",ADMINID);
            intent.putExtra("email",mail);
            intent.putExtra("typeofchange","student");

            startActivity(intent);
            drawerLayout.closeDrawers();
            finish();
        }
        else if(menuItem.getItemId()==R.id.doubts){
            Intent intent=new Intent(getBaseContext(),StudentDoubt.class);
            intent.putExtra("classid",ADMINID);
            startActivity(intent);
            drawerLayout.closeDrawers();
            finish();
        }
        else if(menuItem.getItemId()==R.id.logout){
            AlertDialog.Builder builder = new AlertDialog.Builder(StudentLectures.this);
            builder.setTitle("ALERT").setMessage("ARE YOU SURE DO YOU WANT TO LOGOUT");
            builder.setCancelable(false);
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    SPclassid.edit().putString("myclassid","none").commit();
                    SPtype.edit().putString("mytype","none").commit();
                    SPname.edit().putString("myname","none").commit();
                    SPdata.edit().putString("mydata","none").commit();
                    SPemail.edit().putString("mydata","none").commit();

                    Intent intent=new Intent(getBaseContext(),MainActivity.class);
                    startActivity(intent);
                    finish();
                    String str="Successfully Logout";
                    Toast.makeText(getBaseContext(),str,Toast.LENGTH_SHORT).show();
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
            drawerLayout.closeDrawers();
        }
        return true;
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

}
