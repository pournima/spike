package com.checkinlibrary.settings;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import com.checkinlibrary.CheckinLibraryActivity;
import com.checkinlibrary.NoConnectivityScreen;
import com.checkinlibrary.R;
import com.checkinlibrary.business.BusinessFragment;
import com.checkinlibrary.db.CheckinNativeDatabaseHelper;
import com.checkinlibrary.helpers.AppStatus;
import com.checkinlibrary.imageloader.ImageLoader;
import com.checkinlibrary.models.VideoResult;
import com.checkinlibrary.org.OrganizationFragment;
import com.checkinlibrary.ws.tasks.HowItWorksTask;

@SuppressWarnings("unused")
public class HowItWorksFragment extends Fragment {

	private ViewFlipper flipper;

	private ScrollView scripScroll0;
	private ScrollView scripScroll1;
	private ScrollView scripScroll2;
	private ScrollView scripScroll3;
	private ScrollView scripScroll4;
	private ScrollView scripScroll5;

	private TextView textBusinessText;
	private TextView textBusinessVideo;
	private TextView textCauseText;
	private TextView textFindBusiness;
	private TextView textFindCause;
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
	private static final int VIDEO_2 = 2;
	private static final int VIDEO_3 = 3;

	private ImageView imgViewVideo;
	private ImageView imgViewVideo2;
	private ImageView imgViewVideo3;
	ImageView[] imgViewCurrentPage = new ImageView[6];

	int current_page = VIDEO_No;
	int max_pages = 0;

	int iGstId = 0;

	OrganizationFragment mOrganizationFragment;
	BusinessFragment mBusinessFragment;
	ShareFragment mShareFragment;

	CheckinLibraryActivity context;
	ProgressDialog dialog;

	String[] Videos;
	
	private ImageLoader mImageLoader;

	/*
	 * = { "http://www.youtube.com/watch?v=X6gDTj5MbqI&feature=youtu.be",
	 * "http://www.youtube.com/watch?v=5X8lurY1Y9w",
	 * "http://www.youtube.com/watch?v=OJyxcNVK1so" };
	 */

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.how_it_works_fragment, container,
				false);
		InitAllComponents(v);
		mImageLoader = new ImageLoader(context);
		
		if (CheckinNativeDatabaseHelper.bGetVideoLinks) {
			getVideosLinks();
		} else {
			initializeVideoLinks();
		}

		return v;
	}

	public void InitAllComponents(View v) {
		context = (CheckinLibraryActivity) this.getActivity();

		flipper = (ViewFlipper) v.findViewById(R.id.flipper);

		scripScroll0 = (ScrollView) v.findViewById(R.id.scripScroll0);
		scripScroll1 = (ScrollView) v.findViewById(R.id.scripScroll1);
		scripScroll2 = (ScrollView) v.findViewById(R.id.scripScroll2);
		scripScroll3 = (ScrollView) v.findViewById(R.id.scripScroll3);
		scripScroll4 = (ScrollView) v.findViewById(R.id.scripScroll4);
		scripScroll5 = (ScrollView) v.findViewById(R.id.scripScroll5);

		textBusinessText = (TextView) v.findViewById(R.id.textBusinessTap);
		// Set HTML text in TextView
		textBusinessText.setText(Html.fromHtml(getResources().getString(
				R.string.how_it_works_business_tap)));

		textCauseText = (TextView) v.findViewById(R.id.textCauseTap);
		// Set HTML text in TextView
		textCauseText.setText(Html.fromHtml(getResources().getString(
				R.string.how_it_works_cause_tap)));

		textFindBusiness = (TextView) v.findViewById(R.id.textFindBusiness);
		textFindCause = (TextView) v.findViewById(R.id.textFindCause);

		gestureDetector = new GestureDetector(new MyGestureDetector(0));

		slideLeftIn = AnimationUtils.loadAnimation(context,
				R.layout.slide_in_left);
		slideLeftOut = AnimationUtils.loadAnimation(context,
				R.layout.slide_out_left);
		slideRightIn = AnimationUtils.loadAnimation(context,
				R.layout.slide_in_right);
		slideRightOut = AnimationUtils.loadAnimation(context,
				R.layout.slide_out_right);

		imgViewVideo = (ImageView) v.findViewById(R.id.imageViewVideo);

		imgViewVideo.bringToFront();

		imgViewVideo2 = (ImageView) v.findViewById(R.id.imageViewVideo2);
		imgViewVideo3 = (ImageView) v.findViewById(R.id.imageViewVideo3);
		btnSettingShare = (Button) v.findViewById(R.id.btnSettingShare);

		current_page = 0;

		// Zero based
		max_pages = 5;

		initialiseCurrentPageView(v);
		setOnClickListener();

		dialog = new ProgressDialog(context);
		dialog = ProgressDialog.show(context, null, null);
		dialog.setContentView(R.layout.loader);
		dialog.setIndeterminate(true);
		dialog.setCancelable(false);
	}
	
	
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

		textFindBusiness.setOnTouchListener(new TextView.OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				goToBusinessFragment();
				return true;
			}
		});

		textFindCause.setOnTouchListener(new TextView.OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				goToOrganizationFragment();
				return true;
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

		imgViewVideo2.setOnTouchListener(new ImageView.OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				iGstId = VIDEO_2;
				if (gestureDetector.onTouchEvent(event)) {
					return true;
				}
				return false;

			}
		});
		imgViewVideo3.setOnTouchListener(new ImageView.OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				iGstId = VIDEO_3;
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
										.setImageResource(R.drawable.c4g_videoicon);
								imgViewCurrentPage[current_page]
										.setImageResource(R.drawable.c4g_slideiconselect);
							} else if (current_page == max_pages) {
								imgViewCurrentPage[5]
										.setImageResource(R.drawable.c4g_videoiconselect);
								imgViewCurrentPage[current_page - 1]
										.setImageResource(R.drawable.c4g_slideicon);

							} else {
								imgViewCurrentPage[current_page - 1]
										.setImageResource(R.drawable.c4g_slideicon);
								imgViewCurrentPage[current_page]
										.setImageResource(R.drawable.c4g_slideiconselect);
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
										.setImageResource(R.drawable.c4g_videoiconselect);
								imgViewCurrentPage[current_page + 1]
										.setImageResource(R.drawable.c4g_slideicon);
							} else if (current_page == max_pages - 1) {
								imgViewCurrentPage[5]
										.setImageResource(R.drawable.c4g_videoicon);
								imgViewCurrentPage[current_page]
										.setImageResource(R.drawable.c4g_slideiconselect);
							} else {
								imgViewCurrentPage[current_page + 1]
										.setImageResource(R.drawable.c4g_slideicon);
								imgViewCurrentPage[current_page]
										.setImageResource(R.drawable.c4g_slideiconselect);
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
			} else if (current_page == 5) {
				if (iGstId == VIDEO_2)
					playVideo(1);
				else if (iGstId == VIDEO_3)
					playVideo(2);
			}
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
		if (Videos != null) {
			if (iIndex < Videos.length) {
				String video_path = Videos[iIndex];
				Uri uri = Uri.parse(video_path);
				// With this line the Youtube application, if installed, will
				// launch
				// immediately.
				// Without it you will be prompted with a list of the
				// application to
				// choose.
				// uri = Uri.parse("vnd.youtube:" + uri.getQueryParameter("v"));
				Intent intent = new Intent(Intent.ACTION_VIEW, uri);
				startActivity(intent);
			} else {
				Log.i("Video Play", "Failed to play Video!");
			}
		}
	}

	private void goToBusinessFragment() {

		CheckinLibraryActivity.isFromHowItWorks = true;
		if (mBusinessFragment == null) {
			mBusinessFragment = (BusinessFragment) (context).addFragment(
					R.id.linearLayout2, BusinessFragment.class.getName(),
					"business_fragment");
		} else {
			(context).attachFragment((Fragment) mBusinessFragment);
		}

	}

	private void goToOrganizationFragment() {
		if (mOrganizationFragment == null) {
			mOrganizationFragment = (OrganizationFragment) (context)
					.addFragment(R.id.linearLayout2,
							OrganizationFragment.class.getName(),
							"organization_fragment");
		} else {
			(context).attachFragment((Fragment) mOrganizationFragment);
		}
	}

	public void initialiseCurrentPageView(View v) {
		imgViewCurrentPage[0] = (ImageView) v
				.findViewById(R.id.img_hiw_slider_panel1);
		imgViewCurrentPage[1] = (ImageView) v
				.findViewById(R.id.img_hiw_slider_panel2);
		imgViewCurrentPage[2] = (ImageView) v
				.findViewById(R.id.img_hiw_slider_panel3);
		imgViewCurrentPage[3] = (ImageView) v
				.findViewById(R.id.img_hiw_slider_panel4);
		imgViewCurrentPage[4] = (ImageView) v
				.findViewById(R.id.img_hiw_slider_panel5);
		imgViewCurrentPage[5] = (ImageView) v
				.findViewById(R.id.img_hiw_slider_panel6);

		imgViewCurrentPage[0].setImageResource(R.drawable.c4g_videoiconselect);
		imgViewCurrentPage[5].setImageResource(R.drawable.c4g_videoicon);
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

	private void getVideosLinks() {
		if (CheckinLibraryActivity.appStatus.isOnline()) {
			CheckinLibraryActivity.strProgressMessage = getString(R.string.videosProgressMessage);
			// context.showDialog(0);
			dialog.show();
			new HowItWorksTask(this)
					.execute(CheckinLibraryActivity.appStatus
							.getSharedStringValue(CheckinLibraryActivity.appStatus.AUTH_KEY));
		} else {
			Intent intent = new Intent(context, NoConnectivityScreen.class);
			this.startActivity(intent);
			context.finish();
		}
	}

	public void onVideosResult(VideoResult mVideoResult) {

		// AppStatus appStatus=AppStatus.getInstance(this);

		// store video links in -----------------//
		if (mVideoResult != null) {
			if (mVideoResult.getApp_user_link() != null) {
				CheckinLibraryActivity.appStatus.saveSharedStringValue(
						CheckinLibraryActivity.appStatus.APP_USER_LINK,
						mVideoResult.getApp_user_link().getLink());
				CheckinLibraryActivity.appStatus.saveSharedStringValue(
						CheckinLibraryActivity.appStatus.APP_USER_COVER,
						mVideoResult.getApp_user_link().getCover());
			}

			if (mVideoResult.getGrow_business_link() != null) {
				CheckinLibraryActivity.appStatus.saveSharedStringValue(
						CheckinLibraryActivity.appStatus.GROW_BUS_LINK,
						mVideoResult.getGrow_business_link().getLink());
				CheckinLibraryActivity.appStatus.saveSharedStringValue(
						CheckinLibraryActivity.appStatus.GROW_BUS_COVER,
						mVideoResult.getGrow_business_link().getCover());
			}

			if (mVideoResult.getOverview_link() != null) {
				CheckinLibraryActivity.appStatus.saveSharedStringValue(
						CheckinLibraryActivity.appStatus.OVERVIEW_LINK,
						mVideoResult.getOverview_link().getLink());
				CheckinLibraryActivity.appStatus.saveSharedStringValue(
						CheckinLibraryActivity.appStatus.OVERVIEW_COVER,
						mVideoResult.getOverview_link().getCover());
			}
			CheckinNativeDatabaseHelper.bGetVideoLinks = false;
			initializeVideoLinks();

			// context.removeDialog(0);
			dialog.dismiss();
		}

		/*
		 * //context.removeDialog(0); //dialog.dismiss(); if(result != null){
		 * Bitmap bitmap = null; try { if(result.getApp_user_link() != null){
		 * bitmap = BitmapFactory.decodeStream((InputStream)new
		 * URL(result.getApp_user_link().getCover()).getContent());
		 * imgViewVideo.setImageBitmap(bitmap);
		 * 
		 * if(result.getGrow_business_link() != null){ bitmap =
		 * BitmapFactory.decodeStream((InputStream)new
		 * URL(result.getGrow_business_link().getCover()).getContent());
		 * imgViewVideo2.setImageBitmap(bitmap);
		 * 
		 * if(result.getOverview_link() != null){ bitmap =
		 * BitmapFactory.decodeStream((InputStream)new
		 * URL(result.getOverview_link().getCover()).getContent());
		 * imgViewVideo3.setImageBitmap(bitmap);
		 * 
		 * Videos=new String[3]; Videos[0]=result.getApp_user_link().getLink();
		 * Videos[1]=result.getGrow_business_link().getLink();
		 * Videos[2]=result.getOverview_link().getLink(); } } } } catch
		 * (MalformedURLException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); } catch (IOException e) { // TODO Auto-generated
		 * catch block e.printStackTrace(); } }
		 */
	}

	private void initializeVideoLinks() {

		dialog.dismiss();
		//Bitmap bitmap = null;
		AppStatus appStatus = AppStatus.getInstance(context);
		if (appStatus.getSharedStringValue(appStatus.APP_USER_LINK) != null) {

			/*bitmap = BitmapFactory
					.decodeStream((InputStream) new URL(
							changeLinkName(
									appStatus.APP_USER_COVER,
									appStatus
											.getSharedStringValue(appStatus.APP_USER_COVER)))
							.getContent());
			imgViewVideo.setImageBitmap(bitmap);*/
			
			mImageLoader.DisplayImage(changeLinkName(appStatus.APP_USER_COVER,appStatus
							.getSharedStringValue(appStatus.APP_USER_COVER)), imgViewVideo);

			if (appStatus.getSharedStringValue(appStatus.GROW_BUS_LINK) != null) {

				/*bitmap = BitmapFactory
						.decodeStream((InputStream) new URL(
								changeLinkName(
										appStatus.GROW_BUS_COVER,
										appStatus
												.getSharedStringValue(appStatus.GROW_BUS_COVER)))
								.getContent());
				imgViewVideo2.setImageBitmap(bitmap);*/
				
				mImageLoader.DisplayImage(changeLinkName(appStatus.GROW_BUS_COVER,appStatus
								.getSharedStringValue(appStatus.GROW_BUS_COVER)), imgViewVideo2);

				if (appStatus.getSharedStringValue(appStatus.OVERVIEW_LINK) != null) {

					/*bitmap = BitmapFactory
							.decodeStream((InputStream) new URL(
									changeLinkName(
											appStatus.OVERVIEW_COVER,
											appStatus
													.getSharedStringValue(appStatus.OVERVIEW_COVER)))
									.getContent());
					imgViewVideo3.setImageBitmap(bitmap);*/
					
					mImageLoader.DisplayImage(changeLinkName(appStatus.OVERVIEW_COVER,appStatus
													.getSharedStringValue(appStatus.OVERVIEW_COVER)), imgViewVideo3);

					Videos = new String[3];
					Videos[0] = appStatus
							.getSharedStringValue(appStatus.APP_USER_LINK);
					Videos[1] = appStatus
							.getSharedStringValue(appStatus.GROW_BUS_LINK);
					Videos[2] = appStatus
							.getSharedStringValue(appStatus.OVERVIEW_LINK);
				}
			}
		}
	}

	private String changeLinkName(String key, String value) {
		String strLink = "";
		AppStatus appStatus = AppStatus.getInstance(context);
		if(key.compareTo(appStatus.APP_USER_COVER) == 0){
			strLink = String.valueOf(appStatus.getSharedStringValue(appStatus.APP_USER_COVER));
			strLink=strLink.replace("app_user","android_app_user");
			
		}else if(key.compareTo(appStatus.GROW_BUS_COVER) == 0){
			strLink = String.valueOf(appStatus
					.getSharedStringValue(appStatus.GROW_BUS_COVER));
			strLink=strLink.replace("grow_business","android_grow_business");
		}else if(key.compareTo(appStatus.OVERVIEW_COVER) == 0){
			strLink = String.valueOf(appStatus
					.getSharedStringValue(appStatus.OVERVIEW_COVER));
			strLink = strLink.replace("overview_image","android_overview_image");
		}
		return strLink;
	}
}