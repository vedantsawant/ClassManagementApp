package in.autodice.classmanagementsystem;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import in.autodice.classmanagementsystem.R;

import java.util.ArrayList;

public class AddBatches extends AppCompatActivity {
    Button btaddBatch,btaddsubject,btsubtobatch;
    EditText etbatch,etsubject;
    String batchname,subjectname,selectedBatch="",selectedSubject="",type,baseURL,ADMINID;
    Spinner sp1,sp2;
    ArrayList<String> batchlist,subjectlist;
    HttpServicesHelper helper1,helper2;
    TextView textView1,textView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_batches);

        getSupportActionBar().setTitle("ADD");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);   //show back button


        Bundle extras = getIntent().getExtras();
        ADMINID= extras.getString("classid");

        btaddBatch=findViewById(R.id.btbatch);
        etbatch=findViewById(R.id.etbatchname);
        btaddsubject=findViewById(R.id.btsubject);
        btsubtobatch=findViewById(R.id.btsubtobatch);
        etsubject=findViewById(R.id.etsubjectname);
        sp1=findViewById(R.id.spinnerBatchlist);
        sp2=findViewById(R.id.spinnerSubjectlist);
        textView1=findViewById(R.id.tvspbatch);
        textView2=findViewById(R.id.tvspsubject);

        textView1.setVisibility(View.INVISIBLE);
        textView2.setVisibility(View.INVISIBLE);

        helper1=new HttpServicesHelper(this);
        helper1.execute("getbatchlist","nothing",ADMINID);

        helper2=new HttpServicesHelper(this);
        helper2.execute("getsubjectlist",ADMINID);

        btsubtobatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedBatch= (String) textView1.getText();
                selectedSubject= (String) textView2.getText();
                if(selectedBatch.equals("") || selectedSubject.equals("")){
                    Toast.makeText(getBaseContext(),"Select Data",Toast.LENGTH_SHORT).show();
                }
                else {
                    HttpServicesHelper helper=new HttpServicesHelper(getBaseContext());
                    helper.execute("addsubtobatch",selectedBatch,selectedSubject,ADMINID);
                }
            }
        });

    }


    public void AddSubject(View view) {
        if(etsubject.getText().length()==0){
            etsubject.setError("Enter Batch Name");
        }
        else {
            subjectname = String.valueOf(etsubject.getText());
            HttpServicesHelper helper=new HttpServicesHelper(getBaseContext());
            helper.execute("insertsubject",subjectname,ADMINID);
            etsubject.setText("");
            new HttpServicesHelper(AddBatches.this).execute("getsubjectlist",ADMINID);
        }
    }

    public void AddBatch(View view) {
        if(etbatch.getText().length()==0){
            etbatch.setError("Enter Batch Name");
        }
        else {
            batchname = String.valueOf(etbatch.getText());
            HttpServicesHelper helper=new HttpServicesHelper(getBaseContext());
            helper.execute("insertbatch",batchname,"ACTIVE",ADMINID);
            etbatch.setText("");
            new HttpServicesHelper(AddBatches.this).execute("getbatchlist","nothing",ADMINID);
        }
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
