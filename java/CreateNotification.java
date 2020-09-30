package in.autodice.classmanagementsystem;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import in.autodice.classmanagementsystem.R;

import java.util.ArrayList;
import java.util.Calendar;

public class CreateNotification extends AppCompatActivity {

    RadioGroup radioGroup;
    RadioButton radioButton;
    Button btnotify;
    EditText ettitle,etmsg;
    ArrayList<String> batchlist;
    int day,month,year;
    ArrayAdapter adapter;
    Spinner sp1;
    String batchname="",typeofnotification="",title,message,date,baseURL,type,ADMINID,subjectname;
    HttpServicesHelper helper;
    TextView textView,textView1,select;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_notification);

        getSupportActionBar().setTitle("Send Notification");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);   //show back button

        Bundle extras = getIntent().getExtras();
        ADMINID= extras.getString("classid");

        radioGroup=findViewById(R.id.radiogroup);
        btnotify=findViewById(R.id.btnotify);
        ettitle=findViewById(R.id.ettile);
        etmsg=findViewById(R.id.etmsg);
        sp1=findViewById(R.id.spinnerBatchlist);
        select=findViewById(R.id.tv1);
        textView=findViewById(R.id.tvspbatch);
        textView1=findViewById(R.id.tvspsubject);
        textView1.setVisibility(View.INVISIBLE);
        textView.setVisibility(View.INVISIBLE);

        helper=new HttpServicesHelper(this);

        helper.execute("getbatchlist","nothing",ADMINID);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                radioButton=findViewById(checkedId);
                String type= String.valueOf(radioButton.getText());
                if (type.equals("Student")){
                    select.setText("Select Batch");
                    HttpServicesHelper helper1= new HttpServicesHelper(CreateNotification.this);
                    helper1.execute("getbatchlist","nothing",ADMINID);
                }else if (type.equals("Teacher")){
                    select.setText("Select Teacher");
                    HttpServicesHelper helper2= new HttpServicesHelper(CreateNotification.this);
                    helper2.execute("getallteacherlist",ADMINID);
                }
            }
        });


        btnotify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                batchname= String.valueOf(textView.getText());
                subjectname=String.valueOf(textView1.getText());
                if(ettitle.getText().length()==0 && etmsg.getText().length()==0){
                    etmsg.setError("Enter Message");
                    ettitle.setError("Enter Title");
                }
                else if (ettitle.getText().length()==0){
                    ettitle.setError("Enter Title");
                }
                else if (etmsg.getText().length()==0){
                    etmsg.setError("Enter Message");
                }
                else if(batchname.equals("")){
                    Toast.makeText(getBaseContext(),"Add data in spinner",Toast.LENGTH_SHORT).show();
                }
                else {
                    int id=radioGroup.getCheckedRadioButtonId();
                    radioButton=findViewById(id);
                    title= String.valueOf(ettitle.getText());
                    message= String.valueOf(etmsg.getText());
                    typeofnotification= String.valueOf(radioButton.getText());

                    Calendar c= Calendar.getInstance();

                    day=c.get(Calendar.DAY_OF_MONTH);
                    month=c.get(Calendar.MONTH);
                    year=c.get(Calendar.YEAR);

                    date=String.valueOf(day)+"/"+String.valueOf(month+1)+"/"+String.valueOf(year);
                    HttpServicesHelper helperadd=new HttpServicesHelper(getBaseContext());
                    helperadd.execute("insertnotification",typeofnotification,batchname,title,message,ADMINID,date,subjectname);

                    HttpServicesHelper helpersend=new HttpServicesHelper(getBaseContext());
                    helpersend.execute("sendnotification",typeofnotification,batchname,title,message,ADMINID,date,subjectname);

                    ettitle.setText("");
                    etmsg.setText("");
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
}
