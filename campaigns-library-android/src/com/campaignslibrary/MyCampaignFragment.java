package com.campaignslibrary;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.campaignslibrary.dbAdapters.MyCampaignsDbAdapter;
import com.campaignslibrary.models.CreateCampaignResult;
import com.campaignslibrary.tasks.MyCampaignTask;
import com.checkinlibrary.CheckinLibraryActivity;
import com.checkinlibrary.NoConnectivityScreen;
import com.checkinlibrary.helpers.AppStatus;

public class MyCampaignFragment extends ListFragment {
	CheckinLibraryActivity context;
	public ListView myCmpgnList;
    public List<CreateCampaignResult> mMyCampgnList = null;
    final String TAG=getClass().getName();
    
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		context = (CheckinLibraryActivity) this.getActivity();
		
		View fragmentView = inflater.inflate(R.layout.my_cmpgn_fragment, null);
		initAllComponents(fragmentView);
		
		if(fetchMyCmpgnsFromDB()){
			initializeList();
		}else{
            fetchMyCmpgnsFromApi();
		}
		return fragmentView;
	}
	
	/**
	 * this method initialise UI components
	 */
	private void initAllComponents(View v) {
		myCmpgnList = (ListView) v.findViewById(android.R.id.list);
	}
	
	/**
	 * this method initialise list
	 */
	private void initializeList() {
		MyCmpgnListAdapter mMyCmpgnListAdapter = new MyCmpgnListAdapter(
				context, mMyCampgnList,false);
		myCmpgnList.setAdapter(mMyCmpgnListAdapter);
	}
	
	
	/**
	 * this method fetch data from DB
	 * @return  Boolean  returns true if data exists in DB or false
	 */
	private Boolean fetchMyCmpgnsFromDB() {
		MyCampaignsDbAdapter adpt = new MyCampaignsDbAdapter(context);
		mMyCampgnList=adpt.getList();
		if(mMyCampgnList != null && mMyCampgnList.size() > 0){
			return true;
		}else
			return false;
	}
	
	public void setList(List<CreateCampaignResult> mList) {
		mMyCampgnList = mList;
	}
	
	private void fetchMyCmpgnsFromApi() {
		String[] args = new String[1];
        AppStatus appStatus=AppStatus.getInstance(context);
		args[0] = appStatus.getSharedStringValue(appStatus.AUTH_KEY); 
		if(appStatus.isOnline()) {
			CheckinLibraryActivity.strProgressMessage = getString(R.string.myCampgnListMessage);
			context.showDialog(0);
			new MyCampaignTask(this).execute(args);
		} else {
			Log.v(TAG, "App is not online!");
			Intent intent= new Intent(context,NoConnectivityScreen.class);
			context.startActivity(intent);
			context.finish();
		}
		
	}

	public void getCampaignResult(List<CreateCampaignResult> result) {
		context.removeDialog(0);
		if(result != null && result.size() > 0){
			mMyCampgnList=result;
			initializeList();
			storeMyCampaignsInDB(result);
		}
	}

	private void storeMyCampaignsInDB(List<CreateCampaignResult> result) {
		MyCampaignsDbAdapter adpt = new MyCampaignsDbAdapter(context);
	    CampaignsPhotoClass	mCampaignsPhotoClass=new CampaignsPhotoClass(context);
		try {
			adpt.beginTransaction();
			for(int i=0;i<result.size();i++){
				Log.i(TAG, "Id for camp res " + result.get(i).getCampaign().getName()+
						String.valueOf(result.get(i).getCampaign().getId()));
				adpt.createAssociated(result.get(i));
				mCampaignsPhotoClass.storeCampaignPhotos(result.get(i));
			}
			adpt.succeedTransaction();
		}catch(Exception e){
			e.printStackTrace();
		} finally {
			adpt.endTransaction();                
		}  	
	}
}
