package com.campaignslibrary;

import java.util.List;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.campaignslibrary.models.CreateCampaignResult;
import com.checkinlibrary.CheckinLibraryActivity;

public class CampaignsSearchFragment extends MyCampaignFragment {
	
    protected String mQuery;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = (CheckinLibraryActivity)this.getActivity();
        View v = inflater.inflate(R.layout.my_cmpgn_fragment, container, false);         
        initAllComponents(v);
        return v;
    }
    
    @Override
    // Set up our tabs
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
        
    @Override
    public void onResume() {
        super.onResume();
//        TextView tv = (TextView)context.findViewById(R.id.textViewBusinessSearchQuery);
//        tv.setText(mQuery);
    }
    
    /**
	 * this method initialise UI components
	 */
	private void initAllComponents(View v) {
		myCmpgnList = (ListView) v.findViewById(android.R.id.list);

		MyCmpgnListAdapter mMyCmpgnListAdapter = new MyCmpgnListAdapter(
				context, mMyCampgnList,true);
		myCmpgnList.setAdapter(mMyCmpgnListAdapter);
	}

    public void setQuery(String query) {
        mQuery = query;
    }
    
    public static void addCmpgnSearchFragment(CheckinLibraryActivity context, List<CreateCampaignResult> result, String query) {
        Log.v("CHECKINFORGOOD", "Adding business search fragment");
      
        CampaignsSearchFragment mCampaignsSearchFragment = (CampaignsSearchFragment) (context).addFragment(R.id.linearLayout2,
                CampaignsSearchFragment.class.getName(), "cmpgn_search_list");
        mCampaignsSearchFragment.setList((List<CreateCampaignResult>)result);
        mCampaignsSearchFragment.setQuery(query);
    }
    
    public static void addFilterCampaignsResult( CheckinLibraryActivity context, List<CreateCampaignResult> result){
    	context.removeDialog(0);
    	CampaignsSearchFragment mCampaignsSearchFragment = (CampaignsSearchFragment) (context).addFragment(R.id.linearLayout2,
                CampaignsSearchFragment.class.getName(), "cmpgn_search_list");
        mCampaignsSearchFragment.setList(result);
    }
}