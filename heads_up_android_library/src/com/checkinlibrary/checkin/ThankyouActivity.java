package com.checkinlibrary.checkin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.checkinlibrary.R;
import com.checkinlibrary.TabGroupActivity;
import com.checkinlibrary.helpers.AppStatus;
import com.checkinlibrary.offers.ViewOffersActivity;

public class ThankyouActivity extends Activity {
	
	private String mBusinessName;
	private String mOrganizationName;
	TextView textView_name;
	TextView txtViewTopRaisedMoney;
	AppStatus appStatus;
	

	public void onResume() {
		txtViewTopRaisedMoney.setText(appStatus.setCheckinAmount());
		textView_name.setText("Thank you for checking in at "+mBusinessName + " on behalf of " + mOrganizationName);
		super.onResume();
	}
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.thankyou);
		appStatus = AppStatus.getInstance(this);
		txtViewTopRaisedMoney=(TextView)findViewById(R.id.textViewTopRaisedMoney);
		
		textView_name = (TextView)findViewById(R.id.textView_thankyou);
		
		
		Bundle b = getIntent().getExtras();
		
		mBusinessName = b.getString("business_name");
		mOrganizationName =b.getString("organization_name");
	}

	public void onFindMoreBusinessesClick(View v) {
		Intent intent;
		Log.i("############", "Find More Businesses clicked");
		TabGroupActivity parentActivity = (TabGroupActivity) getParent();

		intent = new Intent(getParent(), ViewOffersActivity.class);
		parentActivity.startChildActivity("Offers headsup", intent);
	}
	
	@Override
	public void onPause() {
		super.onPause();
	}
    
   
}