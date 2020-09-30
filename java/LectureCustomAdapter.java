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

public class LectureCustomAdapter implements ListAdapter {

    Context context;
    ArrayList<LectureDetails> arrayList;

    public LectureCustomAdapter(Context c, ArrayList<LectureDetails> details){
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
        LectureDetails details=arrayList.get(position);

        if(convertView==null){
            LayoutInflater inflater=LayoutInflater.from(context);
            convertView=inflater.inflate(R.layout.mylectlayout,null);
            TextView subvalue=convertView.findViewById(R.id.subject);
            subvalue.setText(details.sub);
            TextView teachervalue=convertView.findViewById(R.id.teacher);
            teachervalue.setText(details.teacher);
            TextView datevalue=convertView.findViewById(R.id.date);
            datevalue.setText(details.date);
            datevalue.setBackgroundColor(Color.rgb(0,255,0));
            TextView timingvalue=convertView.findViewById(R.id.timing);
            timingvalue.setText(details.timing);
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
