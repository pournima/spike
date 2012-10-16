package com.checkinlibrary.org;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;



public class OrganizationListAdapter extends BaseAdapter {
    Context context;
    ArrayList<String> causesTypes;
        
    public OrganizationListAdapter(Context context, ArrayList<String>  causesTypes) {
        this.context = context;
        this.causesTypes = causesTypes;
    }

    @Override
    public int getCount() {
        if (causesTypes != null)
            return causesTypes.size();
        else
            return 0;
    }

    @Override
    public Object getItem(int index) {
        return causesTypes.get(index);
    }

    @Override
    public View getView(int index, View view, ViewGroup viewgroup) {
        OrganizationListLayout layout;
     
        if (view == null)
            layout = new OrganizationListLayout(context, causesTypes.get(index),index);
        else {
            layout = (OrganizationListLayout) view;
            layout.setName(causesTypes.get(index));
            layout.setFlagResource(index);
        }
        return layout;
    }

    
    
    @Override
    public long getItemId(int index) {
        return index;
    }

}