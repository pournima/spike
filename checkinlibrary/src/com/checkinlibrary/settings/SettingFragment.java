package com.checkinlibrary.settings;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.checkinlibrary.CheckinLibraryActivity;
import com.checkinlibrary.R;


public class SettingFragment extends ListFragment {

	ShareFragment mShareFragment;
	HowItWorksFragment mHowItWorksFragment;
	HtmlPageFragment mHtmlPageFragment;
	MyGoodFragment myGoodFragment;
		
	public CheckinLibraryActivity context;
	public ArrayList<String> settingItems;


	public static final int SHARE_FRG = 0;
	public static final int MY_GOOD = 1;
	public static final int HOW_IT_WORKS_FRG = 2;
	public static final int CONTACTS_FRG = 3;
	public static final int PRIPOL_FRG = 4;
	public static final int TERMS_FRG = 5;

	public static int itemPosition;
	
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
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		context = (CheckinLibraryActivity) this.getActivity();
		initializeCauseTypeList();

		View v = inflater.inflate(R.layout.setting_fragment, null);
		return v;
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {

		switch (position) {
		case SHARE_FRG:
			onShareSettingsClick();
			break;
		case MY_GOOD:
			onMyGoodClicked();
			break;
		case HOW_IT_WORKS_FRG:
			onHowItWorksSettingsClick();
			break;
		case CONTACTS_FRG:
			itemPosition = CONTACTS_FRG;
			onHtmlPageClick();
			break;
		case PRIPOL_FRG:
			itemPosition = PRIPOL_FRG;
			onHtmlPageClick();
			break;
		case TERMS_FRG:
			itemPosition = TERMS_FRG;
			onHtmlPageClick();
			break;
		}
	}

	public void onHowItWorksSettingsClick() {
		if (mHowItWorksFragment == null) {
			mHowItWorksFragment = (HowItWorksFragment) (context).addFragment(
					R.id.linearLayout2, HowItWorksFragment.class.getName(),
					"how_it_works_fragment");
		} else {
			(context).attachFragment((Fragment) mHowItWorksFragment);
		}
	}

	public void onShareSettingsClick() {
		if (mShareFragment == null) {
			mShareFragment = (ShareFragment) (context).addFragment(
					R.id.linearLayout2, ShareFragment.class.getName(),
					"share_fragment");
		} else {
			(context).attachFragment((Fragment) mShareFragment);
		}
	}

	public void onHtmlPageClick() {

		if (mHtmlPageFragment == null) {
			mHtmlPageFragment = (HtmlPageFragment) (context)
					.addFragment(R.id.linearLayout2,
							HtmlPageFragment.class.getName(),
							"html_page_Fragment");
		} else {
			(context).attachFragment((Fragment) mHtmlPageFragment);
		}
		
		
	}

	public void onMyGoodClicked() {
		if (myGoodFragment == null) {
			myGoodFragment = (MyGoodFragment) (context).addFragment(
					R.id.linearLayout2, MyGoodFragment.class.getName(),
					"my_good_fragment");
		} else {
			(context).attachFragment((Fragment)myGoodFragment);
		}

	}
	
	public void initializeCauseTypeList() {

		settingItems = new ArrayList<String>();
		settingItems.add("Sharing Settings");
		settingItems.add("My Good");
		settingItems.add("How Check-in for Good Works");
		settingItems.add("Support & Contact Us");
		settingItems.add("Privacy Policy");
		settingItems.add("Terms Of Service");

		// --------------Extract the username -----------
		String mUserName = CheckinLibraryActivity.appStatus
				.getSharedStringValue(CheckinLibraryActivity.appStatus.USER_NAME);
		if (mUserName != null) {
			Log.i("ACCOUNT", mUserName);
			settingItems.add(mUserName);
		}
	}
}