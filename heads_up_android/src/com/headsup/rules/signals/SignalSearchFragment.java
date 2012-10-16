package com.headsup.rules.signals;

import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.checkinlibrary.CheckinLibraryActivity;
import com.headsup.HeadsUpNativeActivity;
import com.headsup.R;
import com.headsup.models.SignalsResult.SignalCategory;

public class SignalSearchFragment extends SignalsCategoryFragment {

	private String mQuery;
	static List<SignalCategory> mList;
	Fragment fragment;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.official_signal_type, container, false);
		initializeWidgets(v);
		gridViewsignal.setAdapter(new SignalsImageAdapter(context,mList));
		onGridImageClicked(v);
		return v;
	}

	private void initializeWidgets(View v) {
		mTextSignalDivider=(TextView) v.findViewById(R.id.signalDivider);
		lnrOfficialSignalHeader=(LinearLayout)v.findViewById(R.id.lnrOfficialSignalHeader);
		btnSort = (Button)v.findViewById(R.id.btnSort);
		lnrSignalsList = (LinearLayout)v.findViewById(R.id.lnrOfficialSignalList);
		
		txtSignalTypeHeader = (TextView)v.findViewById(R.id.txtSignalTypeHeader);
		
		gridViewsignal = (GridView)v.findViewById(R.id.signalGridView);
		
		btnScoring = (Button)v.findViewById(R.id.btnScoring);
		btnDeadBall = (Button)v.findViewById(R.id.btnDeadBall);
		btnLiveBall = (Button)v.findViewById(R.id.btnLiveBall);
		btnConduct = (Button)v.findViewById(R.id.btnConduct);
		
		btnSort.setTag(BTN_SORT);onButtonClick(btnSort,"");
		btnScoring.setTag(BTN_SCORING);onButtonClick(btnScoring,"Scoring & signals");
		btnDeadBall.setTag(BTN_DBALL);onButtonClick(btnDeadBall,"Dead Ball");
		btnLiveBall.setTag(BTN_LBALL);onButtonClick(btnLiveBall,"Live Ball");
		btnConduct.setTag(BTN_CNT);onButtonClick(btnConduct,"Conduct");		
	}
	@Override
	public void onResume() {
		super.onResume();
		if(mList.size() == 0){
			txtSignalTypeHeader.setText("No signal for searched: "+mQuery);
		}else
		txtSignalTypeHeader.setText("You have searched: "+mQuery);
	}

	public void setQuery(String query) {
		mQuery = query;
	}

	public static void addSignalsList(HeadsUpNativeActivity context, List<SignalCategory> result, String query) {
		Log.i("#######", "Clicked");	
		SignalSearchFragment mSignalSearchFragment = (SignalSearchFragment) (context).addFragment(
				R.id.linearLayout2, SignalSearchFragment.class.getName(),
				"signal_search_fragment");
		mList = result;
		mSignalSearchFragment.setQuery(query);	

	}

	public void onGridImageClicked(View v) {
		gridViewsignal.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
				if(CheckinLibraryActivity.appStatus.isOnline()) {
					loadPhoto(v, mList.get(position));
				}else {
					Toast toast = Toast.makeText(context, "Please connect to cellular data or Wi-fi", Toast.LENGTH_LONG);
					toast.show();
				}
			}
		});
	}
	
	@Override
	public void onPause() {
		super.onPause();
	}


}
