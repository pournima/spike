package com.checkinlibrary.org;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.BufferType;

import com.checkinlibrary.CheckinLibraryActivity;
import com.checkinlibrary.NoConnectivityScreen;
import com.checkinlibrary.R;
import com.checkinlibrary.db.MyOrganizationDBAdapter;
import com.checkinlibrary.db.OrganizationDbAdapter;
import com.checkinlibrary.location.GpsFix;
import com.checkinlibrary.location.GpsFix.GpsFixStatus;
import com.checkinlibrary.location.GpsFragment;
import com.checkinlibrary.location.GpsLocator;
import com.checkinlibrary.models.OrganizationResult;
import com.checkinlibrary.ws.tasks.OrganizationListTask;


public class CauseFragment extends ListFragment implements GpsFragment {
	List<OrganizationResult> mList;
	GpsLocator gps;
	GpsFix gpsFix;
	OrganizationCacheManager cacheManager;
	CheckinLibraryActivity context;
	TimedDialogAdapter timedDialog;
	private TextView textViewTopMsg;
	ListView mListView;
	Boolean loadingMoreCauses = false;
	Boolean bIsLastCausePage = false;
	Boolean showSupported = false;
	public int iLastVisibleItemPosition = 0;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View fragmentView = inflater.inflate(R.layout.cause_fragment,
				container, false);
		initialization(fragmentView);

		mListView = (ListView) fragmentView.findViewById(android.R.id.list);

		if (!showSupported)
			setOnListScrollListener(mListView);
		bIsLastCausePage = false;
		return fragmentView;
	}

	private void initialization(View v) {
		if (this.getTag().compareTo("my_cause_fragment") == 0) {
			textViewTopMsg = (TextView) v
					.findViewById(R.id.textViewCausesTopMsg);
			textViewTopMsg.setVisibility(EditText.GONE);
		}
	}

	@Override
	// Set up our tabs
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		context = (CheckinLibraryActivity) this.getActivity();
		showSupported = this.getArguments().getBoolean("supported");
		// Maintained cache only for all causes
		if (!showSupported) {
			cacheManager = new OrganizationCacheManager(context);
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		CheckinLibraryActivity.strProgressMessage = getString(R.string.causeProgressMessage);
		showSupported = this.getArguments().getBoolean("supported");
		int waitSecs = 20;

		mListView.setVisibility(ListView.VISIBLE);

		gpsFix = new GpsFix(waitSecs, 100000, 10800);
		gps = GpsLocator.getGpsLocator((CheckinLibraryActivity) this
				.getActivity());
		
		boolean bMyCauseUpgradeNeeded=CheckinLibraryActivity.appStatus.getSharedBoolValue(CheckinLibraryActivity
				.appStatus.MY_CAUSES_UPGRADE_NEEDED);
		boolean bAllCauseUpgradeNeeded=CheckinLibraryActivity.appStatus.getSharedBoolValue(CheckinLibraryActivity
				.appStatus.ALL_CAUSE_UPGRADE_NEEDED);

		if (showSupported) {
			// Show supported without GPS
			if (!bMyCauseUpgradeNeeded) {
				fetchMyCausesFromDB();
			} else {
				getMyCauseList();
			}
		} else if (!bAllCauseUpgradeNeeded) {
			if (!cacheManager.cacheExpired()) {
				getCauseList();
				Log.e("CHECKINFORGOOD", "Getting CAUSE from unexpired cache");
			} else if (gpsFix.getFix(gps.getBestLocation()) == GpsFixStatus.OK_FIX) {
				Log.e("CHECKINFORGOOD", "Getting CAUSE with ok fix");
				if (cacheManager.cacheExpired()) {
					getCauseList(gps.getBestLocation());
				} else
					getCauseList();
			} else {
				// If we have anything in the cache whatsoever, show it. Then
				// add
				// our observer to improve it.
				if (cacheManager.getCacheFromPrefs() > 0) {
					if (cacheManager.cacheExpired()) {
						getCauseList(gps.getBestLocation());
					}
					Log.e("CHECKINFORGOOD", "Getting CAUSE list anew");
				} else {
					timedDialog = new TimedDialogAdapter(
							new TimedCauseNotFoundRunnable(this), getActivity());
					timedDialog.showDialog(waitSecs, "Searching for location");
				}
				gps.addObserver(this);
			}
		} else {
			getCauseList(gps.getBestLocation());
		}
	}

	public void setList(List<OrganizationResult> list) {
		mList = list;
	}

	@Override
	public void onLocationUpdated(Location location) {
		// We've got a timer ready to explode. Stop it from displaying
		// "Location not found" message.
		if (timedDialog != null)
			timedDialog.stopDialogAndItsCallback();
		// Stop listening. This doesn't need to be super accurate
		// this.timedDialog.stopDialogAndItsCallback();
		gps.removeObserver(this);
		GpsFixStatus fixStatus = gpsFix.isAcceptableFix(location);
		Log.v("CHECKINFORGOOD", "Location updated cause fragment status: "
				+ fixStatus.toString());

		if (fixStatus == GpsFixStatus.OK_FIX) {
			getCauseList(location);
		} else if (fixStatus == GpsFixStatus.TIMED_OUT) {
			// Show location not found error page.
		}
	}

	// Get a cached list of causes, no location needed.
	private void getCauseList() {

		// Call allCauses from DB
		OrganizationDbAdapter allAdpt = new OrganizationDbAdapter(context);
		List<OrganizationResult> allCauses = new ArrayList<OrganizationResult>();
		allCauses = allAdpt.getList(showSupported);
		displayCauses(allCauses);

	}

	// Show progress dialog while we pull from the server.
	private void getCauseList(Location location) {
		if(CheckinLibraryActivity.appStatus.isOnline()) {
			// pull from the server only when it is all causes
			if (!showSupported && (location != null)) {
				Double[] latLng = new Double[] { location.getLatitude(),
						location.getLongitude() };
				CheckinLibraryActivity.strProgressMessage = getString(R.string.causeProgressMessage);
				context.showDialog(0);

				try {
					new OrganizationListTask(this, 1, false).execute(latLng);
					iLastVisibleItemPosition = 0;
					CheckinLibraryActivity.appStatus.saveSharedIntValue(
							CheckinLibraryActivity.appStatus.ALL_ORG_CUR_PAGE, 2);
				} catch (Exception e) {
					Log.e("CHECKINFORGOOD",
							"Exception "
									+ e.getClass().getName()
									+ "thrown in CauseFragment::getCauseList(Location location) "
									+ " with " + e.getMessage());
					context.removeDialog(0);
				}
			}
		}else{
			Intent intent= new Intent(context,NoConnectivityScreen.class);
			this.startActivity(intent);
			context.finish();
		}
	}

	public void storeAllCausesInDB(List<OrganizationResult> list) {
		loadingMoreCauses = false;
		if (list != null && list.size() > 0) {
			CheckinLibraryActivity.appStatus.saveSharedBoolValue(CheckinLibraryActivity.appStatus.
					ALL_CAUSE_UPGRADE_NEEDED, false);
			cacheManager.setCacheToPrefs(new Date().getTime());
			OrganizationDbAdapter adapter = new OrganizationDbAdapter(context);
			int iCurrentPageNo = 1;
			iCurrentPageNo = CheckinLibraryActivity.appStatus
					.getSharedIntValue(CheckinLibraryActivity.appStatus.ALL_ORG_CUR_PAGE);
			adapter.refresh(list, showSupported, iCurrentPageNo);
			list = adapter.getList(showSupported);

			displayCauses(list);
		} else {
			bIsLastCausePage = true;
			cacheManager.setCacheToPrefs(0);
			context.removeDialog(0);
		}
	}

	public void displayCauses(List<OrganizationResult> result) {
		Log.e("CHECKINFORGOOD", "Setting the list adapters ");
		if (result == null) {

			timedDialog = new TimedDialogAdapter(
					new TimedCauseNotFoundRunnable(this), getActivity());
			timedDialog.showDialog(20, "Searching for location");
		} else {
			mList = result;
			CauseListAdapter adapter = new CauseListAdapter(context, mList);
			this.setListAdapter((ListAdapter) adapter);
			if ((result.size() % 10) == 0) {
				mListView.setSelection(iLastVisibleItemPosition);
			} else {
				mListView.setSelection(iLastVisibleItemPosition - 10);
			}
		}
		context.removeDialog(0);
	}

	@Override
	public void onPause() {
		if (timedDialog != null && timedDialog.canSee())
			timedDialog.stopDialogAndItsCallback();
		gps.removeObserver(this);
		super.onPause();
	}

	private void fetchMyCausesFromDB() {
		MyOrganizationDBAdapter adpt = new MyOrganizationDBAdapter(context);
		List<OrganizationResult> myCauses = new ArrayList<OrganizationResult>();
		myCauses = adpt.getList();

		if (myCauses.size() > 0) {
			displayCauses(myCauses);
		} else {
			textViewTopMsg.setVisibility(EditText.VISIBLE);
			textViewTopMsg.setText(this.getAllCausesLink(),
					BufferType.SPANNABLE);
			textViewTopMsg.setMovementMethod(LinkMovementMethod.getInstance());
			mListView.setVisibility(ListView.GONE);
		}
	}

	private SpannableStringBuilder getAllCausesLink() {
		CharSequence sequence = Html
				.fromHtml("You aren't currently supporting any causes.<br/><u>Add causes now</u> to start helping the causes that matter to you.");
		SpannableStringBuilder strBuilder = new SpannableStringBuilder(sequence);
		UnderlineSpan[] underlines = strBuilder.getSpans(0,
				strBuilder.length(), UnderlineSpan.class);

		for (UnderlineSpan span : underlines) {
			int start = strBuilder.getSpanStart(span);
			int end = strBuilder.getSpanEnd(span);
			int flags = strBuilder.getSpanFlags(span);

			ClickableSpan causesLink = new ClickableSpan() {
				public void onClick(View view) {
					Bundle bundle = new Bundle();
					bundle.putBoolean("supported", false);
					context.addFragment(R.id.linearLayout2,
							CauseFragment.class.getName(),
							"all_cause_fragment", bundle);
				}
			};

			strBuilder.setSpan(causesLink, start, end, flags);
		}

		return strBuilder;
	}

	private void setOnListScrollListener(ListView list) {
		list.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {

				// what is the bottom item that is visible
				int lastInScreen = firstVisibleItem + visibleItemCount;

				// is the bottom item visible & not loading more already ? Load
				// more !
				Log.i("Endless List", "########total Item" + totalItemCount);
				if ((totalItemCount != 0) && (lastInScreen == totalItemCount)
						&& !(loadingMoreCauses)
						&& (lastInScreen > visibleItemCount)) {
					Log.i("Endless List", "########getting 10 more List items");
					if (!bIsLastCausePage) {
						getMoreCause();
						iLastVisibleItemPosition = view
								.getLastVisiblePosition();
					}
				}
			}
		});

	}

	private void getMoreCause() {
		// Set flag so we can't load new items 2 at the same time
		if (CheckinLibraryActivity.appStatus.isOnline()) {
			loadingMoreCauses = true;
			int iCurrentPageNo;

			iCurrentPageNo = CheckinLibraryActivity.appStatus
					.getSharedIntValue(CheckinLibraryActivity.appStatus.ALL_ORG_CUR_PAGE);
			Log.i("Checkin",
					"#########Current Page = " + String.valueOf(iCurrentPageNo));
			// pull from the server only when it is all causes
			if (!showSupported) {

				double latitude = 0, longitude = 0;

				String lat = CheckinLibraryActivity.appStatus
						.getSharedStringValue(CheckinLibraryActivity.appStatus.LAT);
				String lon = CheckinLibraryActivity.appStatus
						.getSharedStringValue(CheckinLibraryActivity.appStatus.LONG);
				if ((lat != null) && (lon != null)) {
					latitude = Double.valueOf(lat);
					longitude = Double.valueOf(lon);
				}

				Double[] latLng = new Double[] { latitude, longitude };
				CheckinLibraryActivity.strProgressMessage = getString(R.string.causeProgressMessage);
				context.showDialog(0);

				try {
					new OrganizationListTask(this, iCurrentPageNo, false)
					.execute(latLng);
				} catch (Exception e) {
					Log.e("CHECKINFORGOOD",
							"Exception "
									+ e.getClass().getName()
									+ "thrown in CauseFragment::getCauseList(Location location) "
									+ " with " + e.getMessage());
					context.removeDialog(0);
				}
			}
			iCurrentPageNo++;

			CheckinLibraryActivity.appStatus.saveSharedIntValue(
					CheckinLibraryActivity.appStatus.ALL_ORG_CUR_PAGE,
					iCurrentPageNo);
		} else {
			Log.v("CHECKINFORGOOD", "App is not online!");
			Intent intent = new Intent(context, NoConnectivityScreen.class);
			context.startActivity(intent);
			context.finish();
		}
	}

	private void getMyCauseList() {

		double latitude = 0, longitude = 0;
		if(CheckinLibraryActivity.appStatus.isOnline()) {

			String lat = CheckinLibraryActivity.appStatus
					.getSharedStringValue(CheckinLibraryActivity.appStatus.LAT);
			String lon = CheckinLibraryActivity.appStatus
					.getSharedStringValue(CheckinLibraryActivity.appStatus.LONG);
			if ((lat != null) && (lon != null)) {
				latitude = Double.valueOf(lat);
				longitude = Double.valueOf(lon);
			}
			Double[] latLng = new Double[] { latitude, longitude };
			context.showDialog(0);

			try {
				new OrganizationListTask(this, 1, true).execute(latLng);
			} catch (Exception e) {
				Log.e("CHECKINFORGOOD",
						"Exception "
								+ e.getClass().getName()
								+ "thrown in CauseFragment::getMyCauseList(Location location) "
								+ " with " + e.getMessage());
				// context.removeDialog(0);
			}
		}else{
			Intent intent= new Intent(context,NoConnectivityScreen.class);
			this.startActivity(intent);
			context.finish();
		}
	}

	public void storeMyCausesinDB(List<OrganizationResult> list) {
		if (list != null && list.size() > 0) {
			CheckinLibraryActivity.appStatus.saveSharedBoolValue(CheckinLibraryActivity.appStatus.
					MY_CAUSES_UPGRADE_NEEDED, false);
			MyOrganizationDBAdapter adapter = new MyOrganizationDBAdapter(
					context);
			adapter.refresh(list);
			context.removeDialog(0);
			fetchMyCausesFromDB();
		} else {
			bIsLastCausePage = true;
			context.removeDialog(0);
			CheckinLibraryActivity.appStatus.saveSharedBoolValue(CheckinLibraryActivity.appStatus.
					MY_CAUSES_UPGRADE_NEEDED, false);
			fetchMyCausesFromDB();
		}

	}
}
