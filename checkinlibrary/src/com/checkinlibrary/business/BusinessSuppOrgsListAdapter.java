package com.checkinlibrary.business;

import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.checkinlibrary.R;
import com.checkinlibrary.models.OrganizationResult;


public class BusinessSuppOrgsListAdapter extends BaseAdapter {
    Context context;
    List<OrganizationResult> mCommonCauses;

    public BusinessSuppOrgsListAdapter(Context context,  List<OrganizationResult>  mCommonCauses) {
        this.context = context;
        this.mCommonCauses = mCommonCauses;
    }

    @Override
    public int getCount() {
        if (mCommonCauses != null)
            return mCommonCauses.size();
        else
            return 0;
    }

    @Override
    public Object getItem(int index) {
        return mCommonCauses.get(index);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //View v = inflater.inflate(R.layout.business_supp_orgs_list_item, parent, false);
        
        RelativeLayout v = (RelativeLayout) inflater.inflate(R.layout.business_supp_orgs_list_item, parent, false);
        TextView tv = (TextView) v.findViewById(R.id.textViewOrgName);
        OrganizationResult result=(OrganizationResult)getItem(position);
        tv.setText(result.getOrganization().getName());
        
        return v;
    }

    public void gotoCheckinPage(View v) {
        OrganizationResult result=(OrganizationResult)getItem(v.getId());
        
        Log.e("BusinessSuppOrgsListAdapter",result.getOrganization().getName());
    }  
}
