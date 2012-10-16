package com.checkinlibrary.settings;

import java.util.ArrayList;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.checkinlibrary.CheckinLibraryActivity;
import com.checkinlibrary.R;
import com.checkinlibrary.TabGroupActivity;
import com.checkinlibrary.helpers.AppStatus;

public class SettingsFragment extends Fragment {

	public ListView mListView;
	SettingsListAdapter mSettingsListAdapter;
	public static int itemPosition;
	public static AppStatus appStatus;
	ArrayList<String> settingItems;
	
	String[] settingsListItems = { "About USA Football", "Contact Us",
			"Sharing Settings", "My Good", "How to Check-in for Good",
			"Privacy Policy", "Terms of Use" };
	
	AboutFragment mAboutFragment;
	
	/*
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		TabGroupActivity parentActivity = (TabGroupActivity) getParent();
		Resources res = parentActivity.getResources();
		Intent intent = parentActivity.getIntent();
		setContentView(R.layout.settings);
		
		appStatus = AppStatus.getInstance(this);
		initializeCauseTypeList();
		mListView = (ListView) findViewById(android.R.id.list);
		mSettingsListAdapter = new SettingsListAdapter(SettingsFragment.this,
				settingItems);
		mListView.setAdapter(mSettingsListAdapter);
		mListView.setOnItemClickListener(itemClick);
		
		
		Log.i("########testing app status", String.valueOf(appStatus.getSharedStringValue(appStatus.AUTH_KEY)));	
		
	}
	*/
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v;
		v = inflater.inflate(R.layout.settings, container, false);
		setupButtons(v);
		return v;
	}
	private void setupButtons(View v) { 
		initializeCauseTypeList();
		mListView = (ListView)v.findViewById(android.R.id.list);
		mSettingsListAdapter = new SettingsListAdapter(((Context) this.getActivity()),
				settingItems);
		mListView.setAdapter(mSettingsListAdapter);
		mListView.setOnItemClickListener(itemClick);
	}
	/*public void createList(String... args){
		
		String[] listItems = args;
		
		
		mListView = (ListView) findViewById(android.R.id.list);
		mSettingsListAdapter = new SettingsListAdapter(SettingsActivity.this,
				listItems);
		mListView.setAdapter(mSettingsListAdapter);
		mListView.setOnItemClickListener(itemClick);
		
		appStatus = AppStatus.getInstance(this);
		Log.i("########testing app status", String.valueOf(appStatus.getSharedStringValue(appStatus.AUTH_KEY)));
		
	}
*/
	@Override
	public void onResume() {
		super.onResume();
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

	public OnItemClickListener itemClick = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position,
				long arg3) {
			/*
			TabGroupActivity parentActivity = (TabGroupActivity) getParent();
			Intent intent;
		
			switch (position) {
			case SettingsListAdapter.ABOUT:
				intent = new Intent(getParent(),
						AboutFragment.class);
				parentActivity.startChildActivity("About Us", intent);
				break;
			case SettingsListAdapter.CONTACT:
				itemPosition = SettingsListAdapter.CONTACT;
				intent = new Intent(getParent(),
						TermsOfServiceActivity.class);
				parentActivity.startChildActivity("Settings", intent);
				break;
			case SettingsListAdapter.SHARING:
				intent = new Intent(getParent(),
						SocialSharingActivity.class);
				parentActivity.startChildActivity("Social Sharing", intent);
				break;
			case SettingsListAdapter.MYGOOD:
				intent = new Intent(getParent(),
						MyGoodActivity.class);
				parentActivity.startChildActivity("MyGood", intent);
				break;
			case SettingsListAdapter.HOWTOCHECKIN:
				intent = new Intent(getParent(),
						HowItWorks.class);
				parentActivity.startChildActivity("How It Works", intent);
				break;
			case SettingsListAdapter.PRIVACY:
				itemPosition = SettingsListAdapter.PRIVACY;
				intent = new Intent(getParent(),
						TermsOfServiceActivity.class);
				parentActivity.startChildActivity("Settings", intent);
				break;
			case SettingsListAdapter.TERMSOFUSE:
				itemPosition = SettingsListAdapter.TERMSOFUSE;
				intent = new Intent(getParent(),
						TermsOfServiceActivity.class);
				parentActivity.startChildActivity("Settings", intent);
				break;

			default:
				break;
			}
			*/
		}

	};
	
	private void initializeCauseTypeList() {
		settingItems = new ArrayList<String>();
		for (int i = 0; i < settingsListItems.length; i++) {
			settingItems.add(settingsListItems[i]);
		}
		// --------------Extract the username -----------
		String mUserName = appStatus.getSharedStringValue(appStatus.USER_NAME);
		if (mUserName != null) {
			Log.i("ACCOUNT", mUserName);
			settingItems.add(mUserName);
		}
	}
/*
	public void gotoAboutUs()
	{
		if (mAboutFragment == null) {
			mAboutFragment = (AboutFragment) context.
					addFragment(R.id.linearLayout2,
							AboutFragment.class.getName(),
							"RuleDetailsFragment");
		} else {
			(context).attachFragment((Fragment) mAboutFragment);
		}
		
	}	
	*/
/*	
	  @Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	    	// TODO Auto-generated method stub
	    	super.onActivityResult(requestCode, resultCode, data);
	    	Log.i("SocialSharingActivity", "in on activity result");
	    }
*/
}
