package com.headsup.safety;

import java.util.ArrayList;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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

public class HeatPreparednessFragment extends Fragment {

	HeadsUpNativeActivity context;
	ListView mListViewVideo;
	ListView mListView;
	InjuryPreventionListAdapter mHeatPreparednessListAdapter;
	HeatPreparednessVideoListAdapter mHeatPreparednessVIdeoListAdapter;
	Entry mEntry = new Entry();
	ArrayList<Entry> heatpreparednessResult;
	private int[] heatPreparednessIcons = { R.drawable.heat_preparedness_understand };
	private static final String strActivityFlag = "HeatPreparedness";
	private int noOfItem;

	ProgressDialog progressBar;
	AppStatus appStatus;
	View v;
	XmlDescriptionFragment mXmlDescriptionFragment;
	final String TAG=getClass().getName();
	TextView txtEmpty;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		context = (HeadsUpNativeActivity) this.getActivity();
		v = inflater.inflate(R.layout.heat_preparedness, container, false);
		getParsingData();
		return v;
	}

	/*@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		super.onCreate(savedInstanceState);
		setContentView(R.layout.heat_preparedness);

		getParsingData();
	}*/

	public void setSize() {

		int valPx = densityToPixel(138);

		LinearLayout linearLayoutVideo = (LinearLayout) v.findViewById(R.id.linearLayoutVideo);
		LayoutParams lpVideoBg = (LayoutParams) linearLayoutVideo
				.getLayoutParams();
		lpVideoBg.height = valPx;
		linearLayoutVideo.setLayoutParams(lpVideoBg);

		mListViewVideo = (ListView) v.findViewById(R.id.heat_preparedness_video_list);
		LayoutParams lpVideo = (LayoutParams) mListViewVideo.getLayoutParams();
		lpVideo.height = valPx * 1;
		mListViewVideo.setLayoutParams(lpVideo);

		int valPxList = densityToPixel(getDeviceDensity());
		mListView = (ListView) v.findViewById(android.R.id.list);
		LayoutParams lpArticle = (LayoutParams) mListView.getLayoutParams();
		lpArticle.height = noOfItem * valPxList;
		mListView.setLayoutParams(lpArticle);

	}

	public int densityToPixel(int processVal) {
		int dips = processVal;
		float scale = this.getResources().getDisplayMetrics().density;
		int pixels = Math.round(dips * scale);
		return pixels;
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

	public void getParsingData() {
		context.showDialog(0);
		if (HeadsUpNativeActivity.appStatus.isOnline()) {
			
			String xml_url = XmlUrl.getXmlUrl(XmlID.Heat_Prepaidness);
			new XmlTask(this).execute(xml_url);

		} else {

			Log.v(TAG, "App is not online!");
			Intent intent = new Intent(context, NoConnectivityScreen.class);
			this.startActivity(intent);
			context.finish();
		}
	}

	public void getXmlOutput(ArrayList<Entry> heatPrepardnessArray) {
		Log.d("------------", "" + heatPrepardnessArray);
		if(this.isResumed()){
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
			videoText
			.add("Understand the \ndangers of heat \nand learn how \nto stay safe on \nthe field");

			mHeatPreparednessVIdeoListAdapter = new HeatPreparednessVideoListAdapter(
					this.getActivity(), videoText,
					heatPreparednessIcons, strActivityFlag);
			mListViewVideo.setAdapter(mHeatPreparednessVIdeoListAdapter);

			onHeatPreparednessItemClick();

			// onHeatPreparednessVideoItemClick();

			setSize();
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
				
				mXmlDescriptionFragment = null;	
				if (mXmlDescriptionFragment == null) {
					mXmlDescriptionFragment = (XmlDescriptionFragment) (context).addFragment(
							R.id.linearLayout2, XmlDescriptionFragment.class.getName(),
							"XmlDescriptionFragment",data);
					
				}else {
					(context).attachFragment((Fragment) mXmlDescriptionFragment);
				}
				
				
				
			/*	TabGroupActivity parentActivity = (TabGroupActivity) getParent();
				Intent intent = new Intent(getParent(),
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

						FragmentTransaction ft = context.getSupportFragmentManager().beginTransaction();
						mEntry = heatpreparednessResult.get(position);
						Bundle data = new Bundle();
						data.putString("Title", mEntry.getTitle());
						data.putString("Description", mEntry.getDescription());
						data.putString("DescritionLink", mEntry.getDescriptionLink());
						data.putString("Guid", mEntry.getGuid());
						
						mXmlDescriptionFragment.setArguments(data);
						
						if (mXmlDescriptionFragment == null) {
							mXmlDescriptionFragment = (XmlDescriptionFragment) Fragment.instantiate(context, XmlDescriptionFragment.class.getName());
							
							ft.add(R.id.heatScrollView, mXmlDescriptionFragment);
						}
						
					/*	TabGroupActivity parentActivity = (TabGroupActivity) getParent();
						Intent intent = new Intent(getParent(),
								XmlDescriptionFragment.class);

						mEntry = heatpreparednessResult.get(position);
						intent.putExtra("Title", mEntry.getTitle());
						intent.putExtra("Description", mEntry.getDescription());
						intent.putExtra("DescritionLink",
								mEntry.getDescriptionLink());
						intent.putExtra("Guid", mEntry.getGuid());
						parentActivity.startChildActivity("xml description",
								intent);
*/
					}
				});
	}
}
