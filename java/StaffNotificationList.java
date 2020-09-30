package in.autodice.classmanagementsystem;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
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

public class StaffNotificationList extends AppCompatActivity {

    ListView mylist;
    ArrayList<notidetails> arrayList;
    SwipeRefreshLayout swipeRefreshLayout;
    String ADMINID,typeoflist,datafetched,type,baseURL,fetchedsub;

    ArrayList<notidetails> notidetailsArrayList;
    ArrayList<String> idlist;
    URL url;
    HttpURLConnection connection;
    OutputStream outputStream;
    OutputStreamWriter outputStreamWriter;
    BufferedWriter bufferedWriter;
    String passkey="ydcYkdJhhArwijdoPslK";

    InputStream inputStream;
    InputStreamReader inputStreamReader;
    BufferedReader bufferedReader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_notification_list);

        Bundle extras = getIntent().getExtras();
        ADMINID= extras.getString("classid");
        typeoflist= extras.getString("typelist");
        datafetched= extras.getString("data");
        fetchedsub= extras.getString("mysubject");

        mylist=findViewById(R.id.mynotilist);
        swipeRefreshLayout=findViewById(R.id.mynotiswipe);

        getSupportActionBar().setTitle("Notification List");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);   //show back button

        if (!typeoflist.equals("staff")) {

            HttpServicesHelper helper = new HttpServicesHelper(this);
            helper.execute("getnotificationlist", typeoflist, datafetched, ADMINID,fetchedsub);

        }else if (typeoflist.equals("staff")){
            new NotificationHttp().execute("getstaffnotificationlist",ADMINID);
            Log.i("nolist","data");
        }

            mylist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (typeoflist.equals("staff")) {
                        String selectedid = idlist.get(position);
                        final int mainID = Integer.valueOf(selectedid);

                        AlertDialog.Builder builder = new AlertDialog.Builder(StaffNotificationList.this);
                        builder.setTitle("ALERT").setMessage("ARE YOU SURE DO YOU WANT TO DELETE?");
                        builder.setCancelable(false);
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                new NotificationHttp().execute("delete", String.valueOf(mainID));
                                new NotificationHttp().execute("getstaffnotificationlist", ADMINID);
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

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (!typeoflist.equals("staff")){
                    new HttpServicesHelper(StaffNotificationList.this).execute("getnotificationlist", typeoflist, datafetched, ADMINID,fetchedsub);
                    Toast.makeText(getBaseContext(), "Refreshing", Toast.LENGTH_SHORT).show();
                    swipeRefreshLayout.setRefreshing(false);
                }else if (typeoflist.equals("staff")){
                    new NotificationHttp().execute("getstaffnotifiaction",ADMINID);
                    Toast.makeText(getBaseContext(), "Refreshing", Toast.LENGTH_SHORT).show();
                    swipeRefreshLayout.setRefreshing(false);
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

    public class NotificationHttp extends AsyncTask<String,String,String> {

        @Override
        protected String doInBackground(String... strings) {

            type = strings[0];
            baseURL = "https://autodice.in/projects/classmanagement/";

            if (type.equals("getstaffnotificationlist")) {
                try {
                    String adminid=strings[1];

                    String postmydata;
                    url = new URL(baseURL +"fetchnotificationlist.php");

                    postmydata = URLEncoder.encode("adminid", "UTF-8") + "=" + URLEncoder.encode(adminid, "UTF-8");
                    postmydata += "&" + URLEncoder.encode("passkey", "UTF-8") + "=" + URLEncoder.encode(passkey, "UTF-8");

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
            }else if (type.equals("delete")) {
                String myid = strings[1];

                try {
                    url = new URL(baseURL + "deletenotification.php");
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

            if (type.equals("getstaffnotificationlist")) {
                try {
                    JSONArray jsonArray = new JSONArray(s);
                    JSONObject jsonObject;

                    idlist=new ArrayList<>();
                    notidetailsArrayList = new ArrayList();
                    String fetchedstudent = "";

                    for (int i = jsonArray.length()-1; i >=0; --i) {
                        fetchedstudent = jsonArray.getString(i);
                        jsonObject = new JSONObject(fetchedstudent);
                        String fetchedtitle=jsonObject.getString("title");
                        String fetchedmessage=jsonObject.getString("message");
                        String fetcheddate=jsonObject.getString("date");
                        String fetchedid=jsonObject.getString("id");
                        String batch=jsonObject.getString("batch");

                        fetcheddate+="  "+"Send to:"+batch;

                        idlist.add(fetchedid);
                        notidetailsArrayList.add(new notidetails(fetcheddate,fetchedtitle,fetchedmessage));
                    }
                    if (notidetailsArrayList.size()==0){
                        Toast.makeText(StaffNotificationList.this,"No Notifications",Toast.LENGTH_SHORT).show();
                    }else {
                        ListView listView = findViewById(R.id.mynotilist);
                        noticustomadapter customadapter = new noticustomadapter(StaffNotificationList.this, notidetailsArrayList);
                        listView.setAdapter(customadapter);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else if (type.equals("delete")) {
                String st = "";
                if (s.equals("success")) {
                    st = "Notification Deleted";
                } else {
                    st = "Some error Occured";
                }
                Toast.makeText(StaffNotificationList.this, st, Toast.LENGTH_LONG).show();
            }
        }
    }

}
