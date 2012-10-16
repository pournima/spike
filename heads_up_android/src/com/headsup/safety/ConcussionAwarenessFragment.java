package com.headsup.safety;

import java.util.ArrayList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;

import com.checkinlibrary.NoConnectivityScreen;
import com.checkinlibrary.helpers.AppStatus;
import com.headsup.HeadsUpNativeActivity;
import com.headsup.R;
import com.headsup.helpers.XmlID;
import com.headsup.helpers.XmlUrl;
import com.headsup.models.Entry;
import com.headsup.task.XmlTask;

public class ConcussionAwarenessFragment extends Fragment {


	HeadsUpNativeActivity context;
	ListView mListViewVideo;
	ListView mListView;
	InjuryPreventionListAdapter mHeatPreparednessListAdapter;
	HeatPreparednessVideoListAdapter mHeatPreparednessVIdeoListAdapter;
	Entry mEntry = new Entry();
	ArrayList<Entry> heatpreparednessResult;
	private int[] concussionAwarenessIcons = { R.drawable.concussion_awareness_video };
	ProgressDialog progressBar;
	AppStatus appStatus;
	private static final String strActivityFlag = "ConcussionAwareness";
	private int noOfItem;
	public static final int ACTION_PLAN = 0;
	public static final int VIDEO_SERIES = 1;
	XmlDescriptionFragment mXmlDescriptionFragment;
	TextView txtEmpty;
	
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		context = (HeadsUpNativeActivity) this.getActivity();
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.heat_preparedness, container, false);
		
		TextView tvHeading = (TextView) v.findViewById(R.id.txtHeadPreparednessHeading);
		tvHeading.setText("Concussion Awareness");

		getParsingData();
		return v;
	}
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}


	public void getParsingData() {
		context.showDialog(0);
		if (HeadsUpNativeActivity.appStatus.isOnline()) {

			String xml_url = XmlUrl.getXmlUrl(XmlID.Concussion_articles);
			new XmlTask(this).execute(xml_url);

		} else {

			Log.v("InjuryPreventionActivity", "App is not online!");
			Intent intent = new Intent(context, NoConnectivityScreen.class);
			this.startActivity(intent);
			context.finish();
		}
	}
	
	public void getXmlOutput(ArrayList<Entry> heatPrepardnessArray) {
		Log.d("------------", "" + heatPrepardnessArray);
		if(this.isResumed()){
			View v = getView();

			txtEmpty=(TextView) v.findViewById(android.R.id.empty);
			if(heatPrepardnessArray.size() == 0)
			{
				txtEmpty.setVisibility(TextView.VISIBLE);
				Log.v("no responce", "null data");
			}else{
				txtEmpty.setVisibility(TextView.GONE);
				Log.v("responce", "data");
			}

			heatpreparednessResult = heatPrepardnessArray;

			noOfItem = heatpreparednessResult.size();

			mListViewVideo = (ListView) v.findViewById(R.id.heat_preparedness_video_list);
			mListView = (ListView) v.findViewById(android.R.id.list);

			// mHeatPreparednessListAdapter = new HeatPreparednessListAdapter(
			// HeatPreparednessActivity.this, heatPrepardnessArray);
			// mListView.setAdapter(mHeatPreparednessListAdapter);

			mHeatPreparednessListAdapter = new InjuryPreventionListAdapter(
					this.getActivity(), heatPrepardnessArray);
			mListView.setAdapter(mHeatPreparednessListAdapter);

			ArrayList<String> videoText = new ArrayList<String>();
			videoText.add("Concussion \nPrevention");

			mHeatPreparednessVIdeoListAdapter = new HeatPreparednessVideoListAdapter(
					this.getActivity(), videoText,
					concussionAwarenessIcons, strActivityFlag);
			mListViewVideo.setAdapter(mHeatPreparednessVIdeoListAdapter);

			onHeatPreparednessItemClick();

			setSize();

			onHeatPreparednessVideoItemClick();
		}
		context.removeDialog(0);

	}

	public void onHeatPreparednessItemClick() {

		mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// TODO Auto-generated method stub

				mEntry = heatpreparednessResult.get(position);
				Bundle data = new Bundle();
				data.putString("Title", mEntry.getTitle());
				data.putString("Description", mEntry.getDescription());
				data.putString("DescritionLink", mEntry.getDescriptionLink());
				data.putString("Guid", mEntry.getGuid());
				
				//mXmlDescriptionFragment.setArguments(data);
				/*
				FragmentTransaction ft = context.getSupportFragmentManager().beginTransaction();
				if (mXmlDescriptionFragment == null) {
					mXmlDescriptionFragment = (XmlDescriptionFragment) Fragment.instantiate(context, XmlDescriptionFragment.class.getName());
					
					ft.add(R.id.heatScrollView, mXmlDescriptionFragment);
				}
				
				if (!mXmlDescriptionFragment.isDetached()) {
					ft.detach(mXmlDescriptionFragment);
				}

				ft.attach(mXmlDescriptionFragment);
				ft.commit();
				*/
				mXmlDescriptionFragment = null;	
				if (mXmlDescriptionFragment == null) {
					mXmlDescriptionFragment = (XmlDescriptionFragment) (context).addFragment(
							R.id.linearLayout2, XmlDescriptionFragment.class.getName(),
							"XmlDescriptionFragment",data);
					
				}else {
					(context).attachFragment((Fragment) mXmlDescriptionFragment);
				}
				


				/*TabGroupActivity parentActivity = (TabGroupActivity) getParent();
				Intent intent = new Intent(context,
						XmlDescriptionFragment.class);

				mEntry = heatpreparednessResult.get(position);
				intent.putExtra("Title", mEntry.getTitle());
				intent.putExtra("Description", mEntry.getDescription());
				intent.putExtra("DescritionLink", mEntry.getDescriptionLink());
				intent.putExtra("Guid", mEntry.getGuid());
				parentActivity.startChildActivity("xml description", intent);*/

			}
		});
	}

	public void onHeatPreparednessVideoItemClick() {

		mListViewVideo
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int position, long arg3) {
						// TODO Auto-generated method stub

						Uri uri = Uri
								.parse("http://www.youtube.com/watch?v=WKJFNGjEyuY");
						Intent intent = new Intent(Intent.ACTION_VIEW, uri);
						startActivity(intent);
					}
				});
	}
	
	public void setSize() {

		int valPx = densityToPixel(138);
		View v = getView();
		LinearLayout linearLayoutVideo = (LinearLayout) v.findViewById(R.id.linearLayoutVideo);
		LayoutParams lpVideoBg = (LayoutParams) linearLayoutVideo
				.getLayoutParams();
		lpVideoBg.height = valPx;
		linearLayoutVideo.setLayoutParams(lpVideoBg);

		mListViewVideo = (ListView) v.findViewById(R.id.heat_preparedness_video_list);
		LayoutParams lpVideo = (LayoutParams) mListViewVideo.getLayoutParams();
		lpVideo.height = valPx;
		mListViewVideo.setLayoutParams(lpVideo);

		int valPxList = densityToPixel(getDeviceDensity());

		mListView = (ListView) v.findViewById(android.R.id.list);
		LayoutParams lpArticle = (LayoutParams) mListView.getLayoutParams();
		lpArticle.height = noOfItem * valPxList;
		mListView.setLayoutParams(lpArticle);

	}

	public int getDeviceDensity() {

		int valPxList = 0;
		switch (getResources().getDisplayMetrics().densityDpi) {
		case DisplayMetrics.DENSITY_LOW:
			valPxList = 53;
			break;
		case DisplayMetrics.DENSITY_MEDIUM:
			valPxList = 49;
			break;
		case DisplayMetrics.DENSITY_HIGH:
			valPxList = 49;
			break;
		default:
			valPxList = 50;
			break;
		}
		return valPxList;
	}

	public int pixelsToDensity(int processVal) {
		int pixels = processVal;
		float scale = this.getResources().getDisplayMetrics().density;
		float dips = pixels / scale;
		int originalVal = (int) dips;
		return originalVal;
	}

	public int densityToPixel(int processVal) {
		int dips = processVal;
		float scale = this.getResources().getDisplayMetrics().density;
		int pixels = Math.round(dips * scale);
		return pixels;
	}

}
