package com.headsup.settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.checkinlibrary.CheckinLibraryActivity;
import com.checkinlibrary.R;
import com.checkinlibrary.settings.MyGoodFragment;
import com.headsup.HeadsUpNativeActivity;

public class MyGoodActivity extends MyGoodFragment {

	HeadsUpNativeActivity context;
	TextView txtYouRaised;
	TextView txtTotalCheckins;
	TextView txtTotalCauseSupporting;
	//AppStatus appStatus;

	String totalMyCauses;

	/*
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mygood_headsup);
		//appStatus = AppStatus.getInstance(this);
		initializeMyGoodData();


	}
*/
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.mygood_fragment, container, false);
		context = (HeadsUpNativeActivity) this.getActivity();
		
		initializeMyGoodData(v);

		return v;

	}
	
	public void initializeMyGoodData(View v){
		txtYouRaised = (TextView)v.findViewById(R.id.txtYouRaised);
		txtTotalCheckins = (TextView)v.findViewById(R.id.txtTotalCheckins);		
		
		txtYouRaised.setText(CheckinLibraryActivity.appStatus.getSharedStringValue(CheckinLibraryActivity.appStatus.CHECKIN_AMOUNT).toString());
		txtTotalCheckins.setText(CheckinLibraryActivity.appStatus.getSharedStringValue(CheckinLibraryActivity.appStatus.CHECKIN_COUNT).toString());		
	}
	
}
