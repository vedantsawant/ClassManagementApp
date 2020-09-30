package in.autodice.classmanagementsystem;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import in.autodice.classmanagementsystem.R;

public class AdminLogin extends AppCompatActivity {

    EditText email1,password1;
    String email,password;
    Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        getSupportActionBar().setTitle("Login");

        submit=findViewById(R.id.submit);
        email1=findViewById(R.id.email);
        password1=findViewById(R.id.password);



        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email= String.valueOf(email1.getText());
                password= String.valueOf(password1.getText());
                if (email.length()==0 || password.length()==0){
                    Toast.makeText(getBaseContext(),"Enter Empty Fields",Toast.LENGTH_SHORT).show();
                }
                else {
                    new HttpServicesHelper(AdminLogin.this).execute("adminlogin",email,password);
                }
            }
        });

    }

}
