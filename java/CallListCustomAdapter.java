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

public class CallListCustomAdapter implements ListAdapter {

    Context context;
    ArrayList<singleCallDetails> arrayList;

    public CallListCustomAdapter(Context context,ArrayList<singleCallDetails> arrayList){
        this.context=context;
        this.arrayList=arrayList;
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
        singleCallDetails CallDetails=arrayList.get(position);
        if(convertView==null){

            LayoutInflater inflater=LayoutInflater.from(context);
            convertView=inflater.inflate(R.layout.singlecall,null);

            TextView callername=convertView.findViewById(R.id.callname);
            callername.setText(CallDetails.callername);

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
