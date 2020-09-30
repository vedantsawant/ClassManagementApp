package in.autodice.classmanagementsystem;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import in.autodice.classmanagementsystem.R;

import java.util.regex.Pattern;

public class ChangePassword extends AppCompatActivity {

    EditText email,npass,cpass;
    Button submit;
    String ADMINID,typeofchange,mail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        Bundle extras = getIntent().getExtras();
        ADMINID= extras.getString("classid");
        typeofchange= extras.getString("typeofchange");
        mail= extras.getString("email");

        Log.i("NewError",mail);
        getSupportActionBar().setTitle("Change Password");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);   //show back button


        npass=findViewById(R.id.npass);
        submit=findViewById(R.id.submit);
        cpass=findViewById(R.id.cpass);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String n=String.valueOf(npass.getText());
                String c=String.valueOf(cpass.getText());


                Pattern emailpattern= Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$");

                if(n.length()==0||c.length()==0){
                    Toast.makeText(getBaseContext(),"Fill in all the fields",Toast.LENGTH_SHORT).show();
                }
                else if(n.trim().equals("")){
                    npass.setError("Enter new password");
                }
                else if(c.trim().equals("")){
                    cpass.setError("Enter confirm password");
                }
                else if(n.length()<6|| c.length()<6){
                    Toast.makeText(getBaseContext(),"Password Length cannot be less than 6",Toast.LENGTH_SHORT).show();
                }

                else{

                    if(n.equals(c)){
                        new HttpServicesHelper(ChangePassword.this).execute("changepassword",typeofchange,mail,n,ADMINID);
                        finish();
                    }

                    else if(!n.equals(c)){
                        Toast.makeText(getBaseContext(),"New password and Confirm password not same", Toast.LENGTH_SHORT).show();
                        finish();
                    } }
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
