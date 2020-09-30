package in.autodice.classmanagementsystem;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import in.autodice.classmanagementsystem.R;

import java.net.InetAddress;

public class NewCommonLogin extends AppCompatActivity {

    EditText email1,password1;
    String email,password;
    Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_common_login);

        submit=findViewById(R.id.submit);
        email1=findViewById(R.id.email);
        password1=findViewById(R.id.password);

        getSupportActionBar().hide();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email= String.valueOf(email1.getText());
                password= String.valueOf(password1.getText());
                if (email.length()==0 || password.length()==0){
                    Toast.makeText(getBaseContext(),"Enter Empty Fields",Toast.LENGTH_SHORT).show();
                }
                else {
                    if (isNetworkConnected() || isInternetAvailable()) {
                        new HttpServicesHelper(NewCommonLogin.this).execute("adminlogin", email, password);
                    }else {
                        Toast.makeText(getBaseContext(),"No Internet Connection",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();
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
