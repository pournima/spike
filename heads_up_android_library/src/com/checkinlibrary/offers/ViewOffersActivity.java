package com.checkinlibrary.offers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.checkinlibrary.NoConnectivityScreen;
import com.checkinlibrary.R;
import com.checkinlibrary.TabGroupActivity;
import com.checkinlibrary.checkin.CheckinActivity;
import com.checkinlibrary.db.BusinessDbAdapter;
import com.checkinlibrary.db.BusinessOrgDbAdapter;
import com.checkinlibrary.db.CheckinTimeDbAdapter;
import com.checkinlibrary.db.MyOrganizationDBAdapter;
import com.checkinlibrary.helpers.AppStatus;
import com.checkinlibrary.helpers.MyConstants;
import com.checkinlibrary.location.GpsFix;
import com.checkinlibrary.location.GpsFix.GpsFixStatus;
import com.checkinlibrary.location.GpsFragment;
import com.checkinlibrary.location.GpsLocator;
import com.checkinlibrary.offers.BusinessResult.CheckinTimeResult;
import com.checkinlibrary.offers.BusinessResult.LocationResult.BusinessResultResource.BizOrgSupportsResult;
import com.checkinlibrary.offers.Searcher.SearchType;
import com.checkinlibrary.orgs.CauseResult;
import com.checkinlibrary.ws.tasks.BusinessTask;
import com.checkinlibrary.ws.tasks.SearchTask;

public class ViewOffersActivity extends Activity implements GpsFragment {
	AppStatus appStatus;
	String appName;
	LinearLayout mLinearLayout;
	TextView mTextView;
	ViewOffersActivity mViewOffersActivity;
	boolean abc = true;
	Boolean onlyShowSupported;
	GpsLocator gps;
	GpsFix gpsFix;
	public static Boolean isOnPause;
	OffersListAdapter mOffersListAdapter;
	public ListView mListView;
	public ProgressDialog mProgressDialog;
	private ProgressDialog loading;
	Handler mhandler;
	Context context;
	public static Boolean loadingMore = false;
	private Boolean bIsSupported = true;
	Boolean bIsLastPage=false;
	public int iLastVisibleItemPosition=0;
	int iCurrentPageNo = 2;
	boolean isFromRefresh = false;
	Activity parentActivity;
	BusinessResult mSelectedBussResult;
	List<BusinessResult> mBusinessList;
	public static boolean bIsFromSearch = false;
	public static SearchType offerSearch;
	public static String searchQuery;
	LinearLayout mOfferLinearLayout;
	TextView txtViewTopRaisedMoney;
	String strProgressMessage = "Loading....";
	public static boolean bIsResuming = true;
	private int toastCount = 0;
	public static boolean isSearchedCalled = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.offers);
		appStatus = AppStatus.getInstance(this);
		appName = appStatus.getSharedStringValue(appStatus.APP_NAME);
		setOffersTabHeader();
		this.onlyShowSupported = true;
		isOnPause = false;
		mhandler = new Handler();
		loading = new ProgressDialog(this);
		loading.setMessage("Loading...");
		loading.setCancelable(true);

		mListView = (ListView) findViewById(android.R.id.list);
		mListView.setOnItemClickListener(itemClick);
		setOnListScrollListener(mListView);
		parentActivity = this.getParent();
		txtViewTopRaisedMoney=(TextView)findViewById(R.id.textViewTopRaisedMoney);
		
		Log.v("######HEADS UP", "In onCreateView");
	}

	public void setOffersTabHeader() {

		mLinearLayout = (LinearLayout) findViewById(R.id.linearLayoutBusBtns);
		mOfferLinearLayout = (LinearLayout) findViewById(R.id.linearLayoutOfferRefreshBtn);

		if (0 == (getString(R.string.app_name)).compareTo("USA Football")) {
			mLinearLayout.setVisibility(LinearLayout.GONE);
			mOfferLinearLayout.setVisibility(LinearLayout.VISIBLE);

		} else {

			mLinearLayout.setVisibility(LinearLayout.VISIBLE);
			mOfferLinearLayout.setVisibility(LinearLayout.GONE);
		}

	}

	public void onResume() {

		txtViewTopRaisedMoney.setText(appStatus.setCheckinAmount());
		appStatus = AppStatus.getInstance(this);
		if (ViewOffersActivity.bIsFromSearch) {

			// strProgressMessage = "Loading....";
			strProgressMessage = getString(R.string.searchProgressMessage);
			showDialog(0);
			new SearchTask(this, ViewOffersActivity.offerSearch)
					.execute(ViewOffersActivity.searchQuery);
			Log.v("######HEADS UP", "searching from view offers");
			ViewOffersActivity.bIsFromSearch = false;
		} else {
			/*
			 * if (appStatus.getSharedBoolValue(appStatus.IS_APP_RESUMED)) {
			 * refreshList();
			 * appStatus.saveSharedBoolValue(appStatus.IS_APP_RESUMED, false); }
			 * else { updateLocationView(null); }
			 */
			if (!isSearchedCalled)
				refreshList();
		}

		super.onResume();
	}

	private void refreshList() {
		// Wait up to 60 seconds for a fix, that is accurate to 300 meters and
		// is not older than 2 minutes.
		Integer accuracy;
		isFromRefresh = true;
		iCurrentPageNo=2;

		// Emulator returns no accuracy, so ask for fix that is worse than our
		// default accuracy.
		if ("google_sdk".equals(Build.PRODUCT)) {
			accuracy = 551;
		} else {
			accuracy = 2000;
		}

		gps = GpsLocator.getGpsLocator(this);
		gpsFix = new GpsFix(60, accuracy, 259200);
		gpsFix.getFix(gps.getBestLocation());
		// 'updateLocations(gps.getBestLocation());
		if (gpsFix.getFix(gps.getBestLocation()) == GpsFixStatus.OK_FIX) {
			updateLocations(gps.getBestLocation());
		} else {
			gps.addObserver(this);
			updateLocationView(null);
		}
	}

	public void updateLocations(Location location) {
		if (appStatus.isOnline()) {
			//String strProgressMessage = "Loading....";
			strProgressMessage = (getString(R.string.businessProgressMessage));
			showDialog(0);
			new BusinessTask(this, onlyShowSupported, 1).execute(
					location.getLatitude(), location.getLongitude());
			// new BusinessTask(this,
			// onlyShowSupported,1).execute(37.785834,-122.406417);
		} else {
			Intent intent = new Intent(this, NoConnectivityScreen.class);
			this.startActivity(intent);
			// context.message("Check internet connectivity!");
			// Log.v("BusinessFragment", "App is not online!");
			getParent().finish();
		}
	}

	public void updateLocationView(List<BusinessResult> result) {

		//List<BusinessResult> business = new ArrayList<BusinessResult>();
		mBusinessList = new ArrayList<BusinessResult>();

		bIsLastPage = false;

		if (result != null) {
			if (result.size() > 0) {
				storeBusinessResultIntoDB(result); // Read from
				// db-----------------//
				// business = onlyShowSupported ? result : //
				readBusinessResultfromDB();
				mBusinessList = readBusinessResultfromDB();
			} else {
				mBusinessList = readBusinessResultfromDB();
				bIsLastPage = true;
			}
		} else {
			mBusinessList = readBusinessResultfromDB();
		}

		if (mBusinessList != null) {
			if (mBusinessList.size() > 0) {
				mOffersListAdapter = new OffersListAdapter(this, mBusinessList);
				mListView.setAdapter(mOffersListAdapter);
				mListView.setOnItemClickListener(itemClick);
				loadingMore=false;
				
				if((mBusinessList.size() % 10) == 0)
					mListView.setSelection(iLastVisibleItemPosition);
				else
					mListView.setSelection(iLastVisibleItemPosition-10);
			}
		}

		removeDialog(0);
		//isSearchedCalled = false;

	}

	@Override
	public void onLocationUpdated(Location location) {
		// TODO Auto-generated method stub
		GpsFixStatus fixStatus = gpsFix.isAcceptableFix(location);

		Log.i("CHECKINFORGOOD", "Fix status: " + fixStatus.toString());

		if (fixStatus == GpsFixStatus.OK_FIX) {
			updateLocations(location);
			gps.removeObserver(this);

			Log.i("CHECKINFORGOOD", "Location updated for business fragment");
		} else if (fixStatus == GpsFixStatus.TIMED_OUT) {
			// Show location not found error page.
			Log.i("CHECKINFORGOOD", "Location not found");
		}
	}

	public OnItemClickListener itemClick = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position,
				long arg3) {
			if((mBusinessList != null) && (mBusinessList.size() > 0)){
				mSelectedBussResult=mBusinessList.get(position);
				if(mSelectedBussResult != null){
					showDialog(0);
					getCommonCauses();
				}
			}

		}

	};

	public void DisplayToast(String msg) {
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
		// showDialog(0);
	}

	@Override
	protected Dialog onCreateDialog(int id, Bundle args) {
		final ProgressDialog dialog = new ProgressDialog(this.getParent());
		dialog.setTitle("Please Wait...");
		dialog.setIndeterminate(true);
		dialog.setMessage(String.valueOf(strProgressMessage));
		dialog.setCancelable(true);
		dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
			public void onCancel(DialogInterface dialog) {
				Log.i("LOCATOR", "user cancelling authentication");

			}
		});
		
		dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
			@Override
			public boolean onKey(DialogInterface arg0, int keyCode, KeyEvent event) {
				// TODO Auto-generated method stub
				if(((keyCode == KeyEvent.KEYCODE_SEARCH ) || (keyCode == KeyEvent.KEYCODE_MENU ))) {
					return true;
				}
				return false;
			}
		});
		mProgressDialog = dialog;
		return dialog;
	}

	public void storeBusinessResultIntoDB(List<BusinessResult> result) {
		String mDbBusName,mDbBusOrgsName,mDbCheckinTimeName;
		if (this.onlyShowSupported ) {
			mDbBusName=MyConstants.TABLE_MY_ORGS_BUS;
			mDbBusOrgsName=MyConstants.TABLE_MY_BUS_ORGS;
			mDbCheckinTimeName=MyConstants.TABLE_MY_ORGS_CHECKIN_TIMES;
		} else {
			mDbBusName=MyConstants.TABLE_BUS;
			mDbBusOrgsName=MyConstants.TABLE_BUS_ORGS;
			mDbCheckinTimeName=MyConstants.TABLE_CHECKIN_TIMES;
		}
		BusinessDbAdapter adpt = new BusinessDbAdapter(this, mDbBusName,
				mDbBusOrgsName, mDbCheckinTimeName);
		Log.i("Business Fragment", "########is from refresh "+String.valueOf(isFromRefresh));
		if (isFromRefresh){
			adpt.deleteAllAssociated();
		}
		//adpt.deleteAllAssociated();
		try {
			adpt.beginTransaction();

			for(int i=0;i<result.size();i++) {
				Log.i("CHECKINFORGOOD", "Id for test res " + result.get(i).getName()+String.valueOf(result.get(i).getId()));
				adpt.createAssociated(result.get(i));
			}

			adpt.succeedTransaction();
		} finally {
			adpt.endTransaction();                
		}
	}

	public List<BusinessResult> readBusinessResultfromDB() {
		String mDbBusName,mDbBusOrgsName,mDbCheckinTimeName;
		if (this.onlyShowSupported ) {
			mDbBusName=MyConstants.TABLE_MY_ORGS_BUS;
			mDbBusOrgsName=MyConstants.TABLE_MY_BUS_ORGS;
			mDbCheckinTimeName=MyConstants.TABLE_MY_ORGS_CHECKIN_TIMES;
		} else {
			mDbBusName=MyConstants.TABLE_BUS;
			mDbBusOrgsName=MyConstants.TABLE_BUS_ORGS;
			mDbCheckinTimeName=MyConstants.TABLE_CHECKIN_TIMES;
		}
		BusinessDbAdapter adpt = new BusinessDbAdapter(this,mDbBusName,mDbBusOrgsName,mDbCheckinTimeName);
		BusinessResult singleBusiness=null;
		List<BusinessResult> business =new ArrayList<BusinessResult>();
		Cursor c = adpt.fetchAll(null, "1");

		while (c.moveToNext()) {

			HashMap<String, String> businessParams = adpt.getResultParamsFromCursor(c);

			BusinessOrgDbAdapter bus_org_db = new BusinessOrgDbAdapter(this,mDbBusOrgsName);    
			List<BizOrgSupportsResult> orgs = bus_org_db.getList(Integer.valueOf(businessParams.get("remote_id")));                 

			CheckinTimeDbAdapter checkin_db = new CheckinTimeDbAdapter(this,mDbCheckinTimeName);    
			List<CheckinTimeResult> checkins = checkin_db.getList(Integer.valueOf(businessParams.get("remote_id"))); 

			singleBusiness = new BusinessResult(businessParams, checkins, orgs);
			business.add(singleBusiness);
		}       
		c.close();
		return business;
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
					if(!bIsLastPage) {
						showDialog(0);
						getMoreBusinesses();
						//loadingMore = false;
						isFromRefresh = false;
						iLastVisibleItemPosition=view.getLastVisiblePosition();
					} else {
						toastCount ++; 
						Log.i("Toast display---------", "-------------------"+toastCount);
						
						if(toastCount <= 1){
							DisplayToast("No more Businesses available");
							Log.i("Toast display---------", "No more Businesses available");
						}						
					}
					//Log.i("BusinessList Fragment", "########is from refresh "+String.valueOf(CheckinNativeActivity.isFromRefresh));
				}
			}
		});

	}

	private void getMoreBusinesses() {
		// Set flag so we cant load new items 2 at the same time
		loadingMore = true;

		if (appStatus.isOnline()) {


			Log.i("Checkin", "#########Current Page = "+String.valueOf(iCurrentPageNo));

			double latitude = 0, longitude = 0;

			String lat = appStatus
					.getSharedStringValue(appStatus.LAT);
			String lon = appStatus
					.getSharedStringValue(appStatus.LONG);
			if ((lat != null) && (lon != null)) {
				latitude = Double.valueOf(lat);
				longitude = Double.valueOf(lon);
			}

			new BusinessTask(this, bIsSupported,iCurrentPageNo).execute(
					latitude, longitude);
			iCurrentPageNo++;


		} else {
			// main_context.message("Check internet connectivity!");
			Log.v("CHECKINFORGOOD", "App is not online!");
			Intent intent = new Intent(this, NoConnectivityScreen.class);
			this.startActivity(intent);
			getParent().finish();
		}
		//loadingMore = false;

	}


	@Override
	public boolean onSearchRequested() {
		Log.i("CHECKIN", "Searching...");
		Bundle searchBundle = new Bundle();

		searchBundle.putInt("search_type",
				Searcher.SearchType.BUSINESS.ordinal());
		//TabGroupActivity mTabGroupActivity = (TabGroupActivity)getParent();
		this.getParent().startSearch(null, false, searchBundle, false);
		//isSearchedCalled = true;

		return true;
	}

	@Override
	protected void onNewIntent(Intent intent) {
		Intent oldIntent = this.getIntent();
		setIntent(intent);

		if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
			String query = intent.getStringExtra(SearchManager.QUERY);
			Bundle searchBundle = intent.getBundleExtra(SearchManager.APP_DATA);
			Searcher.SearchType type = Searcher.SearchType.values()[searchBundle
			                                                        .getInt("search_type")];
			String strProgressMessage = "Loading....";
			strProgressMessage=getString(R.string.searchProgressMessage);
			this.showDialog(0);
			new SearchTask(this, type).execute(query);
		}

		// Set our intent back once the search display is done
		setIntent(oldIntent);
	}

	public void onSearchCompleted(List<? extends Object> result,
			Searcher.SearchType type, String query) {

		/*BusinessSearchActivity mBusinessSearchActivity = new BusinessSearchActivity();
		mBusinessSearchActivity.addBusinessActivity((List<BusinessResult>) result, query);
*/
		TabGroupActivity parentActivity = (TabGroupActivity) getParent();

		Bundle bundle = new Bundle();
		bundle.putSerializable("search_result",
				(Serializable) result);
		bundle.putString("search_query", query);
		
		
		mBusinessList = (List<BusinessResult>) result;

		Intent intent = new Intent(getParent(),BusinessSearchActivity.class);
		intent.putExtras(bundle);
		parentActivity.startChildActivity("BusinessSearch", intent);
		
		
		removeDialog(0);
		//isSearchedCalled = false;
	}
	

	public void getCommonCauses() {
		List<CauseResult> mMyCauses = null;
		mMyCauses = fetchMyCausesFromDB();
		List<CauseResult> mCommonCauses = new ArrayList<CauseResult>();
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
					if (mSelectedBussResult.getOrganizations().get(i).get("id").compareTo(mMyCauses
							.get(j).getId().toString()) == 0) {
						Log.i("Common cause", mSelectedBussResult
								.getOrganizations().get(i).get("name")
								+ mMyCauses.get(j).getName());
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
			CauseResult cause = new CauseResult(mSelectedBussResult
					.getOrganizations().get(0).get("id"), mSelectedBussResult
					.getOrganizations().get(0).get("name"), "false");
			gotoCheckinPage(cause);

		} else if (iNoOfBusinessOrgs > 1) {
			List<CauseResult> mCommonCauses = new ArrayList<CauseResult>();
			for (int i = 0; i < iNoOfBusinessOrgs; i++) {
				CauseResult cause = new CauseResult(mSelectedBussResult
						.getOrganizations().get(i).get("id"),
						mSelectedBussResult.getOrganizations().get(i)
						.get("name"), "false");
				mCommonCauses.add(cause);
			}
			gotoOrganizationPage(mCommonCauses);
		} else {
			ShowMessageBox("Business List",
					"No Organizations supported by this business");
		}
		removeDialog(0);
	}

	void gotoCheckinPage(CauseResult mCauseResult) {
		Log.i("gotoCheckinPage OrgId", String.valueOf(mCauseResult.getId()));
		Log.i("gotoCheckinPage BusId",
				String.valueOf(mSelectedBussResult.getId()));
		
		TabGroupActivity parentActivity = (TabGroupActivity) getParent();

		Bundle bundle = new Bundle();
		bundle.putSerializable("selected_cause",
				(Serializable) mCauseResult);
		bundle.putSerializable("selected_business",
				(Serializable) mSelectedBussResult);

		Intent intent = new Intent(getParent(),CheckinActivity.class);
		intent.putExtras(bundle);
		parentActivity.startChildActivity("Checkin", intent);
		
		removeDialog(0);
	}

	void gotoOrganizationPage(List<CauseResult> mCommonCauses) {
		/*if (mCommonCauses != null) {
			Log.i("gotoOraganizationPage", String.valueOf(mCommonCauses.size()));
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
				(context).attachFragment((Fragment) mBusinessSuppOrgsFragment);
			}
		}*/
		removeDialog(0);
	}

	private List<CauseResult> fetchMyCausesFromDB() {
		MyOrganizationDBAdapter adpt = new MyOrganizationDBAdapter(context);
		List<CauseResult> myCauses = new ArrayList<CauseResult>();
		myCauses = adpt.getList();
		return myCauses;
	}
	
	private void ShowMessageBox(String mTitle, String mMsg) {
		AlertDialog.Builder mBuilder = new AlertDialog.Builder(this.getParent());
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
	
	public void onOffersRefeshBtnClick(View v) {
		
		Log.i("######HeadsUp","Refresh button clicked.......");
		refreshList();
		
	}
}
