package com.headsup.settings;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.checkinlibrary.settings.HowItWorksFragment;
import com.checkinlibrary.settings.ShareFragment;
import com.headsup.HeadsUpNativeActivity;
import com.headsup.R;
import com.headsup.helpers.Constants;

public class HowItWorks extends HowItWorksFragment {

	ShareFragment mShareFragment;
	HeadsUpNativeActivity context;
	private ViewFlipper flipper;

	private ScrollView scripScroll0;
	private ScrollView scripScroll1;
	private ScrollView scripScroll2;
	private ScrollView scripScroll3;
	private ScrollView scripScroll4;
	private ScrollView scripScroll5;
	private ScrollView scripScroll6;

	private TextView textBusinessText;
	private TextView txtHowItWorksRules;
	private TextView txtHowItWorksSafety;
	private Button btnSettingShare;

	private GestureDetector gestureDetector;
	private Animation slideLeftIn;
	private Animation slideLeftOut;
	private Animation slideRightIn;
	private Animation slideRightOut;

	private static final int SWIPE_MIN_DISTANCE = 80;// 120;
	private static final int SWIPE_MAX_OFF_PATH = 300;// 250;
	private static final int SWIPE_THRESHOLD_VELOCITY = 150;// 200;

	private static final int VIDEO_No = 0;
	private static final int VIDEO_1 = 1;
//	private static final int VIDEO_2 = 2;
//	private static final int VIDEO_3 = 3;

	private ImageView imgViewVideo;
	ImageView[] imgViewCurrentPage = new ImageView[7];

	int current_page = VIDEO_No;
	int max_pages = 0;

	int iGstId = 0;

	String[] Videos = { Constants.CHECK_IN_VIDEO };
//	"http://www.youtube.com/watch?v=5X8lurY1Y9w",
//	"http://www.youtube.com/watch?v=OJyxcNVK1so" 
/*
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.how_it_works);

		InitAllComponents();
	}
*/
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		context = (HeadsUpNativeActivity) this.getActivity();
		View v = inflater.inflate(R.layout.how_it_works_fragment, container,
				false);
		InitAllComponents(v);
		return v;
	}
	
	@Override
	public void InitAllComponents(View v) {

		flipper = (ViewFlipper)v.findViewById(R.id.flipper);

		scripScroll0 = (ScrollView)v.findViewById(R.id.scripScroll0);
		scripScroll1 = (ScrollView)v.findViewById(R.id.scripScroll1);
		scripScroll2 = (ScrollView)v.findViewById(R.id.scripScroll2);
		scripScroll3 = (ScrollView)v.findViewById(R.id.scripScroll3);
		scripScroll4 = (ScrollView)v.findViewById(R.id.scripScroll4);
		scripScroll5 = (ScrollView)v.findViewById(R.id.scripScroll5);
		scripScroll6 = (ScrollView)v.findViewById(R.id.scripScroll6);

		textBusinessText = (TextView)v.findViewById(R.id.textBusinessTap);
		// Set HTML text in TextView
		textBusinessText.setText(Html.fromHtml(getResources().getString(
				R.string.how_it_works_business_tap)));
		
		txtHowItWorksRules = (TextView)v.findViewById(R.id.txtHowItWorksRules);
		txtHowItWorksRules.setText(Html.fromHtml(getResources().getString(
				R.string.how_it_works_rules_tap)));
		
		txtHowItWorksSafety = (TextView)v.findViewById(R.id.txtHowItWorksSafety);
		txtHowItWorksSafety.setText(Html.fromHtml(getResources().getString(
				R.string.how_it_works_safety)));

		gestureDetector = new GestureDetector(new MyGestureDetector(0));
		
		slideLeftIn = AnimationUtils
				.loadAnimation(context, R.layout.slide_in_left);
		slideLeftOut = AnimationUtils.loadAnimation(context,
				R.layout.slide_out_left);
		slideRightIn = AnimationUtils.loadAnimation(context,
				R.layout.slide_in_right);
		slideRightOut = AnimationUtils.loadAnimation(context,
				R.layout.slide_out_right);

		imgViewVideo = (ImageView)v.findViewById(R.id.imageViewVideo);

		imgViewVideo.bringToFront();
		btnSettingShare = (Button)v.findViewById(R.id.btnSettingShare);

		current_page = 0;

		// Zero based
		max_pages = 6;

		initialiseCurrentPageView(v);
		setOnClickListener();

	}
	
	@Override
	public void setOnClickListener() {
		scripScroll0.setOnTouchListener(new ScrollView.OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				if (gestureDetector.onTouchEvent(event)) {
					return true;
				}
				return false;
			}
		});

		scripScroll1.setOnTouchListener(new ScrollView.OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				if (gestureDetector.onTouchEvent(event)) {
					return true;
				}
				return false;
			}
		});

		scripScroll2.setOnTouchListener(new ScrollView.OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				if (gestureDetector.onTouchEvent(event)) {
					return true;
				}
				return false;
			}
		});

		imgViewVideo.setOnTouchListener(new ImageView.OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {

				Log.i("IMAG", String.valueOf(v.onTouchEvent(event)));
				iGstId = VIDEO_1;
				if (gestureDetector.onTouchEvent(event)) {

					Log.i("How it works", "Video clicked");
					return true;
				}
				return false;

			}
		});

		scripScroll3.setOnTouchListener(new ScrollView.OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				if (gestureDetector.onTouchEvent(event)) {
					return true;
				}
				return false;
			}
		});

		scripScroll4.setOnTouchListener(new ScrollView.OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				if (gestureDetector.onTouchEvent(event)) {
					return true;
				}
				return false;
			}
		});

		scripScroll5.setOnTouchListener(new ScrollView.OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				if (gestureDetector.onTouchEvent(event)) {
					return true;
				}
				return false;
			}
		});
		
		scripScroll6.setOnTouchListener(new ScrollView.OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				if (gestureDetector.onTouchEvent(event)) {
					return true;
				}
				return false;
			}
		});

		btnSettingShare.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.i("BTN_SHARE", "clicked");
				mShareFragment = null;
				if (mShareFragment == null) {
					mShareFragment = (ShareFragment) (context).addFragment(
							R.id.linearLayout2, ShareFragment.class.getName(),
							"share_fragment");
				} else {
					(context).attachFragment((Fragment) mShareFragment);
				}
				
				/*TabGroupActivity parentActivity = (TabGroupActivity) getParent();
				Intent intent;
				intent = new Intent(getParent(), SocialSharingActivity.class);
				parentActivity.startChildActivity("Social Share", intent);*/
			}
		});
	}
	
	class MyGestureDetector extends SimpleOnGestureListener {
		int mGestureId;

		public MyGestureDetector(int iGestureId) {
			this.mGestureId = iGestureId;
		}

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH) {
				return false;
			} else {
				try {
					// right to left swipe
					if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE
							&& Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
						if (canFlipRight()) {
							flipper.setInAnimation(slideRightIn);
							flipper.setOutAnimation(slideLeftOut);
							flipper.showNext();

							if (current_page == 1) {
								imgViewCurrentPage[0]
										.setImageResource(R.drawable.videoicon);
								imgViewCurrentPage[current_page]
										.setImageResource(R.drawable.slideiconselect);
							}else {
								imgViewCurrentPage[current_page - 1]
										.setImageResource(R.drawable.slideicon);
								imgViewCurrentPage[current_page]
										.setImageResource(R.drawable.slideiconselect);
							}
						} else {
							return false;
						}
						// left to right swipe
					} else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE
							&& Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
						if (canFlipLeft()) {
							flipper.setInAnimation(slideLeftIn);
							flipper.setOutAnimation(slideRightOut);
							flipper.showPrevious();

							if (current_page == 0) {
								imgViewCurrentPage[0]
										.setImageResource(R.drawable.videoiconselect);
								imgViewCurrentPage[current_page + 1]
										.setImageResource(R.drawable.slideicon);
							}  else {
								imgViewCurrentPage[current_page + 1]
										.setImageResource(R.drawable.slideicon);
								imgViewCurrentPage[current_page]
										.setImageResource(R.drawable.slideiconselect);
							}
						} else {
							return false;
						}
					}
				} catch (Exception e) {
					// nothing
				}
				return true;
			}
		}

		@Override
		public boolean onSingleTapConfirmed(MotionEvent e) {
			Log.i("GST_ID", String.valueOf(iGstId));
			if (current_page == 0) {
				if (iGstId == VIDEO_1)
					playVideo(0);
			} 
//				else if (current_page == 5) {
//				if (iGstId == VIDEO_2)
//					playVideo(1);
//				else if (iGstId == VIDEO_3)
//					playVideo(2);
//			}
			iGstId = VIDEO_No;
			return true;
		}
	}

	// Determine if on the last page
	private boolean canFlipRight() {
		if (current_page != (max_pages)) {
			current_page++;
			return true;
		}

		return false;
	}

	// Determine if on the first page
	private boolean canFlipLeft() {
		if (current_page != 0) {
			current_page--;
			return true;
		}

		return false;
	}

	public void playVideo(int iIndex) {
		if (iIndex < Videos.length) {
			String video_path = Videos[iIndex];
			Uri uri = Uri.parse(video_path);
			// With this line the Youtube application, if installed, will launch
			// immediately.
			// Without it you will be prompted with a list of the application to
			// choose.
			// uri = Uri.parse("vnd.youtube:" + uri.getQueryParameter("v"));
			Intent intent = new Intent(Intent.ACTION_VIEW, uri);
			startActivity(intent);
		} else {
			Log.i("Video Play", "Failed to play Video!");
		}
	}

	@Override
	public void initialiseCurrentPageView(View v) {
		imgViewCurrentPage[0] = (ImageView)v.findViewById(R.id.img_hiw_slider_panel1);
		imgViewCurrentPage[1] = (ImageView)v.findViewById(R.id.img_hiw_slider_panel2);
		imgViewCurrentPage[2] = (ImageView)v.findViewById(R.id.img_hiw_slider_panel3);
		imgViewCurrentPage[3] = (ImageView)v.findViewById(R.id.img_hiw_slider_panel4);
		imgViewCurrentPage[4] = (ImageView)v.findViewById(R.id.img_hiw_slider_panel5);
		imgViewCurrentPage[5] = (ImageView)v.findViewById(R.id.img_hiw_slider_panel6);
		imgViewCurrentPage[6] = (ImageView)v.findViewById(R.id.img_hiw_slider_panel7);

		imgViewCurrentPage[0].setImageResource(R.drawable.videoiconselect);
		//imgViewCurrentPage[5].setImageResource(R.drawable.videoicon);
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
		// Don't react to gps updates when the tab isn't active.
	}

}
