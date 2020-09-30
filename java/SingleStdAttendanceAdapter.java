package in.autodice.classmanagementsystem;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.TextView;

import in.autodice.classmanagementsystem.R;

import java.util.ArrayList;

public class SingleStdAttendanceAdapter implements ListAdapter {
    Context context;
    ArrayList<SingleStudentAttendance> arrayList;

    public SingleStdAttendanceAdapter(Context c, ArrayList<SingleStudentAttendance> details){
        this.context=c;
        this.arrayList=details;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return true;
    }

    @Override
    public boolean isEnabled(int position) {
        return true;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SingleStudentAttendance singleStudentAttendance=arrayList.get(position);
        if(convertView==null){
            LayoutInflater inflater=LayoutInflater.from(context);
            convertView=inflater.inflate(R.layout.singlerowstdattendance,null);
            TextView datepart=convertView.findViewById(R.id.tvdate);
            datepart.setText(singleStudentAttendance.date);
            TextView subpart=convertView.findViewById(R.id.tvsubname);
            subpart.setText(singleStudentAttendance.subjectname);
            TextView statuspart=convertView.findViewById(R.id.tvstatus);
            statuspart.setText(singleStudentAttendance.status);
            if (singleStudentAttendance.status.equals("Present")){
                statuspart.setBackgroundColor(Color.rgb(0,255,0));
            }else if (singleStudentAttendance.status.equals("Absent")){
                statuspart.setBackgroundColor(Color.rgb(255,0,0));
            }
        }

        return convertView;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return arrayList.size();
    }

    @Override
    public boolean isEmpty() {
        return false;
    }
}
