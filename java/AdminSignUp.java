package in.autodice.classmanagementsystem;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import in.autodice.classmanagementsystem.R;

import java.util.regex.Pattern;

public class AdminSignUp extends AppCompatActivity {

    String type = "insertadmin";
    EditText fname1, contact1, email1, coachingname1, coachinglocation1, passwd1;
    Button submit;
    String lname, fname, contact, email, coachingname, coachinglocation, password;


    //AdminAddHttp adminAddHttp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_sign_up);

        getSupportActionBar().setTitle("SIGN UP");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);   //show back button

        fname1 = findViewById(R.id.fname);

        contact1 = findViewById(R.id.contact);
        email1 = findViewById(R.id.email);
        coachingname1 = findViewById(R.id.coachingname);
        coachinglocation1 = findViewById(R.id.coachinglocation);
        passwd1 = findViewById(R.id.passwd1);


        submit = findViewById(R.id.submit);


        submit.setOnClickListener(new View.OnClickListener() {
                                      @Override
                                      public void onClick(View v) {


                                          Pattern emailpattern = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$");

                                          email = String.valueOf(email1.getText()).trim();
                                          if (fname1.getText().length() == 0 || contact1.getText().length() == 0 || email1.getText().length() == 0 || passwd1.getText().length() == 0 || coachinglocation1.getText().length() == 0 || coachingname1.getText().length() == 0) {
                                              Toast.makeText(getBaseContext(), "Enter Complete Data", Toast.LENGTH_SHORT).show();
                                          } else if (passwd1.getText().length() < 6) {
                                              Toast.makeText(getBaseContext(), "Password Length is Too Short", Toast.LENGTH_SHORT).show();
                                          } else if (!emailpattern.matcher(email).matches()) {
                                              Toast.makeText(getBaseContext(), "Enter Vaild Email ID", Toast.LENGTH_SHORT).show();
                                          } else if (contact1.getText().length() < 10) {
                                              Toast.makeText(getBaseContext(), "Enter Vaild Contact no.", Toast.LENGTH_SHORT).show();
                                          } else {
                                              fname = String.valueOf(fname1.getText());

                                              email = String.valueOf(email1.getText()).trim();
                                              contact = String.valueOf(contact1.getText()).trim();
                                              coachingname = String.valueOf(coachingname1.getText()).trim();
                                              coachinglocation = String.valueOf(coachinglocation1.getText()).trim();
                                              password = String.valueOf(passwd1.getText()).trim();

                                              fname1.setText("");
                                              email1.setText("");
                                              contact1.setText("");
                                              coachingname1.setText("");
                                              coachinglocation1.setText("");
                                              passwd1.setText("");


                                              new HttpServicesHelper(AdminSignUp.this).execute("insertadmin", fname, contact, email, coachingname, coachinglocation, password);
                                              Intent intent = new Intent(AdminSignUp.this,NewCommonLogin.class);
                                              startActivity(intent);
                                              finish();
                                              //Intent intent = new Intent(AdminSignUp.this, AdminLogin.class);
                                              //startActivity(intent);

                                          }

                                      }
                                  }
        );
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(AdminSignUp.this,MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        Intent intent = new Intent(AdminSignUp.this,MainActivity.class);
        startActivity(intent);
        finish();
        return true;
    }
}
