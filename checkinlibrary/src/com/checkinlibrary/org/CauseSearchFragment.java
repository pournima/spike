package com.checkinlibrary.org;

import java.util.List;

import com.checkinlibrary.CheckinLibraryActivity;
import com.checkinlibrary.R;
import com.checkinlibrary.models.OrganizationResult;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.TextView;

public class CauseSearchFragment extends ListFragment {
	List<OrganizationResult> mList;
	CheckinLibraryActivity context;
	private String mQuery;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.cause_search_fragment, container,
				false);
	}

	@Override
	// Set up our tabs
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = (CheckinLibraryActivity) this.getActivity();
	}

	@Override
	public void onResume() {
		super.onResume();
		TextView tv = (TextView) context.findViewById(R.id.textViewSearchQuery);
		tv.setText(mQuery);
		displayCauses();
	}

	public void setList(List<OrganizationResult> list) {
		mList = list;
	}

	public void setQuery(String query) {
		mQuery = query;
	}

	public static void addSearchFragment(CheckinLibraryActivity context,
			List<OrganizationResult> result, String query) {
		CauseSearchFragment mCauseSearchFragment = (CauseSearchFragment) (context)
				.addFragment(R.id.linearLayout2,
						CauseSearchFragment.class.getName(),
						"cause_search_list");
		mCauseSearchFragment.setList((List<OrganizationResult>) result);
		mCauseSearchFragment.setQuery(query);
	}

	public void displayCauses() {
		Log.v("CHECKINFORGOOD", "Displaying search result");
		CauseListAdapter adapter = new CauseListAdapter(context, mList);
		this.setListAdapter((ListAdapter) adapter);
	}
}