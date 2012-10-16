package com.checkinlibrary.business;

import java.util.List;

import com.checkinlibrary.CheckinLibraryActivity;
import com.checkinlibrary.R;
import com.checkinlibrary.models.BusinessResult;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class BusinessSearchFragment extends BusinessListFragment {
    List<BusinessResult> mList;
    private String mQuery;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = (CheckinLibraryActivity)this.getActivity();
        return inflater.inflate(R.layout.business_search_fragment, container, false);    
    }
    
    @Override
    // Set up our tabs
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
        
    @Override
    public void onResume() {
        super.onResume();
        TextView tv = (TextView)context.findViewById(R.id.textViewBusinessSearchQuery);
        tv.setText(mQuery);
    }

    public void setQuery(String query) {
        mQuery = query;
    }
    
    public static void addBusinessFragment(CheckinLibraryActivity context, List<BusinessResult> result, String query) {
        Log.v("CHECKINFORGOOD", "Adding business search fragment");
      
        BusinessSearchFragment mBusinessSearchFragment = (BusinessSearchFragment) (context).addFragment(R.id.linearLayout2,
                BusinessSearchFragment.class.getName(), "business_search_list");
        mBusinessSearchFragment.setList((List<BusinessResult>)result,false,false);
        mBusinessSearchFragment.setQuery(query);
    }
}