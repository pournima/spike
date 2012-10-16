package com.checkinlibrary.business;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.checkinlibrary.CheckinLibraryActivity;
import com.checkinlibrary.NoConnectivityScreen;
import com.checkinlibrary.R;
import com.checkinlibrary.db.BusinessDbAdapter;
import com.checkinlibrary.db.BusinessOrgDbAdapter;
import com.checkinlibrary.db.CheckinTimeDbAdapter;
import com.checkinlibrary.helpers.MyConstants;
import com.checkinlibrary.location.GpsFix;
import com.checkinlibrary.location.GpsFix.GpsFixStatus;
import com.checkinlibrary.location.GpsFragment;
import com.checkinlibrary.location.GpsLocator;
import com.checkinlibrary.models.BusinessResult;
import com.checkinlibrary.models.BusinessResult.CheckinTimeResult;
import com.checkinlibrary.models.BusinessResult.LocationResult.BusinessResultResource.BizOrgSupportsResult;
import com.checkinlibrary.ws.tasks.BusinessTask;


public class BusinessFragment extends Fragment implements GpsFragment {

	BusinessListFragment mBusinessListFragment;
	GpsLocator gps;
	GpsFix gpsFix;
	CheckinLibraryActivity context;
	Boolean onlyShowSupported;
	private Button allButton;
	private Button supportButton;  
	 private ListStatus listStatus;
	    private Location refreshedLocation;
	    
	    private enum ListStatus {
	        NEW, REFRESHED, BEST
	    }
	    
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.context = (CheckinLibraryActivity) this.getActivity();
		this.onlyShowSupported = false;
        Log.v("CHECKINFORGOOD", "In onAttach of BusinessFragment");
	}

	public void onResume() {		
        //Wait up to 60 seconds for a fix, that is accurate to 300 meters and is not older than 2 minutes.
        Integer accuracy;
        Integer maximal_accuracy = 860;

        //Emulator returns no accuracy, so ask for fix that is worse than our default accuracy.
        if ( "google_sdk".equals(Build.PRODUCT) ) { 
            accuracy = 551;  
        } else {
            accuracy=2000;
        }

        gps = GpsLocator.getGpsLocator((CheckinLibraryActivity) this.getActivity());
        gpsFix = new GpsFix(60, accuracy, 259200);
        gpsFix.getFix(gps.getBestLocation());
	
        CheckinLibraryActivity.strProgressMessage=getString(R.string.businessProgressMessage);
    
        if ( this.context.isResuming ) {
            refreshList();
            this.context.isResuming = false;
        } else {
            updateLocationView(null);  //Just update from db, don't do a full pull
            gps.addObserver(this);
        }
    
        Log.v("CHECKINFORGOOD", "In onResume of BusinessFragment");

        super.onResume();
}
	
	private void refreshList() {
		 listStatus = ListStatus.NEW;
        //Wait up to 60 seconds for a fix, that is accurate to 300 meters and is not older than 2 minutes.
        Integer accuracy;
        Integer maximal_accuracy = 860;

        //Emulator returns no accuracy, so ask for fix that is worse than our default accuracy.
        if ( "google_sdk".equals(Build.PRODUCT) ) { 
            accuracy = 551;  
        } else {
            accuracy=2000;
        }

        gps = GpsLocator.getGpsLocator((CheckinLibraryActivity) this.getActivity());
        gpsFix = new GpsFix(10, accuracy, 259200);
        GpsFixStatus gpsStatus = gpsFix.getFix(gps.getBestLocation());
        
        
        if ( gpsStatus == GpsFixStatus.OK_FIX ) {
            updateLocations(gps.getBestLocation());
        } else if ( gpsStatus != GpsFixStatus.OPTIMUM_FIX ) {                     
            updateLocationView(null);      
        } else {
            updateLocationView(null);               
        }
        
        gps.addObserver(this);  
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v;
		
		if (CheckinLibraryActivity.appStatus.isRegistered()) 
		{

				v = inflater.inflate(R.layout.business_fragment, container, false);
				setupButtons(v);

		} else {
			v = inflater.inflate(R.layout.checking_auth, container, false);
		}
		
		setOffersTabHeader(v);
		
		return v;
	}

	public void setOffersTabHeader(View v) {
		LinearLayout mLinearLayout;
		LinearLayout mOfferLinearLayout;
		
		mLinearLayout = (LinearLayout)v.findViewById(R.id.linearLayoutBusBtns);

		ImageView businessOfferRefreshImage = (ImageView)v.findViewById(R.id.businessOfferRefreshImage);
		ImageView businessRefreshImage = (ImageView)v.findViewById(R.id.businessRefreshImage);
		
		if (0 == (getString(R.string.app_name)).compareTo("USA Football")) {
			mOfferLinearLayout = (LinearLayout)v.findViewById(R.id.linearLayoutOfferRefreshBtn);
			
			mLinearLayout.setVisibility(LinearLayout.GONE);
			mOfferLinearLayout.setVisibility(LinearLayout.VISIBLE);
			businessOfferRefreshImage.setVisibility(View.VISIBLE);
			businessRefreshImage.setVisibility(View.GONE);
		} else {
			mLinearLayout.setVisibility(LinearLayout.VISIBLE);
			businessOfferRefreshImage.setVisibility(View.GONE);
			businessRefreshImage.setVisibility(View.VISIBLE);
			//mOfferLinearLayout.setVisibility(LinearLayout.GONE);
		}

	}
	public void updateLocations(Location location) {
		if(CheckinLibraryActivity.appStatus.isOnline()) {
			Fragment fragment = context.getLastFragment();
			if (fragment.getTag() == "business_tab"){
				context.showDialog(0);			
				new BusinessTask(context, onlyShowSupported,1).execute(location.getLatitude(), location.getLongitude());
				if(BusinessFragment.this.onlyShowSupported){
					CheckinLibraryActivity.appStatus.saveSharedIntValue(CheckinLibraryActivity.appStatus.MY_BUS_CUR_PAGE, 2);
				}else{
					CheckinLibraryActivity.appStatus.saveSharedIntValue(CheckinLibraryActivity.appStatus.ALL_BUS_CUR_PAGE, 2);
				}
				CheckinLibraryActivity.isFromRefresh = true;
			}	
			} else {
				Intent intent= new Intent(context,NoConnectivityScreen.class);
				this.startActivity(intent);
				context.finish();
		}
	}

	public void updateLocationView(List<BusinessResult> result) {
	    //---------------------Code to test user location data-----------------------       

		FragmentTransaction ft = this.getActivity().getSupportFragmentManager().beginTransaction();

			if (mBusinessListFragment == null) {
				mBusinessListFragment = (BusinessListFragment) Fragment.instantiate(this.getActivity(), BusinessListFragment.class.getName());
				ft.add(R.id.relativeLayoutBus, mBusinessListFragment, "business_list");
			}
		
		List<BusinessResult> business =new ArrayList<BusinessResult>();
        Boolean bIsLastPage=false;
		if(result != null) {
			if(result.size() > 0) {
				storeBusinessResultIntoDB(result);
				//Read from db-----------------//
				// business = onlyShowSupported ? result : readBusinessResultfromDB();
				business=readBusinessResultfromDB();
			} else{
				business = readBusinessResultfromDB();
				bIsLastPage=true;
			}
		} else {
			business = readBusinessResultfromDB();
		}
		mBusinessListFragment.setList(business,bIsLastPage,this.onlyShowSupported);

		// Do we need this? Does it force onResume?
		if (!mBusinessListFragment.isDetached()) {
			ft.detach(mBusinessListFragment);
		}

		ft.attach(mBusinessListFragment);
		ft.commit();

		context.removeDialog(0);
	}

	@Override
	public void onLocationUpdated(Location location) {
		GpsFixStatus fixStatus = gpsFix.isAcceptableFix(location);

		Log.i("CHECKINFORGOOD", "Fix status: " + fixStatus.toString());
		Log.i("CHECKINFORGOOD", "Accuracy: " + location.getAccuracy());
		if ( fixStatus == GpsFixStatus.OK_FIX ) {
		    if ( listStatus == ListStatus.NEW ) {
		        //Update our business list and continue to listen
		        refreshedLocation = location;
		        updateLocations(location);
		        updateListStatus();  //List status is set to refreshed, so we don't continue to pull after the timeout.
		    } else if ( gpsFix.isOldLocation(refreshedLocation)  )  {  //We need a timeout from the first refresh here that removes the observer.  30-60 seconds after the first pull should be enough.
                refreshedLocation = null;
                updateListStatus();
                Log.i("CHECKINFORGOOD", "Fix status: CONTINUE LISTENING");
		        //gps.removeObserver(this);
		    }
		    
			Log.i("CHECKINFORGOOD", "Location updated for business fragment");
        } else if ( fixStatus == GpsFixStatus.OPTIMUM_FIX ) {
            updateListStatus();
            updateLocations(location);
            Log.i("CHECKINFORGOOD", "Fix status: REMOVING OBSERVER");
            gps.removeObserver(this);  //We've got the best fix we need.  Stop listening no matter what.
		} else if ( fixStatus == GpsFixStatus.TIMED_OUT ) {
			//Show location not found error page.
			Log.i("CHECKINFORGOOD", "Location not found");
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		//Don't react to gps updates when the tab isn't active.
		/*if ( gps != null ) gps.removeObserver(this);
		listStatus = null;
		refreshedLocation = null;*/
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
		BusinessDbAdapter adpt = new BusinessDbAdapter(context, mDbBusName,
				mDbBusOrgsName, mDbCheckinTimeName);
		Log.i("Business Fragment", "########is from refresh "+String.valueOf(CheckinLibraryActivity.isFromRefresh));
		if (CheckinLibraryActivity.isFromRefresh){
			adpt.deleteAllAssociated();
		}
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
		BusinessDbAdapter adpt = new BusinessDbAdapter(context,mDbBusName,mDbBusOrgsName,mDbCheckinTimeName);
		BusinessResult singleBusiness=null;
		List<BusinessResult> business =new ArrayList<BusinessResult>();
		Cursor c = adpt.fetchAll(null, "1");

		while (c.moveToNext()) {

			HashMap<String, String> businessParams = adpt.getResultParamsFromCursor(c);

			BusinessOrgDbAdapter bus_org_db = new BusinessOrgDbAdapter(context,mDbBusOrgsName);    
			List<BizOrgSupportsResult> orgs = bus_org_db.getList(Integer.valueOf(businessParams.get("remote_id")));                 

			CheckinTimeDbAdapter checkin_db = new CheckinTimeDbAdapter(context,mDbCheckinTimeName);    
			List<CheckinTimeResult> checkins = checkin_db.getList(Integer.valueOf(businessParams.get("remote_id"))); 

			singleBusiness = new BusinessResult(businessParams, checkins, orgs);
			business.add(singleBusiness);
		}       
		c.close();
		return business;
	}


	private void setupButtons(View v) {        
		allButton = (Button)v.findViewById(R.id.businessAllButton);
		supportButton = (Button)v.findViewById(R.id.businessSupportingButton);         
		addButtonClickListener(false, allButton);
		addButtonClickListener(true, supportButton);
		allButton.setSelected(true);
		ImageView businessRefreshImage = (ImageView)v.findViewById(R.id.businessRefreshImage);
		ImageView businessOfferRefreshImage = (ImageView)v.findViewById(R.id.businessOfferRefreshImage);
		addImageRefreshListener(businessRefreshImage);
		addImageOfferRefreshListener(businessOfferRefreshImage);
		
	}

	private void addImageRefreshListener(ImageView businessRefreshImage) {
	   businessRefreshImage.setOnClickListener(new OnClickListener() {
           @Override
           public void onClick(View v) {
        	   Log.i("CHECKINFORGOOD", "########On refresh button click ");
        	   if(BusinessFragment.this.onlyShowSupported){
        		   CheckinLibraryActivity.appStatus.saveSharedIntValue(CheckinLibraryActivity.appStatus.MY_BUS_CUR_PAGE, 2);
        	   }else{
        		   CheckinLibraryActivity.appStatus.saveSharedIntValue(CheckinLibraryActivity.appStatus.ALL_BUS_CUR_PAGE, 2);
        	   }
        	   CheckinLibraryActivity.isFromRefresh = true;
               BusinessFragment.this.refreshList();
               Log.i("Business Fragment refresh button", "########is from refresh: "+String.valueOf(CheckinLibraryActivity.isFromRefresh));
               
           }	       
	   });
    }
	
	private void addImageOfferRefreshListener(ImageView businessRefreshImage) {
		   businessRefreshImage.setOnClickListener(new OnClickListener() {
	           @Override
	           public void onClick(View v) {
	        	   Log.i("CHECKINFORGOOD", "########On refresh button click ");
	        	   if(BusinessFragment.this.onlyShowSupported){
	        		   CheckinLibraryActivity.appStatus.saveSharedIntValue(CheckinLibraryActivity.appStatus.MY_BUS_CUR_PAGE, 2);
	        	   }else{
	        		   CheckinLibraryActivity.appStatus.saveSharedIntValue(CheckinLibraryActivity.appStatus.ALL_BUS_CUR_PAGE, 2);
	        	   }
	        	   CheckinLibraryActivity.isFromRefresh = true;
	               BusinessFragment.this.refreshList();
	               Log.i("Business Fragment refresh button", "########is from refresh: "+String.valueOf(CheckinLibraryActivity.isFromRefresh));
	               
	           }	       
		   });
	    }

    private void addButtonClickListener(Boolean showSupported, Button button) {
		final Boolean showSupportedFinal = showSupported;        

		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(mBusinessListFragment != null)
					mBusinessListFragment.iLastVisibleItemPosition=0;
				BusinessFragment.this.onlyShowSupported = showSupportedFinal;
				updateButtonImageForOver(showSupportedFinal);
				if ((context.isResuming)) {
					if ((gps != null) && (gpsFix != null)) {
						if (gpsFix.getFix(gps.getBestLocation()) == GpsFixStatus.OK_FIX) {
							updateLocations(gps.getBestLocation());
						} else {
							gps.addObserver(BusinessFragment.this);
							updateLocationView(null);
						}
					} else {
						gps.addObserver(BusinessFragment.this);
						updateLocationView(null);
					}
				} else {
					if(BusinessFragment.this.onlyShowSupported){
						String mDbBusName,mDbBusOrgsName,mDbCheckinTimeName;
						
							mDbBusName=MyConstants.TABLE_MY_ORGS_BUS;
							mDbBusOrgsName=MyConstants.TABLE_MY_BUS_ORGS;
							mDbCheckinTimeName=MyConstants.TABLE_MY_ORGS_CHECKIN_TIMES;
						
						BusinessDbAdapter adpt = new BusinessDbAdapter(context,mDbBusName,mDbBusOrgsName,mDbCheckinTimeName);
						Cursor c = adpt.fetchAll(null, "1");
						int cnt = c.getCount();
						Log.i("Checkin", String.valueOf(cnt));
						if(cnt<1){
							refreshList();
						}else{
							updateLocationView(null);
						}
						c.close();
					}else{
					updateLocationView(null);
					}
					
				}
			}
		});
	}

	private void updateButtonImageForOver(final Boolean onlyShowSupported){
		if(onlyShowSupported == true){
			supportButton.setSelected(true);
			allButton.setSelected(false);
		} else {
			supportButton.setSelected(false);
			allButton.setSelected(true);
		}
	}
	
	/*private void updateBusSuppMyCauses() {
		if(CheckinLibraryActivity.appStatus.isOnline()) {
			GpsLocator gps = GpsLocator.getGpsLocator(context);
			Location currentLocation=gps.getBestLocation();
			double latitude = 0,longitude = 0;
			if(currentLocation != null) {
				context.showDialog(0);
				new BusinessTask(context, onlyShowSupported).execute(latitude, longitude);
			} else {
				String lat=CheckinLibraryActivity.appStatus.getSharedStringValue(CheckinLibraryActivity.appStatus.LAT);
				String lon=CheckinLibraryActivity.appStatus.getSharedStringValue(CheckinLibraryActivity.appStatus.LONG);
				if((lat!= null) && (lon!=null)) {
					latitude=Double.valueOf(lat);
					longitude=Double.valueOf(lon);
				}
				context.showDialog(0);
				new BusinessTask(context, onlyShowSupported).execute(latitude, longitude);
			}
		} else {
			context.message("Check internet connectivity!");
			Log.v("CHECKINFORGOOD", "App is not online!");
		}
	}*/
	
	 private void updateListStatus(ListStatus status) {
	        listStatus = status;
	    }
		
		private void updateListStatus() {
		    if ( listStatus == ListStatus.NEW ) {
		        listStatus = ListStatus.REFRESHED;
		    } else if ( listStatus == ListStatus.REFRESHED ) {
		        listStatus = ListStatus.BEST;
		    }
		}
		
		public void hideProgress() {
			context.removeDialog(0);
		}
}