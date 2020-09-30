package in.autodice.classmanagementsystem;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import in.autodice.classmanagementsystem.R;

public class CommonLogin extends AppCompatActivity {

    EditText email1,password1,classid1;
    String email,password,classid;
    Button submit;
    String logindir;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_login);

        submit=findViewById(R.id.submit);
        email1=findViewById(R.id.email);
        password1=findViewById(R.id.password);
        classid1=findViewById(R.id.etclassid);

        Bundle extras = getIntent().getExtras();
        logindir= extras.getString("logintype");


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email= String.valueOf(email1.getText());
                password= String.valueOf(password1.getText());
                classid= String.valueOf(classid1.getText());
                if (email.length()==0 || password.length()==0){
                    Toast.makeText(getBaseContext(),"Enter Empty Fields",Toast.LENGTH_SHORT).show();
                }
                else {
                    if (logindir.equals("staff")){
                        new HttpServicesHelper(CommonLogin.this).execute("commonlogin","stafflogin",email,classid,password);
                    }else if (logindir.equals("student")){
                        new HttpServicesHelper(CommonLogin.this).execute("commonlogin","studentlogin",email,classid,password);
                    }else if (logindir.equals("teacher")){
                        new HttpServicesHelper(CommonLogin.this).execute("commonlogin","teacherlogin",email,classid,password);
                    }
                }
            }
        });

    }
}
