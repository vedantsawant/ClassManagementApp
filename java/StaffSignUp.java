package in.autodice.classmanagementsystem;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import in.autodice.classmanagementsystem.R;

import java.util.regex.Pattern;

public class StaffSignUp extends AppCompatActivity {
    Button submit;
    TextView name1,email1,passwd1,phone1;
    String name,email,password,adminid,contact,ADMINID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_sign_up);

        getSupportActionBar().setTitle("Staff Sign Up");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);   //show back button

        Bundle extras = getIntent().getExtras();
        ADMINID= extras.getString("classid");
        Log.i("MYERROR",ADMINID);

        name1=findViewById(R.id.name);
        email1=findViewById(R.id.email);
        passwd1=findViewById(R.id.passwd);
        phone1=findViewById(R.id.contact);
        submit=findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Pattern emailpattern = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$");

                email = String.valueOf(email1.getText()).trim();
                if (name1.getText().length() == 0 || phone1.getText().length() == 0 || email1.getText().length() == 0 || passwd1.getText().length() == 0) {
                    Toast.makeText(getBaseContext(), "Enter Complete Data", Toast.LENGTH_SHORT).show();
                } else if (passwd1.getText().length() < 6) {
                    Toast.makeText(getBaseContext(), "Password Length is Too Short", Toast.LENGTH_SHORT).show();
                } else if (!emailpattern.matcher(email).matches()) {
                    Toast.makeText(getBaseContext(), "Enter Vaild Email ID", Toast.LENGTH_SHORT).show();
                } else if (phone1.getText().length() < 10) {
                    Toast.makeText(getBaseContext(), "Enter Vaild Contact no.", Toast.LENGTH_SHORT).show();
                } else {


                    name = String.valueOf(name1.getText()).trim();

                    email = String.valueOf(email1.getText()).trim();
                    contact = String.valueOf(phone1.getText()).trim();
                    password = String.valueOf(passwd1.getText()).trim();

                    name1.setText("");
                    email1.setText("");
                    phone1.setText("");
                    passwd1.setText("");
                    new HttpServicesHelper(StaffSignUp.this).execute("insertstaff", name, contact, email, password, ADMINID);

                    email1.setText("");
                    phone1.setText("");
                    passwd1.setText("");
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
