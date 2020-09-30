package in.autodice.classmanagementsystem;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import in.autodice.classmanagementsystem.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;

public class HttpServicesHelper extends AsyncTask<String,String,String> {

    Context context;
    String type,baseURL="https://autodice.in/projects/classmanagement/",adminidforbatch,adminidforsubject,adminidforcommonlogin,mainemail,typelogin;

    String dirbatch,dirsubject,gettype,typetest,admindataforlecture,admindatafortest,validate;
    HttpServicesHelper myhelper;
    URL url;
    String passkey="ydcYkdJhhArwijdoPslK";
    HttpURLConnection connection;
    OutputStream outputStream;
    OutputStreamWriter outputStreamWriter;
    BufferedWriter bufferedWriter;
    ArrayList<String> lectureidlist,testidlist;
    ProgressDialog progressDialog;
    InputStream inputStream;
    InputStreamReader inputStreamReader;
    BufferedReader bufferedReader;

    public HttpServicesHelper(Context context){
        this.context=context;

    }

    @Override
    protected String doInBackground(String... strings) {

        type=strings[0];

        if (type.equals("insertstudent")) {
            String namedata = strings[1];
            String addressdata = strings[2];
            String mobiledata = strings[3];
            String pmobiledata = strings[4];
            String emaildata = strings[5];
            String batchdata = strings[6];
            String dobdata = strings[7];
            String passworddata = strings[8];
            String statusdata = strings[9];
            String adminid=strings[10];
            String std=strings[11];


            try {
                url = new URL(baseURL + "insertstudent.php");
                connection = (HttpURLConnection) url.openConnection();
                connection.setDoOutput(true);
                connection.setDoInput(true);
                connection.setRequestMethod("POST");

                outputStream = connection.getOutputStream();
                outputStreamWriter = new OutputStreamWriter(outputStream);
                bufferedWriter = new BufferedWriter(outputStreamWriter);

                String postdata = URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(namedata, "UTF-8");
                postdata += "&" + URLEncoder.encode("mobile", "UTF-8") + "=" + URLEncoder.encode(mobiledata, "UTF-8");
                postdata += "&" + URLEncoder.encode("address", "UTF-8") + "=" + URLEncoder.encode(addressdata, "UTF-8");
                postdata += "&" + URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(emaildata, "UTF-8");
                postdata += "&" + URLEncoder.encode("pmobile", "UTF-8") + "=" + URLEncoder.encode(pmobiledata, "UTF-8");
                postdata += "&" + URLEncoder.encode("batch", "UTF-8") + "=" + URLEncoder.encode(batchdata, "UTF-8");
                postdata += "&" + URLEncoder.encode("dob", "UTF-8") + "=" + URLEncoder.encode(dobdata, "UTF-8");
                postdata += "&" + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(passworddata, "UTF-8");
                postdata += "&" + URLEncoder.encode("status", "UTF-8") + "=" + URLEncoder.encode(statusdata, "UTF-8");
                postdata += "&" + URLEncoder.encode("adminid", "UTF-8") + "=" + URLEncoder.encode(adminid, "UTF-8");
                postdata += "&" + URLEncoder.encode("standard", "UTF-8") + "=" + URLEncoder.encode(std, "UTF-8");
                postdata += "&" + URLEncoder.encode("passkey", "UTF-8") + "=" + URLEncoder.encode(passkey, "UTF-8");

                bufferedWriter.write(postdata);
                bufferedWriter.flush();

                bufferedWriter.close();
                outputStreamWriter.close();
                outputStream.close();

                inputStream = connection.getInputStream();
                inputStreamReader = new InputStreamReader(inputStream);
                bufferedReader = new BufferedReader(inputStreamReader);
                String line = "";
                String result = "";

                while ((line = bufferedReader.readLine()) != null) {
                    result += line;
                }
                bufferedReader.close();
                inputStreamReader.close();
                inputStream.close();
                connection.disconnect();

                return result;


            } catch (MalformedURLException e) {
                Log.i("msg", "Catch1");
                e.printStackTrace();
            } catch (IOException e) {
                Log.i("msg", e.getMessage());
                e.printStackTrace();
            }
        } else if (type.equals("getbatchlist")) {
            try {
                adminidforbatch=strings[2];
                dirbatch=strings[1];

                Log.i("catchmsg","msg :"+baseURL);
                URL url = new URL(baseURL +"fetchbatchlist.php");
                connection = (HttpURLConnection) url.openConnection();
                connection.setDoOutput(true);
                connection.setDoInput(true);
                connection.setRequestMethod("POST");

                outputStream = connection.getOutputStream();
                outputStreamWriter = new OutputStreamWriter(outputStream);
                bufferedWriter = new BufferedWriter(outputStreamWriter);

                String postdata = URLEncoder.encode("adminid", "UTF-8") + "=" + URLEncoder.encode(adminidforbatch, "UTF-8");
                postdata += "&" + URLEncoder.encode("passkey", "UTF-8") + "=" + URLEncoder.encode(passkey, "UTF-8");

                bufferedWriter.write(postdata);
                bufferedWriter.flush();

                bufferedWriter.close();
                outputStreamWriter.close();
                outputStream.close();

                inputStream = connection.getInputStream();
                inputStreamReader = new InputStreamReader(inputStream);
                bufferedReader = new BufferedReader(inputStreamReader);

                String line = "";
                String result = "";

                while ((line = bufferedReader.readLine()) != null) {
                    Log.i("Msg", "In while");
                    result += line;
                }
                bufferedReader.close();
                inputStream.close();
                inputStreamReader.close();
                connection.disconnect();

                Log.i("checkmsg", "after getting jason batchlist: "+result);

                return result;
            } catch (MalformedURLException e) {
                Log.i("catchmsg","catch1 :"+e);
                e.printStackTrace();
            } catch (IOException e) {
                Log.i("catchmsg","catch2 :"+e);
                e.printStackTrace();
            }
        }else if (type.equals("insertteacher")) {
            String namedata = strings[1];
            String mobiledata = strings[2];
            String emaildata = strings[3];
            String subjectdata=strings[4];
            String passworddata = strings[5];
            String adminid=strings[6];

            try {
                url = new URL(baseURL + "insertteacherdata.php");
                connection = (HttpURLConnection) url.openConnection();
                connection.setDoOutput(true);
                connection.setDoInput(true);
                connection.setRequestMethod("POST");

                Log.i("checkmsg", "Inside else if");

                outputStream = connection.getOutputStream();
                outputStreamWriter = new OutputStreamWriter(outputStream);
                bufferedWriter = new BufferedWriter(outputStreamWriter);

                String postdata = URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(namedata, "UTF-8");
                postdata += "&" + URLEncoder.encode("mobile", "UTF-8") + "=" + URLEncoder.encode(mobiledata, "UTF-8");
                postdata += "&" + URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(emaildata, "UTF-8");
                postdata += "&" + URLEncoder.encode("subject", "UTF-8") + "=" + URLEncoder.encode(subjectdata, "UTF-8");
                postdata += "&" + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(passworddata, "UTF-8");
                postdata += "&" + URLEncoder.encode("adminid", "UTF-8") + "=" + URLEncoder.encode(adminid, "UTF-8");
                postdata += "&" + URLEncoder.encode("passkey", "UTF-8") + "=" + URLEncoder.encode(passkey, "UTF-8");

                Log.i("checkmsg", postdata);
                bufferedWriter.write(postdata);
                bufferedWriter.flush();

                bufferedWriter.close();
                outputStreamWriter.close();
                outputStream.close();

                inputStream = connection.getInputStream();
                inputStreamReader = new InputStreamReader(inputStream);
                bufferedReader = new BufferedReader(inputStreamReader);
                String line = "";
                String result = "";

                while ((line = bufferedReader.readLine()) != null) {
                    result += line;
                    Log.i("checkmsg", "Inside while");
                }

                bufferedReader.close();
                inputStreamReader.close();
                inputStream.close();
                connection.disconnect();

                Log.i("checkmsg", "after getting jason batchlist: "+result);

                return result;


            } catch (MalformedURLException e) {
                Log.i("checkmsg", "Catch1");
                e.printStackTrace();
            } catch (IOException e) {
                Log.i("checkmsg", e.getMessage());
                e.printStackTrace();
            }
        } else if (type.equals("getsubjectlist")) {
            try {

                String adminid=strings[1];
                Log.i("catchmsg","msg :"+baseURL);
                URL url = new URL(baseURL +"fetchsubjectlist.php");

                connection = (HttpURLConnection) url.openConnection();
                connection.setDoOutput(true);
                connection.setDoInput(true);
                connection.setRequestMethod("POST");

                outputStream = connection.getOutputStream();
                outputStreamWriter = new OutputStreamWriter(outputStream);
                bufferedWriter = new BufferedWriter(outputStreamWriter);

                String postdata = URLEncoder.encode("adminid", "UTF-8") + "=" + URLEncoder.encode(adminid, "UTF-8");
                postdata += "&" + URLEncoder.encode("passkey", "UTF-8") + "=" + URLEncoder.encode(passkey, "UTF-8");

                bufferedWriter.write(postdata);
                bufferedWriter.flush();

                bufferedWriter.close();
                outputStreamWriter.close();
                outputStream.close();

                InputStream inputStream = connection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);


                String line = "";
                String result = "";

                while ((line = bufferedReader.readLine()) != null) {
                    Log.i("Msg", "In while");
                    result += line;
                }
                bufferedReader.close();
                inputStream.close();
                inputStreamReader.close();
                connection.disconnect();

                Log.i("checkmsg", "after getting jason batchlist: "+result);

                return result;
            } catch (MalformedURLException e) {
                Log.i("catchmsg","catch1 :"+e);
                e.printStackTrace();
            } catch (IOException e) {
                Log.i("catchmsg","catch2 :"+e);
                e.printStackTrace();
            }
        }else if (type.equals("getallteacherlist")) {
            try {

                String adminid=strings[1];
                Log.i("catchmsg","msg :"+baseURL);
                URL url = new URL(baseURL +"fetchallteacherlist.php");

                connection = (HttpURLConnection) url.openConnection();
                connection.setDoOutput(true);
                connection.setDoInput(true);
                connection.setRequestMethod("POST");

                outputStream = connection.getOutputStream();
                outputStreamWriter = new OutputStreamWriter(outputStream);
                bufferedWriter = new BufferedWriter(outputStreamWriter);

                String postdata = URLEncoder.encode("adminid", "UTF-8") + "=" + URLEncoder.encode(adminid, "UTF-8");
                postdata += "&" + URLEncoder.encode("passkey", "UTF-8") + "=" + URLEncoder.encode(passkey, "UTF-8");

                bufferedWriter.write(postdata);
                bufferedWriter.flush();

                bufferedWriter.close();
                outputStreamWriter.close();
                outputStream.close();

                InputStream inputStream = connection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);


                String line = "";
                String result = "";

                while ((line = bufferedReader.readLine()) != null) {
                    Log.i("Msg", "In while");
                    result += line;
                }
                bufferedReader.close();
                inputStream.close();
                inputStreamReader.close();
                connection.disconnect();

                Log.i("checkmsg", "after getting jason batchlist: "+result);

                return result;
            } catch (MalformedURLException e) {
                Log.i("catchmsg","catch1 :"+e);
                e.printStackTrace();
            } catch (IOException e) {
                Log.i("catchmsg","catch2 :"+e);
                e.printStackTrace();
            }
        }
        else if (type.equals("insertsubject")) {
            String subjectnamedata = strings[1];
            String ADMINno = strings[2];

            try {
                url = new URL(baseURL + "insertsubject.php");
                connection = (HttpURLConnection) url.openConnection();
                connection.setDoOutput(true);
                connection.setDoInput(true);
                connection.setRequestMethod("POST");

                outputStream = connection.getOutputStream();
                outputStreamWriter = new OutputStreamWriter(outputStream);
                bufferedWriter = new BufferedWriter(outputStreamWriter);

                String postdata = URLEncoder.encode("subject", "UTF-8") + "=" + URLEncoder.encode(subjectnamedata, "UTF-8");
                postdata += "&" + URLEncoder.encode("adminid", "UTF-8") + "=" + URLEncoder.encode(ADMINno, "UTF-8");
                postdata += "&" + URLEncoder.encode("passkey", "UTF-8") + "=" + URLEncoder.encode(passkey, "UTF-8");

                bufferedWriter.write(postdata);
                bufferedWriter.flush();

                bufferedWriter.close();
                outputStreamWriter.close();
                outputStream.close();

                inputStream = connection.getInputStream();
                inputStreamReader = new InputStreamReader(inputStream);
                bufferedReader = new BufferedReader(inputStreamReader);
                String line = "";
                String result = "";

                while ((line = bufferedReader.readLine()) != null) {
                    result += line;
                }
                bufferedReader.close();
                inputStreamReader.close();
                inputStream.close();
                connection.disconnect();

                return result;


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else if (type.equals("insertbatch")) {
            String batchnamedata = strings[1];
            String statusdata=strings[2];
            String ADMINno = strings[3];

            try {
                url = new URL(baseURL + "insertbatch.php");
                connection = (HttpURLConnection) url.openConnection();
                connection.setDoOutput(true);
                connection.setDoInput(true);
                connection.setRequestMethod("POST");

                outputStream = connection.getOutputStream();
                outputStreamWriter = new OutputStreamWriter(outputStream);
                bufferedWriter = new BufferedWriter(outputStreamWriter);

                String postdata = URLEncoder.encode("batchname", "UTF-8") + "=" + URLEncoder.encode(batchnamedata, "UTF-8");
                postdata += "&" + URLEncoder.encode("status", "UTF-8") + "=" + URLEncoder.encode(statusdata, "UTF-8");
                postdata += "&" + URLEncoder.encode("adminid", "UTF-8") + "=" + URLEncoder.encode(ADMINno, "UTF-8");
                postdata += "&" + URLEncoder.encode("passkey", "UTF-8") + "=" + URLEncoder.encode(passkey, "UTF-8");

                bufferedWriter.write(postdata);
                bufferedWriter.flush();

                bufferedWriter.close();
                outputStreamWriter.close();
                outputStream.close();

                inputStream = connection.getInputStream();
                inputStreamReader = new InputStreamReader(inputStream);
                bufferedReader = new BufferedReader(inputStreamReader);
                String line = "";
                String result = "";

                while ((line = bufferedReader.readLine()) != null) {
                    result += line;
                }
                bufferedReader.close();
                inputStreamReader.close();
                inputStream.close();
                connection.disconnect();

                return result;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else if (type.equals("addsubtobatch")) {
            String batchnamedata = strings[1];
            String subjectnamedata = strings[2];
            String ADMINno = strings[3];

            try {
                url = new URL(baseURL + "addsubtobatch.php");
                connection = (HttpURLConnection) url.openConnection();
                connection.setDoOutput(true);
                connection.setDoInput(true);
                connection.setRequestMethod("POST");

                outputStream = connection.getOutputStream();
                outputStreamWriter = new OutputStreamWriter(outputStream);
                bufferedWriter = new BufferedWriter(outputStreamWriter);

                String postdata = URLEncoder.encode("batchname", "UTF-8") + "=" + URLEncoder.encode(batchnamedata, "UTF-8");
                postdata += "&" + URLEncoder.encode("subject", "UTF-8") + "=" + URLEncoder.encode(subjectnamedata, "UTF-8");
                postdata += "&" + URLEncoder.encode("adminid", "UTF-8") + "=" + URLEncoder.encode(ADMINno, "UTF-8");
                postdata += "&" + URLEncoder.encode("passkey", "UTF-8") + "=" + URLEncoder.encode(passkey, "UTF-8");

                bufferedWriter.write(postdata);
                bufferedWriter.flush();

                bufferedWriter.close();
                outputStreamWriter.close();
                outputStream.close();

                inputStream = connection.getInputStream();
                inputStreamReader = new InputStreamReader(inputStream);
                bufferedReader = new BufferedReader(inputStreamReader);
                String line = "";
                String result = "";

                while ((line = bufferedReader.readLine()) != null) {
                    result += line;
                }
                bufferedReader.close();
                inputStreamReader.close();
                inputStream.close();
                connection.disconnect();

                return result;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else if (type.equals("insertlecture")) {
            String batchnamedata = strings[1];
            String subjectdataname = strings[2];
            String teacherdataname = strings[3];
            String datedata=strings[4];
            String timedata = strings[5];
            String adminiddata=strings[6];

            try {
                url = new URL(baseURL + "insertlecture.php");
                connection = (HttpURLConnection) url.openConnection();
                connection.setDoOutput(true);
                connection.setDoInput(true);
                connection.setRequestMethod("POST");

                outputStream = connection.getOutputStream();
                outputStreamWriter = new OutputStreamWriter(outputStream);
                bufferedWriter = new BufferedWriter(outputStreamWriter);

                String postdata = URLEncoder.encode("batchname", "UTF-8") + "=" + URLEncoder.encode(batchnamedata, "UTF-8");
                postdata += "&" + URLEncoder.encode("subject", "UTF-8") + "=" + URLEncoder.encode(subjectdataname, "UTF-8");
                postdata += "&" + URLEncoder.encode("teachername", "UTF-8") + "=" + URLEncoder.encode(teacherdataname, "UTF-8");
                postdata += "&" + URLEncoder.encode("time", "UTF-8") + "=" + URLEncoder.encode(timedata, "UTF-8");
                postdata += "&" + URLEncoder.encode("date", "UTF-8") + "=" + URLEncoder.encode(datedata, "UTF-8");
                postdata += "&" + URLEncoder.encode("adminid", "UTF-8") + "=" + URLEncoder.encode(adminiddata, "UTF-8");
                postdata += "&" + URLEncoder.encode("passkey", "UTF-8") + "=" + URLEncoder.encode(passkey, "UTF-8");

                bufferedWriter.write(postdata);
                bufferedWriter.flush();

                bufferedWriter.close();
                outputStreamWriter.close();
                outputStream.close();

                inputStream = connection.getInputStream();
                inputStreamReader = new InputStreamReader(inputStream);
                bufferedReader = new BufferedReader(inputStreamReader);
                String line = "";
                String result = "";

                while ((line = bufferedReader.readLine()) != null) {
                    result += line;
                }
                bufferedReader.close();
                inputStreamReader.close();
                inputStream.close();
                connection.disconnect();

                return result;


            } catch (MalformedURLException e) {
                Log.i("msg", "Catch1");
                e.printStackTrace();
            } catch (IOException e) {
                Log.i("msg", e.getMessage());
                e.printStackTrace();
            }
        }else if (type.equals("getSubjectFromBatch")) {
            try {
                String batchnamedata=strings[2];
                adminidforsubject=strings[3];
                dirsubject=strings[1];

                Log.i("catchmsg","msg :"+baseURL);
                URL url = new URL(baseURL +"fetchsubjectfromtable.php");

                connection = (HttpURLConnection) url.openConnection();
                connection.setDoOutput(true);
                connection.setDoInput(true);
                connection.setRequestMethod("POST");

                outputStream = connection.getOutputStream();
                outputStreamWriter = new OutputStreamWriter(outputStream);
                bufferedWriter = new BufferedWriter(outputStreamWriter);

                String postdata = URLEncoder.encode("batchname", "UTF-8") + "=" + URLEncoder.encode(batchnamedata, "UTF-8");
                postdata += "&" + URLEncoder.encode("adminid", "UTF-8") + "=" + URLEncoder.encode(adminidforsubject, "UTF-8");
                postdata += "&" + URLEncoder.encode("passkey", "UTF-8") + "=" + URLEncoder.encode(passkey, "UTF-8");

                bufferedWriter.write(postdata);
                bufferedWriter.flush();

                bufferedWriter.close();
                outputStreamWriter.close();
                outputStream.close();

                inputStream = connection.getInputStream();
                inputStreamReader = new InputStreamReader(inputStream);
                bufferedReader = new BufferedReader(inputStreamReader);


                String line = "";
                String result = "";

                while ((line = bufferedReader.readLine()) != null) {
                    Log.i("Msg", "In while");
                    result += line;
                }
                bufferedReader.close();
                inputStream.close();
                inputStreamReader.close();
                connection.disconnect();

                Log.i("checkmsg", "after getting jason: "+result);

                return result;
            } catch (MalformedURLException e) {
                Log.i("catchmsg","catch1 :"+e);
                e.printStackTrace();
            } catch (IOException e) {
                Log.i("catchmsg","catch2 :"+e);
                e.printStackTrace();
            }
        }else if (type.equals("getTeacherFromSubject")) {
            try {
                String subjectnamedata=strings[1];
                String adminiddata=strings[2];

                Log.i("catchmsg","msg :"+baseURL);
                URL url = new URL(baseURL +"fetchteacherlist.php");

                connection = (HttpURLConnection) url.openConnection();
                connection.setDoOutput(true);
                connection.setDoInput(true);
                connection.setRequestMethod("POST");

                outputStream = connection.getOutputStream();
                outputStreamWriter = new OutputStreamWriter(outputStream);
                bufferedWriter = new BufferedWriter(outputStreamWriter);

                String postdata = URLEncoder.encode("subject", "UTF-8") + "=" + URLEncoder.encode(subjectnamedata, "UTF-8");
                postdata += "&" + URLEncoder.encode("adminid", "UTF-8") + "=" + URLEncoder.encode(adminiddata, "UTF-8");
                postdata += "&" + URLEncoder.encode("passkey", "UTF-8") + "=" + URLEncoder.encode(passkey, "UTF-8");

                bufferedWriter.write(postdata);
                bufferedWriter.flush();

                bufferedWriter.close();
                outputStreamWriter.close();
                outputStream.close();

                inputStream = connection.getInputStream();
                inputStreamReader = new InputStreamReader(inputStream);
                bufferedReader = new BufferedReader(inputStreamReader);


                String line = "";
                String result = "";

                while ((line = bufferedReader.readLine()) != null) {
                    Log.i("Msg", "In while");
                    result += line;
                }
                bufferedReader.close();
                inputStream.close();
                inputStreamReader.close();
                connection.disconnect();

                Log.i("checkmsg", "after getting jason: "+result);

                return result;
            } catch (MalformedURLException e) {
                Log.i("catchmsg","catch1 :"+e);
                e.printStackTrace();
            } catch (IOException e) {
                Log.i("catchmsg","catch2 :"+e);
                e.printStackTrace();
            }
        }else if (type.equals("insertattendance")) {
            String batchnamedata = strings[1];
            String subjectdataname = strings[2];
            String studentdataname = strings[3];
            String datedata=strings[4];
            String attendancedata = strings[5];
            String adminiddata=strings[6];

            try {
                url = new URL(baseURL + "insertattendance.php");
                connection = (HttpURLConnection) url.openConnection();
                connection.setDoOutput(true);
                connection.setDoInput(true);
                connection.setRequestMethod("POST");

                outputStream = connection.getOutputStream();
                outputStreamWriter = new OutputStreamWriter(outputStream);
                bufferedWriter = new BufferedWriter(outputStreamWriter);

                String postdata = URLEncoder.encode("batchname", "UTF-8") + "=" + URLEncoder.encode(batchnamedata, "UTF-8");
                postdata += "&" + URLEncoder.encode("subject", "UTF-8") + "=" + URLEncoder.encode(subjectdataname, "UTF-8");
                postdata += "&" + URLEncoder.encode("studentname", "UTF-8") + "=" + URLEncoder.encode(studentdataname, "UTF-8");
                postdata += "&" + URLEncoder.encode("date", "UTF-8") + "=" + URLEncoder.encode(datedata, "UTF-8");
                postdata += "&" + URLEncoder.encode("attendance", "UTF-8") + "=" + URLEncoder.encode(attendancedata, "UTF-8");
                postdata += "&" + URLEncoder.encode("adminid", "UTF-8") + "=" + URLEncoder.encode(adminiddata, "UTF-8");
                postdata += "&" + URLEncoder.encode("passkey", "UTF-8") + "=" + URLEncoder.encode(passkey, "UTF-8");

                bufferedWriter.write(postdata);
                bufferedWriter.flush();

                bufferedWriter.close();
                outputStreamWriter.close();
                outputStream.close();

                inputStream = connection.getInputStream();
                inputStreamReader = new InputStreamReader(inputStream);
                bufferedReader = new BufferedReader(inputStreamReader);
                String line = "";
                String result = "";

                while ((line = bufferedReader.readLine()) != null) {
                    result += line;
                }
                bufferedReader.close();
                inputStreamReader.close();
                inputStream.close();
                connection.disconnect();

                return result;


            } catch (MalformedURLException e) {
                Log.i("msg", "Catch1");
                e.printStackTrace();
            } catch (IOException e) {
                Log.i("msg", e.getMessage());
                e.printStackTrace();
            }
        }else if (type.equals("getStudentFromBatch")) {
            try {
                String batchnamedata=strings[1];
                adminidforsubject=strings[2];

                Log.i("catchmsg","msg :"+baseURL);
                URL url = new URL(baseURL +"fetchstudentfromtable.php");

                connection = (HttpURLConnection) url.openConnection();
                connection.setDoOutput(true);
                connection.setDoInput(true);
                connection.setRequestMethod("POST");

                outputStream = connection.getOutputStream();
                outputStreamWriter = new OutputStreamWriter(outputStream);
                bufferedWriter = new BufferedWriter(outputStreamWriter);

                String postdata = URLEncoder.encode("batchname", "UTF-8") + "=" + URLEncoder.encode(batchnamedata, "UTF-8");
                postdata += "&" + URLEncoder.encode("adminid", "UTF-8") + "=" + URLEncoder.encode(adminidforsubject, "UTF-8");
                postdata += "&" + URLEncoder.encode("status", "UTF-8") + "=" + URLEncoder.encode("ACTIVE", "UTF-8");
                postdata += "&" + URLEncoder.encode("passkey", "UTF-8") + "=" + URLEncoder.encode(passkey, "UTF-8");

                bufferedWriter.write(postdata);
                bufferedWriter.flush();

                bufferedWriter.close();
                outputStreamWriter.close();
                outputStream.close();

                inputStream = connection.getInputStream();
                inputStreamReader = new InputStreamReader(inputStream);
                bufferedReader = new BufferedReader(inputStreamReader);


                String line = "";
                String result = "";

                while ((line = bufferedReader.readLine()) != null) {
                    Log.i("Msg", "In while");
                    result += line;
                }
                bufferedReader.close();
                inputStream.close();
                inputStreamReader.close();
                connection.disconnect();

                Log.i("checkmsg", "after getting jason: "+result);

                return result;
            } catch (MalformedURLException e) {
                Log.i("catchmsg","catch1 :"+e);
                e.printStackTrace();
            } catch (IOException e) {
                Log.i("catchmsg","catch2 :"+e);
                e.printStackTrace();
            }
        }else if (type.equals("insertnotification")) {
            String typedata = strings[1];
            String batchnamedata = strings[2];
            String titledata= strings[3];
            String messagedata=strings[4];
            String adminiddata=strings[5];
            String datedata = strings[6];
            String subject = strings[7];

            try {
                url = new URL(baseURL + "insertnotification.php");
                connection = (HttpURLConnection) url.openConnection();
                connection.setDoOutput(true);
                connection.setDoInput(true);
                connection.setRequestMethod("POST");

                outputStream = connection.getOutputStream();
                outputStreamWriter = new OutputStreamWriter(outputStream);
                bufferedWriter = new BufferedWriter(outputStreamWriter);

                String postdata = URLEncoder.encode("type", "UTF-8") + "=" + URLEncoder.encode(typedata, "UTF-8");
                postdata += "&" + URLEncoder.encode("batchname", "UTF-8") + "=" + URLEncoder.encode(batchnamedata, "UTF-8");
                postdata += "&" + URLEncoder.encode("title", "UTF-8") + "=" + URLEncoder.encode(titledata, "UTF-8");
                postdata += "&" + URLEncoder.encode("message", "UTF-8") + "=" + URLEncoder.encode(messagedata, "UTF-8");
                postdata += "&" + URLEncoder.encode("adminid", "UTF-8") + "=" + URLEncoder.encode(adminiddata, "UTF-8");
                postdata += "&" + URLEncoder.encode("date", "UTF-8") + "=" + URLEncoder.encode(datedata, "UTF-8");
                postdata += "&" + URLEncoder.encode("subject", "UTF-8") + "=" + URLEncoder.encode(subject, "UTF-8");
                postdata += "&" + URLEncoder.encode("passkey", "UTF-8") + "=" + URLEncoder.encode(passkey, "UTF-8");

                Log.i("checkmsg","data:"+postdata);
                bufferedWriter.write(postdata);
                bufferedWriter.flush();

                bufferedWriter.close();
                outputStreamWriter.close();
                outputStream.close();

                inputStream = connection.getInputStream();
                inputStreamReader = new InputStreamReader(inputStream);
                bufferedReader = new BufferedReader(inputStreamReader);
                String line = "";
                String result = "";

                while ((line = bufferedReader.readLine()) != null) {
                    result += line;
                }
                bufferedReader.close();
                inputStreamReader.close();
                inputStream.close();
                connection.disconnect();

                Log.i("checkmsg", "after getting jason: "+result);

                return result;


            } catch (MalformedURLException e) {
                Log.i("msg", "Catch1");
                e.printStackTrace();
            } catch (IOException e) {
                Log.i("msg", e.getMessage());
                e.printStackTrace();
            }
        }else if (type.equals("sendnotification")) {
            String typedata = strings[1];
            String batchnamedata = strings[2];
            String titledata= strings[3];
            String messagedata=strings[4];
            String adminiddata=strings[5];
            String datedata = strings[6];
            String subject = strings[7];

            try {
                if (typedata.equals("Student")) {
                    url = new URL(baseURL + "sendnotificationtostudent.php");
                }else if (typedata.equals("Teacher")){
                    url = new URL(baseURL + "sendnotificationtoteacher.php");
                }
                connection = (HttpURLConnection) url.openConnection();
                connection.setDoOutput(true);
                connection.setDoInput(true);
                connection.setRequestMethod("POST");

                outputStream = connection.getOutputStream();
                outputStreamWriter = new OutputStreamWriter(outputStream);
                bufferedWriter = new BufferedWriter(outputStreamWriter);

                String postdata = URLEncoder.encode("batchname", "UTF-8") + "=" + URLEncoder.encode(batchnamedata, "UTF-8");
                postdata += "&" + URLEncoder.encode("title", "UTF-8") + "=" + URLEncoder.encode(titledata, "UTF-8");
                postdata += "&" + URLEncoder.encode("message", "UTF-8") + "=" + URLEncoder.encode(messagedata, "UTF-8");
                postdata += "&" + URLEncoder.encode("adminid", "UTF-8") + "=" + URLEncoder.encode(adminiddata, "UTF-8");
                postdata += "&" + URLEncoder.encode("date", "UTF-8") + "=" + URLEncoder.encode(datedata, "UTF-8");
                postdata += "&" + URLEncoder.encode("subject", "UTF-8") + "=" + URLEncoder.encode(subject, "UTF-8");
                postdata += "&" + URLEncoder.encode("passkey", "UTF-8") + "=" + URLEncoder.encode(passkey, "UTF-8");

                Log.i("checkmsg","data:"+postdata);
                bufferedWriter.write(postdata);
                bufferedWriter.flush();

                bufferedWriter.close();
                outputStreamWriter.close();
                outputStream.close();

                inputStream = connection.getInputStream();
                inputStreamReader = new InputStreamReader(inputStream);
                bufferedReader = new BufferedReader(inputStreamReader);
                String line = "";
                String result = "";

                while ((line = bufferedReader.readLine()) != null) {
                    result += line;
                }
                bufferedReader.close();
                inputStreamReader.close();
                inputStream.close();
                connection.disconnect();

                Log.i("checkmsg", "after getting jason: "+result);

                return result;


            } catch (MalformedURLException e) {
                Log.i("msg", "Catch1");
                e.printStackTrace();
            } catch (IOException e) {
                Log.i("msg", e.getMessage());
                e.printStackTrace();
            }
        }else if (type.equals("inserttest")) {
            String batchnamedata = strings[1];
            String subjectdataname = strings[2];
            String topicdataname = strings[3];
            String marksdataname = strings[4];
            String datedata=strings[5];
            String timedata = strings[6];
            String adminiddata=strings[7];

            try {
                url = new URL(baseURL + "inserttest.php");
                connection = (HttpURLConnection) url.openConnection();
                connection.setDoOutput(true);
                connection.setDoInput(true);
                connection.setRequestMethod("POST");

                outputStream = connection.getOutputStream();
                outputStreamWriter = new OutputStreamWriter(outputStream);
                bufferedWriter = new BufferedWriter(outputStreamWriter);

                String postdata = URLEncoder.encode("batchname", "UTF-8") + "=" + URLEncoder.encode(batchnamedata, "UTF-8");
                postdata += "&" + URLEncoder.encode("subject", "UTF-8") + "=" + URLEncoder.encode(subjectdataname, "UTF-8");
                postdata += "&" + URLEncoder.encode("topic", "UTF-8") + "=" + URLEncoder.encode(topicdataname, "UTF-8");
                postdata += "&" + URLEncoder.encode("marks", "UTF-8") + "=" + URLEncoder.encode(marksdataname, "UTF-8");
                postdata += "&" + URLEncoder.encode("time", "UTF-8") + "=" + URLEncoder.encode(timedata, "UTF-8");
                postdata += "&" + URLEncoder.encode("date", "UTF-8") + "=" + URLEncoder.encode(datedata, "UTF-8");
                postdata += "&" + URLEncoder.encode("adminid", "UTF-8") + "=" + URLEncoder.encode(adminiddata, "UTF-8");
                postdata += "&" + URLEncoder.encode("passkey", "UTF-8") + "=" + URLEncoder.encode(passkey, "UTF-8");

                bufferedWriter.write(postdata);
                bufferedWriter.flush();

                bufferedWriter.close();
                outputStreamWriter.close();
                outputStream.close();

                inputStream = connection.getInputStream();
                inputStreamReader = new InputStreamReader(inputStream);
                bufferedReader = new BufferedReader(inputStreamReader);
                String line = "";
                String result = "";

                while ((line = bufferedReader.readLine()) != null) {
                    result += line;
                }
                bufferedReader.close();
                inputStreamReader.close();
                inputStream.close();
                connection.disconnect();

                return result;


            } catch (MalformedURLException e) {
                Log.i("msg", "Catch1");
                e.printStackTrace();
            } catch (IOException e) {
                Log.i("msg", e.getMessage());
                e.printStackTrace();
            }
        }else if (type.equals("getStudentFromNameBatch")) {
            String batchnamedata = strings[1];
            String studentdataname = strings[2];
            String adminiddata=strings[3];

            try {
                url = new URL(baseURL + "fetchsinglestudent.php");
                connection = (HttpURLConnection) url.openConnection();
                connection.setDoOutput(true);
                connection.setDoInput(true);
                connection.setRequestMethod("POST");

                outputStream = connection.getOutputStream();
                outputStreamWriter = new OutputStreamWriter(outputStream);
                bufferedWriter = new BufferedWriter(outputStreamWriter);

                String postdata = URLEncoder.encode("batchname", "UTF-8") + "=" + URLEncoder.encode(batchnamedata, "UTF-8");
                postdata += "&" + URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(studentdataname, "UTF-8");
                postdata += "&" + URLEncoder.encode("adminid", "UTF-8") + "=" + URLEncoder.encode(adminiddata, "UTF-8");
                postdata += "&" + URLEncoder.encode("passkey", "UTF-8") + "=" + URLEncoder.encode(passkey, "UTF-8");

                bufferedWriter.write(postdata);
                bufferedWriter.flush();

                bufferedWriter.close();
                outputStreamWriter.close();
                outputStream.close();

                inputStream = connection.getInputStream();
                inputStreamReader = new InputStreamReader(inputStream);
                bufferedReader = new BufferedReader(inputStreamReader);
                String line = "";
                String result = "";

                while ((line = bufferedReader.readLine()) != null) {
                    result += line;
                }
                bufferedReader.close();
                inputStreamReader.close();
                inputStream.close();
                connection.disconnect();

                return result;


            } catch (MalformedURLException e) {
                Log.i("msg", "Catch1");
                e.printStackTrace();
            } catch (IOException e) {
                Log.i("msg", e.getMessage());
                e.printStackTrace();
            }
        }else if (type.equals("getbatchattendance")) {
            String batchnamedata = strings[1];
            String subjectataname = strings[2];
            String datedata=strings[3];
            String adminiddata=strings[4];

            URL url;
            HttpURLConnection connection;
            OutputStream outputStream;
            OutputStreamWriter outputStreamWriter;
            BufferedWriter bufferedWriter;

            InputStream inputStream;
            InputStreamReader inputStreamReader;
            BufferedReader bufferedReader;


            try {
                url = new URL(baseURL + "fetchbatchattendance.php");
                connection = (HttpURLConnection) url.openConnection();
                connection.setDoOutput(true);
                connection.setDoInput(true);
                connection.setRequestMethod("POST");

                outputStream = connection.getOutputStream();
                outputStreamWriter = new OutputStreamWriter(outputStream);
                bufferedWriter = new BufferedWriter(outputStreamWriter);

                String postdata = URLEncoder.encode("batchname", "UTF-8") + "=" + URLEncoder.encode(batchnamedata, "UTF-8");
                postdata += "&" + URLEncoder.encode("subjectname", "UTF-8") + "=" + URLEncoder.encode(subjectataname, "UTF-8");
                postdata += "&" + URLEncoder.encode("date", "UTF-8") + "=" + URLEncoder.encode(datedata, "UTF-8");
                postdata += "&" + URLEncoder.encode("adminid", "UTF-8") + "=" + URLEncoder.encode(adminiddata, "UTF-8");
                postdata += "&" + URLEncoder.encode("passkey", "UTF-8") + "=" + URLEncoder.encode(passkey, "UTF-8");

                bufferedWriter.write(postdata);
                bufferedWriter.flush();

                bufferedWriter.close();
                outputStreamWriter.close();
                outputStream.close();

                inputStream = connection.getInputStream();
                inputStreamReader = new InputStreamReader(inputStream);
                bufferedReader = new BufferedReader(inputStreamReader);
                String line = "";
                String result = "";

                while ((line = bufferedReader.readLine()) != null) {
                    result += line;
                }
                bufferedReader.close();
                inputStreamReader.close();
                inputStream.close();
                connection.disconnect();

                return result;


            } catch (MalformedURLException e) {
                Log.i("msg", "Catch1");
                e.printStackTrace();
            } catch (IOException e) {
                Log.i("msg", e.getMessage());
                e.printStackTrace();
            }
        }else if (type.equals("getnotificationlist")) {
            try {
                String typelist=strings[1];
                String datafetched=strings[2];
                String adminid=strings[3];
                String fetchedsub=strings[4];

                String postmydata = null;

                if (typelist.equals("student")){
                    url = new URL(baseURL +"studentnotification.php");
                    postmydata = URLEncoder.encode("adminid", "UTF-8") + "=" + URLEncoder.encode(adminid, "UTF-8");
                    postmydata += "&" + URLEncoder.encode("batch", "UTF-8") + "=" + URLEncoder.encode(datafetched, "UTF-8");
                    postmydata += "&" + URLEncoder.encode("passkey", "UTF-8") + "=" + URLEncoder.encode(passkey, "UTF-8");
                    Log.i("checkmsg","link:"+postmydata);

                }else if (typelist.equals("teacher")){
                    url = new URL(baseURL +"teachernotification.php");
                    postmydata = URLEncoder.encode("adminid", "UTF-8") + "=" + URLEncoder.encode(adminid, "UTF-8");
                    postmydata += "&" + URLEncoder.encode("teacher", "UTF-8") + "=" + URLEncoder.encode(datafetched, "UTF-8");
                    postmydata += "&" + URLEncoder.encode("subject", "UTF-8") + "=" + URLEncoder.encode(fetchedsub, "UTF-8");
                    postmydata += "&" + URLEncoder.encode("passkey", "UTF-8") + "=" + URLEncoder.encode(passkey, "UTF-8");
                    Log.i("checkmsg","Data:"+postmydata);
                }


                Log.i("catchmsg","msg :"+baseURL);
                connection = (HttpURLConnection) url.openConnection();
                connection.setDoOutput(true);
                connection.setDoInput(true);
                connection.setRequestMethod("POST");

                outputStream = connection.getOutputStream();
                outputStreamWriter = new OutputStreamWriter(outputStream);
                bufferedWriter = new BufferedWriter(outputStreamWriter);

                bufferedWriter.write(postmydata);
                bufferedWriter.flush();

                bufferedWriter.close();
                outputStreamWriter.close();
                outputStream.close();

                inputStream = connection.getInputStream();
                inputStreamReader = new InputStreamReader(inputStream);
                bufferedReader = new BufferedReader(inputStreamReader);

                String line = "";
                String result = "";

                while ((line = bufferedReader.readLine()) != null) {
                    Log.i("Msg", "In while");
                    result += line;
                }
                bufferedReader.close();
                inputStream.close();
                inputStreamReader.close();
                connection.disconnect();

                Log.i("checkmsg", "after getting jason batchlist: "+result);

                return result;
            } catch (MalformedURLException e) {
                Log.i("catchmsg","catch1 :"+e);
                e.printStackTrace();
            } catch (IOException e) {
                Log.i("catchmsg","catch2 :"+e);
                e.printStackTrace();
            }
        } else if (type.equals("insertadmin")){
            String namedata=strings[1];
            String contactdata=strings[2];
            String emaildata=strings[3];
            String coachingnamedata=strings[4];
            String coachinglocationdata=strings[5];
            String passworddata=strings[6];

            try {
                url=new URL(baseURL+"insertadmin.php");
                connection=(HttpURLConnection)url.openConnection();
                connection.setDoOutput(true);
                connection.setDoInput(true);
                connection.setRequestMethod("POST");

                outputStream=connection.getOutputStream();
                outputStreamWriter=new OutputStreamWriter(outputStream);
                bufferedWriter=new BufferedWriter(outputStreamWriter);

                Log.i("MYERROR","Below Buffer Writer");

                String postdata= URLEncoder.encode("name","UTF-8")+"="+URLEncoder.encode(namedata,"UTF-8");
                postdata+="&"+URLEncoder.encode("email","UTF-8")+"="+URLEncoder.encode(emaildata,"UTF-8");
                postdata+="&"+URLEncoder.encode("contact",  "UTF-8")+"="+URLEncoder.encode(contactdata,"UTF-8");
                postdata+="&"+URLEncoder.encode("password","UTF-8")+"="+URLEncoder.encode(passworddata,"UTF-8");
                postdata+="&"+URLEncoder.encode("coachingname","UTF-8")+"="+URLEncoder.encode(coachingnamedata,"UTF-8");
                postdata+="&"+URLEncoder.encode("coachinglocation","UTF-8")+"="+URLEncoder.encode(coachinglocationdata,"UTF-8");
                postdata += "&" + URLEncoder.encode("passkey", "UTF-8") + "=" + URLEncoder.encode(passkey, "UTF-8");


                Log.i("MYERROR","Printing data :"+postdata);
                bufferedWriter.write(postdata);
                bufferedWriter.flush();

                bufferedWriter.close();
                outputStreamWriter.close();
                outputStream.close();

                inputStream=connection.getInputStream();
                inputStreamReader=new InputStreamReader(inputStream);
                bufferedReader=new BufferedReader(inputStreamReader);
                String line="";
                String result="";

                while ((line=bufferedReader.readLine())!=null){
                    result+=line;
                }
                bufferedReader.close();
                inputStreamReader.close();
                inputStream.close();
                connection.disconnect();

                Log.i("same",result);
                return result;


            } catch (MalformedURLException e) {
                Log.i("msg","Catch1");
                e.printStackTrace();
            } catch (IOException e) {
                Log.i("msg",e.getMessage());
                e.printStackTrace();
            }

        }else if (type.equals("adminlogin")){
            mainemail=strings[1];
            String password=strings[2];

            ((Activity)context).runOnUiThread(new Runnable() {
                public void run() {
                    progressDialog=new ProgressDialog(context);
                    progressDialog.setMessage("Connecting...");
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                }
            });

            try {
                url=new URL(baseURL+"adminlogin.php");
                connection=(HttpURLConnection)url.openConnection();
                connection.setDoOutput(true);
                connection.setDoInput(true);
                connection.setRequestMethod("POST");

                outputStream=connection.getOutputStream();
                outputStreamWriter=new OutputStreamWriter(outputStream);
                bufferedWriter=new BufferedWriter(outputStreamWriter);

                Log.i("MYERROR","Below Buffer Writer");

                String postdata= URLEncoder.encode("email","UTF-8")+"="+URLEncoder.encode(mainemail,"UTF-8");
                postdata+="&"+URLEncoder.encode("password","UTF-8")+"="+URLEncoder.encode(password,"UTF-8");
                postdata += "&" + URLEncoder.encode("passkey", "UTF-8") + "=" + URLEncoder.encode(passkey, "UTF-8");

                Log.i("MYERROR","Printing data :"+postdata);
                bufferedWriter.write(postdata);
                bufferedWriter.flush();

                bufferedWriter.close();
                outputStreamWriter.close();
                outputStream.close();

                inputStream=connection.getInputStream();
                inputStreamReader=new InputStreamReader(inputStream);
                bufferedReader=new BufferedReader(inputStreamReader);
                String line="";
                String result="";

                while ((line=bufferedReader.readLine())!=null){
                    result+=line;
                }
                bufferedReader.close();
                inputStreamReader.close();
                inputStream.close();
                connection.disconnect();

                Log.i("same",result);
                return result;


            } catch (MalformedURLException e) {
                Log.i("msg","Catch1");
                e.printStackTrace();
            } catch (IOException e) {
                Log.i("msg",e.getMessage());
                e.printStackTrace();
            }

        }else if (type.equals("insertstaff")){
            String namedata=strings[1];
            String contactdata=strings[2];
            String emaildata=strings[3];
            String passworddata=strings[4];
            String adminiddata=strings[5];

            try {
                url=new URL(baseURL+"insertstaff.php");
                connection=(HttpURLConnection)url.openConnection();
                connection.setDoOutput(true);
                connection.setDoInput(true);
                connection.setRequestMethod("POST");


                outputStream=connection.getOutputStream();
                outputStreamWriter=new OutputStreamWriter(outputStream);
                bufferedWriter=new BufferedWriter(outputStreamWriter);

                Log.i("MYERROR","Below Buffer Writer");

                String postdata= URLEncoder.encode("name","UTF-8")+"="+URLEncoder.encode(namedata,"UTF-8");
                postdata+="&"+URLEncoder.encode("email","UTF-8")+"="+URLEncoder.encode(emaildata,"UTF-8");
                postdata+="&"+URLEncoder.encode("contact",  "UTF-8")+"="+URLEncoder.encode(contactdata,"UTF-8");
                postdata+="&"+URLEncoder.encode("password","UTF-8")+"="+URLEncoder.encode(passworddata,"UTF-8");
                postdata+="&"+URLEncoder.encode("adminid","UTF-8")+"="+URLEncoder.encode(adminiddata,"UTF-8");
                postdata += "&" + URLEncoder.encode("passkey", "UTF-8") + "=" + URLEncoder.encode(passkey, "UTF-8");

                Log.i("MYERROR","Printing data :"+postdata);
                bufferedWriter.write(postdata);
                bufferedWriter.flush();

                bufferedWriter.close();
                outputStreamWriter.close();
                outputStream.close();

                inputStream=connection.getInputStream();
                inputStreamReader=new InputStreamReader(inputStream);
                bufferedReader=new BufferedReader(inputStreamReader);
                String line="";
                String result="";

                while ((line=bufferedReader.readLine())!=null){
                    result+=line;
                }
                bufferedReader.close();
                inputStreamReader.close();
                inputStream.close();
                connection.disconnect();

                Log.i("MYERROR","Printing result :"+result);

                return result;


            } catch (MalformedURLException e) {
                Log.i("msg","Catch1");
                e.printStackTrace();
            } catch (IOException e) {
                Log.i("msg",e.getMessage());
                e.printStackTrace();
            }

        }else if (type.equals("commonlogin")) {
            try {
                typelogin=strings[1];
                mainemail=strings[2];
                adminidforcommonlogin=strings[3];
                String password=strings[4];

                ((Activity)context).runOnUiThread(new Runnable() {
                    public void run() {
                        progressDialog=new ProgressDialog(context);
                        progressDialog.setMessage("Connecting...");
                        progressDialog.setCancelable(false);
                        progressDialog.show();
                    }
                });


                Log.i("catchmsg","msg :"+baseURL);
                if (typelogin.equals("stafflogin")){
                    url = new URL(baseURL +"stafflogin.php");

                }else if (typelogin.equals("studentlogin")){
                    url = new URL(baseURL +"studentlogin.php");

                }else if (typelogin.equals("teacherlogin")){
                    url = new URL(baseURL +"teacherlogin.php");
                }

                connection = (HttpURLConnection) url.openConnection();
                connection.setDoOutput(true);
                connection.setDoInput(true);
                connection.setRequestMethod("POST");

                outputStream = connection.getOutputStream();
                outputStreamWriter = new OutputStreamWriter(outputStream);
                bufferedWriter = new BufferedWriter(outputStreamWriter);

                String postdata = URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(mainemail, "UTF-8");
                postdata += "&" + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");
                postdata += "&" + URLEncoder.encode("adminid", "UTF-8") + "=" + URLEncoder.encode(adminidforcommonlogin, "UTF-8");
                postdata += "&" + URLEncoder.encode("passkey", "UTF-8") + "=" + URLEncoder.encode(passkey, "UTF-8");

                bufferedWriter.write(postdata);
                bufferedWriter.flush();

                bufferedWriter.close();
                outputStreamWriter.close();
                outputStream.close();

                inputStream = connection.getInputStream();
                inputStreamReader = new InputStreamReader(inputStream);
                bufferedReader = new BufferedReader(inputStreamReader);


                String line = "";
                String result = "";

                while ((line = bufferedReader.readLine()) != null) {
                    Log.i("Msg", "In while");
                    result += line;
                }
                bufferedReader.close();
                inputStream.close();
                inputStreamReader.close();
                connection.disconnect();

                Log.i("checkmsg", "after getting jason: login "+result);


                return result;


            } catch (MalformedURLException e) {
                Log.i("catchmsg","catch1 :"+e);
                e.printStackTrace();
            } catch (IOException e) {
                Log.i("catchmsg","catch2 :"+e);
                e.printStackTrace();
            }
        }else if (type.equals("changepassword")) {
            try {
                String typeofchange=strings[1];
                String emaildata=strings[2];
                String newpassord=strings[3];
                String adminid=strings[4];



                Log.i("catchmsg","msg :"+baseURL);
                if (typeofchange.equals("staff")){
                    url = new URL(baseURL +"updatestaffpass.php");

                }else if (typeofchange.equals("student")){
                    url = new URL(baseURL +"updatestudentpass.php");

                }else if (typeofchange.equals("teacher")){
                    url = new URL(baseURL +"updateteacherpass.php");

                }else if (typeofchange.equals("admin")){
                    url = new URL(baseURL +"updateadminpass.php");
                }

                connection = (HttpURLConnection) url.openConnection();
                connection.setDoOutput(true);
                connection.setDoInput(true);
                connection.setRequestMethod("POST");

                outputStream = connection.getOutputStream();
                outputStreamWriter = new OutputStreamWriter(outputStream);
                bufferedWriter = new BufferedWriter(outputStreamWriter);

                String postdata = URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(emaildata, "UTF-8");
                postdata += "&" + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(newpassord, "UTF-8");
                postdata += "&" + URLEncoder.encode("adminid", "UTF-8") + "=" + URLEncoder.encode(adminid, "UTF-8");
                postdata += "&" + URLEncoder.encode("passkey", "UTF-8") + "=" + URLEncoder.encode(passkey, "UTF-8");

                bufferedWriter.write(postdata);
                bufferedWriter.flush();

                bufferedWriter.close();
                outputStreamWriter.close();
                outputStream.close();

                inputStream = connection.getInputStream();
                inputStreamReader = new InputStreamReader(inputStream);
                bufferedReader = new BufferedReader(inputStreamReader);


                String line = "";
                String result = "";

                while ((line = bufferedReader.readLine()) != null) {
                    Log.i("Msg", "In while");
                    result += line;
                }
                bufferedReader.close();
                inputStream.close();
                inputStreamReader.close();
                connection.disconnect();

                Log.i("checkmsg", "after getting jason: "+result);

                return result;
            } catch (MalformedURLException e) {
                Log.i("catchmsg","catch1 :"+e);
                e.printStackTrace();
            } catch (IOException e) {
                Log.i("catchmsg","catch2 :"+e);
                e.printStackTrace();
            }
        }
        else if (type.equals("getlecturelist")){
            try {
                gettype=strings[1];
                admindataforlecture=strings[2];
                String batchdata=strings[3];
                String postlecture=null;

                Calendar c= Calendar.getInstance();

                int day=c.get(Calendar.DAY_OF_MONTH);
                int month=c.get(Calendar.MONTH);
                int year=c.get(Calendar.YEAR);

                String date=String.valueOf(day)+"/"+String.valueOf(month+1)+"/"+String.valueOf(year);
                String date1=String.valueOf(day+1)+"/"+String.valueOf(month+1)+"/"+String.valueOf(year);
                String date2=String.valueOf(day+2)+"/"+String.valueOf(month+1)+"/"+String.valueOf(year);
                String date3=String.valueOf(day+3)+"/"+String.valueOf(month+1)+"/"+String.valueOf(year);
                String date4=String.valueOf(day+4)+"/"+String.valueOf(month+1)+"/"+String.valueOf(year);
                String date5=String.valueOf(day+5)+"/"+String.valueOf(month+1)+"/"+String.valueOf(year);
                String date6=String.valueOf(day+6)+"/"+String.valueOf(month+1)+"/"+String.valueOf(year);
                String date7=String.valueOf(day+7)+"/"+String.valueOf(month+1)+"/"+String.valueOf(year);

                Log.i("checkmsg","Data:"+gettype);
                if (gettype.equals("student")) {
                    url = new URL(baseURL + "fetchlectureforstudent.php");
                    postlecture = URLEncoder.encode("adminid", "UTF-8") + "=" + URLEncoder.encode(admindataforlecture, "UTF-8");
                    postlecture += "&" + URLEncoder.encode("batch", "UTF-8") + "=" + URLEncoder.encode(batchdata, "UTF-8");
                    postlecture += "&" + URLEncoder.encode("date", "UTF-8") + "=" + URLEncoder.encode(date, "UTF-8");
                    postlecture += "&" + URLEncoder.encode("date1", "UTF-8") + "=" + URLEncoder.encode(date1, "UTF-8");
                    postlecture += "&" + URLEncoder.encode("date2", "UTF-8") + "=" + URLEncoder.encode(date2, "UTF-8");
                    postlecture += "&" + URLEncoder.encode("date3", "UTF-8") + "=" + URLEncoder.encode(date3, "UTF-8");
                    postlecture += "&" + URLEncoder.encode("date4", "UTF-8") + "=" + URLEncoder.encode(date4, "UTF-8");
                    postlecture += "&" + URLEncoder.encode("date5", "UTF-8") + "=" + URLEncoder.encode(date5, "UTF-8");
                    postlecture += "&" + URLEncoder.encode("date6", "UTF-8") + "=" + URLEncoder.encode(date6, "UTF-8");
                    postlecture += "&" + URLEncoder.encode("date7", "UTF-8") + "=" + URLEncoder.encode(date7, "UTF-8");
                    postlecture += "&" + URLEncoder.encode("passkey", "UTF-8") + "=" + URLEncoder.encode(passkey, "UTF-8");


                }else if (gettype.equals("teacher")){
                    url = new URL(baseURL + "fetchlectureforteacher.php");
                    postlecture = URLEncoder.encode("adminid", "UTF-8") + "=" + URLEncoder.encode(admindataforlecture, "UTF-8");
                    postlecture += "&" + URLEncoder.encode("batch", "UTF-8") + "=" + URLEncoder.encode(batchdata, "UTF-8");
                    postlecture += "&" + URLEncoder.encode("date", "UTF-8") + "=" + URLEncoder.encode(date, "UTF-8");
                    postlecture += "&" + URLEncoder.encode("date1", "UTF-8") + "=" + URLEncoder.encode(date1, "UTF-8");
                    postlecture += "&" + URLEncoder.encode("date2", "UTF-8") + "=" + URLEncoder.encode(date2, "UTF-8");
                    postlecture += "&" + URLEncoder.encode("date3", "UTF-8") + "=" + URLEncoder.encode(date3, "UTF-8");
                    postlecture += "&" + URLEncoder.encode("date4", "UTF-8") + "=" + URLEncoder.encode(date4, "UTF-8");
                    postlecture += "&" + URLEncoder.encode("date5", "UTF-8") + "=" + URLEncoder.encode(date5, "UTF-8");
                    postlecture += "&" + URLEncoder.encode("date6", "UTF-8") + "=" + URLEncoder.encode(date6, "UTF-8");
                    postlecture += "&" + URLEncoder.encode("date7", "UTF-8") + "=" + URLEncoder.encode(date7, "UTF-8");
                    postlecture += "&" + URLEncoder.encode("passkey", "UTF-8") + "=" + URLEncoder.encode(passkey, "UTF-8");

                }else if(gettype.equals("staff")){
                    url = new URL(baseURL + "fetchlectureforstaff.php");
                    postlecture = URLEncoder.encode("adminid", "UTF-8") + "=" + URLEncoder.encode(admindataforlecture, "UTF-8");
                    postlecture += "&" + URLEncoder.encode("batch", "UTF-8") + "=" + URLEncoder.encode(batchdata, "UTF-8");
                    postlecture += "&" + URLEncoder.encode("passkey", "UTF-8") + "=" + URLEncoder.encode(passkey, "UTF-8");

                }

                connection = (HttpURLConnection) url.openConnection();
                connection.setDoOutput(true);
                connection.setDoInput(true);
                connection.setRequestMethod("POST");

                outputStream = connection.getOutputStream();
                outputStreamWriter = new OutputStreamWriter(outputStream);
                bufferedWriter = new BufferedWriter(outputStreamWriter);

                bufferedWriter.write(postlecture);
                bufferedWriter.flush();

                bufferedWriter.close();
                outputStreamWriter.close();
                outputStream.close();

                inputStream=connection.getInputStream();
                inputStreamReader=new InputStreamReader(inputStream);
                bufferedReader=new BufferedReader(inputStreamReader);

                String line="";
                String result="";

                while((line=bufferedReader.readLine())!=null){

                    result+=line;
                }
                bufferedReader.close();
                inputStreamReader.close();
                inputStream.close();
                connection.disconnect();

                Log.i("Checkmsg",result);

                return result;

            } catch (MalformedURLException e) {
                Log.i("ecx", String.valueOf(e));
                e.printStackTrace();
            } catch (IOException e) {
                Log.i("ecx1", String.valueOf(e));
                e.printStackTrace();
            }
        }else if (type.equals("gettestlist")){
            try {
                admindatafortest=strings[1];
                String batchdata=strings[2];

                if (batchdata.equals("nothing")){
                    url=new URL(baseURL+"fetchtestlistforstaff.php");
                    typetest="staff";
                }
                else {
                    url=new URL(baseURL+"fetchtestlist.php");
                    typetest="student";
                }
                connection = (HttpURLConnection) url.openConnection();
                connection.setDoOutput(true);
                connection.setDoInput(true);
                connection.setRequestMethod("POST");

                outputStream = connection.getOutputStream();
                outputStreamWriter = new OutputStreamWriter(outputStream);
                bufferedWriter = new BufferedWriter(outputStreamWriter);

                String postdata = URLEncoder.encode("adminid", "UTF-8") + "=" + URLEncoder.encode(admindatafortest, "UTF-8");
                postdata += "&" + URLEncoder.encode("batch", "UTF-8") + "=" + URLEncoder.encode(batchdata, "UTF-8");
                postdata += "&" + URLEncoder.encode("passkey", "UTF-8") + "=" + URLEncoder.encode(passkey, "UTF-8");

                bufferedWriter.write(postdata);
                bufferedWriter.flush();

                bufferedWriter.close();
                outputStreamWriter.close();
                outputStream.close();

                inputStream=connection.getInputStream();
                inputStreamReader=new InputStreamReader(inputStream);
                bufferedReader=new BufferedReader(inputStreamReader);

                String line="";
                String result="";

                while((line=bufferedReader.readLine())!=null){

                    result+=line;
                }
                bufferedReader.close();
                inputStreamReader.close();
                inputStream.close();
                connection.disconnect();

                Log.i("Checkmsg",result);

                return result;

            } catch (MalformedURLException e) {
                Log.i("ecx", String.valueOf(e));
                e.printStackTrace();
            } catch (IOException e) {
                Log.i("ecx1", String.valueOf(e));
                e.printStackTrace();
            }
        }else if (type.equals("inserttoken")) {
            try {
                String tokentype = strings[1];
                String email = strings[2];
                String token = strings[3];

                if (tokentype.equals("student")) {
                    url = new URL(baseURL + "studenttoken.php");
                }else if (tokentype.equals("teacher")){
                    url = new URL(baseURL + "teachertoken.php");
                }

                connection = (HttpURLConnection) url.openConnection();
                connection.setDoOutput(true);
                connection.setDoInput(true);
                connection.setRequestMethod("POST");

                outputStream = connection.getOutputStream();
                outputStreamWriter = new OutputStreamWriter(outputStream);
                bufferedWriter = new BufferedWriter(outputStreamWriter);

                String postdata = URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8");
                postdata += "&" + URLEncoder.encode("token", "UTF-8") + "=" + URLEncoder.encode(token, "UTF-8");
                postdata += "&" + URLEncoder.encode("passkey", "UTF-8") + "=" + URLEncoder.encode(passkey, "UTF-8");

                bufferedWriter.write(postdata);
                bufferedWriter.flush();

                bufferedWriter.close();
                outputStreamWriter.close();
                outputStream.close();

                inputStream = connection.getInputStream();
                inputStreamReader = new InputStreamReader(inputStream);
                bufferedReader = new BufferedReader(inputStreamReader);

                String line = "";
                String result = "";

                while ((line = bufferedReader.readLine()) != null) {

                    result += line;
                }
                bufferedReader.close();
                inputStreamReader.close();
                inputStream.close();
                connection.disconnect();

                Log.i("Checkmsg", result);

                return result;

            } catch (MalformedURLException e) {
                Log.i("ecx", String.valueOf(e));
                e.printStackTrace();
            } catch (IOException e) {
                Log.i("ecx1", String.valueOf(e));
                e.printStackTrace();
            }
        }else if (type.equals("delete")) {
            String myid = strings[2];
            String typedelete = strings[1];

            try {
                if (typedelete.equals("lecture")){
                    url = new URL(baseURL + "deletelecture.php");
                }else if (typedelete.equals("test")){
                    url = new URL(baseURL + "deletetest.php");
                }else if (typedelete.equals("att")){
                    url = new URL(baseURL + "deleteatt.php");
                }
                connection = (HttpURLConnection) url.openConnection();
                connection.setDoOutput(true);
                connection.setDoInput(true);
                connection.setRequestMethod("POST");

                outputStream = connection.getOutputStream();
                outputStreamWriter = new OutputStreamWriter(outputStream);
                bufferedWriter = new BufferedWriter(outputStreamWriter);

                String postdata = URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(myid, "UTF-8");
                postdata += "&" + URLEncoder.encode("passkey", "UTF-8") + "=" + URLEncoder.encode(passkey, "UTF-8");

                bufferedWriter.write(postdata);
                bufferedWriter.flush();

                bufferedWriter.close();
                outputStreamWriter.close();
                outputStream.close();

                inputStream = connection.getInputStream();
                inputStreamReader = new InputStreamReader(inputStream);
                bufferedReader = new BufferedReader(inputStreamReader);
                String line = "";
                String result = "";

                while ((line = bufferedReader.readLine()) != null) {
                    result += line;
                }
                bufferedReader.close();
                inputStreamReader.close();
                inputStream.close();
                connection.disconnect();

                Log.i("same","data:"+result);



                return result;


            } catch (MalformedURLException e) {
                Log.i("msg", "Catch1");
                e.printStackTrace();
            } catch (IOException e) {
                Log.i("msg", e.getMessage());
                e.printStackTrace();
            }
        }





            return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        if (type.equals("insertstudent")) {
            String st = "";
            if (s.equals("success")) {
                st = "Student Account Added Successfully";
            } else {
                st = "EmailID Already Exists Use Another EmailID";
            }
            Toast.makeText(context, st, Toast.LENGTH_LONG).show();
        }else if (type.equals("changepassword")) {
            String st = "";
            if (s.equals("success")) {
                st = "Password Changed Successfully";
            } else {
                st = "Verify Email ID";
            }
            Toast.makeText(context, st, Toast.LENGTH_LONG).show();
        }else if (type.equals("validcheck")) {
            String st = "";
            validate="";
            if (s.equals("failure")) {
                validate="false";
            } else if (s.equals("success")){
                validate="true";
            }
            TextView txt=(TextView) ((Activity)context).findViewById(R.id.checktext);
            txt.setText(validate);

        } else if (type.equals("delete")) {
            String st = "";
            if (s.equals("success")) {
                st = "Deleted !..";
            } else {
                st = "Some error Occured";
            }
            Toast.makeText(context, st, Toast.LENGTH_LONG).show();
        }else if (type.equals("inserttoken")) {
            String st = "";
            if (s.equals("success")) {
                st = "Token Inserted";
            } else {
                st = "Verify Email ID";
            }
           // Toast.makeText(context, st, Toast.LENGTH_LONG).show();
        }
        else if (type.equals("insertstaff")){
            String st="";
            if(s.equals("success"))
            {
                st+="Staff Account Created successfully";
            }
            else
            {
                st+="EmailID Already Exists Use Another EmailID";
            }
            Toast.makeText(context,st,Toast.LENGTH_LONG).show();
        }else if (type.equals("commonlogin")){
            String st="";

            progressDialog.dismiss();

            if(!s.equals("failure"))
            {
                st+="Successfully Logged In";
                if (typelogin.equals("stafflogin")){
                    Intent intent=new Intent(context,StaffFunctions.class);
                    intent.putExtra("classid",adminidforcommonlogin);
                    intent.putExtra("email",mainemail);
                    Log.i("NewError",mainemail);
                    context.startActivity(intent);
                    ((Activity)context).finish();
                }else if (typelogin.equals("studentlogin")){
                    Intent intent=new Intent(context,StudentFunctions.class);
                    try {
                        JSONArray jsonArray = new JSONArray(s);
                        JSONObject jsonObject;

                        String fetchedstudent = "";
                        for (int i = 0; i < jsonArray.length(); i++) {
                            fetchedstudent = jsonArray.getString(i);
                            jsonObject = new JSONObject(fetchedstudent);
                            String name=jsonObject.getString("name");
                            String batch=jsonObject.getString("batch");

                            intent.putExtra("myname",name);
                            intent.putExtra("mybatch",batch);
                            intent.putExtra("email",mainemail);
                            Log.i("NewError",mainemail);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    intent.putExtra("classid",adminidforcommonlogin);
                    context.startActivity(intent);
                    ((Activity)context).finish();
                }else if (typelogin.equals("teacherlogin")){
                    Intent intent=new Intent(context,TeacherFunctions.class);
                    try {
                        JSONArray jsonArray = new JSONArray(s);
                        JSONObject jsonObject;

                        String fetchedstudent = "";
                        for (int i = 0; i < jsonArray.length(); i++) {
                            fetchedstudent = jsonArray.getString(i);
                            jsonObject = new JSONObject(fetchedstudent);
                            String name=jsonObject.getString("name");
                            String sub=jsonObject.getString("subject");

                            intent.putExtra("myname",name);
                            intent.putExtra("mysubject",sub);
                            intent.putExtra("email",mainemail);
                            Log.i("NewError",mainemail);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    intent.putExtra("classid",adminidforcommonlogin);
                    context.startActivity(intent);
                    ((Activity)context).finish();
                }
            }
            else
            {
                st+="Invalid Credentials";

            }
            Toast.makeText(context,st,Toast.LENGTH_LONG).show();
        } else if (type.equals("getbatchlist")) {
            try {
                JSONArray jsonArray = new JSONArray(s);
                JSONObject jsonObject;

                final ArrayList<String> batchlist = new ArrayList();

                final String[] fetchedbatch = {""};
                for (int i = 0; i < jsonArray.length(); i++) {
                    fetchedbatch[0] = jsonArray.getString(i);
                    jsonObject = new JSONObject(fetchedbatch[0]);
                    batchlist.add(jsonObject.getString("name"));
                }
                ArrayAdapter adapter = new ArrayAdapter(context, android.R.layout.simple_list_item_1, batchlist);
                Spinner spinner = ((Activity) context).findViewById(R.id.spinnerBatchlist);
                spinner.setAdapter(adapter);

                final String[] selectedBatch = new String[1];
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        selectedBatch[0] = batchlist.get(position);
                        TextView textView=((Activity)context).findViewById(R.id.tvspbatch);
                        textView.setText(selectedBatch[0]);


                        if (dirbatch.equals("callsubjectandteacher")) {
                            TextView textView2=((Activity)context).findViewById(R.id.tvspsubject);
                            textView2.setText("");
                            HttpServicesHelper helper = new HttpServicesHelper(context);
                            helper.execute("getSubjectFromBatch","callteacher", selectedBatch[0], adminidforbatch);

                        }else if (dirbatch.equals("callsubjectandstudent")){
                            TextView textView2=((Activity)context).findViewById(R.id.tvspsubject);
                            textView2.setText("");
                            new HttpServicesHelper(context).execute("getSubjectFromBatch","callstudent", selectedBatch[0], adminidforbatch);


                            new HttpServicesHelper(context).execute("getStudentFromBatch", selectedBatch[0], adminidforbatch);
                        }else if (dirbatch.equals("callsubject")){
                            TextView textView2=((Activity)context).findViewById(R.id.tvspsubject);
                            textView2.setText("");
                            HttpServicesHelper helper = new HttpServicesHelper(context);
                            helper.execute("getSubjectFromBatch","nothing", selectedBatch[0],adminidforbatch);
                        }else if (dirbatch.equals("callsinglestudent")){
                            HttpServicesHelper helper1 = new HttpServicesHelper(context);
                            helper1.execute("getStudentFromBatch", selectedBatch[0], adminidforbatch);
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (type.equals("insertteacher")) {
            String st = "";
            if (s.equals("success")) {
                st = "Teacher Account Created Successfully";
            } else {
                st = "EmailID Already Exists Use Another EmailID";
            }
            Toast.makeText(context, st, Toast.LENGTH_LONG).show();
        }else if (type.equals("sendnotification")) {

            Log.i("MyNotification","Sended Data:"+s);
        }
        else if (type.equals("adminlogin")){
            String st="";
           // ProgressDialog progressDialog=new ProgressDialog(context);
            progressDialog.dismiss();
            Intent intent=new Intent(context,AdminFunctions.class);
            if(!s.equals("failure"))
            {
                st+="Successfully Logged In";
                try {
                    JSONArray jsonArray = new JSONArray(s);
                    JSONObject jsonObject;

                    String fetchedstudent = "";
                    for (int i = 0; i < jsonArray.length(); i++) {
                        fetchedstudent = jsonArray.getString(i);
                        jsonObject = new JSONObject(fetchedstudent);
                        int classid=jsonObject.getInt("id");
                        String temp=String.valueOf(classid);
                        intent.putExtra("classid",temp);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                intent.putExtra("email",mainemail);
                context.startActivity(intent);
                ((Activity)context).finish();
            }
            else
            {
                st+="Invalid Credentials";
            }
            Toast.makeText(context,st,Toast.LENGTH_LONG).show();
        }
        else if (type.equals("getsubjectlist")) {
            try {
                JSONArray jsonArray = new JSONArray(s);
                JSONObject jsonObject;
                ArrayAdapter adapter;
                final ArrayList<String> subjectlist = new ArrayList();

                final String[] selectedSubject = new String[1];
                String fetchedsubject = "";
                for (int i = 0; i < jsonArray.length(); i++) {
                    fetchedsubject = jsonArray.getString(i);
                    jsonObject = new JSONObject(fetchedsubject);
                    subjectlist.add(jsonObject.getString("subject"));
                }
                adapter = new ArrayAdapter(context, android.R.layout.simple_list_item_1, subjectlist);
                Spinner spinner = ((Activity) context).findViewById(R.id.spinnerSubjectlist);
                spinner.setAdapter(adapter);
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        selectedSubject[0] =subjectlist.get(position);
                        TextView textView=((Activity)context).findViewById(R.id.tvspsubject);
                        textView.setText(selectedSubject[0]);
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else if (type.equals("getallteacherlist")) {
            try {
                JSONArray jsonArray = new JSONArray(s);
                JSONObject jsonObject;

                final ArrayList<String> allteacherlist = new ArrayList();
                final ArrayList<String> newlist = new ArrayList();
                final ArrayList<String> sublist = new ArrayList();

                final String[] selectedSubject = new String[1];
                final String[] selectedSub = new String[1];
                String fetchedsubject = "";
                for (int i = 0; i < jsonArray.length(); i++) {
                    fetchedsubject = jsonArray.getString(i);
                    jsonObject = new JSONObject(fetchedsubject);
                    String tname=jsonObject.getString("name");
                    String subj=jsonObject.getString("subject");
                    String comb=tname+"-"+subj;

                    sublist.add(subj);
                    allteacherlist.add(tname);
                    newlist.add(comb);
                }
                ArrayAdapter adapter = new ArrayAdapter(context, android.R.layout.simple_list_item_1, newlist);
                Spinner spinner = ((Activity) context).findViewById(R.id.spinnerBatchlist);
                spinner.setAdapter(adapter);
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        selectedSubject[0] =allteacherlist.get(position);
                        selectedSub[0]=sublist.get(position);
                        TextView textView=((Activity)context).findViewById(R.id.tvspbatch);
                        textView.setText(selectedSubject[0]);
                        TextView textView2=((Activity)context).findViewById(R.id.tvspsubject);
                        textView2.setText(selectedSub[0]);
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else if (type.equals("insertsubject")) {
            String st = "";
            if (s.equals("success")) {
                st = "Subject Added Successfully";
            } else {
                st = "Some error Occured";
            }
            Toast.makeText(context, st, Toast.LENGTH_LONG).show();
        } else if (type.equals("insertbatch")) {
            String st = "";
            if (s.equals("success")) {
                st = "Batch Added Successfully";
            } else {
                st = "Some error Occured";
            }
            Toast.makeText(context, st, Toast.LENGTH_LONG).show();
        } else if (type.equals("addsubtobatch")) {
            String st = "";
            if (s.equals("success")) {
                st = "Subject added to Batch Successfully";
            } else {
                st = "Some error Occured";
            }
            Toast.makeText(context, st, Toast.LENGTH_LONG).show();
        } else if (type.equals("insertlecture")) {
            String st = "";
            if (s.equals("success")) {
                st = "Lecture Added Successfully";
            } else {
                st = "Some error Occured";
            }
            Toast.makeText(context, st, Toast.LENGTH_LONG).show();
        }else if (type.equals("insertadmin")){
            String st="";
            if(s.equals("success"))
            {
                st+="Account Created successfully";
            }
            else
            {
                st+="Some error occured or Email is already existing";
            }
            Toast.makeText(context,st,Toast.LENGTH_LONG).show();
        }
        else if (type.equals("getSubjectFromBatch")) {
            try {
                JSONArray jsonArray = new JSONArray(s);
                JSONObject jsonObject;

                final ArrayList<String> subjectlist = new ArrayList<>();
                String fetchedsubject = "";
                for (int i = 0; i < jsonArray.length(); i++) {
                    fetchedsubject = jsonArray.getString(i);
                    jsonObject = new JSONObject(fetchedsubject);
                    subjectlist.add(jsonObject.getString("subject"));
                }
                ArrayAdapter adapter = new ArrayAdapter(context, android.R.layout.simple_list_item_1, subjectlist);
                Spinner spinner = ((Activity) context).findViewById(R.id.spinnerSubjectFrombatch);
                spinner.setAdapter(adapter);

                final String[] selectedSubject = new String[1];
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        selectedSubject[0] = subjectlist.get(position);
                        TextView textView=((Activity)context).findViewById(R.id.tvspsubject);
                        textView.setText(selectedSubject[0]);

                        if (dirsubject.equals("callteacher")) {
                            HttpServicesHelper helper2 = new HttpServicesHelper(context);
                            helper2.execute("getTeacherFromSubject", selectedSubject[0], adminidforsubject);
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (type.equals("getTeacherFromSubject")) {
            try {
                JSONArray jsonArray = new JSONArray(s);
                JSONObject jsonObject;
                final ArrayList<String> teacherlist = new ArrayList<>();
                String fetchedteacher = "";

                for (int i = 0; i < jsonArray.length(); i++) {
                    fetchedteacher = jsonArray.getString(i);
                    jsonObject = new JSONObject(fetchedteacher);
                    teacherlist.add(jsonObject.getString("name"));
                }
                ArrayAdapter adapter = new ArrayAdapter(context, android.R.layout.simple_list_item_1, teacherlist);
                Spinner spinner = ((Activity) context).findViewById(R.id.spinnerTeacherFromSubject);
                spinner.setAdapter(adapter);
                final String[] selectedTeacher = new String[1];
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        selectedTeacher[0] = teacherlist.get(position);
                        TextView textView=((Activity)context).findViewById(R.id.tvspteacher);
                        textView.setText(selectedTeacher[0]);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (type.equals("insertattendance")) {
            String st = "";
            if (s.equals("success")) {
                st = "Attendance Marked Successfully";
            } else {
                st = "Some error Occured";
            }
            Toast.makeText(context, st, Toast.LENGTH_LONG).show();
        } else if (type.equals("getStudentFromBatch")) {
            try {
                JSONArray jsonArray = new JSONArray(s);
                JSONObject jsonObject;

                final ArrayList<String> studentlist = new ArrayList();
                String fetchedstudent = "";
                for (int i = 0; i < jsonArray.length(); i++) {
                    fetchedstudent = jsonArray.getString(i);
                    jsonObject = new JSONObject(fetchedstudent);
                    studentlist.add(jsonObject.getString("name"));
                }
                ArrayAdapter adapter = new ArrayAdapter(context, android.R.layout.simple_list_item_1, studentlist);
                Spinner spinner = ((Activity) context).findViewById(R.id.spinnerStudentFromBatch);
                spinner.setAdapter(adapter);

                final String[] selectedStudent = new String[1];
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        selectedStudent[0] = (String) studentlist.get(position);
                        TextView textView=((Activity)context).findViewById(R.id.tvspstudent);
                        textView.setText(selectedStudent[0]);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (type.equals("insertnotification")) {
            String st = "";
            if (s.equals("success")) {
                st = "Notification Sent Successfully";
            } else {
                st = "Some error Occured";
            }
            Toast.makeText(context, st, Toast.LENGTH_LONG).show();
        } else if (type.equals("inserttest")) {
            String st = "";
            if (s.equals("success")) {
                st = "Test Added Successfully";
            } else {
                st = "Some error Occured";
            }
            Toast.makeText(context, st, Toast.LENGTH_LONG).show();
        } else if (type.equals("getStudentFromNameBatch")) {
            try {
                JSONArray jsonArray = new JSONArray(s);
                JSONObject jsonObject;
                for (int i = 0; i < jsonArray.length(); i++) {
                    String fetchedstudent = "";
                    fetchedstudent = jsonArray.getString(i);
                    jsonObject = new JSONObject(fetchedstudent);
                    String fetchedname = jsonObject.getString("name");
                    String fetchedcontact = jsonObject.getString("mobile");
                    String fetchedaddress = jsonObject.getString("address");
                    String fetchedpcontact = jsonObject.getString("pmobile");
                    String fetchedemail = jsonObject.getString("email");
                    String fetchedstatus = jsonObject.getString("status");
                    String fetcheddob = jsonObject.getString("dob");
                    String fetchedid = jsonObject.getString("id");

                    //TextView email1, address1, name1, contact1, status1, pContact1, dob1,id;

                    TextView email1 = ((Activity) context).findViewById(R.id.email);
                    TextView address1 =((Activity) context).findViewById(R.id.address);
                    TextView name1 =((Activity) context).findViewById(R.id.name);
                    TextView contact1 =((Activity) context).findViewById(R.id.contact);
                    TextView status1 =((Activity) context).findViewById(R.id.status);
                    TextView pContact1 =((Activity) context).findViewById(R.id.pContact);
                    TextView dob1 =((Activity) context).findViewById(R.id.dob);
                    TextView id=((Activity)context).findViewById(R.id.idvalue);

                    email1.setText(fetchedemail);
                    address1.setText(fetchedaddress);
                    name1.setText(fetchedname);
                    contact1.setText(fetchedcontact);
                    status1.setText(fetchedstatus);
                    pContact1.setText(fetchedpcontact);
                    dob1.setText(fetcheddob);
                    id.setText(fetchedid);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }else if (type.equals("getbatchattendance")) {
            try {
                JSONArray jsonArray = new JSONArray(s);
                JSONObject jsonObject;
                int ptotal=0,total=0;

                BatchAttendanceAdapter batchAttendanceAdapter;
                ListView listView;
                ArrayList<SingleBatchAtt> arrayList=new ArrayList<SingleBatchAtt>();
                listView=((Activity)context).findViewById(R.id.batchattList);
                Button button=((Activity)context).findViewById(R.id.btgetatt);
                listView.setVisibility(View.VISIBLE);

                for(int i=0;i<jsonArray.length();i++) {
                    String fetchedstudent = "";
                    fetchedstudent = jsonArray.getString(i);
                    jsonObject = new JSONObject(fetchedstudent);
                    String fetchedname = jsonObject.getString("name");
                    String fetchedattendance = jsonObject.getString("attendance");

                    total++;
                    if (fetchedattendance.equals("Present")){
                        ptotal++;
                    }
                    arrayList.add(new SingleBatchAtt(fetchedname,fetchedattendance));
                }
                if (arrayList.size()==0){
                    Toast.makeText(context,"No Attendance for Selected Batch",Toast.LENGTH_SHORT).show();
                    listView.setVisibility(View.INVISIBLE);
                }else{
                    batchAttendanceAdapter=new BatchAttendanceAdapter(context,arrayList);
                    listView.setAdapter(batchAttendanceAdapter);
                    Toast.makeText(context,"Present:"+String.valueOf(ptotal)+" Absent:"+String.valueOf(total-ptotal),Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else if (type.equals("getnotificationlist")) {
            try {
                JSONArray jsonArray = new JSONArray(s);
                JSONObject jsonObject;

                final ArrayList<notidetails> notidetailsArrayList = new ArrayList();
                String fetchedstudent = "";

                for (int i = 0; i < jsonArray.length(); i++) {
                    fetchedstudent = jsonArray.getString(i);
                    jsonObject = new JSONObject(fetchedstudent);
                    String fetchedtitle=jsonObject.getString("title");
                    String fetchedmessage=jsonObject.getString("message");
                    String fetcheddate=jsonObject.getString("date");
                    notidetailsArrayList.add(new notidetails(fetcheddate,fetchedtitle,fetchedmessage));
                }
                if (notidetailsArrayList.size()==0){
                    Toast.makeText(context,"No Notifications",Toast.LENGTH_SHORT).show();
                }else {
                    ListView listView = ((Activity) context).findViewById(R.id.mynotilist);
                    noticustomadapter customadapter = new noticustomadapter(context, notidetailsArrayList);
                    listView.setAdapter(customadapter);
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else if (type.equals("getlecturelist")) {
            try {
                JSONArray jsonArray = new JSONArray(s);
                JSONObject jsonObject;

                final ArrayList<LectureDetails> arrayList = new ArrayList();

                String fetchedstudent = "";
                lectureidlist=new ArrayList<>();
                for (int i = jsonArray.length()-1; i > -1; --i) {
                    fetchedstudent = jsonArray.getString(i);
                    jsonObject = new JSONObject(fetchedstudent);

                    String fetchedsub=jsonObject.getString("subject");
                    String fetchedteacher=jsonObject.getString("teacher");
                    String fetchedtime=jsonObject.getString("time");
                    String fetcheddate=jsonObject.getString("date");
                    String fetchedid=jsonObject.getString("id");

                    lectureidlist.add(fetchedid);
                    arrayList.add(new LectureDetails(fetchedsub,fetchedteacher,fetcheddate,fetchedtime));
                }
                if (arrayList.size()==0){
                    Toast.makeText(context,"No Lectures Scheduled Yet",Toast.LENGTH_SHORT).show();
                }else {
                    ListView listView = ((Activity) context).findViewById(R.id.mylectlist);
                    LectureCustomAdapter customadapter = new LectureCustomAdapter(context, arrayList);
                    listView.setAdapter(customadapter);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            if (gettype.equals("staff")){
                                String selectedid = lectureidlist.get(position);
                                final int mainID = Integer.valueOf(selectedid);

                                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                builder.setTitle("ALERT").setMessage("ARE YOU SURE DO YOU WANT TO DELETE?");
                                builder.setCancelable(false);
                                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        new HttpServicesHelper(context).execute("delete","lecture", String.valueOf(mainID));
                                        new HttpServicesHelper(context).execute("getlecturelist","staff",admindataforlecture,"nothing");;
                                        dialog.cancel();
                                    }
                                });
                                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                                AlertDialog alert=builder.create();
                                alert.show();

                            }
                        }
                    });
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else if (type.equals("gettestlist")) {
            try {
                JSONArray jsonArray = new JSONArray(s);
                JSONObject jsonObject;

                testidlist=new ArrayList<>();
                final ArrayList<TestDetails> arrayList = new ArrayList();
                String fetchedstudent = "";
                for (int i = jsonArray.length()-1; i >=0; --i) {
                    fetchedstudent = jsonArray.getString(i);
                    jsonObject = new JSONObject(fetchedstudent);
                    String fetchedsub=jsonObject.getString("subject");
                    String fetchedtopic=jsonObject.getString("topic");
                    String fetchedmarks=jsonObject.getString("marks");
                    String fetchedtime=jsonObject.getString("time");
                    String fetcheddate=jsonObject.getString("date");
                    String fetchedid=jsonObject.getString("id");

                    testidlist.add(fetchedid);

                    arrayList.add(new TestDetails(fetchedsub,fetchedtopic,fetchedmarks,fetcheddate,fetchedtime));
                }if (arrayList.size()==0){
                    Toast.makeText(context,"No Test Scheduled Yet",Toast.LENGTH_SHORT).show();
                }else {
                    ListView listView = ((Activity) context).findViewById(R.id.mytestlist);
                    TestCustomAdapter customadapter = new TestCustomAdapter(context, arrayList);
                    listView.setAdapter(customadapter);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            if (typetest.equals("staff")){
                                String selectedid = testidlist.get(position);
                                final int mainID = Integer.valueOf(selectedid);

                                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                builder.setTitle("ALERT").setMessage("ARE YOU SURE DO YOU WANT TO DELETE?");
                                builder.setCancelable(false);
                                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        new HttpServicesHelper(context).execute("delete","test", String.valueOf(mainID));
                                        new HttpServicesHelper(context).execute("gettestlist",admindatafortest,"nothing");
                                        dialog.cancel();
                                    }
                                });
                                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                                AlertDialog alert=builder.create();
                                alert.show();

                            }
                        }
                    });

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
