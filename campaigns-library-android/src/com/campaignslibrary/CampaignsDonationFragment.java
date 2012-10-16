package com.campaignslibrary;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.checkinlibrary.CheckinLibraryActivity;

public class CampaignsDonationFragment extends Fragment {

	CheckinLibraryActivity context;
	private ListView donateList;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		context = (CheckinLibraryActivity) this.getActivity();

		View fragmentView = inflater.inflate(R.layout.cmpgn_donation_fragment, null);

		initAllComponents(fragmentView);
		initializeOnClickHandlers(fragmentView);

		return fragmentView;
	}

	private void initAllComponents(View v) {
		donateList = (ListView) v.findViewById(R.id.listDonation);
		
		ArrayList<String> donationArray = new ArrayList<String>();
		donationArray.add("Donate Online");
		donationArray.add("Donate via Text(SMS)");
		CmpgnDonationListAdapter mCmpgnDonationListAdapter = new CmpgnDonationListAdapter(context,donationArray);
		donateList.setAdapter(mCmpgnDonationListAdapter);
	}

	private void initializeOnClickHandlers(View v) {

	}
}
