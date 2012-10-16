package checkinforgood.com;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.List;

import android.app.SearchManager;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TabHost;
import android.widget.TextView;

import com.campaignslibrary.CampaignsFragment;
import com.campaignslibrary.CampaignsSearchFragment;
import com.campaignslibrary.helpers.CampaignSearcher;
import com.campaignslibrary.helpers.Constants;
import com.campaignslibrary.models.CreateCampaignResult;
import com.campaignslibrary.tasks.CampaignSearchTask;
import com.checkinlibrary.CheckinLibraryActivity;
import com.checkinlibrary.NoConnectivityScreen;
import com.checkinlibrary.R;
import com.checkinlibrary.Searcher;
import com.checkinlibrary.business.BusinessFragment;
import com.checkinlibrary.business.BusinessSearchFragment;
import com.checkinlibrary.helpers.AppStatus;
import com.checkinlibrary.helpers.MyConstants;
import com.checkinlibrary.location.GpsLocator;
import com.checkinlibrary.models.BusinessResult;
import com.checkinlibrary.models.OrganizationResult;
import com.checkinlibrary.org.CauseSearchFragment;
import com.checkinlibrary.org.OrganizationFragment;
import com.checkinlibrary.settings.HowItWorksFragment;
import com.checkinlibrary.ws.tasks.SearchTask;

public class CheckinNativeActivity extends CheckinLibraryActivity {
	/** Called when the activity is first created. */

	Bundle instanceState;
	public static MyConstants mConstants;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		instanceState=savedInstanceState;
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public void setupTabs(Bundle savedInstanceState) {
		
		Log.d("Checkin native", "*******##########");
		textViewRaisedMoney = (TextView) findViewById(R.id.textViewTopRaisedMoney);
		mTabHost = (TabHost) findViewById(android.R.id.tabhost);
		mTabHost.setup();
		mTabManager = new TabManager(this, mTabHost, R.id.linearLayout2);

		Resources res = getResources();

		mTabManager.addTab(
				mTabHost.newTabSpec("business_tab").setIndicator("Businesses",
						res.getDrawable(R.drawable.tabbarbusiness)),
				BusinessFragment.class, null);
		mTabManager.addTab(
				mTabHost.newTabSpec("organization_tab").setIndicator("Causes",
						res.getDrawable(R.drawable.tabbarcauses)),
				OrganizationFragment.class, null);
		
		mTabManager.addTab(
				mTabHost.newTabSpec("campaigns_tab").setIndicator("Campaigns",
						res.getDrawable(R.drawable.tabbarcauses)),
						CampaignsFragment.class, null);

		
		mTabManager.addTab(
				mTabHost.newTabSpec("setting_tab").setIndicator("Settings",
						res.getDrawable(R.drawable.tabbarsettings)),
				CheckinSettingFragment.class, null);

		
		if (instanceState != null) {
			mTabHost.setCurrentTabByTag(new Integer(instanceState
					.getInt("tab")).toString());
		}
		
	}
	
	@Override
	 protected void loginIfNeeded() {
		
		if (appStatus.isOnline()) {
			Log.v("CHECKIN_NATIVE_ACTIVITY", "App is online");
			mAuthToken = appStatus.getSharedStringValue(appStatus.AUTH_KEY);
			Log.v("auth_key", mAuthToken + " key");
			APP_NAME=appStatus.getSharedStringValue(appStatus.APP_NAME);
			setContentView(R.layout.main);
			setupTabs(instanceState);
			setCheckinAmount();
			if (isFromLogin) {
				CheckinNativeActivity.this.mTabHost.setCurrentTab(3);
				goToHowItWorksFragment();
			}
		} else {
			Intent intent = new Intent(this, NoConnectivityScreen.class);
			this.startActivity(intent);
			finish();
			Log.v("CHECKIN_NATIVE_ACTIVITY", "You are not online!!!!");
		}
		
	}
	
	@Override
	protected void goToHowItWorksFragment() {

		HowItWorksFragment mHowItWorksFragment = (HowItWorksFragment) this
				.addFragment(R.id.linearLayout2,
						HowItWorksFragment.class.getName(),
						"how_it_works_fragment");

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		// setMenuBackground();
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.businesses:
			if (this.mTabHost.getCurrentTab() == 0) {
				this.mTabHost.setCurrentTab(1);
			}
			this.mTabHost.setCurrentTab(0);
			break;
		case R.id.causes:
			if (this.mTabHost.getCurrentTab() == 1) {
				this.mTabHost.setCurrentTab(2);
			}
			this.mTabHost.setCurrentTab(1);
			break;
		case R.id.campaigns:
			if (this.mTabHost.getCurrentTab() == 2) {
				this.mTabHost.setCurrentTab(3);
			}
			this.mTabHost.setCurrentTab(2);
			break;
		case R.id.settings:
			// When we go to "How it works fragment" by clicking How it works
			// menu item, from there
			// if we click Settings menu item then Settings main fragment will
			// not open but shows same
			// how it works fragment since there is no tab changed occurred.
			if (this.mTabHost.getCurrentTab() == 3) {
				this.mTabHost.setCurrentTab(1);
			}
			this.mTabHost.setCurrentTab(3);
			break;
		case R.id.howItWorks:		
			this.mTabHost.setCurrentTab(3);
			goToHowItWorksFragment();
			break;
		}
		return true;
	}

	
	@Override
	public boolean onSearchRequested() {
		Bundle searchBundle = new Bundle();
		Fragment fragment = getLastFragment();
		gps = GpsLocator.getGpsLocator(this);
		if(gps.isProviderEnabled()){
			if (fragment.getTag() == "business_tab") {
				searchBundle.putInt("search_type",
						Searcher.SearchType.BUSINESS.ordinal());
				startSearch(null, false, searchBundle, false);
			} else if (fragment.getTag() == "organization_tab") {
				searchBundle.putInt("search_type",
						Searcher.SearchType.CAUSE.ordinal());
				startSearch(null, false, searchBundle, false);
			}	else if (fragment.getTag() == "tab_offers") {
				searchBundle.putInt("search_type",
						Searcher.SearchType.BUSINESS.ordinal());
				startSearch(null, false, searchBundle, false);
			} else if (fragment.getTag() == "OfficailSignalsFragment") {
				searchBundle.putInt("search_type",
						Searcher.SearchType.SIGNALS.ordinal());
				startSearch(null, false, searchBundle, false);
			}if(fragment.getTag() == "campaigns_tab"){
				searchBundle.putInt("search_type",
						CampaignSearcher.SearchType.CAMPAIGN.ordinal());
				startSearch(null, false, searchBundle, false);
			}	
		}else{
			listenForLocation();
		}
		
		return true;
	}

	@Override
	protected void onNewIntent(Intent intent) {
		Intent oldIntent = this.getIntent();
		setIntent(intent);

		if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
			String query = intent.getStringExtra(SearchManager.QUERY);
			Bundle searchBundle = intent.getBundleExtra(SearchManager.APP_DATA);
			if(searchBundle != null){
				
				Searcher.SearchType type = Searcher.SearchType.values()[searchBundle
				                                                        .getInt("search_type")];
				CheckinLibraryActivity.strProgressMessage=getString(R.string.searchProgressMessage);
				this.showDialog(0);
				if(type == Searcher.SearchType.CAMPAIGN){
					new CampaignSearchTask(this, type).execute(query);
				}else{
					new SearchTask(this, type).execute(query);
				}
			}
		}

		// Set our intent back once the search display is done
		setIntent(oldIntent);
	}

	@SuppressWarnings("unchecked")
	public void onSearchCompleted(List<? extends Object> result,
			Searcher.SearchType type, String query) {
		if (type == Searcher.SearchType.CAUSE) {
			CauseSearchFragment.addSearchFragment(this,
					(List<OrganizationResult>) result, query);
		}else if (type == Searcher.SearchType.CAMPAIGN) {
			CampaignsSearchFragment.addCmpgnSearchFragment(this, (List<CreateCampaignResult>) result, query);
		} else {
			BusinessSearchFragment.addBusinessFragment(this,
					(List<BusinessResult>) result, query);
		}

		this.removeDialog(0);
	}
	@Override
	public void onActivityResult(final int requestCode, final int resultCode,
			final Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 6186 && resultCode == 0) {
			gps.destroyGpsDialog();
			AppStatus.getInstance(this).GPS_STATUS_ON = true;
			startActivity(new Intent(this, CheckinNativeActivity.class));
		}
		if (resultCode == Constants.INSTANCE.REQUEST_CODE) {
			if (data != null) {
				String scanResult = data.getStringExtra("SCAN_RESULT");
				if (scanResult != null) {
					appStatus.mFromQrCode = true;
					appStatus.mQrCodeResult = scanResult;
				} else {
					appStatus.mFromQrCode = false;
				}
			}
		} else if (requestCode == Constants.INSTANCE.SELECT_PHOTO_FROM_GALLERY && resultCode == RESULT_OK) {
			if (data != null) {
				Uri selectedImage = data.getData();
				InputStream imageStream = null;
				try {
					imageStream = getContentResolver().openInputStream(selectedImage);
					Bitmap yourSelectedImage = BitmapFactory.decodeStream(imageStream);
					Constants.INSTANCE.SELECTED_PHOTO=yourSelectedImage;
					Constants.INSTANCE.mFromPhotoSelection=true;
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Constants.INSTANCE.mFromPhotoSelection=false;
				}
			}else{
				Constants.INSTANCE.mFromPhotoSelection=false;
			}
		}else if ( requestCode == Constants.INSTANCE.SELECT_PHOTO_FROM_CAMERA && resultCode == RESULT_OK) {
			if (data != null) {
				String filename = String.valueOf(Calendar.getInstance().getTimeInMillis());
				Bitmap yourSelectedImage = (Bitmap) data.getExtras().get("data");
				Constants.INSTANCE.SELECTED_PHOTO=yourSelectedImage;
				Constants.INSTANCE.mFromPhotoSelection=true;
			}else{
				Constants.INSTANCE.mFromPhotoSelection=false;
			}
		}else {
			appStatus.mFromQrCode = false;
			Constants.INSTANCE.mFromPhotoSelection=false;
		}

	}
}
