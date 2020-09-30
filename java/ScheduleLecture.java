package in.autodice.classmanagementsystem;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import in.autodice.classmanagementsystem.R;

import java.util.Calendar;

public class ScheduleLecture extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    Button btdate,bttime,btlecture;
    DatePickerDialog datePickerDialog;
    TimePickerDialog timePickerDialog;
    int day,month,year,hour,min;

    TextView tv1,tv2,tv3,datetv,starttime,endtime;
    String mydate="";
    String mytime="",mytime2="",ADMINID;
    Boolean b1,b2;

    Spinner sp1,sp2,sp3;
    String batchname="";
    String subjectname="";
    String teachername="";
    HttpServicesHelper helper1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_lecture);

        getSupportActionBar().setTitle("Schedule Lecture");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);   //show back button

        Bundle extras = getIntent().getExtras();
        ADMINID= extras.getString("classid");

        datetv=findViewById(R.id.btAddDate);
        starttime=findViewById(R.id.btstartTime);
        endtime=findViewById(R.id.btendTime);
        btlecture=findViewById(R.id.btaddLecture);

        tv1=findViewById(R.id.tvspbatch);
        tv2=findViewById(R.id.tvspsubject);
        tv3=findViewById(R.id.tvspteacher);

        tv1.setVisibility(View.INVISIBLE);
        tv2.setVisibility(View.INVISIBLE);
        tv3.setVisibility(View.INVISIBLE);




        Calendar c= Calendar.getInstance();

        day=c.get(Calendar.DAY_OF_MONTH);
        month=c.get(Calendar.MONTH);
        year=c.get(Calendar.YEAR);

        hour=c.get(Calendar.HOUR);
        min=c.get(Calendar.MINUTE);

        helper1=new HttpServicesHelper(this);

        sp1=findViewById(R.id.spinnerBatchlist);
        sp2=findViewById(R.id.spinnerSubjectFrombatch);
        sp3=findViewById(R.id.spinnerTeacherFromSubject);


        datePickerDialog=new DatePickerDialog(this,DatePickerDialog.THEME_DEVICE_DEFAULT_LIGHT,this,year,month,day);

        timePickerDialog=new TimePickerDialog(this,TimePickerDialog.THEME_DEVICE_DEFAULT_LIGHT,this,hour,min,false);

        helper1.execute("getbatchlist","callsubjectandteacher",ADMINID);

        datetv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show();
            }
        });

        starttime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePickerDialog.show();
                b1=true;
                b2=false;
            }
        });

        endtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePickerDialog.show();
                b1=false;
                b2=true;
            }
        });

        btlecture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                batchname= String.valueOf(tv1.getText());
                subjectname= String.valueOf(tv2.getText());
                teachername= String.valueOf(tv3.getText());

                if(mydate.equals("") && mytime.equals("") && mytime2.equals("")){
                    Toast.makeText(getBaseContext(),"Select Time and Date",Toast.LENGTH_SHORT).show();
                }
                else if(mytime.equals("")){
                    Toast.makeText(getBaseContext(),"Select Time",Toast.LENGTH_SHORT).show();
                }
                else if(mydate.equals("")){
                    Toast.makeText(getBaseContext(),"Select Date",Toast.LENGTH_SHORT).show();
                }
                else if (subjectname.equals("") || teachername.equals("") || batchname.equals("")){
                    Toast.makeText(getBaseContext(),"Select Data",Toast.LENGTH_SHORT).show();
                }
                else {
                    String timerange=mytime+" to "+mytime2;
                    HttpServicesHelper helper=new HttpServicesHelper(getBaseContext());
                    helper.execute("insertlecture",batchname,subjectname,teachername,mydate,timerange,ADMINID);
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
        datetv.setText(mydate);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        int temp=hourOfDay%12;
        String m="AM";
        if(temp==0){
            temp=12;
        }
        if(hourOfDay>12)
        {
            m="PM";
        }
        if (b1==true && b2==false) {
            mytime = String.valueOf(temp + ":" + minute + "-" + m);
            starttime.setText(mytime);
        }else if (b2==true && b1==false) {
            mytime2 = String.valueOf(temp + ":" + minute + "-" + m);
            endtime.setText(mytime2);
        }
    }


}
