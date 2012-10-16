package com.checkinlibrary.settings;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.checkinlibrary.CheckinLibraryActivity;
import com.checkinlibrary.R;
import com.checkinlibrary.db.MyOrganizationDBAdapter;


public class MyGoodFragment extends Fragment {

	CheckinLibraryActivity context;
	
	TextView txtYouRaised;
	TextView txtTotalCheckins;
	TextView txtTotalCauseSupporting;
	
	String totalMyCauses;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.mygood_fragment, container, false);
		context = (CheckinLibraryActivity) this.getActivity();
		
		initializeMyGoodData(v);

		return v;

	}

	@Override
	public void onResume() {
		super.onResume();

	}
	
	public void initializeMyGoodData(View v){
		txtYouRaised = (TextView) v.findViewById(R.id.txtYouRaised);
		txtTotalCheckins = (TextView) v.findViewById(R.id.txtTotalCheckins);
		txtTotalCauseSupporting = (TextView) v.findViewById(R.id.txtTotalCauseSupporting);
		
		MyOrganizationDBAdapter adpt = new MyOrganizationDBAdapter(context);
		totalMyCauses = String.valueOf(adpt.getCount());
		
		
		txtYouRaised.setText((CheckinLibraryActivity.appStatus.getSharedStringValue(CheckinLibraryActivity.appStatus.CHECKIN_AMOUNT)).toString());
		txtTotalCauseSupporting.setText(totalMyCauses);
		
		txtTotalCheckins.setText((CheckinLibraryActivity.appStatus.getSharedStringValue(CheckinLibraryActivity.appStatus.CHECKIN_COUNT)).toString());
		
	}

}
