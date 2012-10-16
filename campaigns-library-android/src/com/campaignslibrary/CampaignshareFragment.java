package com.campaignslibrary;

import java.util.ArrayList;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListAdapter;

import com.campaignslibrary.dbAdapters.MyCampaignsDbAdapter;
import com.campaignslibrary.helpers.Constants;
import com.campaignslibrary.models.CreateCampaignResult;
import com.campaignslibrary.models.LaunchCampaignResult;
import com.campaignslibrary.tasks.LaunchCampaignTask;
import com.checkinlibrary.CheckinLibraryActivity;
import com.checkinlibrary.NoConnectivityScreen;
import com.checkinlibrary.helpers.AppStatus;
import com.checkinlibrary.settings.ShareListAdapter;


public class CampaignshareFragment extends ListFragment {

	CheckinLibraryActivity context;
	int iCampaignId;
	final String TAG=getClass().getName();
	CampaignCongratsFragment mCampaignCongratsFragment=null;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		context = (CheckinLibraryActivity) this.getActivity();
		View v = inflater.inflate(R.layout.campaign_share_fragment, container, false);
        iCampaignId=this.getArguments().getInt("campgn_id");
        Button btnStartCamp=(Button)v.findViewById(R.id.btnStartCampgnShare);
        
        btnStartCamp.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				goToLaunchCampaign(iCampaignId);
				//goToCongratsCampaignFragment();
			}
		});
		return v;

	}

	@Override
	public void onResume() {
		super.onResume();

		Log.e("CHECKINFORGOOD", "Setting the list adapters ");

		ArrayList<String> lst = new ArrayList<String>();
		lst.add("facebook");
		lst.add("twitter");

		ShareListAdapter adapter = new ShareListAdapter(this.getActivity(),
				lst, context);
		this.setListAdapter((ListAdapter) adapter);
	}

	@Override
	public void onPause() {
		super.onPause();
		// Don't react to gps updates when the tab isn't active.
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	
	/**
	 * This method use to call launch campaign api
	 * @param iCampaignId campaign id which is going to launch
	 */
	private void goToLaunchCampaign(int iCampaignId){
		AppStatus appStatus=AppStatus.getInstance(context);
		String[] args = new String[2];

		args[0] = appStatus.getSharedStringValue(appStatus.AUTH_KEY); 
		args[1] = String.valueOf(iCampaignId); // campaign id

		if(appStatus.isOnline()) {
			 CheckinLibraryActivity.strProgressMessage=getString(R.string.launchCampaignProgressMessage);
			 context.showDialog(0);
			new LaunchCampaignTask(this).execute(args);
		} else {
			Log.v(TAG, "App is not online!");
			Intent intent= new Intent(context,NoConnectivityScreen.class);
			context.startActivity(intent);
			context.finish();
		}
	}
	
	/**
	 * This method use to get response data from launch campaign 
	 * api call
	 * @param LaunchCampaignResult response from api
	 */
	public void getLaunchCampaignResult(LaunchCampaignResult result){
		context.removeDialog(0);
		if(result != null ){
			if(result.getSuccess() ){
				updateCampaignIntoDB(result);
				Log.v(TAG, "Campaign launched!"); 
			}
		}
	}

	/**
	 * This method use to update campaign updated info in DB
	 * @param result  data to update in DB
	 */
	private void updateCampaignIntoDB(LaunchCampaignResult result){
		MyCampaignsDbAdapter adpt = new MyCampaignsDbAdapter(context);

		Cursor c = adpt.fetchAll("remote_id=" + String.valueOf(result.getCampaign_id()), "1");
		int campgnRowIdColumn = c.getColumnIndex("_id");

		if (c.moveToFirst()) {
			int campgn_rowId = c.getInt(campgnRowIdColumn);
			CreateCampaignResult mCampaignResult=adpt.getResult(result.getCampaign_id());
			if(mCampaignResult != null){
				mCampaignResult.getCampaign().setPublic_link(result.getPublic_link());
				mCampaignResult.getCampaign().setIs_active(result.isIs_active());
				adpt = new MyCampaignsDbAdapter(context);
				Boolean bIsUpdated=adpt.update(Long.valueOf(campgn_rowId),mCampaignResult);
				Log.i(TAG,"row updated :"+String.valueOf(result.getCampaign_id())+" name: "
						+mCampaignResult.getCampaign().getName()+ "status: "+String.valueOf(bIsUpdated));
				if(bIsUpdated){
					goToCongratsCampaignFragment(result.getCampaign_id());
				}
			}
		}
	}
	
	/**
	 * This method use to open congrats campaign fragment
	 */
	private void goToCongratsCampaignFragment(int iCampgnId){
		Constants.INSTANCE.mSelectedBitmaps=new ArrayList<Bitmap>();
		mCampaignCongratsFragment=null;
		if (mCampaignCongratsFragment == null) {
			Bundle bundle=new Bundle();
			bundle.putBoolean("isFromWhere", true);
			bundle.putInt("campgn_id", iCampgnId);
			mCampaignCongratsFragment = (CampaignCongratsFragment) ((CheckinLibraryActivity) context)
					.addFragment(R.id.linearLayout2,
							CampaignCongratsFragment.class.getName(),
							"congrats_campaign_fragment",bundle);
		} else {
			((CheckinLibraryActivity) this.context)
					.attachFragment((Fragment) mCampaignCongratsFragment);
		}
	}
}