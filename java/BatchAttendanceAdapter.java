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

public class BatchAttendanceAdapter implements ListAdapter {
    Context context;
    ArrayList<SingleBatchAtt> arrayList;

    public BatchAttendanceAdapter(Context c, ArrayList<SingleBatchAtt> details){
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
        SingleBatchAtt batchAtt=arrayList.get(position);
        if(convertView==null){
            LayoutInflater inflater=LayoutInflater.from(context);
            convertView=inflater.inflate(R.layout.singlerowbatchatt,null);

            TextView namepart=convertView.findViewById(R.id.tvstdname);
            namepart.setText(batchAtt.name);

            TextView statuspart=convertView.findViewById(R.id.tvbtstatus);
            statuspart.setText(batchAtt.status);
            if (batchAtt.status.equals("Present")){
                statuspart.setBackgroundColor(Color.rgb(0,255,0));
            }else if (batchAtt.status.equals("Absent")){
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
