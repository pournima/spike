package com.headsup.settings;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.checkinlibrary.CheckinLibraryActivity;
import com.checkinlibrary.R;
import com.checkinlibrary.settings.SettingFragment;
import com.checkinlibrary.settings.SettingsListAdapter;
import com.checkinlibrary.settings.ShareFragment;
import com.headsup.HeadsUpNativeActivity;

public class SettingsFragment extends SettingFragment {

	public ListView mListView;
	SettingsListAdapter mSettingsListAdapter;
	public static int itemPosition;
	//public static AppStatus appStatus;
	ArrayList<String> settingItems;

	HowItWorks mHowItWorks;
	ShareFragment mShareFragment;
	HtmlHeadsupPageFragment mHtmlHeadsupPageFragment;
	MyGoodActivity mMyGoodActivity;
	AboutUsFragment mAboutFragment;
	HeadsUpNativeActivity context ;
	public static final int ABOUT = 0;
	public static final int CONTACT = 1;
	public static final int SHARING = 2;
	public static final int MYGOOD = 3;
	public static final int HOWTOCHECKIN = 4;
	public static final int PRIVACY = 5;
	public static final int TERMSOFUSE = 6;
	public static final int C4GABOUT = 7;
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View v = inflater.inflate(R.layout.settings, null);
		context = (HeadsUpNativeActivity) this.getActivity();
		initializeCauseTypeList();

		return v;
	}
	
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	@Override
	public void onResume() {
		super.onResume();
		initializeCauseTypeList();
		SettingsListAdapter adapter = new SettingsListAdapter(
				((Context) this.getActivity()), settingItems);
		this.setListAdapter((ListAdapter) adapter);
	}
	
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}
	
	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}

	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
	
		switch (position) {
		
		case ABOUT:
			onAboutClick();
			break;
		case CONTACT:
			itemPosition =CONTACT;
			onHtmlPageClick();
			break;
		case SHARING:
			onShareSettingsClick();
			break;
		case MYGOOD:
			onMyGoodClicked();
			break;
		case HOWTOCHECKIN:
			onHowItWorksSettingsClick();
			break;
		case PRIVACY:
			itemPosition = PRIVACY;
			onHtmlPageClick();
			break;
		case TERMSOFUSE:
			itemPosition = TERMSOFUSE;
			onHtmlPageClick();
			break;

		default:
			break;
		}
	}

	@Override
	public void initializeCauseTypeList() {
		
		String[] settingsListItems = { "About USA Football", "Contact Us",
				"Sharing Settings", "My Good", "How to Check-in for Good",
				"Privacy Policy", "Terms of Use" };
		
		settingItems = new ArrayList<String>();
			
		for (int i = 0; i < settingsListItems.length; i++) {
			settingItems.add(settingsListItems[i]);
		}
		// --------------Extract the username -----------
		String mUserName = CheckinLibraryActivity.appStatus.getSharedStringValue(CheckinLibraryActivity.appStatus.USER_NAME);
		if (mUserName != null) {
			Log.i("ACCOUNT", mUserName);
			settingItems.add(mUserName);
		}
	}
	
	
	private void onAboutClick(){
		if (mAboutFragment == null) {
			mAboutFragment = (AboutUsFragment) (context).addFragment(
					R.id.linearLayout2, AboutUsFragment.class.getName(),
					"about_fragment");
		} else {
			(context).attachFragment((Fragment) mAboutFragment);
		}
	}
	
	@Override
	public void onHowItWorksSettingsClick() {
		if (mHowItWorks == null) {
			mHowItWorks = (HowItWorks) (context).addFragment(
					R.id.linearLayout2, HowItWorks.class.getName(),
					"how_it_works");
		} else {
			(context).attachFragment((Fragment) mHowItWorks);
		}
	}

	@Override
	public void onShareSettingsClick() { 
		if (mShareFragment == null) {
			mShareFragment = (ShareFragment) (context).addFragment(
					R.id.linearLayout2, ShareFragment.class.getName(),
					"share_fragment");
		} else {
			(context).attachFragment((Fragment) mShareFragment);
		}
	}
	
	@Override
	public void onHtmlPageClick() {

		if (mHtmlHeadsupPageFragment == null) {
			mHtmlHeadsupPageFragment = (HtmlHeadsupPageFragment) (context)
					.addFragment(R.id.linearLayout2,
							HtmlHeadsupPageFragment.class.getName(),
							"html_headsup_page_fragment");
		} else {
			(context).attachFragment((Fragment) mHtmlHeadsupPageFragment);
		}
	}

	@Override
	public void onMyGoodClicked() {
		if (mMyGoodActivity == null) {
			mMyGoodActivity = (MyGoodActivity) (context).addFragment(
					R.id.linearLayout2, MyGoodActivity.class.getName(),
					"my_good_fragment");
		} else {
			(context).attachFragment((Fragment)mMyGoodActivity);
		}	
		
	}
/*	
	  @Override
	    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    	// TODO Auto-generated method stub
	    	super.onActivityResult(requestCode, resultCode, data);
	    	Log.i("SocialSharingActivity", "in on activity result");
	    }
*/
}
