package com.campaignslibrary;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.campaignslibrary.helpers.Constants;
import com.campaignslibrary.imageloader.ImageLoader;
import com.campaignslibrary.models.CampaignDetailResult;
import com.campaignslibrary.models.CreateCampaignResult;
import com.campaignslibrary.models.CreateCampaignResult.CampgnPhotos;
import com.campaignslibrary.tasks.EndingFeaturedCampaignTask;
import com.campaignslibrary.tasks.FilterCampaignTask;
import com.checkinlibrary.CheckinLibraryActivity;
import com.checkinlibrary.NoConnectivityScreen;
import com.checkinlibrary.helpers.AppStatus;
import com.checkinlibrary.ws.services.WebServiceIface;

public class CampaignsFragment extends Fragment implements WebServiceIface {
	
	CheckinLibraryActivity context;
	private StartCampgnFragment mStartCampgnFragment = null;
	public static AppStatus appStatus;
	
	private CampaignDetailsFragment mCampaignDetailsFragment;

	final String TAG = getClass().getName();

	private ViewFlipper viewFlipper;

	private ScrollView FlipScroll0;
	private ScrollView FlipScroll1;
	private ScrollView FlipScroll2;

	private GestureDetector gestureDetector;
	private Animation slideLeftIn;
	private Animation slideLeftOut;
	private Animation slideRightIn;
	private Animation slideRightOut;

	private int current_page = 0;
	private int max_pages = 0;
	private int iGstId = 0;

	private ImageView[] imgViewCurrentPage = new ImageView[3];

	private ImageView imgSearch, imgFlipperOne, imgFlipperTwo, imgFlipperThree, imgFlipperFour, imgFlipperFive,
			imgFlipperSix, imgEndingSoonNext;
	
	private RelativeLayout relativeLSearchList;
	private ListView searchListView;
	private LinearLayout lnrLtCampgnPhotos;
	
	private LinearLayout.LayoutParams lp;
	private CampaignDetailResult mCampaignDetailResult;
	
	private static final int IMAGE_1 = 0;
	private static final int IMAGE_2 = 1;
	private static final int IMAGE_3 = 2;
	private static final int IMAGE_4 = 3;
	private static final int IMAGE_5 = 4;
	private static final int IMAGE_6 = 5;	
	
	public ImageLoader imageLoader;
	private Resources resources;
	
	private TextView mTextView1,mTextView2,mTextView3,mTextView4,mTextView5,mTextView6;
	
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	@Override
	public void onResume() {		
		super.onResume();
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		context = (CheckinLibraryActivity) this.getActivity();
		appStatus = AppStatus.getInstance(context);

		View fragmentView = inflater.inflate(R.layout.campaigns_fragment, null);
		
		resources = getResources();

		initAllComponents(fragmentView);
		initializeOnClickHandlers(fragmentView);
		
		fetchCampaigns();
				
		return fragmentView;
	}
	
	/**
	 * This method used to retrieve featured and ending soon campaigns list from api
	 */
	private void fetchCampaigns() {
		if (CheckinLibraryActivity.appStatus.isOnline()) {
			String[] args = new String[1];
			args[0] = appStatus.getSharedStringValue(appStatus.AUTH_KEY);
			new EndingFeaturedCampaignTask(this).execute(args);
		} else {
			Log.v(TAG, "App is not online!");
			Intent intent = new Intent(context, NoConnectivityScreen.class);
			context.startActivity(intent);
			context.finish();
		}
	}

	/**
	 * This method initialises onclick listener for ui components
	 * @param v View for given fragment
	 */
	private void initializeOnClickHandlers(View v) {
		Button btnStartOwnCampgn = (Button) v.findViewById(R.id.btnStartCampgn);
		btnStartOwnCampgn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onStartCampgnClick();
			}
		});

		imgSearch.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onSearchClick();
			}
		});
		
		imgEndingSoonNext.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				Toast.makeText(context, "Loading next images", Toast.LENGTH_SHORT).show();
			}
		});
		
		searchListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				String[] args = new String[2];
				args[0] = appStatus.getSharedStringValue(appStatus.AUTH_KEY);
				args[1] = "" + (arg2 + 1);

				if (appStatus.isOnline()) {
					CheckinLibraryActivity.strProgressMessage=getString(R.string.searchProgressMessage);
					context.showDialog(0);
					new FilterCampaignTask(context).execute(args);
					relativeLSearchList.setVisibility(RelativeLayout.GONE);
				} else {
					Log.v(TAG, "App is not online!");
					Intent intent = new Intent(context, NoConnectivityScreen.class);
					context.startActivity(intent);
					context.finish();
				}
			}
		});
	}
	
	/**
	 * This method called when Flipper images clicked
	 */	
	private void onMotionHandle(ImageView mImageView,final int iIndex){
		mImageView.setTag(iIndex);
		mImageView.setOnTouchListener(new ImageView.OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				iGstId = iIndex;
				if (gestureDetector.onTouchEvent(event)) {
					Log.i("onClick", ""+v.getTag());
					return true;
				}
				return false;
			}
		});
	}

	/**
	 * This method called when start campaigns btn clicked
	 */
	public void onStartCampgnClick() {
		if (mStartCampgnFragment == null) {
			mStartCampgnFragment = (StartCampgnFragment) ((CheckinLibraryActivity) context).addFragment(
					R.id.linearLayout2, StartCampgnFragment.class.getName(), "start_campaign_fragment", new Bundle());
		} else {
			((CheckinLibraryActivity) this.context).attachFragment((Fragment) mStartCampgnFragment);
		}
		Log.i(TAG, "onStartCampgnClick");
	}

	/**
	 * This method called when start search btn clicked
	 */
	private void onSearchClick() {
		if (searchListView.getVisibility() == ListView.GONE) {
			searchListView.setVisibility(ListView.VISIBLE);
			searchListView.bringToFront();
			Animation slideDown = AnimationUtils.loadAnimation(context, R.drawable.btn_slide_down_pressed);
			searchListView.startAnimation(slideDown);
			relativeLSearchList.bringToFront();
		} else {
			Animation slideUp= AnimationUtils.loadAnimation(context, R.drawable.btn_slide_up_pressed);
			searchListView.setVisibility(ListView.GONE);
			searchListView.startAnimation(slideUp);
		}
	}

	/**
	 * This method initialises entire UI
	 */
	private void initAllComponents(View v) {
		
		imageLoader=new ImageLoader(context);

		imgSearch = (ImageView) v.findViewById(R.id.imgCmpgnSearch);
		relativeLSearchList = (RelativeLayout) v.findViewById(R.id.relativeLSearchList);
		searchListView = (ListView) v.findViewById(R.id.listViewSearch);		
		lnrLtCampgnPhotos=(LinearLayout)v.findViewById(R.id.linearLCampPhotos);	
		
		imgFlipperOne = (ImageView) v.findViewById(R.id.imgFlipper_one);
		imgFlipperTwo = (ImageView) v.findViewById(R.id.imgFlipper_two);
		imgFlipperThree = (ImageView) v.findViewById(R.id.imgFlipper_three);
		imgFlipperFour = (ImageView) v.findViewById(R.id.imgFlipper_four);
		imgFlipperFive = (ImageView) v.findViewById(R.id.imgFlipper_five);
		imgFlipperSix = (ImageView) v.findViewById(R.id.imgFlipper_six);
		
		mTextView1=(TextView)v.findViewById(R.id.txtMyCmpgnName1);
		mTextView2=(TextView)v.findViewById(R.id.txtMyCmpgnName2);
		mTextView3=(TextView)v.findViewById(R.id.txtMyCmpgnName3);
		mTextView4=(TextView)v.findViewById(R.id.txtMyCmpgnName4);
		mTextView5=(TextView)v.findViewById(R.id.txtMyCmpgnName5);
		mTextView6=(TextView)v.findViewById(R.id.txtMyCmpgnName6);
		
		imgEndingSoonNext = (ImageView) v.findViewById(R.id.imgNextCount);

		viewFlipper = (ViewFlipper) v.findViewById(R.id.cmpgnFlipper);

		FlipScroll0 = (ScrollView) v.findViewById(R.id.cmpgnFlipScroll0);
		FlipScroll1 = (ScrollView) v.findViewById(R.id.cmpgnFlipScroll1);
		FlipScroll2 = (ScrollView) v.findViewById(R.id.cmpgnFlipScroll2);

		gestureDetector = new GestureDetector(new MyGestureDetector(0));

		slideLeftIn = AnimationUtils.loadAnimation(context, R.layout.slide_in_left);
		slideLeftOut = AnimationUtils.loadAnimation(context, R.layout.slide_out_left);
		slideRightIn = AnimationUtils.loadAnimation(context, R.layout.slide_in_right);
		slideRightOut = AnimationUtils.loadAnimation(context, R.layout.slide_out_right);	
		
		current_page = 0;

		// Zero based
		max_pages = 2;
		
		onMotionHandle(imgFlipperOne,0); onMotionHandle(imgFlipperTwo,1);
		onMotionHandle(imgFlipperThree,2); onMotionHandle(imgFlipperFour,3);
		onMotionHandle(imgFlipperFive,4); onMotionHandle(imgFlipperSix,5);
		
		onFilpHandles(FlipScroll0); onFilpHandles(FlipScroll1); onFilpHandles(FlipScroll2);

		initialiseCurrentPageView(v);
	}

	/**
	 * This method initialises the UI for initial page
	 */
	private void initialiseCurrentPageView(View v) {
		imgViewCurrentPage[0] = (ImageView) v.findViewById(R.id.img_fetrdCmpgn_slider_panel1);
		imgViewCurrentPage[1] = (ImageView) v.findViewById(R.id.img_fetrdCmpgn_slider_panel2);
		imgViewCurrentPage[2] = (ImageView) v.findViewById(R.id.img_fetrdCmpgn_slider_panel3);

		imgViewCurrentPage[0].setImageResource(R.drawable.c4g_slideiconselect);		
		
		lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		lp.setMargins(5, 0, 0, 0);		
		Float density = this.getResources().getDisplayMetrics().density;
		lp.width=(int) (45*density);
		lp.height=(int) (45*density);
		
		String[] arrSearchArray = resources.getStringArray(R.array.search_campgn_arr);
		ArrayList<String> searchArray = new ArrayList<String>();
		for(int i=0;i<arrSearchArray.length;i++){
			searchArray.add(arrSearchArray[i]);		
		}

		CampaignSearchListAdapter mCampaignSearchListAdapter = new CampaignSearchListAdapter(context, searchArray);
		searchListView.setAdapter(mCampaignSearchListAdapter);
	}
	
	/**
	 * This method use to handles the onClick events while flip
	 */
	private void onFilpHandles(ScrollView FlipScroll){
		FlipScroll.setOnTouchListener(new ScrollView.OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				if (gestureDetector.onTouchEvent(event)) {
					return true;
				}
				return false;
			}
		});
	}

	/**
	 * This class handles the gesture
	 */
	class MyGestureDetector extends SimpleOnGestureListener {
		int mGestureId;

		public MyGestureDetector(int iGestureId) {
			this.mGestureId = iGestureId;
		}

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
			if (Math.abs(e1.getY() - e2.getY()) > Constants.INSTANCE.SWIPE_MAX_OFF_PATH) {
				return false;
			} else {
				try {
					// right to left swipe
					if (e1.getX() - e2.getX() > Constants.INSTANCE.SWIPE_MIN_DISTANCE && Math.abs(velocityX) > Constants.INSTANCE.SWIPE_THRESHOLD_VELOCITY) {
						if (canFlipRight()) {
							viewFlipper.setInAnimation(slideRightIn);
							viewFlipper.setOutAnimation(slideLeftOut);
							viewFlipper.showNext();

							if (current_page == 1) {
								imgViewCurrentPage[0].setImageResource(R.drawable.c4g_slideicon);
								imgViewCurrentPage[current_page].setImageResource(R.drawable.c4g_slideiconselect);
							} else if (current_page == max_pages) {
								imgViewCurrentPage[2].setImageResource(R.drawable.c4g_slideiconselect);
								imgViewCurrentPage[current_page - 1].setImageResource(R.drawable.c4g_slideicon);

							} else {
								imgViewCurrentPage[current_page - 1].setImageResource(R.drawable.c4g_slideicon);
								imgViewCurrentPage[current_page].setImageResource(R.drawable.c4g_slideiconselect);
							}
						} else {
							return false;
						}
						// left to right swipe
					} else if (e2.getX() - e1.getX() > Constants.INSTANCE.SWIPE_MIN_DISTANCE
							&& Math.abs(velocityX) > Constants.INSTANCE.SWIPE_THRESHOLD_VELOCITY) {
						if (canFlipLeft()) {
							viewFlipper.setInAnimation(slideLeftIn);
							viewFlipper.setOutAnimation(slideRightOut);
							viewFlipper.showPrevious();

							if (current_page == 0) {
								imgViewCurrentPage[0].setImageResource(R.drawable.c4g_slideiconselect);
								imgViewCurrentPage[current_page + 1].setImageResource(R.drawable.c4g_slideicon);
							} else {
								imgViewCurrentPage[current_page + 1].setImageResource(R.drawable.c4g_slideicon);
								imgViewCurrentPage[current_page].setImageResource(R.drawable.c4g_slideiconselect);
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
			if (mCampaignDetailResult != null) {
				if (current_page == 0) {
					if (iGstId == IMAGE_1) {
						if (mCampaignDetailResult.getFeatured_campaigns().size() > 0 && 
								mCampaignDetailResult.getFeatured_campaigns().get(0) != null) {
							onFeaturedPhotoClick(mCampaignDetailResult.getFeatured_campaigns().get(0));
						}
					} else if (iGstId == IMAGE_2) {
						if (mCampaignDetailResult.getFeatured_campaigns().size() > 1 && 
								mCampaignDetailResult.getFeatured_campaigns().get(1) != null ) {
							onFeaturedPhotoClick(mCampaignDetailResult.getFeatured_campaigns().get(1));
						}
					}
				} else if (current_page == 1) {
					if (iGstId == IMAGE_3) {
						if (mCampaignDetailResult.getFeatured_campaigns().size() > 2 && 
								mCampaignDetailResult.getFeatured_campaigns().get(2) != null ) {
							onFeaturedPhotoClick(mCampaignDetailResult.getFeatured_campaigns().get(2));
						}
					} else if (iGstId == IMAGE_4) {
						if ( mCampaignDetailResult.getFeatured_campaigns().size() > 3 &&  
								mCampaignDetailResult.getFeatured_campaigns().get(3) != null) {
							onFeaturedPhotoClick(mCampaignDetailResult.getFeatured_campaigns().get(3));
						}
					}
				} else if (current_page == 2) {
					if (iGstId == IMAGE_5) {
						if (mCampaignDetailResult.getFeatured_campaigns().size() > 4 && 
								mCampaignDetailResult.getFeatured_campaigns().get(4) != null) {
							onFeaturedPhotoClick(mCampaignDetailResult.getFeatured_campaigns().get(4));
						}
					} else if (iGstId == IMAGE_6) {
						if (mCampaignDetailResult.getFeatured_campaigns().size() > 5 && 
								mCampaignDetailResult.getFeatured_campaigns().get(5) != null) {
							onFeaturedPhotoClick(mCampaignDetailResult.getFeatured_campaigns().get(5));
						}
					}
				}

			}
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

	/**
	 * This method handles the api response from featured and ending soon campaigns
	 * @param result List<CreateCampaignResult> list of campaigns
	 */
	public void getCampaignResult(CampaignDetailResult result) {
		if(result != null){
			mCampaignDetailResult = result;
			
			displayFeaturedCmpgn();
			displayEndingSoon();
		}
	}	

	/**
	 * This method displays the photos to featured campaign list
	 */
	private void displayFeaturedCmpgn() {
		if (mCampaignDetailResult != null) {
			if (mCampaignDetailResult.getFeatured_campaigns() != null
					&& mCampaignDetailResult.getFeatured_campaigns().size() > 0) {
				for (int i = 0; i < mCampaignDetailResult.getFeatured_campaigns().size(); i++) {
					if (mCampaignDetailResult.getFeatured_campaigns().get(i).getCampaign().getPhotos() != null
							&& mCampaignDetailResult.getFeatured_campaigns().get(i).getCampaign().getPhotos().size() > 0) {
						getFetCmpgnPhotosFromList(i, mCampaignDetailResult.getFeatured_campaigns().get(i).getCampaign()
								.getPhotos().get(0),
								mCampaignDetailResult.getFeatured_campaigns().get(i).getCampaign().getName());
			
					}
				}
			}
		}
	}
	
	/**
	 * This method used to set photos from link using lazy load.
	 * @param mImageView ImageView mImageView
	 * @param strImgUrl String strImgUrl
	 */
	private void setPhotoFromLink(ImageView mImageView, String strImgUrl) {
		String strUrl = BASE_URL + strImgUrl;
		imageLoader.DisplayImage(strUrl, mImageView);
	}
	
	/**
	 * This method used to set photos from List.
	 * @param campgnName 
	 */
	private void getFetCmpgnPhotosFromList(int iIndex,CampgnPhotos mCampgnPhotos, String campgnName){
		switch(iIndex){
		case 0:
			setPhotoFromLink(imgFlipperOne, mCampgnPhotos.getImage_link());
			mTextView1.setText(campgnName);
			break;
		case 1:
			setPhotoFromLink(imgFlipperTwo, mCampgnPhotos.getImage_link());
			mTextView2.setText(campgnName);
			break;
		case 2:
			setPhotoFromLink(imgFlipperThree, mCampgnPhotos.getImage_link());
			mTextView3.setText(campgnName);
			break;
		case 3:
			setPhotoFromLink(imgFlipperFour, mCampgnPhotos.getImage_link());
			mTextView4.setText(campgnName);
			break;
		case 4:
			setPhotoFromLink(imgFlipperFive, mCampgnPhotos.getImage_link());
			mTextView5.setText(campgnName);
			break;
		case 5:
			setPhotoFromLink(imgFlipperSix, mCampgnPhotos.getImage_link());
			mTextView6.setText(campgnName);
			break;
		default:
			break;
		}
	}

	/**
	 * This method handles the photo click event of ending soon
	 */
	private void onFeaturedPhotoClick(CreateCampaignResult mCreateCampaignResult){
		mCampaignDetailsFragment=null;
		if (mCampaignDetailsFragment == null) {
			Bundle bundle = new Bundle();
			bundle.putSerializable("selected_campaign", mCreateCampaignResult);
			
			mCampaignDetailsFragment = (CampaignDetailsFragment) context.addFragment(
					R.id.linearLayout2, CampaignDetailsFragment.class.getName(),
					"campgn_details_fragment", bundle);
		} else {
			(context).attachFragment((Fragment) mCampaignDetailsFragment);
		}
	}
	
	/**
	 * This method displays the photos to ending soon list
	 */
	private void displayEndingSoon() {
		if(mCampaignDetailResult != null){
			if(mCampaignDetailResult.getCurrent_campaigns() != null){	
				
				for(int i=0;i<mCampaignDetailResult.getCurrent_campaigns().size();i++){
					if (mCampaignDetailResult.getCurrent_campaigns().get(i).getCampaign().getPhotos() != null &&
							mCampaignDetailResult.getCurrent_campaigns().get(i).getCampaign().getPhotos().size() > 0) {
						String EndingSoonImgLink = mCampaignDetailResult.getCurrent_campaigns().get(i).getCampaign()
								.getPhotos().get(0).getImage_link();

						ImageView mImgVPhoto = new ImageView(context);
						setPhotoFromLink(mImgVPhoto, EndingSoonImgLink);
						addOnclickListenerForImage(mImgVPhoto);
						lnrLtCampgnPhotos.addView(mImgVPhoto, lp);
						if(i==4) // display only 5 images
							break;
					}
				}
			}
		}
	}

	private void addOnclickListenerForImage(ImageView mImageView) {
		mImageView.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				CampaignsSearchFragment mCampaignsSearchFragment = (CampaignsSearchFragment) (context).addFragment(R.id.linearLayout2,
		                CampaignsSearchFragment.class.getName(), "cmpgn_search_list");
		        mCampaignsSearchFragment.setList(mCampaignDetailResult.getCurrent_campaigns());
			}
		});
	}
}