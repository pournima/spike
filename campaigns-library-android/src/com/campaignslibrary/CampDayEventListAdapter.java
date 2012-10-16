package com.campaignslibrary;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class CampDayEventListAdapter extends BaseAdapter {
    Context context;
    String[] events;

    public CampDayEventListAdapter(Context context, String[] evts) {
        this.context = context;
        this.events = evts;
    }

    @Override
    public int getCount() {
        if (events != null)
            return events.length;
        else
            return 0;
    }

    @Override
    public Object getItem(int index) {
        return events[index];
    }

    @Override
    public View getView(int index, View view, ViewGroup viewgroup) {

        LayoutInflater inflater = (LayoutInflater) context
        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        
        RelativeLayout v = (RelativeLayout) inflater.inflate(R.layout.camp_day_event_list_item, viewgroup, false);
        initializeAllObjects(v,index);
        return v;
    }

    @Override
    public long getItemId(int index) {
        return index;
    }
    
    private void initializeAllObjects(RelativeLayout v,int iIndex) {
        TextView textViewName=(TextView)v.findViewById(R.id.campDayEText);
        textViewName.setText(events[iIndex]);
    }
}