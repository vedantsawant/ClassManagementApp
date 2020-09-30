package in.autodice.classmanagementsystem;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import in.autodice.classmanagementsystem.R;

import java.util.ArrayList;

public class GetStudentData extends AppCompatActivity {

    Spinner sp1, sp2;
    Button btgetdata,btedit;
    String batchname="", studentname="";
    ArrayList<String> batchlist, studentlist;

    TextView name1,email1,dob1,contact1,pContact1,address1,status1,id,tv1,tv2;
    HttpServicesHelper helper;
    String name,email,dob,addr,pc,c,type,baseURL,fetchedid,ADMINID;
    HttpServicesHelper helper1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_student_data);

        getSupportActionBar().setTitle("Student Data");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);   //show back button

        sp1 = findViewById(R.id.spinnerBatchlist);
        sp2 = findViewById(R.id.spinnerStudentFromBatch);
        btgetdata=findViewById(R.id.btdata);
        btedit=findViewById(R.id.btedit);
        tv1=findViewById(R.id.tvspbatch);
        tv2=findViewById(R.id.tvspstudent);

        tv1.setVisibility(View.INVISIBLE);
        tv2.setVisibility(View.INVISIBLE);

        Bundle extras = getIntent().getExtras();
        ADMINID= extras.getString("classid");

        Log.i("displayid","ID:"+ADMINID);

        name1=findViewById(R.id.name);
        email1=findViewById(R.id.email);
        dob1=findViewById(R.id.dob);
        contact1=findViewById(R.id.contact);
        pContact1=findViewById(R.id.pContact);
        address1=findViewById(R.id.address);
        status1=findViewById(R.id.status);
        id=findViewById(R.id.idvalue);

        id.setVisibility(View.INVISIBLE);

        helper=new HttpServicesHelper(this);
        btedit.setEnabled(false);

        helper.execute("getbatchlist","callsinglestudent",ADMINID);


        btgetdata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                helper1=new HttpServicesHelper(v.getContext());
                batchname= String.valueOf(tv1.getText());
                studentname= String.valueOf(tv2.getText());

                if (studentname.equals("")){
                    Toast.makeText(getBaseContext(), "Select Student Name", Toast.LENGTH_SHORT).show();
                }
                else if (batchname.equals("")){
                    Toast.makeText(getBaseContext(), "Select batch name", Toast.LENGTH_SHORT).show();
                }
                else {

                    helper1.execute("getStudentFromNameBatch",batchname,studentname,ADMINID);
                    btedit.setEnabled(true);
                }
            }



        });

        btedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchedid= (String) id.getText();
                Intent intent=new Intent(GetStudentData.this,EditStudentData.class);
                intent.putExtra("studentid",fetchedid);
                intent.putExtra("classid",ADMINID);
                startActivity(intent);
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
