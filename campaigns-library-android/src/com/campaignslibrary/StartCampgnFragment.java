package com.campaignslibrary;

import java.lang.reflect.Method;
import java.util.ArrayList;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.campaignslibrary.helpers.Constants;
import com.checkinlibrary.CheckinLibraryActivity;

public class StartCampgnFragment extends Fragment {
	CheckinLibraryActivity context;
	final String TAG = getClass().getName();
	ListView mListViewOneDayEvent;
	ListView mListViewOngoingAct;
	ListView mListViewRaiseAwareness;
	private CreateCampgnFragment mCreateCampgnFragment = null;
	
	String[] oneDayEvents;
	String[] raiseAwaEvents;
	String[] ongoingActEvents;
	
	Resources resources;

	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	@Override
	public void onResume() {
		super.onResume();

	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		context = (CheckinLibraryActivity) this.getActivity();

		View fragmentView = inflater.inflate(R.layout.start_campgn_fragment,
				null);
		
		resources = getResources();

		initialization(fragmentView);
		initializeOnClickHandlers(fragmentView);
		return fragmentView;
	}

	/**
	 * This method initialises ui components
	 * @param v View for given fragment
	 * 
	 */
	private void initialization(View v) {
		mListViewOneDayEvent = (ListView) v.findViewById(R.id.lstCampgn_oneEve);
		mListViewOngoingAct = (ListView) v
				.findViewById(R.id.lstCampgn_OngoingAct);
		mListViewRaiseAwareness = (ListView) v
				.findViewById(R.id.lstCampgn_RaiseAwa);
		initializeListViews();

		TextView txtView = (TextView) v.findViewById(R.id.txtVCampgnOneETop);
		setTextViewOnclickListener(txtView, mListViewOneDayEvent);
		txtView = (TextView) v.findViewById(R.id.txtVCampgnOneEMid);
		setTextViewOnclickListener(txtView, mListViewOneDayEvent);
		txtView = (TextView) v.findViewById(R.id.txtVCampgnOneEBot);
		setTextViewOnclickListener(txtView, mListViewOneDayEvent);

		txtView = (TextView) v.findViewById(R.id.txtVCampgnOngoinTop);
		setTextViewOnclickListener(txtView, mListViewOngoingAct);
		txtView = (TextView) v.findViewById(R.id.txtVCampgnOngoinMid);
		setTextViewOnclickListener(txtView, mListViewOngoingAct);
		txtView = (TextView) v.findViewById(R.id.txtVCampgnOngoinBot);
		setTextViewOnclickListener(txtView, mListViewOngoingAct);

		txtView = (TextView) v.findViewById(R.id.txtVCampgnRaiseAMid);
		setTextViewOnclickListener(txtView, mListViewRaiseAwareness);
		txtView = (TextView) v.findViewById(R.id.txtVCampgnRaiseABot);
		setTextViewOnclickListener(txtView, mListViewRaiseAwareness);
	}

	/**
	 * This method initialises all listviews
	 * 
	 * @param v
	 *            View for given fragment
	 * 
	 */
	private void initializeListViews() {
		
		oneDayEvents = resources.getStringArray(R.array.campgn_oneday_event_arr);
		ongoingActEvents = resources.getStringArray(R.array.campgn_ongoing_arr);		
		raiseAwaEvents = resources.getStringArray(R.array.campgn_raise_awareness_arr);

		Float density = this.getResources().getDisplayMetrics().density;
		int coachListHeight = (int) (oneDayEvents.length * (47 * density));
		android.widget.LinearLayout.LayoutParams lpt = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT, coachListHeight);

		CampDayEventListAdapter mCampDayEventListAdapter = new CampDayEventListAdapter(
				context, oneDayEvents);
		mListViewOneDayEvent.setAdapter(mCampDayEventListAdapter);
		mListViewOneDayEvent.setLayoutParams(lpt);

		mCampDayEventListAdapter = new CampDayEventListAdapter(context,
				ongoingActEvents);
		mListViewOngoingAct.setAdapter(mCampDayEventListAdapter);
		mListViewOngoingAct.setLayoutParams(lpt);

		mCampDayEventListAdapter = new CampDayEventListAdapter(context,
				raiseAwaEvents);
		mListViewRaiseAwareness.setAdapter(mCampDayEventListAdapter);
		mListViewRaiseAwareness.setLayoutParams(lpt);
	}

	/**
	 * This method initialises onclick listener for ui components
	 * 
	 * @param v
	 *            View for given fragment
	 * 
	 */
	private void initializeOnClickHandlers(View v) {

		mListViewOneDayEvent.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				/*Toast.makeText(context, "Click One Day list Event " + position,
						Toast.LENGTH_SHORT).show();*/
				onListViewItemClick(Constants.INSTANCE.ONE_DAY_EVENT,position);
			}
		});

		mListViewOngoingAct.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				/*Toast.makeText(context,
						"Click Ongoing Activity list Event " + position,
						Toast.LENGTH_SHORT).show();*/
				onListViewItemClick(Constants.INSTANCE.ONGOING_ACT,position);
			}
		});

		mListViewRaiseAwareness
				.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						/*Toast.makeText(
								context,
								"Click Raised Awareness list Event " + position,
								Toast.LENGTH_SHORT).show();*/
						onListViewItemClick(Constants.INSTANCE.RAISE_AWARENESS,position);
					}
				});

	}

	/**
	 * This method initialises onclick listener for textView
	 * 
	 * @param iResourceId
	 *            resource id for given textView
	 * 
	 */
	private void setTextViewOnclickListener(TextView txtView,
			final ListView mListView) {
		txtView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (mListView.getVisibility() == ListView.VISIBLE) {
					mListView.setVisibility(ListView.GONE);
//					Animation anim = expand(mListView, false);
//					mListView.startAnimation(anim);
				} else {
					mListView.setVisibility(ListView.VISIBLE);
//					Animation anim = expand(mListView, true);
//					mListView.startAnimation(anim);
				}
			}
		});
	}

	/**
	 * This method initialises onclick listener for listView Item
	 * 
	 */
	public void onListViewItemClick(String strCampgnCat, int iCampgnPosition) {
		Constants.INSTANCE.mSelectedBitmaps=new ArrayList<Bitmap>();
		
		mCreateCampgnFragment=null;
		if (mCreateCampgnFragment == null) {
			Bundle bundle = new Bundle();
			bundle.putString("campgn_cat", strCampgnCat);
			bundle.putInt("campgn_sub_cat", iCampgnPosition);
			
			mCreateCampgnFragment = (CreateCampgnFragment) ((CheckinLibraryActivity) context)
					.addFragment(R.id.linearLayout2,
							CreateCampgnFragment.class.getName(),
							"create_campaign_fragment",bundle);
		} else {
			((CheckinLibraryActivity) this.context)
					.attachFragment((Fragment) mCreateCampgnFragment);
		}
		Log.i(TAG, "onClickOneDayEventList");
	}

	public static Animation expand(final View v, final boolean expand) {
		try {
			Method m = v.getClass().getDeclaredMethod("onMeasure", int.class,
					int.class);
			m.setAccessible(true);
			m.invoke(v,
					MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
					MeasureSpec.makeMeasureSpec(
							((View) v.getParent()).getMeasuredWidth(),
							MeasureSpec.AT_MOST));
		} catch (Exception e) {
			e.printStackTrace();
		}
		final int initialHeight = v.getMeasuredHeight();
		if (expand) {
			v.getLayoutParams().height = 0;
		} else {
			v.getLayoutParams().height = initialHeight;
		}
		v.setVisibility(View.VISIBLE);
		Animation a = new Animation() {
			@Override
			protected void applyTransformation(float interpolatedTime,
					Transformation t) {
				int newHeight = 0;
				if (expand) {
					newHeight = (int) (initialHeight * interpolatedTime);
				} else {
					newHeight = (int) (initialHeight * (1 - interpolatedTime));
				}
				v.getLayoutParams().height = newHeight;
				v.requestLayout();
				if (interpolatedTime == 1 && !expand)
					v.setVisibility(View.GONE);
			}

			@Override
			public boolean willChangeBounds() {
				return true;
			}
		};
		a.setDuration(450);
		return a;
	}
}