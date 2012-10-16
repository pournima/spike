package com.campaignslibrary;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.campaignslibrary.dbAdapters.CauseCategoryDBAdapter;
import com.campaignslibrary.models.CauseCategoryResult;
import com.checkinlibrary.CheckinLibraryActivity;
import com.checkinlibrary.db.MyOrganizationDBAdapter;
import com.checkinlibrary.models.OrganizationResult;
import com.checkinlibrary.org.OrganizationCacheManager;

public class CampgnCauseListFragment extends ListFragment  {
	List<OrganizationResult> mList;
	
	OrganizationCacheManager cacheManager;
	CheckinLibraryActivity context;
	private TextView textViewTopMsg;
	ListView mListView;
	public Boolean bIsFromCampgnCat = false;
	final String TAG=getClass().getName();
	
	private List<OrganizationResult> myCauses;
	private List<CauseCategoryResult> myCausesCat;
	
	public OrganizationResult mSelectedCauseResult;
	public CauseCategoryResult mSelectedCauseCatResult;
	
	public Boolean bIsItemClick=false;
    

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		context = (CheckinLibraryActivity) this.getActivity();
		View fragmentView = inflater.inflate(R.layout.cause_fragment,
				container, false);
		
		initialization(fragmentView);
		bIsFromCampgnCat = this.getArguments().getBoolean("campgn_list_type");
		bIsItemClick=false;
		
		return fragmentView;
	}

	/**
	 * This method initialises ui components
	 * @param v View for given fragment
	 */
	private void initialization(View v) {
		textViewTopMsg = (TextView) v.findViewById(R.id.textViewCausesTopMsg);
		mListView = (ListView) v.findViewById(android.R.id.list);
		mListView.setVisibility(ListView.VISIBLE);
	}

	@Override
	public void onResume() {
		super.onResume();
		
		bIsFromCampgnCat = this.getArguments().getBoolean("campgn_list_type");
		initializeList();	
	}

	/**
	 * This method initialises listview with list of data(campaign categories or cause list)
	 */
	private void initializeList(){
		if(bIsFromCampgnCat){
			textViewTopMsg.setText(getString(R.string.top_msg_campgn_cat_list));
			myCausesCat=fetchCauseCategoryListFromDB();
			if(myCausesCat != null){
				String[] strList=new String[myCausesCat.size()];
				
				for(int i=0;i<myCausesCat.size();i++){
					strList[i]=myCausesCat.get(i).getCategory().getName();
				}
				CampaignCauseListAdapter adapter = new CampaignCauseListAdapter(context, strList);
				this.setListAdapter((ListAdapter) adapter);
			}
			
		}else{ // from my cause list
			textViewTopMsg.setText(getString(R.string.top_msg_campgn_cause_list));
			
			myCauses=fetchMyCausesFromDB();
			if(myCauses != null){
				String[] strList=new String[myCauses.size()];
				
				for(int i=0;i<myCauses.size();i++){
					strList[i]=myCauses.get(i).getOrganization().getName();
				}
				CampaignCauseListAdapter adapter = new CampaignCauseListAdapter(context, strList);
				this.setListAdapter((ListAdapter) adapter);
			}
		}	
	}
	
	/**
	 * This method gives list of my causes
	 * @return myCauses  my causes list
	 */
	private List<OrganizationResult> fetchMyCausesFromDB() {
		MyOrganizationDBAdapter adpt = new MyOrganizationDBAdapter(context);
		List<OrganizationResult> myCauses = new ArrayList<OrganizationResult>();
		myCauses = adpt.getList();
		return myCauses;
	}
	
	/**
	 * This method gives list of  causes category
	 * @return myCauses  my causes list
	 */
	private List<CauseCategoryResult> fetchCauseCategoryListFromDB() {
		CauseCategoryDBAdapter adpt = new CauseCategoryDBAdapter(context);
		List<CauseCategoryResult> mCauseCategoryResults = new ArrayList<CauseCategoryResult>();
		mCauseCategoryResults = adpt.getList();
		return mCauseCategoryResults;
	}
	
	
	 @Override
	    public void onListItemClick(ListView l, View v, int position, long id) {
	        super.onListItemClick(l, v, position, id);
	        if(bIsFromCampgnCat){
	        	mSelectedCauseCatResult=myCausesCat.get(position);
	        }else{
	        	mSelectedCauseResult=myCauses.get(position);
	        }
			bIsItemClick=true;
		
	        context.popFragmentFromStack();
	    }
}
