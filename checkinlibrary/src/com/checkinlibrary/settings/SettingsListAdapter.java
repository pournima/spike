package com.checkinlibrary.settings;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;


public class SettingsListAdapter extends BaseAdapter {
    Context context;
    ArrayList<String> settingsList;
    
    public SettingsListAdapter(Context context, ArrayList<String>  settingsList) {
        this.context = context;
        this.settingsList = settingsList;
    }

    @Override
    public int getCount() {
        if (settingsList != null)
            return settingsList.size();
        else
            return 0;
    }

    @Override
    public Object getItem(int index) {
        return settingsList.get(index);
    }

    @Override
    public View getView(int index, View view, ViewGroup viewgroup) {
        SettingsListLayout layout;
     
        if (view == null)
            layout = new SettingsListLayout(context, settingsList.get(index),index,settingsList.size()-1);
        else {
            layout = (SettingsListLayout) view;
            layout.setName(settingsList.get(index));
        }
        return layout;
    }

    
    
    @Override
    public long getItemId(int index) {
        return index;
    }

}