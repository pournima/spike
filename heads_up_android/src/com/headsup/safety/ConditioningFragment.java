package com.headsup.safety;

import java.util.ArrayList;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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

public class ConditioningFragment extends Fragment {
	AppStatus appStatus;
	ListView mListView;
	TextView mTxtConditioningHeader;
	InjuryPreventionListAdapter mConditioningListAdapter;
	Entry mEntry = new Entry();
	ArrayList<Entry> mConditioningArray;
	ArrayList<Entry> mConditioningResult;
	HeadsUpNativeActivity context;
	View v;
	private int noOfItem;
	ProgressDialog progressBar;
	XmlDescriptionFragment mXmlDescriptionFragment;
	TextView txtEmpty;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		context = (HeadsUpNativeActivity) this.getActivity();
	
		v = inflater.inflate(R.layout.injury_prevention, container, false);

		mTxtConditioningHeader=(TextView)v.findViewById(R.id.txtInjuryPrevantionHeading);
		mTxtConditioningHeader.setText(String.valueOf(getString(R.string.conditioning)));
		getParsingData();
		return v;
	}

	public void getParsingData() {
		//appStatus = AppStatus.getInstance(this);
		context.showDialog(0);
		if (HeadsUpNativeActivity.appStatus.isOnline()) {

			String xml_url = XmlUrl.getXmlUrl(XmlID.Conditioning);
			new XmlTask(this).execute(xml_url);

			// InputStream inputStream = Connect.INSTANCE
			// .getXmlFromUrl(xml_url);
			// injuryPreventionArray = XmlParser.INSTANCE
			// .parse(inputStream);
		} else {

			Log.v("Conditioning Activity", "App is not online!");
			Intent intent = new Intent(context, NoConnectivityScreen.class);
			this.startActivity(intent);
			context.finish();
		}

	}

	public void getXmlOutput(ArrayList<Entry> conditioningArray) {
		Log.d("------------", "" + conditioningArray);
		if(this.isResumed()){
			txtEmpty=(TextView) v.findViewById(android.R.id.empty);
			if(conditioningArray.size() == 0)
			{
				txtEmpty.setVisibility(TextView.VISIBLE);
				Log.v("no responce", "null data");
			}else{
				txtEmpty.setVisibility(TextView.GONE);
				Log.v("responce", "data");
			}
			this.mConditioningResult = conditioningArray;

			noOfItem = mConditioningResult.size();

			mListView = (ListView) v.findViewById(android.R.id.list);
			mConditioningListAdapter = new InjuryPreventionListAdapter(
					this.getActivity(), mConditioningResult);
			mListView.setAdapter(mConditioningListAdapter);

			onInjuryItemClick();

			setSize();
		}
		context.removeDialog(0);

	}

	public void setSize() {

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

	public void onInjuryItemClick() {

		mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// TODO Auto-generated method stub

				mEntry = mConditioningResult.get(position);
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
			}
		});
	}
}
