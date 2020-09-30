package in.autodice.classmanagementsystem;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.TextView;

import in.autodice.classmanagementsystem.R;

import java.util.ArrayList;

public class noticustomadapter implements ListAdapter {

    Context context;
    ArrayList<notidetails> arrayList;

    public noticustomadapter(Context c, ArrayList<notidetails> details){
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
        notidetails details=arrayList.get(position);

        if(convertView==null){
            LayoutInflater inflater=LayoutInflater.from(context);
            convertView=inflater.inflate(R.layout.mynotilayout,null);
            TextView subvalue=convertView.findViewById(R.id.date);
            subvalue.setText(details.date);
            TextView teachervalue=convertView.findViewById(R.id.title);
            teachervalue.setText(details.title);
            TextView marksvalue=convertView.findViewById(R.id.content);
            marksvalue.setText(details.content);
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
