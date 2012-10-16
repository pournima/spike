package com.headsup.safety;

import java.util.ArrayList;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.headsup.R;

public class SafetyChecklistsFragment extends Fragment {
	ListView mListView;
	SafetyCoachesListAdapter mCoachesListAdapter;
	SafetyCoachesListAdapter mCommissionerListAdapter;
	Resources res;
	String TAG = getClass().getName();
	String[] strParentsHeader;
	ArrayList<ParentsTabItems> mParentsTabItems;
	Button btnParents;
	Button btnCoaches;
	Button btnCommissioners;
	LinearLayout linearltParentsTab;

	TextView openClose1;
	TextView openClose2;
	TextView openClose3;
	TextView openClose4;
	TextView openClose5;

	LinearLayout itemContent1;
	LinearLayout itemContent2;
	LinearLayout itemContent3;
	LinearLayout itemContent4;
	LinearLayout itemContent5;

	ImageView imgOpenClose1;
	ImageView imgOpenClose2;
	ImageView imgOpenClose3;
	ImageView imgOpenClose4;
	ImageView imgOpenClose5;

	int[] imgVideoResourse = { R.id.imgVideo1, R.id.imgVideo2, R.id.imgVideo3,
			R.id.imgVideo4, R.id.imgVideo5, R.id.imgVideo6, R.id.imgVideo7,
			R.id.imgVideo8, R.id.imgVideo9, R.id.imgVideo10, R.id.imgVideo11,
			R.id.imgVideo12, R.id.imgVideo13 };
	String[] parentsVideoLinks = {
			"http://www.youtube.com/watch?v=fDdRjY_iTj0&feature=plcp",
			"http://www.youtube.com/watch?v=2guVgJIhiLs&feature=plcp",
			"http://www.youtube.com/watch?v=F-EvFj4byZc&feature=plcp",
			"http://www.youtube.com/watch?v=tDdMTsYil0A&feature=plcp",
			"http://www.youtube.com/watch?v=1jlaeSl7gTk&feature=plcp",
			"http://www.youtube.com/watch?v=i53UeBlowm8&feature=plcp",
			"http://www.youtube.com/watch?v=KExA8grY1NE",
			"http://www.youtube.com/watch?v=YfIqqKFfINY&feature=youtu.be",
			"http://www.youtube.com/watch?v=SQaBJ-1IGtw&feature=plcp",
			"http://www.youtube.com/watch?v=scoLo7xZWwc&feature=plcp",
			"http://www.youtube.com/watch?v=mmrnuVcgpfo&feature=plcp",
			"http://www.youtube.com/watch?v=67bucnKrONg&feature=plcp",
			"http://www.youtube.com/watch?v=Wdq4ZupQQes&feature=plcp" };

	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.safety_checklist, container,false);
		
		
		mListView = (ListView) v.findViewById(android.R.id.list);
		setParentsTabData(v);
		res = getResources();

		btnParents = (Button) v.findViewById(R.id.btnParents);
		btnCoaches = (Button) v.findViewById(R.id.btnCoaches);
		btnCommissioners = (Button) v.findViewById(R.id.btnCommissioners);

		linearltParentsTab = (LinearLayout) v.findViewById(R.id.linearLtParentsTab);

		setOnClickListenerForVideos(v);
		
		onParentsClick();
		setButtonClicked();
		return v;
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	public void setButtonClicked(){
		/*
		 * Safety checklist main buttons i.e Parent,Coaches and Commissioners
		 */
		btnParents.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				onParentsClick();
			}
		});
		
		btnCoaches.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				onCoachesClick();
			}
		});
		
		btnCommissioners.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				onCommissionerClick();
			}
		});
		
		/*
		 * Parent image button click Details
		 */
		imgOpenClose1.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				openCloseitem1();
			}
		});
		
		imgOpenClose2.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				openCloseitem2();
			}
		});
		
		imgOpenClose3.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				openCloseitem3();
			}
		});		

		imgOpenClose4.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				openCloseitem4();
			}
		});

		imgOpenClose5.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				openCloseitem5();
			}
		});
		
		/*
		 * Parent image button text click Details
		 */		
		openClose1.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				openCloseitem1();
			}
		});
		
		openClose2.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				openCloseitem2();
			}
		});
		
		openClose3.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				openCloseitem3();
			}
		});		

		openClose4.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				openCloseitem4();
			}
		});

		openClose5.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				openCloseitem5();
			}
		});
	}

	public void onParentsClick() {
		linearltParentsTab.setVisibility(LinearLayout.VISIBLE);
		mListView.setVisibility(ListView.GONE);
		btnParents.setBackgroundResource(R.drawable.parents_hover_btn);
		btnCoaches.setBackgroundResource(R.drawable.coaches_btn);
		btnCommissioners.setBackgroundResource(R.drawable.commissioners_btn);

		btnParents.setTextColor(getResources().getColor(R.color.white));
		btnCoaches.setTextColor(getResources().getColor(R.color.blue_dark));
		btnCommissioners.setTextColor(getResources()
				.getColor(R.color.blue_dark));

		/*
		 * mListView.setAdapter(mParentCheckListAdapter);
		 * mListView.setLayoutParams(new
		 * LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,
		 * 1420)); Log.i(TAG, String.valueOf(mListView.getHeight()));
		 */
	}

	public void onCoachesClick() {
		linearltParentsTab.setVisibility(LinearLayout.GONE);
		mListView.setVisibility(ListView.VISIBLE);

		btnParents.setBackgroundResource(R.drawable.parents_btn);
		btnCoaches.setBackgroundResource(R.drawable.coaches_btn_hover);
		btnCommissioners.setBackgroundResource(R.drawable.commissioners_btn);

		btnParents.setTextColor(getResources().getColor(R.color.blue_dark));
		btnCoaches.setTextColor(getResources().getColor(R.color.white));
		btnCommissioners.setTextColor(getResources()
				.getColor(R.color.blue_dark));
		String[] lstCoaches = res.getStringArray(R.array.coaches_list);

		Float density = this.getResources().getDisplayMetrics().density;
		int densityPixcel;
		if(density < 1){
			densityPixcel=65;
		}else{
			densityPixcel=61;
		}
		
		int coachListHeight = (int) (lstCoaches.length * (densityPixcel * density));
		Log.i(TAG, String.valueOf(coachListHeight));
		mCoachesListAdapter = new SafetyCoachesListAdapter((this.getActivity()), lstCoaches);
		mListView.setAdapter(mCoachesListAdapter);
		mListView.setLayoutParams(new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT, coachListHeight));
		Log.i(TAG, String.valueOf(mListView.getHeight()));
	}

	public void onCommissionerClick() {
		linearltParentsTab.setVisibility(LinearLayout.GONE);
		mListView.setVisibility(ListView.VISIBLE);

		btnParents.setBackgroundResource(R.drawable.parents_btn);
		btnCoaches.setBackgroundResource(R.drawable.coaches_btn);
		btnCommissioners
				.setBackgroundResource(R.drawable.commissioners_btn_hover);

		btnParents.setTextColor(getResources().getColor(R.color.blue_dark));
		btnCoaches.setTextColor(getResources().getColor(R.color.blue_dark));
		btnCommissioners.setTextColor(getResources().getColor(R.color.white));

		String[] lstCommissioners = res
				.getStringArray(R.array.commissioners_list);


		Float density = this.getResources().getDisplayMetrics().density;
		int densityPixcel;
		if(density < 1){
			densityPixcel=77;
		}else{
			densityPixcel=75;
		}
		int commisionerListHeight = (int) (lstCommissioners.length * (densityPixcel * density));
		Log.i(TAG, String.valueOf(commisionerListHeight));

		mCoachesListAdapter = new SafetyCoachesListAdapter(this.getActivity(),
				lstCommissioners);
		mListView.setAdapter(mCoachesListAdapter);
		mListView.setLayoutParams(new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT, commisionerListHeight));
		Log.i(TAG, String.valueOf(mListView.getHeight()));

	}

	public void setParentsTabData(View v) {

		openClose1 = (TextView) v.findViewById(R.id.txtOpenClose1);
		openClose1.setText(Html.fromHtml(getResources().getString(
				R.string.parents_tab_video_labels_1)));
		itemContent1 = (LinearLayout) v.findViewById(R.id.itemContent1);

		openClose2 = (TextView) v.findViewById(R.id.txtOpenClose2);
		openClose2.setText(Html.fromHtml(getResources().getString(
				R.string.parents_tab_video_labels_2)));
		itemContent2 = (LinearLayout) v.findViewById(R.id.itemContent2);

		openClose3 = (TextView) v.findViewById(R.id.txtOpenClose3);
		openClose3.setText(Html.fromHtml(getResources().getString(
				R.string.parents_tab_video_labels_3)));
		itemContent3 = (LinearLayout) v.findViewById(R.id.itemContent3);

		openClose4 = (TextView) v.findViewById(R.id.txtOpenClose4);
		openClose4.setText(Html.fromHtml(getResources().getString(
				R.string.parents_tab_video_labels_4)));
		itemContent4 = (LinearLayout) v.findViewById(R.id.itemContent4);

		openClose5 = (TextView) v.findViewById(R.id.txtOpenClose5);
		openClose5.setText(Html.fromHtml(getResources().getString(
				R.string.parents_tab_video_labels_5)));
		itemContent5 = (LinearLayout) v.findViewById(R.id.itemContent5);

		imgOpenClose1 = (ImageView) v.findViewById(R.id.imgOpenClose1);
		imgOpenClose2 = (ImageView) v.findViewById(R.id.imgOpenClose2);
		imgOpenClose3 = (ImageView) v.findViewById(R.id.imgOpenClose3);
		imgOpenClose4 = (ImageView) v.findViewById(R.id.imgOpenClose4);
		imgOpenClose5 = (ImageView) v.findViewById(R.id.imgOpenClose5);

	}

	public void openCloseitem1() {
		if (itemContent1.getVisibility() == LinearLayout.GONE) {
			itemContent1.setVisibility(LinearLayout.VISIBLE);
			openClose1.setText(Html.fromHtml(getResources().getString(
					R.string.parents_tab_video_labels_close)));
			imgOpenClose1.setImageResource(R.drawable.close_videos);
		} else {
			itemContent1.setVisibility(LinearLayout.GONE);
			openClose1.setText(Html.fromHtml(getResources().getString(
					R.string.parents_tab_video_labels_1)));
			imgOpenClose1.setImageResource(R.drawable.watch_more_video);
		}

	}

	public void openCloseitem2() {
		if (itemContent2.getVisibility() == LinearLayout.GONE) {
			itemContent2.setVisibility(LinearLayout.VISIBLE);
			openClose2.setText(Html.fromHtml(getResources().getString(
					R.string.parents_tab_video_labels_close)));
			imgOpenClose2.setImageResource(R.drawable.close_videos);
		} else {
			itemContent2.setVisibility(LinearLayout.GONE);
			openClose2.setText(Html.fromHtml(getResources().getString(
					R.string.parents_tab_video_labels_2)));
			imgOpenClose2.setImageResource(R.drawable.watch_more_video);
		}

	}

	public void openCloseitem3() {
		if (itemContent3.getVisibility() == LinearLayout.GONE) {
			itemContent3.setVisibility(LinearLayout.VISIBLE);
			openClose3.setText(Html.fromHtml(getResources().getString(
					R.string.parents_tab_video_labels_close)));
			imgOpenClose3.setImageResource(R.drawable.close_videos);
		} else {
			itemContent3.setVisibility(LinearLayout.GONE);
			openClose3.setText(Html.fromHtml(getResources().getString(
					R.string.parents_tab_video_labels_3)));
			imgOpenClose3.setImageResource(R.drawable.watch_more_video);
		}

	}

	public void openCloseitem4() {
		if (itemContent4.getVisibility() == LinearLayout.GONE) {
			itemContent4.setVisibility(LinearLayout.VISIBLE);
			openClose4.setText(Html.fromHtml(getResources().getString(
					R.string.parents_tab_video_labels_close)));
			imgOpenClose4.setImageResource(R.drawable.close_videos);
		} else {
			itemContent4.setVisibility(LinearLayout.GONE);
			openClose4.setText(Html.fromHtml(getResources().getString(
					R.string.parents_tab_video_labels_4)));
			imgOpenClose4.setImageResource(R.drawable.watch_more_video);
		}

	}

	public void openCloseitem5() {
		if (itemContent5.getVisibility() == LinearLayout.GONE) {
			itemContent5.setVisibility(LinearLayout.VISIBLE);
			openClose5.setText(Html.fromHtml(getResources().getString(
					R.string.parents_tab_video_labels_close)));
//			MarginLayoutParams marginLayoutParams = new MarginLayoutParams(
//					openClose5.getLayoutParams());
//			marginLayoutParams.setMargins(105, 100, 5, 10) //(58, 56, 5, 10);
//			RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
//					marginLayoutParams);
//			openClose5.setLayoutParams(layoutParams);
			imgOpenClose5.setImageResource(R.drawable.close_videos);
		} else {
			itemContent5.setVisibility(LinearLayout.GONE);
			openClose5.setText(Html.fromHtml(getResources().getString(
					R.string.parents_tab_video_labels_5)));
//			MarginLayoutParams marginLayoutParams = new MarginLayoutParams(
//					openClose5.getLayoutParams());
//			marginLayoutParams.setMargins(105, 90, 0, 10)
//			RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
//					marginLayoutParams);
//			openClose5.setLayoutParams(layoutParams);
			imgOpenClose5.setImageResource(R.drawable.watch_more_video);
		}

	}

	public String getDeviceDensity() {
		String strPxList =null;
		switch (getResources().getDisplayMetrics().densityDpi) {
		case DisplayMetrics.DENSITY_LOW:
			strPxList = "ldpi";
			break;
		case DisplayMetrics.DENSITY_MEDIUM:
			strPxList = "mdpi";
			break;
		case DisplayMetrics.DENSITY_HIGH:
			strPxList = "hdpi";
			break;
		default:
			strPxList = "hdpi";
			break;
		}
		return strPxList;
	}
	private void setOnClickListenerForVideos(View v) {
		// TODO Auto-generated method stub
		ImageView imgView;
		for (int i = 0; i < imgVideoResourse.length; i++) {
			imgView = (ImageView) v.findViewById(imgVideoResourse[i]);
			imgView.setTag(parentsVideoLinks[i]);
			imgView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Log.i("CLICK", String.valueOf(v.getTag()));
					Uri uri = Uri.parse((String) v.getTag());
					Intent intent = new Intent(Intent.ACTION_VIEW, uri);
					startActivity(intent);

				}
			});
		}
	}

}
