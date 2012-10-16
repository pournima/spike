package com.checkinlibrary.business;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.checkinlibrary.R;
import com.checkinlibrary.models.BusinessResult;

public class BusinessListAdapter extends BaseAdapter {
    Context context;
    List<BusinessResult> businesses;

    public BusinessListAdapter(Context context, List<BusinessResult> businesses) {
        this.context = context;
        this.businesses = businesses;
    }

    @Override
    public int getCount() {
        if (businesses != null)
            return businesses.size();
        else
            return 0;
    }

    @Override
    public Object getItem(int index) {
        return businesses.get(index);
    }

    @Override
    public View getView(int index, View view, ViewGroup viewgroup) {

        LayoutInflater inflater = (LayoutInflater) context
        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        RelativeLayout v = (RelativeLayout) inflater.inflate(R.layout.business_list_item, viewgroup, false);
        initializeAllObjects(v,index);
        return v;
    }

    @Override
    public long getItemId(int index) {
        return index;
    }
    
    private void initializeAllObjects(RelativeLayout v,int iIndex) {
        TextView textViewName=(TextView)v.findViewById(R.id.businessName);
        TextView textViewAddress=(TextView)v.findViewById(R.id.businessAddress);
        TextView textViewDistance=(TextView)v.findViewById(R.id.businessDistance);
        
        BusinessResult mBusinessResult=businesses.get(iIndex);
        textViewName.setText(mBusinessResult.getName());
        textViewAddress.setText(mBusinessResult.getAddress());

        BigDecimal distance = new BigDecimal(mBusinessResult.getDistance()).setScale(2, BigDecimal.ROUND_CEILING);
        distance = new BigDecimal(distance.floatValue()/1.609344f);  //Convert from km to miles
        String strDst=new DecimalFormat("#.#").format(distance);    
        textViewDistance.setText(strDst);
    }
}