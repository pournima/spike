package com.headsup.safety;
/*
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.checkinlibrary.TabGroupActivity;
import com.checkinlibrary.helpers.AppStatus;
import com.headsup.helpers.MyConstants;

public class TabGroupSafetyActivity extends TabGroupActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.i("Health and Safety", "on Resume");
		AppStatus appStatus = AppStatus.getInstance(this);
		if(appStatus.getSharedBoolValue(appStatus.SAFETY_TAB_CLICKED)){
			mIdList.clear();
			startChildActivity(MyConstants.ACT_HEALTH_SAFETY, new Intent(
					TabGroupSafetyActivity.this, SafetyFragment.class));
			appStatus.saveSharedBoolValue(appStatus.SAFETY_TAB_CLICKED,false);
		}

	}
}
*/