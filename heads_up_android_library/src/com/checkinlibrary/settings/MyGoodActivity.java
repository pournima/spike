package com.checkinlibrary.settings;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.checkinlibrary.R;
import com.checkinlibrary.helpers.AppStatus;

public class MyGoodActivity extends Activity {

	TextView txtYouRaised;
	TextView txtTotalCheckins;
	TextView txtTotalCauseSupporting;
	AppStatus appStatus;

	String totalMyCauses;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mygood_headsup);
		appStatus = AppStatus.getInstance(this);
		initializeMyGoodData();


	}

	private void initializeMyGoodData(){
		txtYouRaised = (TextView)findViewById(R.id.txtYouRaised);
		txtTotalCheckins = (TextView)findViewById(R.id.txtTotalCheckins);		
		
		txtYouRaised.setText(appStatus.getSharedStringValue(appStatus.CHECKIN_AMOUNT).toString());
		txtTotalCheckins.setText(appStatus.getSharedStringValue(appStatus.CHECKIN_COUNT).toString());		
	}
	
}
