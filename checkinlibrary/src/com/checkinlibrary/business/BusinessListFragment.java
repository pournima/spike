package com.checkinlibrary.business;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.checkinlibrary.CheckinLibraryActivity;
import com.checkinlibrary.NoConnectivityScreen;
import com.checkinlibrary.R;
import com.checkinlibrary.db.MyOrganizationDBAdapter;
import com.checkinlibrary.models.BusinessResult;
import com.checkinlibrary.models.OrganizationResult;
import com.checkinlibrary.models.OrganizationResult.cause_profile_result;
import com.checkinlibrary.ws.tasks.BusinessTask;

public class BusinessListFragment extends ListFragment {
	private List<BusinessResult> mList;
	protected CheckinLibraryActivity context;
	BusinessSuppOrgsFragment mBusinessSuppOrgsFragment;
	BusinessCheckinFragment mBusinessCheckinFragment;
	private BusinessResult mSelectedBussResult;
	Boolean loadingMore = false;
	private Boolean bIsSupported = false;
	BusinessFragment mBusinessFragment;
	Boolean bIsLastPage=false;
	public int iLastVisibleItemPosition=0;
	ListView list;
	
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	@Override
	public void onResume() {
		super.onResume();
		BusinessListAdapter adapter = new BusinessListAdapter(
				((Context) this.getActivity()), mList);
		this.setListAdapter((ListAdapter) adapter);
		if (CheckinLibraryActivity.isFromRefresh){
			iLastVisibleItemPosition=0;
		}
		
		if(list != null){
			if((mList.size() % 10) == 0)
				list.setSelection(iLastVisibleItemPosition);
			else
				list.setSelection(iLastVisibleItemPosition-10);
			}
		
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		context = (CheckinLibraryActivity) this.getActivity();
		
		View fragmentView = inflater.inflate(
				R.layout.business_list_fragment_view, null);
		
		RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		
		layoutParams.addRule(RelativeLayout.BELOW, R.id.linearLayoutBusBtns);
				 
		View footerView = inflater.inflate(R.layout.listfooter, null, false);
		list = (ListView) fragmentView.findViewById(android.R.id.list);
		fragmentView.setLayoutParams(layoutParams);

		// list.setOnScrollListener(new EndlessScrollListener(context,false));
		// /this.getListView().setOnScrollListener(new )
		setOnListScrollListener(list);
;		return fragmentView;
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		// Go to checkin screen
		
		mSelectedBussResult = mList.get(position);
		Log.i("Business list clicked", mSelectedBussResult.getName());

		if (mSelectedBussResult != null) {
			context.showDialog(0);
			getCommonCauses();
		}

	}

	public void setList(List<BusinessResult> businesses,Boolean bIsLastPage,Boolean isSupported) {
		mList = businesses;
		this.bIsLastPage=bIsLastPage;
		loadingMore=false;
		this.bIsSupported=isSupported;
	}

	public void getCommonCauses() {
		List<OrganizationResult> mMyCauses = null;
		mMyCauses = fetchMyCausesFromDB();
		List<OrganizationResult> mCommonCauses = new ArrayList<OrganizationResult>();
		int iMatchCount = 0;

		// check common causes
		if (mMyCauses == null) {
			noCommonOrNoMyCauses();
		} else {
			int iNoOfBusinessOrgs = mSelectedBussResult.getOrganizations()
					.size();
			int iNoOfMyCauses = mMyCauses.size();
			for (int i = 0; i < iNoOfBusinessOrgs; i++) {
				for (int j = 0; j < iNoOfMyCauses; j++) {
					Log.v("ID = ", ""+mSelectedBussResult.getOrganizations().get(i).get("id"));
					if (mSelectedBussResult.getOrganizations().get(i).get("id").equals(String.valueOf(mMyCauses
							.get(j).getOrganization().getId()))) {
						Log.i("Common cause", mSelectedBussResult
								.getOrganizations().get(i).get("name")
								+ mMyCauses.get(j).getOrganization().getName());
						mCommonCauses.add(mMyCauses.get(j));
						iMatchCount++;
						break;
					}
				}
			}

			if (iMatchCount == 0) {
				noCommonOrNoMyCauses();
			} else if (iMatchCount == 1) {
				if (mCommonCauses.size() > 0)
					gotoCheckinPage(mCommonCauses.get(0));
			} else if (iMatchCount > 1) {
				gotoOrganizationPage(mCommonCauses);
			}
		}
	}

	// if no common causes or no causes i am supporting
	void noCommonOrNoMyCauses() {
		int iNoOfBusinessOrgs = mSelectedBussResult.getOrganizations().size();

		if (iNoOfBusinessOrgs == 1) {
			OrganizationResult cause = new OrganizationResult(new cause_profile_result(),0,mSelectedBussResult
					.getOrganizations().get(0).get("name"),0,Integer.parseInt(mSelectedBussResult
					.getOrganizations().get(0).get("id")), false);
			gotoCheckinPage(cause);

		} else if (iNoOfBusinessOrgs > 1) {
			List<OrganizationResult> mCommonCauses = new ArrayList<OrganizationResult>();
			for (int i = 0; i < iNoOfBusinessOrgs; i++) {
				OrganizationResult cause = new OrganizationResult(new cause_profile_result(),0,mSelectedBussResult
						.getOrganizations().get(i).get("name"),0,Integer.parseInt(mSelectedBussResult
						.getOrganizations().get(i).get("id")), false);
				mCommonCauses.add(cause);
			}
			gotoOrganizationPage(mCommonCauses);
		} else {
			ShowMessageBox("Business List",
					"No Organizations supported by this business");
		}
		context.removeDialog(0);
	}

	void gotoCheckinPage(OrganizationResult mOrganizationResult) {
		Log.i("gotoCheckinPage OrgId", String.valueOf(mOrganizationResult.getOrganization().getId()));
		Log.i("gotoCheckinPage BusId",
				String.valueOf(mSelectedBussResult.getId()));
		mBusinessCheckinFragment = null;
		if (mBusinessCheckinFragment == null) {
			Bundle bundle = new Bundle();
			bundle.putSerializable("selected_cause",
					(Serializable) mOrganizationResult);
			bundle.putSerializable("selected_business",
					(Serializable) mSelectedBussResult);

			mBusinessCheckinFragment = (BusinessCheckinFragment) context
					.addFragment(R.id.linearLayout2,
							BusinessCheckinFragment.class.getName(),
							"checkin_fragment", bundle);
		} else {
			(context).attachFragment((Fragment) mBusinessCheckinFragment,"checkin_fragment");
		}
		context.removeDialog(0);
	}

	void gotoOrganizationPage(List<OrganizationResult> mCommonCauses) {
		if (mCommonCauses != null) {
			Log.i("gotoOraganizationPage", String.valueOf(mCommonCauses.size()));
			mBusinessSuppOrgsFragment = null;
			if (mBusinessSuppOrgsFragment == null) {
				Bundle bundle = new Bundle();
				bundle.putSerializable("common_causes",
						(Serializable) mCommonCauses);
				bundle.putSerializable("selected_business",
						(Serializable) mSelectedBussResult);

				mBusinessSuppOrgsFragment = (BusinessSuppOrgsFragment) (context)
						.addFragment(R.id.linearLayout2,
								BusinessSuppOrgsFragment.class.getName(),
								"business_supports_orgs", bundle);
			} else {
				(context).attachFragment((Fragment) mBusinessSuppOrgsFragment,"business_supports_orgs");
			}
		}
		context.removeDialog(0);
	}

	private List<OrganizationResult> fetchMyCausesFromDB() {
		MyOrganizationDBAdapter adpt = new MyOrganizationDBAdapter(context);
		List<OrganizationResult> myCauses = new ArrayList<OrganizationResult>();
		myCauses = adpt.getList();
		return myCauses;
	}

	private void ShowMessageBox(String mTitle, String mMsg) {
		AlertDialog.Builder mBuilder = new AlertDialog.Builder(context);
		mBuilder.setTitle(mTitle);
		mBuilder.setMessage(mMsg);

		mBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});

		AlertDialog mAlert = mBuilder.create();
		mAlert.show();
	}

	private void setOnListScrollListener(ListView list) {
		list.setOnScrollListener(new OnScrollListener() {

			// useless here, skip!
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
			}

			// dumdumdum
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {

				// what is the bottom iten that is visible
				int lastInScreen = firstVisibleItem + visibleItemCount;

				// is the bottom item visible & not loading more already ? Load
				// more !
				//Log.i("Endless List", "########last in screen" + lastInScreen);
				Log.i("Endless List", "########total Item" + totalItemCount);
				//Log.i("Endless List", "########first Item" + firstVisibleItem);
				if ((totalItemCount != 0) && (lastInScreen == totalItemCount)
						&& !(loadingMore) && (lastInScreen > visibleItemCount)){
					Log.i("Endless List", "########getting 10 more List items");
					if(!bIsLastPage)
					{
						context.showDialog(0);
						getMoreBusinesses();
						//loadingMore = false;
						CheckinLibraryActivity.isFromRefresh = false;
						iLastVisibleItemPosition=view.getLastVisiblePosition();
					}
					Log.i("BusinessList Fragment", "########is from refresh "+String.valueOf(CheckinLibraryActivity.isFromRefresh));
				}
			}
		});

	}

	private void getMoreBusinesses() {
		// Set flag so we cant load new items 2 at the same time
		loadingMore = true;

		if (CheckinLibraryActivity.appStatus.isOnline()) {
			int iCurrentPageNo;
			 if(this.bIsSupported){
      		   iCurrentPageNo=CheckinLibraryActivity.appStatus.getSharedIntValue(CheckinLibraryActivity.appStatus.MY_BUS_CUR_PAGE);
      	   }else{
      		 iCurrentPageNo=CheckinLibraryActivity.appStatus.getSharedIntValue(CheckinLibraryActivity.appStatus.ALL_BUS_CUR_PAGE);
      	   }
			 
			 Log.i("Checkin", "#########Current Page = "+String.valueOf(iCurrentPageNo));
			 
			double latitude = 0, longitude = 0;

			String lat = CheckinLibraryActivity.appStatus
					.getSharedStringValue(CheckinLibraryActivity.appStatus.LAT);
			String lon = CheckinLibraryActivity.appStatus
					.getSharedStringValue(CheckinLibraryActivity.appStatus.LONG);
			if ((lat != null) && (lon != null)) {
				latitude = Double.valueOf(lat);
				longitude = Double.valueOf(lon);
			}

			new BusinessTask(context, bIsSupported,iCurrentPageNo).execute(
					latitude, longitude);
			iCurrentPageNo++;
			
			if(this.bIsSupported){
				CheckinLibraryActivity.appStatus.saveSharedIntValue(CheckinLibraryActivity.appStatus.MY_BUS_CUR_PAGE, iCurrentPageNo);
     	   }else{
     		  CheckinLibraryActivity.appStatus.saveSharedIntValue(CheckinLibraryActivity.appStatus.ALL_BUS_CUR_PAGE, iCurrentPageNo);
     	   }

		} else {
			// main_context.message("Check internet connectivity!");
			Log.v("CHECKINFORGOOD", "App is not online!");
			Intent intent = new Intent(context, NoConnectivityScreen.class);
			context.startActivity(intent);
			context.finish();
		}
		 //loadingMore = false;

	}

}
