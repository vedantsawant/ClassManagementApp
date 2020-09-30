package in.autodice.classmanagementsystem;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

public class DoubtList extends AppCompatActivity {

    ListView mylist;
    ArrayList<notidetails> notidetailsArrayList;
    ArrayList<String> idlist;
    SwipeRefreshLayout swipeRefreshLayout;
    String ADMINID,sub,trname,type,baseURL;

    URL url;
    HttpURLConnection connection;
    OutputStream outputStream;
    OutputStreamWriter outputStreamWriter;
    BufferedWriter bufferedWriter;

    InputStream inputStream;
    InputStreamReader inputStreamReader;
    BufferedReader bufferedReader;
    String passkey="ydcYkdJhhArwijdoPslK";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doubt_list);

        Bundle extras = getIntent().getExtras();
        ADMINID= extras.getString("classid");
        trname=extras.getString("myname");
        sub=extras.getString("mysubject");

        mylist=findViewById(R.id.doubtlist);
        swipeRefreshLayout=findViewById(R.id.mynotiswipe);

        getSupportActionBar().setTitle("Doubts");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);   //show back button

        new DoubtlistHttp().execute("getdoubtlist",sub,trname,ADMINID);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new DoubtlistHttp().execute("getdoubtlist",sub,trname,ADMINID);
                Toast.makeText(getBaseContext(),"Refreshing", Toast.LENGTH_SHORT).show();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        registerForContextMenu(mylist);

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.cm_doubtlist,menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info= (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int pos=info.position;
        String selectedid=idlist.get(pos);
        notidetails details=notidetailsArrayList.get(pos);
        String emailgot=details.date;
        String dd=details.content;

        final int mainID=Integer.valueOf(selectedid);
        if(item.getTitle().equals("Marks As Solved")){
            AlertDialog.Builder builder = new AlertDialog.Builder(DoubtList.this);
            builder.setTitle("ALERT").setMessage("ARE YOU SURE DO YOU WANT TO MARK AS SOLVED");
            builder.setCancelable(false);
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    new DoubtlistHttp().execute("updatestatus",String.valueOf(mainID));
                    new DoubtlistHttp().execute("getdoubtlist",sub,trname,ADMINID);
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

        }else  if(item.getTitle().equals("Reply")){
            Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + emailgot));
            intent.putExtra(Intent.EXTRA_SUBJECT, sub+" Doubt's Solution");
            intent.putExtra(Intent.EXTRA_TEXT, dd+"\nSolution:\n");
            startActivity(intent);
        }
        return super.onContextItemSelected(item);
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

    public class DoubtlistHttp extends AsyncTask<String,String,String> {

        @Override
        protected String doInBackground(String... strings) {

            type = strings[0];
            baseURL = "https://autodice.in/projects/classmanagement/";

            if (type.equals("getdoubtlist")) {
                try {
                    String sub=strings[1];
                    String teacher=strings[2];
                    String adminid=strings[3];

                    String postmydata;
                    url = new URL(baseURL +"fetchdoubtlist.php");

                    postmydata = URLEncoder.encode("adminid", "UTF-8") + "=" + URLEncoder.encode(adminid, "UTF-8");
                    postmydata += "&" + URLEncoder.encode("teacher", "UTF-8") + "=" + URLEncoder.encode(teacher, "UTF-8");
                    postmydata += "&" + URLEncoder.encode("subject", "UTF-8") + "=" + URLEncoder.encode(sub, "UTF-8");
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
            }else if (type.equals("updatestatus")) {
                String myid = strings[1];

                try {
                    url = new URL(baseURL + "deletedoubt.php");
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

            if (type.equals("getdoubtlist")) {
                try {
                    JSONArray jsonArray = new JSONArray(s);
                    JSONObject jsonObject;

                    notidetailsArrayList = new ArrayList();
                    idlist=new ArrayList<>();
                    String fetchedstudent = "";

                    for (int i = 0; i < jsonArray.length(); i++) {
                        fetchedstudent = jsonArray.getString(i);
                        jsonObject = new JSONObject(fetchedstudent);
                        String fetchedtitle=jsonObject.getString("name");
                        String fetchedmessage=jsonObject.getString("doubt");
                        String fetcheddate=jsonObject.getString("email");
                        String fetchedid=jsonObject.getString("id");

                        idlist.add(fetchedid);
                        notidetailsArrayList.add(new notidetails(fetcheddate,fetchedtitle,fetchedmessage));
                    }
                    if (notidetailsArrayList.size()==0){
                        Toast.makeText(DoubtList.this,"No Doubts",Toast.LENGTH_SHORT).show();
                    }else {
                        noticustomadapter customadapter = new noticustomadapter(DoubtList.this, notidetailsArrayList);
                        mylist.setAdapter(customadapter);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else if (type.equals("updatestatus")) {
                String st = "";
                if (s.equals("success")) {
                    st = "Marked As Inactive";
                } else {
                    st = "Some error Occured";
                }
                Toast.makeText(DoubtList.this, st, Toast.LENGTH_LONG).show();
            }
        }
    }

}
